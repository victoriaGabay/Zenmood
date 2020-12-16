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
import com.example.zenmood.classes.InformationItem
import com.example.zenmood.classes.MeditationItem

class InformationListAdapter (private var informationList: MutableList<InformationItem>, var context: Context, val onItemClick: (Int) -> Unit): RecyclerView.Adapter<InformationListAdapter.InformationHolder>() {

    companion object{
        private val TAG = "INFORMATION LIST ADAPTER"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_information,parent,false)
        return (InformationHolder(
            view
        ))
    }

    override fun getItemCount(): Int {

        return informationList.size
    }

    override fun onBindViewHolder(holder: InformationHolder, position: Int) {

        holder.bindItems(informationList[position], context)
        holder.getCardLayout().setOnClickListener {
            onItemClick(position)
        }
    }

    class InformationHolder (v: View) : RecyclerView.ViewHolder(v) {

        private var view: View

        init {
            this.view = v
        }

        fun bindItems(model: InformationItem, context: Context) {
            val txtname: TextView = view.findViewById(R.id.txt_name_item2)

            txtname.text = model.name // El nombre del ejercicio/actividad

            var image: ImageView = view.findViewById(R.id.img_item2)

            Glide
                .with(context)
                .load(model.imageUrl)
                .centerInside()
                .circleCrop()
                .into(image)
        }

        fun getCardLayout(): CardView {
            return view.findViewById(R.id.card_package_item2)
        }

    }
}


