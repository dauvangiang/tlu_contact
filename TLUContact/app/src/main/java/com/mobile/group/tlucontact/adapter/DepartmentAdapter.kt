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
import com.bumptech.glide.Glide
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.DepartmentDetailActivity
import com.mobile.group.tlucontact.models.Department
import java.text.Collator
import java.util.Locale

class DepartmentAdapter(private val context: Context, var departments: MutableList<Department>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_DEPARTMENT = 1
    }

    private var isAscending = true
    private lateinit var items: MutableList<Any>

    init {
        setSortedList(departments, isAscending)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortedList(departments: MutableList<Department>, ascending: Boolean) {
        isAscending = ascending

        val collator: Collator = Collator.getInstance(Locale("vi", "VN")).apply {
            strength = Collator.PRIMARY
        }
        departments.sortWith { s1, s2 ->
            if (isAscending) collator.compare(s1.name, s2.name)
            else collator.compare(s2.name, s1.name)
        }

        val groupedDepartments = linkedMapOf<String, MutableList<Department>>()
        for (department in departments) {
            val firstLetter = department.name.substring(0,1).uppercase()
            groupedDepartments.putIfAbsent(firstLetter, mutableListOf())
            groupedDepartments[firstLetter]?.add(department)
        }

        items = mutableListOf()
        for ((key, value) in groupedDepartments) {
            items.add(key)
            items.addAll(value)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_DEPARTMENT
    }

    fun filter(filteredDepartments: MutableList<Department>) {
        setSortedList(filteredDepartments, isAscending)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_contact_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_department, parent, false)
            DepartmentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(items[position] as String)
        } else {
            val department = items[position] as Department
            (holder as DepartmentViewHolder).bind(department)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, DepartmentDetailActivity::class.java).apply {
                    putExtra("departmentSelected", department)
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

    class DepartmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewLogo: ImageView = itemView.findViewById(R.id.imageViewLogo)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewPhone: TextView = view.findViewById(R.id.textViewPhone)

        fun bind(department: Department) {
            textViewName.text = department.name
            textViewPhone.text = department.phone
            Glide.with(itemView.context)
                .load(department.logoUrl)
                .placeholder(R.drawable.tlu)
                .error(R.drawable.tlu)
                .into(imageViewLogo)

//            imageViewLogo.setImageResource(department.avatarResId)
        }
    }
}