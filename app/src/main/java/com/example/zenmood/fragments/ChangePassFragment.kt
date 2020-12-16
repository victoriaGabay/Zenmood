package com.example.zenmood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.findNavController
import com.example.zenmood.R
import com.example.zenmood.classes.firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [ChangePassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePassFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btnSendEmail : Button
    private lateinit var txtEmail : EditText
    private lateinit var imgGoBack : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_change_pass, container, false)
        txtEmail = v.findViewById(R.id.CPtxtEmail)
        btnSendEmail = v.findViewById(R.id.btnSendEmail)
        return v
    }

    override fun onStart() {
        super.onStart()
        val parentJob = Job()
        val fbScope = CoroutineScope(Dispatchers.Main + parentJob)

        btnSendEmail.setOnClickListener {
            val email = txtEmail.text.toString()
            if(email.isNotEmpty()){
                fbScope.launch {
                    firebase.resetPass(email)//send and email to change password.
             val action = ChangePassFragmentDirections.actionChangePassFragmentToLogInFragment()
                    v.findNavController().navigate(action)//go back to logIn

                }
            }
        }
    }
}