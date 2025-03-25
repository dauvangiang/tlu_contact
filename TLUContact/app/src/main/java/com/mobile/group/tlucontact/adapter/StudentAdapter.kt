package com.mobile.group.tlucontact.adapter

import com.mobile.group.tlucontact.models.Student
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
import com.mobile.group.tlucontact.activities.StudentDetailActivity
import java.text.Collator
import java.util.Locale

class StudentAdapter(private val context: Context, var students: MutableList<Student>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_STUDENT = 1
    }

    private var isAscending = true
    private lateinit var items: MutableList<Any>

    init {
        setSortedList(students, isAscending)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortedList(students: MutableList<Student>, ascending: Boolean) {
        isAscending = ascending

        val collator: Collator = Collator.getInstance(Locale("vi", "VN")).apply {
            strength = Collator.PRIMARY
        }
        students.sortWith { s1, s2 ->
            if (isAscending) collator.compare(s1.fullName, s2.fullName)
            else collator.compare(s2.fullName, s1.fullName)
        }

        val groupedStudents = linkedMapOf<String, MutableList<Student>>()
        for (student in students) {
            val firstLetter = student.fullName.substring(0,1).uppercase()
            groupedStudents.putIfAbsent(firstLetter, mutableListOf())
            groupedStudents[firstLetter]?.add(student)
        }

        items = mutableListOf()
        for ((key, value) in groupedStudents) {
            items.add(key)
            items.addAll(value)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_STUDENT
    }

    fun filter(filteredStudents: MutableList<Student>) {
        setSortedList(filteredStudents, isAscending)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_contact_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_student, parent, false)
            StudentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(items[position] as String)
        } else {
            val student = items[position] as Student
            (holder as StudentViewHolder).bind(student)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, StudentDetailActivity::class.java).apply {
                    putExtra("studentSelected", student)
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

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewStudentInfo: TextView = view.findViewById(R.id.textViewStudentInfo)

        fun bind(student: Student) {
            textViewName.text = student.fullName
            textViewStudentInfo.text = student.displayInfo
            imageViewAvatar.setImageResource(student.avatarResId)
        }
    }
}