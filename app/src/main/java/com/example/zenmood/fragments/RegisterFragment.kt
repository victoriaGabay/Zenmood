package com.example.zenmood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController
import com.example.zenmood.R
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.firebase
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btnContinue : Button
    private lateinit var txtInputLytEmail : TextInputLayout
    private lateinit var txtEmail : EditText
    private lateinit var txtInputLytPassword : TextInputLayout
    private lateinit var txtPassword : EditText
    private lateinit var txtInputLytConfirmPassword : TextInputLayout
    private lateinit var txtConfirmPassword : EditText
    private lateinit var txtUserName : EditText
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_register, container, false)
        btnContinue = v.findViewById(R.id.btnContinue)
        txtEmail = v.findViewById(R.id.txtEmail)
        txtInputLytEmail = v.findViewById(R.id.TILEmail)
        txtPassword = v.findViewById(R.id.txtPass)
        txtInputLytPassword = v.findViewById(R.id.TILpassword)
        txtConfirmPassword = v.findViewById(R.id.txtConfirmPass)
        txtInputLytConfirmPassword = v.findViewById(R.id.TILConfirmPassword)
        txtUserName = v.findViewById(R.id.txtUserName)

                return v
    }

    override fun onStart() {
        super.onStart()
        val parentJob = Job()
        val fbScope = CoroutineScope(Dispatchers.Main + parentJob)
        btnContinue.setOnClickListener {
            loader4.visibility = View.VISIBLE
            val userEmail = txtEmail.text.toString()
            val confirmPass = txtConfirmPassword.text.toString()
            val pass = txtPassword.text.toString()
            if(userEmail.isNotEmpty() && confirmPass.isNotEmpty() && pass.isNotEmpty()){
                if(pass.equals(confirmPass)){
                    fbScope.launch {
                        firebase.register(userEmail,pass,txtUserName.text.toString(),false)
                        Snackbar.make(v,"Usuario creado existosamente",Snackbar.LENGTH_LONG).show()
                        v.findNavController().navigateUp()
                        loader4.visibility = View.INVISIBLE

                    }


                }
            }
        }

    }


}