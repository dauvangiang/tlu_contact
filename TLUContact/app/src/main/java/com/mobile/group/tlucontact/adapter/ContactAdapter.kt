package com.mobile.group.tlucontact.adapter

import Contact
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
import com.mobile.group.tlucontact.activities.ContactDetailActivity
import java.text.Collator
import java.util.Locale

class ContactAdapter(private val context: Context, var contacts: MutableList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTACT = 1
    }

    private var isAscending = true
    private lateinit var items: MutableList<Any>

    init {
        setSortedList(contacts, isAscending)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSortedList(contacts: MutableList<Contact>, ascending: Boolean) {
        isAscending = ascending

        val collator: Collator = Collator.getInstance(Locale("vi", "VN")).apply {
            strength = Collator.PRIMARY
        }
        contacts.sortWith { s1, s2 ->
            if (isAscending) collator.compare(s1.name, s2.name)
            else collator.compare(s2.name, s1.name)
        }

        val groupedContacts = linkedMapOf<String, MutableList<Contact>>()
        for (contact in contacts) {
            val firstLetter = contact.name.substring(0,1).uppercase()
            groupedContacts.putIfAbsent(firstLetter, mutableListOf())
            groupedContacts[firstLetter]?.add(contact)
        }

        items = mutableListOf()
        for ((key, value) in groupedContacts) {
            items.add(key)
            items.addAll(value)
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_CONTACT
    }

    fun filter(filteredContacts: MutableList<Contact>) {
        setSortedList(filteredContacts, isAscending)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_contact_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_contact, parent, false)
            ContactViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(items[position] as String)
        } else {
            val contact = items[position] as Contact
            (holder as ContactViewHolder).bind(contact)
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ContactDetailActivity::class.java).apply {
                    putExtra("contactSelected", contact)
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

    class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewPosition: TextView = view.findViewById(R.id.textViewPosition)

        fun bind(contact: Contact) {
            textViewName.text = contact.name
            textViewPosition.text = contact.displayPosition
            imageViewAvatar.setImageResource(contact.avatarResId)
        }
    }
}
