package com.example.zenmood.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.zenmood.R
import com.example.zenmood.classes.InformationItem
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import com.example.zenmood.classes.userFavs
import kotlinx.android.synthetic.main.fragment_show_info.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ShowInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowInfoFragment : Fragment() {
   private lateinit var v: View
private lateinit var showTxt : TextView
    private lateinit var txtTitle : TextView
    private lateinit var infoImg : ImageView
    private lateinit var imgSave : ImageView
    private lateinit var user : User
    private var isFavorite : Boolean = false
    val parentJob = Job()
    var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_info, container, false)
        txtTitle = v.findViewById(R.id.txtInfoTitle)
        showTxt = v.findViewById(R.id.txtShowText)
        infoImg = v.findViewById(R.id.imgInfo)
        imgSave = v.findViewById(R.id.imgSaveInfoItem)
        return v
    }

    override fun onStart() {
        super.onStart()

        var infoItem : InformationItem = ShowInfoFragmentArgs.fromBundle(requireArguments()).infoItemInfo //getting item information
        user = ShowInfoFragmentArgs.fromBundle(requireArguments()).user
        checkFavs(infoItem.name,requireContext())
        //SHOWING ITEM IN SCREEN
        txtTitle.text = infoItem.name
        showTxt.text = infoItem.text
        Glide
            .with(requireContext())
            .load(infoItem.imageUrl)
            .centerCrop()
            .into(imgInfo)

        imgSave.setOnClickListener {
            if(!isFavorite){
                val savedIcon = resources.getDrawable(R.drawable.ic_favorite_filled)
                Glide
                    .with(requireContext())
                    .load(savedIcon)
                    .centerCrop()
                    .into(imgSave)
                fbScope.launch {
                    var fav = userFavs(infoItem.name,"information")
                    firebase.setFavs(user.email,fav)//updating favs list
                    checkFavs(infoItem.name,requireContext())

                }

                isFavorite = true
            }else{
                Glide
                    .with(requireContext())
                    .load(resources.getDrawable(R.drawable.ic_favorite))
                    .centerCrop()
                    .into(imgSave)
                val itemToDelete = userFavs(infoItem.name,"information")
                firebase.deleteFav(user.email,itemToDelete)
                isFavorite = false

            }
        }

    }
    private fun checkFavs(infoItemName : String,context : Context){
        fbScope.launch {
            user = firebase.getUser()
            for (item in user.favs) {
                if (infoItemName == item.name) {
                    Glide
                        .with(context)
                        .load(resources.getDrawable(R.drawable.ic_favorite_filled))
                        .centerCrop()
                        .into(imgSave)
                    isFavorite = true
                }
            }
        }
    }



}