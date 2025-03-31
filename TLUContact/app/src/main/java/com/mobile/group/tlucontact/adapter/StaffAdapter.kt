package com.mobile.group.tlucontact.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.StaffDetailActivity
import com.mobile.group.tlucontact.models.Staff
import java.text.Collator
import java.util.Locale

class StaffAdapter(private val context: Context, var staffList: MutableList<Staff>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_STAFF = 1
    }

    private var isAscending = true
    private lateinit var items: MutableList<Any>

    init {
        setSortedList(staffList, isAscending)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortedList(staffList: MutableList<Staff>, ascending: Boolean) {
        isAscending = ascending

        val collator: Collator = Collator.getInstance(Locale("vi", "VN")).apply {
            strength = Collator.PRIMARY
        }
        staffList.sortWith { s1, s2 ->
            if (isAscending) collator.compare(s1.fullName, s2.fullName)
            else collator.compare(s2.fullName, s1.fullName)
        }

        val groupedStaff = linkedMapOf<String, MutableList<Staff>>()
        for (staff in staffList) {
            val firstLetter = staff.fullName.substring(0,1).uppercase()
            groupedStaff.putIfAbsent(firstLetter, mutableListOf())
            groupedStaff[firstLetter]?.add(staff)
        }

        items = mutableListOf()
        for ((key, value) in groupedStaff) {
            items.add(key)
            items.addAll(value)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_STAFF
    }

    fun filter(filteredStaff: MutableList<Staff>) {
        setSortedList(filteredStaff, isAscending)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_contact_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_staff, parent, false)
            StaffViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(items[position] as String)
        } else {
            val staff = items[position] as Staff
            (holder as StaffViewHolder).bind(staff)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, StaffDetailActivity::class.java).apply {
                    putExtra("staffSelected", staff)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvHeader: TextView = view.findViewById(R.id.header_text)

        fun bind(header: String) {
            tvHeader.text = header
        }
    }

    class StaffViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewPosition: TextView = view.findViewById(R.id.textViewPosition)

        fun bind(staff: Staff) {
            textViewName.text = staff.fullName
            textViewPosition.text = staff.position + " - " + staff.unit
            imageViewAvatar.setImageResource(staff.avatarResId)
        }
    }
}