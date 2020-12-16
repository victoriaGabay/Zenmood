package com.example.zenmood.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import com.example.zenmood.R
import com.example.zenmood.classes.firebase
import com.example.zenmood.classes.User
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView
import com.github.angads25.toggle.widget.LabeledSwitch
import kotlinx.android.synthetic.main.item_questions.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [PreferencesFrg.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreferencesFrg : Fragment() {
private lateinit var v : View
    private lateinit var user : User
    private var finished : Boolean = false
    private lateinit var btnShowQuestions : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_preferences_frg, container, false)
        btnShowQuestions = v.findViewById(R.id.btnSetPreferences)

        return v
    }

    override fun onStart() {
        super.onStart()
        btnShowQuestions.setOnClickListener {
            user = PreferencesFrgArgs.fromBundle(requireArguments()).user
            //inflate alertDialog
            val dialogV = LayoutInflater.from(this.context).inflate(R.layout.item_questions, null)

            //AlertDialogBuilder
            var contadorDeFalses : Int = 0
            val mBuilder = AlertDialog.Builder(this.context)
                .setView(dialogV)
                .setNegativeButton("Listo!", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                    val action = PreferencesFrgDirections.actionPreferencesFrgToHomeFragment()
                    v.findNavController().navigate(action)})//setting nevative button
            mBuilder.setCancelable(true)


            mBuilder.show()//show dialog in
            var x: Int = 0
            val labeledSwitch : LabeledSwitch = dialogV.switch1
            dialogV.txt_view10.text = "¿Tiene algun diagnostico profesional? (ansiedad,depresion,etc)"
            labeledSwitch.setOnToggledListener(OnToggledListener() { toggleableView: ToggleableView, b: Boolean ->
                fun onSwitched(labeledSwitch: LabeledSwitch, isOn: Boolean) {
                    if (isOn) {
                        firebase.setDiagostic(user.email, true)

                    } else {
                        firebase.setDiagostic(user.email, false)
                    }
                }
            dialogV.imgNextQstn.setOnClickListener {
                when (x) {
                    0 -> {
                        dialogV.txt_view10.text = "¿Alguna vez hizo yoga?"
                        if (labeledSwitch.isOn) {

                        }else{
                            contadorDeFalses++
                        }
                        labeledSwitch.isOn = false
                        x = 1
                    }
                    1 -> {
                        dialogV.txt_view10.text = "¿Tiene conocimientos respecto a la salud mental?"
                        if (labeledSwitch.isOn) {

                        }else {
                            contadorDeFalses++
                        }
                        x = 2
                        labeledSwitch.isOn = false
                    }
                    2 -> {
                        dialogV.txt_view10.text = "¿Su ansiedad se presenta diariamente?"
                        if (labeledSwitch.isOn) {

                        }else {
                            contadorDeFalses++
                        }
                        dialogV.imgNextQstn.visibility = View.INVISIBLE
                        x = 3
                        firebase.updatePrefVal(user.email,true)//preferences are setted, now the app will work properly
                        if(contadorDeFalses == 3){
                            firebase.setMoreInfo(user.email,"level","basico")
                        }
                        if(contadorDeFalses == 2){
                            firebase.setMoreInfo(user.email,"level","basico")
                        }
                        if(contadorDeFalses < 2){
                            firebase.setMoreInfo(user.email,"level","basico")
                        }
                        }
                    }
                }
            })
        }
    }
}