package com.mobile.group.tlucontact.fragment

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
import android.widget.ImageView
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.adapter.StudentAdapter
import com.mobile.group.tlucontact.models.Student

class StudentListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StudentAdapter
    private lateinit var editTextSearch: EditText
    private lateinit var spinnerFilter: Spinner
    private lateinit var buttonSort: ImageButton
    private lateinit var layoutHeader: ConstraintLayout
    private lateinit var cardViewSearch: CardView
    private lateinit var cardViewMenu: CardView
    private lateinit var imageViewSearchIcon: ImageView
    private lateinit var imageViewClose: ImageView
    private lateinit var imageViewAdd: ImageView

    private var isAscending = true
    private val mockStudents = createMockData()
    private lateinit var currDatas: MutableList<Student>

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
        layoutHeader = view.findViewById(R.id.layoutHeader)
        cardViewSearch = view.findViewById(R.id.cardViewSearch)
        cardViewMenu = view.findViewById(R.id.cardViewMenu)
        imageViewSearchIcon = view.findViewById(R.id.imageViewSearchIcon)
        imageViewClose = view.findViewById(R.id.imageViewClose)
        imageViewAdd = view.findViewById(R.id.imageViewAdd)

        setupRecyclerView(requireContext())
        setupSearch()
        setupFilter()
        setupSort()
        setupHeaderActions()
    }

    private fun setupRecyclerView(context: Context) {
        adapter = StudentAdapter(context, mockStudents)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        currDatas = mockStudents
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

    private fun setupHeaderActions() {
        // Search icon click - show search card and hide header
        imageViewSearchIcon.setOnClickListener {
            layoutHeader.visibility = View.GONE
            cardViewSearch.visibility = View.VISIBLE
            imageViewClose.visibility = View.VISIBLE
            cardViewMenu.visibility = View.GONE
        }

        // Close button click - hide search card and show header
        imageViewClose.setOnClickListener {
            cardViewSearch.visibility = View.GONE
            imageViewClose.visibility = View.GONE
            layoutHeader.visibility = View.VISIBLE
            // Clear search text when closing
            editTextSearch.text.clear()
        }

        // Add icon click - toggle menu visibility
        imageViewAdd.setOnClickListener {
            if (cardViewMenu.visibility == View.VISIBLE) {
                cardViewMenu.visibility = View.GONE
            } else {
                cardViewMenu.visibility = View.VISIBLE
            }
        }
    }

    private fun filter(text: String) {
        val students = mutableListOf<Student>()

        mockStudents.forEach { student ->
            if (student.fullName.contains(text, ignoreCase = true) ||
                student.studentId.contains(text) ||
                student.phone.contains(text) ||
                student.email.contains(text, ignoreCase = true) ||
                student.className.contains(text, ignoreCase = true) ||
                student.unit.contains(text, ignoreCase = true)
            ) {
                students.add(student)
            }
        }

        currDatas = students
        adapter.filter(students)
    }

    private fun setupFilter() {
        val filterOptions = arrayOf("Tất cả", "Khoa CNTT", "Khoa KTXD", "Khoa KTTC")
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

        spinnerFilter.setOnItemSelectedListener { position ->
            val selectedFilter = filterOptions[position]
            filterStudents(selectedFilter)
        }
    }

    private fun filterStudents(filter: String) {
        if (filter == "Tất cả") {
            adapter.filter(mockStudents)
            currDatas = mockStudents
        } else {
            val filtered = mockStudents.filter { it.unit == filter }.toMutableList()
            currDatas = filtered
            adapter.filter(filtered)
        }
    }

    private fun setupSort() {
        buttonSort.setOnClickListener {
            isAscending = !isAscending
            adapter.setSortedList(currDatas, isAscending)
            // Hide the menu after selecting an option
            cardViewMenu.visibility = View.GONE
        }
    }

    private fun createMockData(): MutableList<Student> {
        return mutableListOf(
            Student("2251061001", "Nguyễn Văn An", "", "0123456789", "nva@gmail.com", "Hà Nội", "64CNTT1", "Khoa CNTT", "user1", R.drawable.user_avatar),
            Student("2251061002", "Trần Thị Bình", "", "0123456789", "ttb@gmail.com", "Hà Nội", "64CNTT1", "Khoa CNTT", "user2", R.drawable.user_avatar),
            Student("2251061003", "Lê Văn Cường", "", "0123456789", "lvc@gmail.com", "Hà Nội", "64CNTT1", "Khoa CNTT", "user3", R.drawable.user_avatar),
            Student("2251061004", "Phạm Thị Dung", "", "0123456789", "ptd@gmail.com", "Hà Nội", "64CNTT2", "Khoa CNTT", "user4", R.drawable.user_avatar),
            Student("2251061005", "Hoàng Văn Em", "", "0123456789", "hve@gmail.com", "Hà Nội", "64CNTT2", "Khoa CNTT", "user5", R.drawable.user_avatar),
            Student("2251061006", "Ngô Thị Phương", "", "0123456789", "ntp@gmail.com", "Hà Nội", "64CNTT2", "Khoa CNTT", "user6", R.drawable.user_avatar),
            Student("2251062001", "Đỗ Văn Giang", "", "0123456789", "dvg@gmail.com", "Hà Nội", "64KTXD1", "Khoa KTXD", "user7", R.drawable.user_avatar),
            Student("2251062002", "Bùi Thị Hoa", "", "0123456789", "bth@gmail.com", "Hà Nội", "64KTXD1", "Khoa KTXD", "user8", R.drawable.user_avatar),
            Student("2251062003", "Nguyễn Văn Thắm", "", "0123456789", "nvt@gmail.com", "Hà Nội", "64KTXD1", "Khoa KTXD", "user9", R.drawable.user_avatar),
            Student("2251062004", "Trần Văn Khánh", "", "0123456789", "tvk@gmail.com", "Hà Nội", "64KTXD2", "Khoa KTXD", "user10", R.drawable.user_avatar),
            Student("2251062005", "Lê Thị Lan", "", "0123456789", "ltl@gmail.com", "Hà Nội", "64KTXD2", "Khoa KTXD", "user11", R.drawable.user_avatar),
            Student("2251063001", "Phạm Văn Minh", "", "0123456789", "pvm@gmail.com", "Hà Nội", "64KTTC1", "Khoa KTTC", "user12", R.drawable.user_avatar),
            Student("2251063002", "Hoàng Thị Ngọc", "", "0123456789", "htn@gmail.com", "Hà Nội", "64KTTC1", "Khoa KTTC", "user13", R.drawable.user_avatar),
            Student("2251063003", "Ngô Văn Oanh", "", "0123456789", "nvo@gmail.com", "Hà Nội", "64KTTC1", "Khoa KTTC", "user14", R.drawable.user_avatar),
            Student("2251063004", "Đỗ Thị Phương", "", "0123456789", "dtp@gmail.com", "Hà Nội", "64KTTC2", "Khoa KTTC", "user15", R.drawable.user_avatar),
            Student("2251063005", "Bùi Văn Quang", "", "0123456789", "bvq@gmail.com", "Hà Nội", "64KTTC2", "Khoa KTTC", "user16", R.drawable.user_avatar)
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