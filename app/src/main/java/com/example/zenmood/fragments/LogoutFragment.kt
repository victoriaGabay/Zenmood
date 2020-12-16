package com.example.zenmood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.zenmood.R
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.firebase
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 * Use the [LogoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogoutFragment : Fragment() {
    private lateinit var v : View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_logout, container, false)
        firebase.logOut()

        return v
    }

    override fun onStart() {
        super.onStart()
        //logging out, changing value of shared preferences
        val sp : SharedPreferences = SharedPreferences(requireContext())
        sp.changePreferences(false)
        firebase.logOut()
        val action = LogoutFragmentDirections.actionLogoutFragmentToLogInFragment()
        v.findNavController().navigate(action)
    }



}