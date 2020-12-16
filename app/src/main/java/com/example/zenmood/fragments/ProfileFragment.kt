package com.example.zenmood.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.zenmood.R
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import kotlinx.android.synthetic.main.item_change_username.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.github.nikartm.button.FitButton
import gun0912.tedbottompicker.TedBottomPicker


/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btnShowFavs : Button
    private lateinit var btnChangePreferences : Button
    private lateinit var user : User
    private lateinit var profilePic : ImageView
    private lateinit var txtShowMeditatedTimes : TextView
    private lateinit var txtProfileName : TextView
    private lateinit var imgChangeUserName :  FitButton
    private lateinit var imgChangeProfilePic : FitButton
    private val parentJob = Job()
    private var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        btnChangePreferences = v.findViewById(R.id.btnChangePreferences)
        btnShowFavs = v.findViewById(R.id.btnShowFavs)
        txtShowMeditatedTimes = v.findViewById(R.id.txtShowMedTime)
        profilePic = v.findViewById(R.id.imgProfile)
        txtProfileName = v.findViewById(R.id.txtProfileName)
        imgChangeUserName = v.findViewById(R.id.imgChangeUserName)
        imgChangeProfilePic = v.findViewById(R.id.imgChangeProfilePic)
        return v
    }
    override fun onStart() {
        super.onStart()
        updateUI()

        btnShowFavs.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToShowFavsFragment()//show favs list
            v.findNavController().navigate(action)
        }
        btnChangePreferences.setOnClickListener {
            fbScope.launch {
                val action = ProfileFragmentDirections.actionProfileFragmentToPreferencesFrg(user)
                v.findNavController().navigate(action)//something changed, lets try set everything again
            }
        }
        imgChangeUserName.setOnClickListener {
            val dialogV = LayoutInflater.from(this.context).inflate(R.layout.item_change_username, null)
            val mBuilder = AlertDialog.Builder(this.context)
                .setView(dialogV)
                .setNegativeButton("CAMBIAR!", DialogInterface.OnClickListener { dialog, id ->
                    val newName = dialogV.edTxtChangUsername.text
                    firebase.updateUsername(user.email,newName.toString())
                    dialog.cancel()
                    updateUI()
                })
            mBuilder.show()
        }
        imgChangeProfilePic.setOnClickListener{
            TedBottomPicker.with(activity as AppCompatActivity)
                .showCameraTile(false)
                .showTitle(false)
                .setCompleteButtonText("Done")
                .setEmptySelectionText("No Select")
                .show { uri->
                    Glide.with(v)
                        .load(uri)
                        .centerCrop()
                        .into(profilePic)
                    val imgURL = uri.toString()
                    fbScope.launch {
                        val imgUrl = firebase.changeUserPic(imgURL, user.email)
                        firebase.updateImg(imgURL,user)
                        //updateUI()
                    }
                }
        }
    }
    private fun updateUI(){
        fbScope.launch {
            user = firebase.getUser()
            txtProfileName.text = user.username
            Glide
                .with(requireContext())
                .load(user.imgUrl)
                .centerCrop()
                .into(profilePic)//show profile pic in screen
        }
    }
}