package com.example.zenmood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.zenmood.R
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.firebase
import com.example.zenmood.classes.User
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
   private lateinit var v : View
    private lateinit var btnSignIn : Button
    private lateinit var btnRegister : Button
    private lateinit var txtUserEmail : EditText
    private lateinit var txtPassword : EditText
    private lateinit var txtForgotPassword : TextView
    private lateinit var cbStayLogged : CheckBox
    private lateinit var loadingImage : ImageView
    private lateinit var sp : SharedPreferences
    private lateinit var  parentJob : CompletableJob
    private lateinit var fbScope : CoroutineScope
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_log_in, container, false)
        btnRegister = v.findViewById(R.id.btnRegister)
        btnSignIn = v.findViewById(R.id.btnSignIn)
        txtPassword = v.findViewById(R.id.txtPassword)
        txtUserEmail = v.findViewById(R.id.txtUserEmail)
        txtForgotPassword = v.findViewById(R.id.txtForgotPassword)

        cbStayLogged = v.findViewById(R.id.cbStayLogged)
       // loadingImage = v.findViewById(R.id.grayblurr)
         parentJob = Job()
        fbScope = CoroutineScope(Dispatchers.Main + parentJob)
        sp = SharedPreferences(requireContext())
        return v
    }

    override fun onStart() {
        super.onStart()

        if(sp.checkStayLogged()){//user checked "Recordarme" checkBox
            logIn(sp.getUserName(),sp.getPassword())//logIn with preferences
        }
        btnSignIn.setOnClickListener {
            var userEmail = txtUserEmail.text.toString()
            var userPassword = txtPassword.text.toString()
            if (userEmail.isEmpty() && userPassword.isEmpty()) {
                Snackbar.make(v, "Campos vacios", Snackbar.LENGTH_LONG).show()
            } else {
                logIn(userEmail,userPassword) //normal login
            }
        }
        btnRegister.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegisterFragment()
            v.findNavController().navigate(action)//create a new user
        }
        txtForgotPassword.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToChangePassFragment()
            v.findNavController().navigate(action)//set preferences
        }




    }

     fun logIn(userEmail : String, password : String) {
         fbScope.launch {//launch coroutine to call suspend functions
             loader3.visibility = View.VISIBLE
             val authRes = firebase.signIn(userEmail, password) //log in with firebase
             if (authRes) { //authentication succesful

                 if (cbStayLogged.isChecked || sp.checkStayLogged()) {
                     sp.savePreferences(userEmail, password, true) //user wants to stay logged
                 } else {
                     sp.savePreferences(userEmail,password, false)//user does not  want to stay logged
                 }
                 val user  = firebase.getUser()
                 if (firebase.checkPreferences(userEmail)) {//check if preferences exists.
                     Snackbar.make(v, "Bienvenido ${user.username}", Snackbar.LENGTH_LONG).show()
                     val action = LogInFragmentDirections.actionLogInFragmentToHomeFragment()
                     v.findNavController().navigate(action)//everything is ok, start using the app
                     loader3.visibility = View.INVISIBLE

                 } else {
                     val action = LogInFragmentDirections.actionLogInFragmentToPreferencesFrg(user)
                     Snackbar.make(v, "Bienvenido ${user.username}", Snackbar.LENGTH_LONG).show()
                     v.findNavController().navigate(action)//set preferences before starting to use the app
                     loader3.visibility = View.INVISIBLE

                 }
             }
             else {
                 loader3.visibility = View.INVISIBLE

                 Snackbar.make(v, "no se pudo iniciar sesion correctamente", Snackbar.LENGTH_LONG).show()
             }

         }

     }
}


