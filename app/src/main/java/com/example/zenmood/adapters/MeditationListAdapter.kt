package com.example.zenmood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zenmood.R
import com.example.zenmood.classes.MeditationItem

class MeditationListAdapter(private var meditationList: MutableList<MeditationItem>, var context: Context, val onItemClick: (Int) -> Unit): RecyclerView.Adapter<MeditationListAdapter.MeditationHolder>() {

    companion object{
        private val TAG = "MeditationListAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_meditation,parent,false)
        return (MeditationHolder(
            view
        ))
    }

    override fun getItemCount(): Int {

        return meditationList.size
    }

    override fun onBindViewHolder(holder: MeditationHolder, position: Int) {

        holder.bindItems(meditationList[position], context)
        holder.getCardLayout().setOnClickListener {
            onItemClick(position)
        }
    }

    class MeditationHolder (v: View) : RecyclerView.ViewHolder(v){

        private var view: View

        init {
            this.view = v
        }

        fun bindItems(model: MeditationItem, context: Context){
            val txtname : TextView = view.findViewById(R.id.txt_name_item)
            val txtlevel : TextView = view.findViewById(R.id.txt_name_tipo)

            txtname.text = model.name // El nombre del ejercicio/actividad
            txtlevel.text = "nivel: " + model.level // El nombre del estilo (escuchar, leer, hacer algo)

            var image: ImageView = view.findViewById(R.id.img_item)

            Glide
                .with(context)
                .load(model.imageUrl)
                .centerInside()
                .circleCrop()
                .into(image)
        }

        fun getCardLayout (): CardView {
            return view.findViewById(R.id.card_package_item)
        }

    }


}