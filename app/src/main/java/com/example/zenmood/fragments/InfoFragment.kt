package com.example.zenmood.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenmood.R
import com.example.zenmood.adapters.InformationListAdapter
import com.example.zenmood.adapters.MeditationListAdapter
import com.example.zenmood.classes.InformationItem
import com.example.zenmood.classes.MeditationItem
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {


    private lateinit var recInformation : RecyclerView
    private var informationList : MutableList<InformationItem> = ArrayList<InformationItem>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var informationListAdapter : InformationListAdapter
    private lateinit var v: View
    private lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_info, container, false)
        recInformation = v.findViewById(R.id.recInfo)
        return v
    }
    override fun onStart() {
        super.onStart()
        val parentJob = Job()
        var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        loader2.visibility = View.VISIBLE
        //start recicler view
        recInformation.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(context)
        recInformation.layoutManager = linearLayoutManager
        fbScope.launch {
            user = firebase.getUser()//getting current logged in user
            //showing information items
            informationList = firebase.getInfoItems()
            informationListAdapter = InformationListAdapter(informationList,requireContext()){position -> onItemClick(position)}
            recInformation.adapter = informationListAdapter
            loader2.visibility = View.INVISIBLE

        }

    }

    private fun onItemClick (position : Int ) {
        val action = HomeFragmentDirections.actionHomeFragmentToShowInfoFragment(informationList[position],user)
        //val action = InfoFragmentDirections.actionInfoFragmentToShowInfoFragment(informationList[position].name)
        v.findNavController().navigate(action)
    }


}