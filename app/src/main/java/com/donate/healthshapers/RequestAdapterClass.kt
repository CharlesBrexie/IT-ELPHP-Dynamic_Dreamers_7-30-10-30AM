import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.donate.healthshapers.R
import com.donate.healthshapers.RequestData

class RequestAdapterClass(private var requestDataList: ArrayList<RequestData>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RequestAdapterClass.RequestViewHolder>() {

    inner class RequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val userNameTextView: TextView = itemView.findViewById(R.id.ADUserName)
        val userTypeTextView: TextView = itemView.findViewById(R.id.ADUserType)
        val phoneNumTextView: TextView = itemView.findViewById(R.id.ADPhoneNum)
        val statTextView: TextView = itemView.findViewById(R.id.ADstatus)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(requestDataList[position])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(data: RequestData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_layout, parent, false)
        return RequestViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val currentItem = requestDataList[position]

        holder.userNameTextView.text = currentItem.username
        holder.userTypeTextView.text = currentItem.userType
        holder.phoneNumTextView.text = currentItem.phoneNumber
        holder.statTextView.text = currentItem.status ?: "Pending"
    }

    override fun getItemCount(): Int {
        return requestDataList.size
    }

    // Function to update adapter data
    fun updateData(newData: ArrayList<RequestData>) {
        requestDataList = newData
        notifyDataSetChanged()
    }
}