package com.example.zenmood.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.zenmood.R
import com.example.zenmood.classes.User
import com.example.zenmood.classes.firebase
import com.example.zenmood.classes.userFavs
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_show_meditation.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [ShowMeditationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShowMeditationFragment : Fragment() {
    private  lateinit var videoUrl : String
    private  var playWhenReady = true
    private var playbackPosition: Long = 0
    private var currentWindow = 0
    private var player: SimpleExoPlayer? = null
    private lateinit var v : View
    private lateinit var txtTitle : TextView
   // private lateinit var imgMeditation : ImageView
    private lateinit var imgSave : ImageView
    private lateinit var user : User
    private  var isFavorite : Boolean = false
    val parentJob = Job()
    var fbScope: CoroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_show_meditation, container, false)
        txtTitle = v.findViewById(R.id.txtMedTittle)
        //imgMeditation = v.findViewById(R.id.imgMeditation)
        imgSave = v.findViewById(R.id.imgSave)
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        player = SimpleExoPlayer.Builder(requireContext()).build()
        v.video_view.player = player
    }

    override fun onStart() {
        super.onStart()
        user = ShowMeditationFragmentArgs.fromBundle(requireArguments()).user
        val medItem = ShowMeditationFragmentArgs.fromBundle(requireArguments()).medItemInfo//getting item info
        checkFavs(medItem.name,requireContext())
        //SHOWING ITEM IN SCREEN
        txtTitle.text = medItem.name

       /* Glide
            .with(requireContext())
            .load(medItem.imageUrl)
            .centerCrop()
            .into(imgMeditation)*/
        videoUrl = medItem.urlVideo
        imgSave.setOnClickListener {
            if(!isFavorite){
                val savedIcon = resources.getDrawable(R.drawable.ic_favorite_filled)
                Glide
                    .with(requireContext())
                    .load(savedIcon)
                    .centerCrop()
                    .into(imgSave)
            fbScope.launch {
                var fav = userFavs(medItem.name,"meditation")
                firebase.setFavs(user.email,fav)//updating favs list
                checkFavs(medItem.name,requireContext())
            }
                isFavorite = true
            }else{
                Glide
                    .with(requireContext())
                    .load(resources.getDrawable(R.drawable.ic_favorite))
                    .centerCrop()
                    .into(imgSave)
                val itemToDelete = userFavs(medItem.name,"meditation")
                firebase.deleteFav(user.email,itemToDelete)
                isFavorite = false
            }
        }

            //          STARTING         //
          //              EXO          //
        //              PLAYER       //

        if (Util.SDK_INT >= 24) {

            buildMediaSource((Uri.parse(videoUrl)))?.let {
                initializePlayer(
                    it
                )
            }
        }
    }

    private fun checkFavs(infoItemName : String, context : Context){
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

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || player == null) {
            buildMediaSource((Uri.parse(videoUrl)))?.let {
                initializePlayer(
                    it
                )
            }
        }
    }
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }


    private fun initializePlayer(mediaSource: MediaSource){
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition)
        player?.prepare(mediaSource)
    }



    private fun buildMediaSource(uri: Uri): MediaSource? {
        val dataSourceFactory = DefaultDataSourceFactory(this.requireContext(), "exoplayer-codelab")
        return ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
    }
}


