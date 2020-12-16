package com.example.zenmood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenmood.R
import com.example.zenmood.adapters.MeditationListAdapter
import com.example.zenmood.classes.MeditationItem
import com.example.zenmood.classes.SharedPreferences
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import kotlinx.android.synthetic.main.fragment_meditation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [MeditationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeditationFragment : Fragment() {

    private lateinit var recMeditation : RecyclerView
    private var meditationList : MutableList<MeditationItem> = ArrayList<MeditationItem>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var meditationListAdapter : MeditationListAdapter
    private lateinit var v: View
    private lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_meditation, container, false)
        recMeditation = v.findViewById(R.id.recMeditations)
        return v
    }

    override fun onStart() {
        super.onStart()
        val parentJob = Job()
        var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        //staring reciclerView
        loader1.visibility = VISIBLE
        recMeditation.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recMeditation.layoutManager = linearLayoutManager
        fbScope.launch {
            user = firebase.getUser()//getting current logged in user
            //showing meditationItems
            meditationList = firebase.getMedItems()!!
            meditationListAdapter = MeditationListAdapter(meditationList, requireContext()) { position -> onItemClick(position) }
            recMeditation.adapter = meditationListAdapter
            loader1.visibility = INVISIBLE


        }
    }

    private fun onItemClick (position : Int ) {
        val action = HomeFragmentDirections.actionHomeFragmentToShowMeditationFragment(meditationList[position],user)
        v.findNavController().navigate(action)
    }

}