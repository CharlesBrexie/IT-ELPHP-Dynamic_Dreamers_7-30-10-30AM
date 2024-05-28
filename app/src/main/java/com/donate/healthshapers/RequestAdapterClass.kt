package com.donate.healthshapers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RequestAdapterClass(private val requestList: ArrayList<DataClass>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RequestAdapterClass.RequestViewHolder>() {
    private val userArrayList: ArrayList<DataClass> = ArrayList()

    fun updateData(newData: List<DataClass>) {
        userArrayList.clear()
        userArrayList.addAll(newData)
        notifyDataSetChanged()
    }
    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val userNameTextView: TextView = itemView.findViewById(R.id.ADUserName)
        val userTypeTextView: TextView = itemView.findViewById(R.id.ADUserType)
        val phoneNumTextView: TextView = itemView.findViewById(R.id.ADPhoneNum)
        val imageView: ImageView = itemView.findViewById(R.id.ADimage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(requestList[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(data: DataClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_layout, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = requestList[position]

        holder.userNameTextView.text = currentItem.itemName
        holder.userTypeTextView.text = currentItem.timeOfPreparation
        holder.phoneNumTextView.text = currentItem.quantity

        // Load image using Picasso library
        if (!currentItem.pfp.isNullOrEmpty()) {
            Picasso.get()
                .load(currentItem.pfp)
                .placeholder(R.drawable.pfp) // Placeholder image while loading
                .error(R.drawable.pfp) // Error image if loading fails
                .into(holder.imageView)
        } else {
            // Load placeholder image if pfp is null or empty
            holder.imageView.setImageResource(R.drawable.pfp)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}

