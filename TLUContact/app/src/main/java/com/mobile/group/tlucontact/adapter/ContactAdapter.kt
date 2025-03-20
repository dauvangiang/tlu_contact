package com.mobile.group.tlucontact.adapter

import Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobile.group.tlucontact.R

class ContactAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTACT = 1
    }

    private var items: List<ContactListItem> = emptyList()
    private var filteredItems: List<ContactListItem> = emptyList()

    fun setData(contacts: List<Contact>) {
        // Group contacts by first letter and create headers
        val groupedContacts = contacts.groupBy { it.firstLetter }
            .toSortedMap()
            .flatMap { (letter, contactsInGroup) ->
                listOf(ContactListItem.Header(letter)) +
                        contactsInGroup.map { ContactListItem.ContactItem(it) }
            }

        items = groupedContacts
        filteredItems = groupedContacts
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        if (query.isEmpty()) {
            filteredItems = items
        } else {
            // Filter contacts and rebuild the list with headers
            val filteredContacts = items.filterIsInstance<ContactListItem.ContactItem>()
                .filter { it.contact.name.contains(query, ignoreCase = true) }
                .groupBy { it.contact.firstLetter }
                .toSortedMap()
                .flatMap { (letter, contactsInGroup) ->
                    listOf(ContactListItem.Header(letter)) + contactsInGroup
                }

            filteredItems = filteredContacts
        }
        notifyDataSetChanged()
    }

    fun sort(ascending: Boolean) {
        // Extract contacts, sort them, and rebuild the list with headers
        val contacts = items.filterIsInstance<ContactListItem.ContactItem>()
            .map { it.contact }
            .let {
                if (ascending) it.sortedBy { contact -> contact.name }
                else it.sortedByDescending { contact -> contact.name }
            }

        setData(contacts)
    }

    override fun getItemViewType(position: Int): Int {
        return when (filteredItems[position]) {
            is ContactListItem.Header -> TYPE_HEADER
            is ContactListItem.ContactItem -> TYPE_CONTACT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_CONTACT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_contact, parent, false)
                ContactViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = filteredItems[position]) {
            is ContactListItem.Header -> (holder as HeaderViewHolder).bind(item)
            is ContactListItem.ContactItem -> (holder as ContactViewHolder).bind(item.contact)
        }
    }

    override fun getItemCount(): Int = filteredItems.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewHeader: TextView = itemView.findViewById(R.id.textViewHeader)

        fun bind(header: ContactListItem.Header) {
            textViewHeader.text = header.letter
        }
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewAvatar: ImageView = itemView.findViewById(R.id.imageViewAvatar)
        private val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        private val textViewPosition: TextView = itemView.findViewById(R.id.textViewPosition)

        fun bind(contact: Contact) {
            textViewName.text = contact.name
            textViewPosition.text = contact.displayPosition

            // Sử dụng setImageResource thay vì Glide
            imageViewAvatar.setImageResource(contact.avatarResId)
        }
    }
}

sealed class ContactListItem {
    data class Header(val letter: String) : ContactListItem()
    data class ContactItem(val contact: Contact) : ContactListItem()
}