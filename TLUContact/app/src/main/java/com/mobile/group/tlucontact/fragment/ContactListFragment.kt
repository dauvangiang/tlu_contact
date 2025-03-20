package com.mobile.group.tlucontact.fragment

import Contact
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.adapter.ContactAdapter

class ContactListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var buttonSort: ImageButton

    private var isAscending = true
    private val mockContacts = createMockData()
    private lateinit var currDatas: MutableList<Contact>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        editTextSearch = view.findViewById(R.id.editTextSearch)
        spinnerFilter = view.findViewById(R.id.spinnerFilter)
        buttonSort = view.findViewById(R.id.buttonSort)

        setupRecyclerView(requireContext())
        setupSearch()
        setupFilter()
        setupSort()
    }

    private fun setupRecyclerView(context: Context) {
        adapter = ContactAdapter(context, mockContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        currDatas = mockContacts
    }

    private fun setupSearch() {
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filter(text: String) {
        val contacts = mutableListOf<Contact>()

        mockContacts.forEach { contact ->
            if (contact.name.contains(text, ignoreCase = true) ||
                contact.phone.contains(text) ||
                contact.email.contains(text, ignoreCase = true)
            ) {
                contacts.add(contact)
            }
        }

        currDatas = contacts
        adapter.filter(contacts)
    }

    private fun setupFilter() {
        val filterOptions = arrayOf("Tất cả", "Giảng viên", "Sinh viên", "Nhân viên")
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

        spinnerFilter.setOnItemSelectedListener { position ->
            val selectedFilter = filterOptions[position]
            filterContacts(selectedFilter)
        }
    }

    private fun filterContacts(filter: String) {
        if (filter == "Tất cả") {
            adapter.filter(mockContacts)
            currDatas = mockContacts
        } else {
            val filtered = mockContacts.filter { it.position == filter }.toMutableList()
            currDatas = filtered
            adapter.filter(filtered)
        }
    }

    private fun setupSort() {
        buttonSort.setOnClickListener {
            isAscending = !isAscending
            adapter.setSortedList(currDatas, isAscending)
        }
    }

    private fun createMockData(): MutableList<Contact> {
        return mutableListOf(
            Contact("1", "Nguyễn Văn An", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("2", "Trần Thị Bình", "0123456789", "ttb@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("3", "Lê Văn Cường", "0123456789", "lvc@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("4", "Phạm Thị Dung", "0123456789", "ptd@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("5", "Hoàng Văn Em", "0123456789", "hve@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("6", "Ngô Thị Phương", "0123456789", "ntp@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("7", "Đỗ Văn Giang", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("8", "Bùi Thị Hoa", "0123456789", "nva@gmail.com", "Nhân viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("9", "Nguyễn Văn Thắm", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("10", "Trần Văn Khánh", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("11", "Lê Thị Lan", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("12", "Phạm Văn Minh", "0123456789", "nva@gmail.com", "Nhân viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("13", "Hoàng Thị Ngọc", "0123456789", "nva@gmail.com", "Nhân viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("14", "Ngô Văn Oanh", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("15", "Đỗ Thị Phương", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("16", "Bùi Văn Quang", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("17", "Nguyễn Thị Rạng", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("18", "Trần Văn Sơn", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("19", "Lê Thị Tuyết", "0123456789", "nva@gmail.com", "Giảng viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("20", "Phạm Văn Uy", "0123456789", "nva@gmail.com", "Nhân viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("21", "Hoàng Thị Vân", "0123456789", "nva@gmail.com", "Nhân viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("22", "Ngô Văn Xuân", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("23", "Đỗ Thị Yến", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar),
            Contact("24", "Bùi Văn Zương", "0123456789", "nva@gmail.com", "Sinh viên", "Khoa CNTT", R.drawable.user_avatar)
        )
    }
}

// Extension function for Spinner
private fun Spinner.setOnItemSelectedListener(listener: (Int) -> Unit) {
    this.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }

        override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
    })
}