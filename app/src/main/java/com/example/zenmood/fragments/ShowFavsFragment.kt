package com.example.zenmood.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zenmood.R
import com.example.zenmood.adapters.InformationListAdapter
import com.example.zenmood.adapters.MeditationListAdapter
import com.example.zenmood.classes.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_show_favs.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [ShowFavsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowFavsFragment : Fragment() {
    private lateinit var v : View
    private lateinit var btn : Button
   // private lateinit var grayBlurr : ImageView
    private lateinit var favs : ArrayList<userFavs>
    private var meditationList : MutableList<MeditationItem> = ArrayList<MeditationItem>()
    private var informationList : MutableList<InformationItem> = ArrayList<InformationItem>()
    private lateinit var recFavMed : RecyclerView
    private lateinit var recFavInfo : RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var meditationListAdapter : MeditationListAdapter
    private lateinit var informationListAdapter : InformationListAdapter
    private lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_favs, container, false)
        recFavMed = v.findViewById(R.id.recFavMed)
        recFavInfo = v.findViewById(R.id.recFavInfo)
        //grayBlurr = v.findViewById(R.id.grayblur)
        //((grayBlurr.visibility= VISIBLE
        return v
    }

    override fun onStart() {
        super.onStart()
        meditationList.clear()
        informationList.clear()
        val parentJob = Job()
        var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
        fbScope.launch {
            loader.visibility = VISIBLE
            user = firebase.getUser()
            for(item in user.favs){
                if(item.type == "meditation"){  //meditation objetc
                    val med = firebase.getMedItemByName(item.name!!)//getting document from storage
                    meditationList.add(med) //adding object to list, to show in med rc
                }else{
                    //Snackbar.make(v, item.name + "    " + item.type,Snackbar.LENGTH_LONG).show()
                    val info = firebase.getInfoItemByName(item.name!!)//getting document from storage
                    informationList.add(info)//adding object to list, to show in info rc
                }
            }

            //starting reciclerView for meditationItems
            recFavMed.setHasFixedSize(true)
            linearLayoutManager = LinearLayoutManager(context)
            recFavMed.layoutManager = linearLayoutManager
            meditationListAdapter = MeditationListAdapter(meditationList,requireContext()){position -> onItemClickMed(position)}
            recFavMed.adapter = meditationListAdapter

            //starting reciclerView for informationItems
            recFavInfo.setHasFixedSize(true)
            linearLayoutManager = LinearLayoutManager(context)
            recFavInfo.layoutManager = linearLayoutManager
            informationListAdapter = InformationListAdapter(informationList,requireContext()){position -> onItemClickInfo(position)}
            recFavInfo.adapter = informationListAdapter
            loader.visibility = INVISIBLE


        }
    }
    private fun onItemClickMed (position : Int ) {
        val action = ShowFavsFragmentDirections.actionShowFavsFragmentToShowMeditationFragment(meditationList[position],user)
        v.findNavController().navigate(action)
    }
    private fun onItemClickInfo (position : Int ) {
        val action = ShowFavsFragmentDirections.actionShowFavsFragmentToShowInfoFragment(informationList[position],user)
        //val action = InfoFragmentDirections.actionInfoFragmentToShowInfoFragment(informationList[position].name)
        v.findNavController().navigate(action)
    }


}