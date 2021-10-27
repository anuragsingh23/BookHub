package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapater(val context: Context, val itemList: ArrayList<Book>):
    RecyclerView.Adapter<DashboardRecyclerAdapater.DashboardViewHolder>() {
    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){


        val txtBookName: TextView=view.findViewById(R.id.txtBookName)
        val txtBookAuther:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView =view.findViewById(R.id.imgBookImage)
        val content :LinearLayout=view.findViewById(R.id.content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
       var view=LayoutInflater.
       from(parent.context).
       inflate(R.layout.recycler_dashboard_single_row,parent,false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int)
    {
        val book=itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuther.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
      //  holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)

        holder.content.setOnClickListener {
        val intent=Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookID)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}