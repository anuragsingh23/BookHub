package com.example.bookhub.adapter

import android.media.Image
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.adapter.FavouritesRecyclerAdapter.*
import org.w3c.dom.Text

class FavouritesRecyclerAdapter:RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>() {
    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtbookName:TextView=view.findViewById(R.id.txtFavBookTitle)
        val txtBookAuthor:TextView=view.findViewById(R.id.txtFavBookAuthor)
        val txtBookPrice: TextView=view.findViewById(R.id.txtFavBookPrice)
        val txtBookRating: TextView=view.findViewById(R.id.txtFavBookRating)
        val imgBookImage:ImageView=view.findViewById(R.id.imgFavBookImage)
        val llContent:LinearLayout=view.findViewById(R.id.llFavContent)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
       return  FavouriteViewHolder(view =int)
    }


    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

    }


    override fun getItemCount(): Int {

    }

}