
package com.example.zenmood.classes

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class firebase(){


   companion object {
       private var auth : FirebaseAuth = Firebase.auth
       private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
       private val storage : FirebaseStorage = FirebaseStorage.getInstance()

      suspend fun signIn(email: String, password: String): Boolean {
          try {
               auth.signInWithEmailAndPassword(email, password).await()
              return true
          }catch (e : Exception){
              when(e){
                  is FirebaseAuthInvalidCredentialsException -> {
                      return false
                  }
              }
          }
          return false
      }
        fun deleteFav(email : String, item: userFavs){
            db.collection("users").document(email).update("favs",FieldValue.arrayRemove(item))
        }
       suspend fun setFavs(email : String, item : userFavs){
           db.collection("users").document(email).update("favs",FieldValue.arrayUnion(item)).await()
       }
       /*suspend fun getFavs() : ArrayList<userFavs> {
           val document = db.collection("userFavs").document(auth.currentUser?.email.toString()).get().await()
           var favs = arrayListOf<userFavs>()
           var arrayOfMaps = document.get("favs") as ArrayList<HashMap<String,String>>
           arrayOfMaps.forEach {
               val name = it.get("name")
               val type = it.get("type")
               val fav = userFavs(name, type)
               favs.add(fav)}
           return favs
       }*/
       suspend fun register(email : String, password : String, userName: String,preferences: Boolean){
          auth.createUserWithEmailAndPassword(email,password).await()
           val user = User(userName, email, preferences,"","https://firebasestorage.googleapis.com/v0/b/loginapp-9943e.appspot.com/o/profile%20pic.png?alt=media&token=f662a076-cf6e-4bd7-952a-92d640fb1098", arrayListOf())
           db.collection("users").document(auth.currentUser?.email.toString()).set(user).await()
       }
       fun setMoreInfo(userName : String, newTag : String, newInfo : String){
         db.collection("users").document(userName).update(newTag,newInfo)
       }
       fun setDiagostic(userName : String, value : Boolean){
           db.collection("users").document(userName).update("diagnostic",value)
       }

       suspend fun getUser() : User {
           val ref = db.collection("users").document(auth.currentUser?.email.toString()).get().await()
           val user = ref.toObject(User::class.java)
           return user!!
       }
       fun updateUsername (oldUserName: String,newUserName : String){
           db.collection("users").document(oldUserName).update("username",newUserName)
       }
       fun updatePrefVal (userName: String,pref : Boolean){
           db.collection("users").document(userName).update("preferences",pref)
       }
        suspend fun resetPass(email: String) {
            auth.sendPasswordResetEmail(email).await()
        }

        fun  checkLogIn() : Boolean{
            auth.currentUser?.email.toString()
            if(auth.currentUser?.email.toString() == "null"){
                return false
            }else{
                return true
            }
       }

       fun logOut(){
           auth.signOut()
       }
        suspend fun checkPreferences(userName: String) :Boolean{
            var pref = false
            val reference = db.collection("users").document(userName).get().await()
            val user : User? = reference.toObject(User::class.java)
            if(user!!.preferences){
                pref = true
            }
            return pref
       }

       suspend fun getMedItems() : MutableList<MeditationItem>? {
           val name = auth.currentUser?.email.toString()
           val ref = db.collection("users").document(name).get().await()
           val user = ref.toObject(User::class.java)
           when(user?.level){
               "basico" -> {
                   val reference = db.collection("meditationItems").whereEqualTo("level","basico").get().await()
                   val list = reference.toObjects(MeditationItem::class.java)
                   return list
               }
               "intermedio" -> {
                   val reference = db.collection("meditationItems").whereEqualTo("level","intermedio").get().await()
                   val list = reference.toObjects(MeditationItem::class.java)
                   return list
               }
               "alto" -> {
                   val reference = db.collection("meditationItems").whereEqualTo("level","alto").get().await()
                   val list = reference.toObjects(MeditationItem::class.java)
                   return list
               }
           }
           return null
       }
       suspend fun getInfoItems() : MutableList<InformationItem>{
           val reference = db.collection("informationItems").get().await()
           val infoList = reference.toObjects(InformationItem::class.java)
           return infoList
       }
       suspend fun getMedItemByName(name : String) : MeditationItem{
           val reference = db.collection("meditationItems").document(name).get().await()
           val medItem = reference.toObject(MeditationItem::class.java)
           return medItem!!
       }
       suspend fun getInfoItemByName(name : String) : InformationItem{
           val reference = db.collection("informationItems").document(name).get().await()
           val infoItem = reference.toObject(InformationItem::class.java)
           return infoItem!!
       }


        fun updateImg(imgUrl : String,user : User){
           db.collection("users").document(user.email).update("imgUrl",imgUrl)
       }

       suspend fun changeUserPic(filePath : String, name: String) : String{
          val storage = FirebaseStorage.getInstance()
          val storageRef = storage.reference

          val metadata = StorageMetadata.Builder()
              .setContentType("image/jpeg")
              .build()
          //val file = Uri.fromFile(File(filePath))
          val imageRef = storageRef.child("images/$name")
          return withContext(Dispatchers.IO) {
              imageRef.putFile(filePath.toUri(), metadata).await().storage.downloadUrl.await().toString()
          }
      }
   }
}