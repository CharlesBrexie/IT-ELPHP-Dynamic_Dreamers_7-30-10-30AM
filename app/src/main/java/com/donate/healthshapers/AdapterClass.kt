package com.donate.healthshapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterClass(private val userList:ArrayList<DataClass>,
                   private val listener: OnItemClickListener,
                  ) : RecyclerView.Adapter<AdapterClass.MyViewHolder>(){

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]

        // Bind other data
        holder.itemName.text = currentItem.itemName.toString()
        holder.timeOfPreparation.text = currentItem.timeOfPreparation.toString()
        holder.quantity.text = currentItem.quantity.toString()
        holder.address.text = currentItem.address.toString()
        holder.utensilsRequired.text = currentItem.utensilsRequired.toString()

        // Set click listener on item view
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
    }

    interface OnItemClickListener {
        fun onItemClick(data: DataClass)
    }

}