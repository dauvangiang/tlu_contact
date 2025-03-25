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
import com.mobile.group.tlucontact.adapter.StaffAdapter
import com.mobile.group.tlucontact.models.Staff

class StaffListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StaffAdapter
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
    private val mockStaff = createMockData()
    private lateinit var currDatas: MutableList<Staff>

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
        adapter = StaffAdapter(context, mockStaff)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        currDatas = mockStaff
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
        val staffList = mutableListOf<Staff>()

        mockStaff.forEach { staff ->
            if (staff.fullName.contains(text, ignoreCase = true) ||
                staff.phone.contains(text) ||
                staff.email.contains(text, ignoreCase = true) ||
                staff.position.contains(text, ignoreCase = true) ||
                staff.unit.contains(text, ignoreCase = true)
            ) {
                staffList.add(staff)
            }
        }

        currDatas = staffList
        adapter.filter(staffList)
    }

    private fun setupFilter() {
        val filterOptions = arrayOf("Tất cả", "Giảng viên", "Nhân viên", "Quản lý")
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

        spinnerFilter.setOnItemSelectedListener { position ->
            val selectedFilter = filterOptions[position]
            filterStaff(selectedFilter)
        }
    }

    private fun filterStaff(filter: String) {
        if (filter == "Tất cả") {
            adapter.filter(mockStaff)
            currDatas = mockStaff
        } else {
            val filtered = mockStaff.filter { it.position == filter }.toMutableList()
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

    private fun createMockData(): MutableList<Staff> {
        return mutableListOf(
            Staff("GV001", "Nguyễn Văn An", "Giảng viên", "0123456789", "nva@tlu.edu.vn", "", "Khoa CNTT", "user1", R.drawable.user_avatar),
            Staff("GV002", "Trần Thị Bình", "Giảng viên", "0123456789", "ttb@tlu.edu.vn", "", "Khoa CNTT", "user2", R.drawable.user_avatar),
            Staff("GV003", "Lê Văn Cường", "Giảng viên", "0123456789", "lvc@tlu.edu.vn", "", "Khoa CNTT", "user3", R.drawable.user_avatar),
            Staff("GV004", "Phạm Thị Dung", "Giảng viên", "0123456789", "ptd@tlu.edu.vn", "", "Khoa CNTT", "user4", R.drawable.user_avatar),
            Staff("NV001", "Hoàng Văn Em", "Nhân viên", "0123456789", "hve@tlu.edu.vn", "", "Phòng Đào tạo", "user5", R.drawable.user_avatar),
            Staff("NV002", "Ngô Thị Phương", "Nhân viên", "0123456789", "ntp@tlu.edu.vn", "", "Phòng CTSV", "user6", R.drawable.user_avatar),
            Staff("QL001", "Đỗ Văn Giang", "Quản lý", "0123456789", "dvg@tlu.edu.vn", "", "Khoa CNTT", "user7", R.drawable.user_avatar),
            Staff("QL002", "Bùi Thị Hoa", "Quản lý", "0123456789", "bth@tlu.edu.vn", "", "Phòng Đào tạo", "user8", R.drawable.user_avatar),
            Staff("GV005", "Nguyễn Văn Thắm", "Giảng viên", "0123456789", "nvt@tlu.edu.vn", "", "Khoa CNTT", "user9", R.drawable.user_avatar),
            Staff("GV006", "Trần Văn Khánh", "Giảng viên", "0123456789", "tvk@tlu.edu.vn", "", "Khoa KTXD", "user10", R.drawable.user_avatar),
            Staff("GV007", "Lê Thị Lan", "Giảng viên", "0123456789", "ltl@tlu.edu.vn", "", "Khoa KTXD", "user11", R.drawable.user_avatar),
            Staff("NV003", "Phạm Văn Minh", "Nhân viên", "0123456789", "pvm@tlu.edu.vn", "", "Phòng TCHC", "user12", R.drawable.user_avatar),
            Staff("NV004", "Hoàng Thị Ngọc", "Nhân viên", "0123456789", "htn@tlu.edu.vn", "", "Phòng TCHC", "user13", R.drawable.user_avatar),
            Staff("QL003", "Ngô Văn Oanh", "Quản lý", "0123456789", "nvo@tlu.edu.vn", "", "Khoa KTXD", "user14", R.drawable.user_avatar),
            Staff("QL004", "Đỗ Thị Phương", "Quản lý", "0123456789", "dtp@tlu.edu.vn", "", "Phòng TCHC", "user15", R.drawable.user_avatar)
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