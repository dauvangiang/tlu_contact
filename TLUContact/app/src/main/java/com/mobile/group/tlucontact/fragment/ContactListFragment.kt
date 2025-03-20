package com.mobile.group.tlucontact.fragment

import Contact
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

    private var isAscendingSort = true
    private val mockContacts = createMockData()

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

        setupRecyclerView()
        setupSearch()
        setupFilter()
        setupSort()
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Set initial data
        adapter.setData(mockContacts)
    }

    private fun setupSearch() {
        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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
            adapter.setData(mockContacts)
        } else {
            val filtered = mockContacts.filter { it.position == filter }
            adapter.setData(filtered)
        }
    }

    private fun setupSort() {
        buttonSort.setOnClickListener {
            isAscendingSort = !isAscendingSort
            adapter.sort(isAscendingSort)
        }
    }

    private fun createMockData(): List<Contact> {
        return listOf(
            Contact("1", "Nguyễn Văn An", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("2", "Trần Thị Bình", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("3", "Lê Văn Cường", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("4", "Phạm Thị Dung", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("5", "Hoàng Văn Em", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("6", "Ngô Thị Phương", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("7", "Đỗ Văn Giang", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("8", "Bùi Thị Hoa", "Nhân viên", "Khoa CNTT", R.drawable.hmm),
            Contact("9", "Nguyễn Văn Thắm", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("10", "Trần Văn Khánh", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("11", "Lê Thị Lan", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("12", "Phạm Văn Minh", "Nhân viên", "Khoa CNTT", R.drawable.hmm),
            Contact("13", "Hoàng Thị Ngọc", "Nhân viên", "Khoa CNTT", R.drawable.hmm),
            Contact("14", "Ngô Văn Oanh", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("15", "Đỗ Thị Phương", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("16", "Bùi Văn Quang", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("17", "Nguyễn Thị Rạng", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("18", "Trần Văn Sơn", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("19", "Lê Thị Tuyết", "Giảng viên", "Khoa CNTT", R.drawable.hmm),
            Contact("20", "Phạm Văn Uy", "Nhân viên", "Khoa CNTT", R.drawable.hmm),
            Contact("21", "Hoàng Thị Vân", "Nhân viên", "Khoa CNTT", R.drawable.hmm),
            Contact("22", "Ngô Văn Xuân", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("23", "Đỗ Thị Yến", "Sinh viên", "Khoa CNTT", R.drawable.hmm),
            Contact("24", "Bùi Văn Zương", "Sinh viên", "Khoa CNTT", R.drawable.hmm)
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