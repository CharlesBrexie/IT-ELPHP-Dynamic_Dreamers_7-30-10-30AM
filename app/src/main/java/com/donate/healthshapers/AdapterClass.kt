package com.donate.healthshapers

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class AdapterClass(private val userList: ArrayList<DataClass>,
                   private val listener: OnItemClickListener) :
                    RecyclerView.Adapter<AdapterClass.MyViewHolder>(){

    fun updateData(newData: ArrayList<DataClass>) {
        userList.clear() // Clear the existing list
        userList.addAll(newData) // Add all elements from the new list
        notifyDataSetChanged() // Notify the adapter that the dataset has changed
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout
        ,parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return userList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        Log.d("ImageUrl", "Current item imageUrl: ${currentItem.imageUrl}")

        // Bind other data
        holder.itemName.text = currentItem.itemName.toString()
        holder.timeOfPreparation.text = currentItem.timeOfPreparation.toString()
        holder.quantity.text = currentItem.quantity.toString()
        holder.address.text = currentItem.address.toString()
        if(currentItem.utensilsRequired.toString() == "true"){
            holder.utensilsRequired.text = "Utensils are needed"
        }
        else{
            holder.utensilsRequired.text = "Utensils are not needed"
        }

            // Check if imageUrl is not null or empty
            if (!currentItem.imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(currentItem.imageUrl)
                    .placeholder(R.drawable.healthshapers) // Placeholder image while loading
                    .error(R.drawable.profilepic) // Error image if loading fails
                    .into(holder.imageView)
            } else {
                // Load placeholder image if imageUrl is null or empty
                Picasso.get()
                    .load(R.drawable.profilepic)
                    .into(holder.imageView)
            }

            // Set click listener
            holder.itemView.setOnClickListener {
                listener.onItemClick(currentItem)
            }
    }
    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.ADitemName)
        val timeOfPreparation : TextView = itemView.findViewById(R.id.ADtimePrep)
        val quantity : TextView = itemView.findViewById(R.id.ADtquantity)
        val address : TextView = itemView.findViewById(R.id.ADaddress)
        val utensilsRequired : TextView = itemView.findViewById(R.id.ADutensilsRequired)
        val imageView : ImageView = itemView.findViewById(R.id.ADimage)
    }

    interface OnItemClickListener {
        fun onItemClick(data: DataClass)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // Initialize views...
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(userList[position])
            }
        }
    }

}