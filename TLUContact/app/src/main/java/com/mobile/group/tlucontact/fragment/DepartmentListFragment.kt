package com.mobile.group.tlucontact.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.adapter.DepartmentAdapter
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.databinding.FragmentDepartmentListBinding
import com.mobile.group.tlucontact.services.department.DepartmentService
import com.mobile.group.tlucontact.services.department.DepartmentServiceImpl
import kotlinx.coroutines.launch

class DepartmentListFragment : Fragment() {

    private var _binding: FragmentDepartmentListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DepartmentAdapter
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
    private var mockDepartments: MutableList<Department> = mutableListOf()
    private lateinit var currDatas: MutableList<Department>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launch {
            mockDepartments = createMockData()
            Toast.makeText(requireContext(), mockDepartments.size.toString(), Toast.LENGTH_SHORT).show()
        }
        _binding = FragmentDepartmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        adapter = DepartmentAdapter(context, mockDepartments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        currDatas = mockDepartments
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
        val departments = mutableListOf<Department>()

        mockDepartments.forEach { department ->
            if (department.name.contains(text, ignoreCase = true) ||
                department.phone.contains(text, ignoreCase = true) ||
            department.phone.contains(text) ||
                    department.email.contains(text, ignoreCase = true)
            ) {
            departments.add(department)
        }
        }

        currDatas = departments
        adapter.filter(departments)
    }

    private fun setupFilter() {
        val filterOptions = arrayOf("Tất cả", "Khoa", "Phòng ban", "Trung tâm")
        val filterAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            filterOptions
        )
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerFilter.adapter = filterAdapter

        spinnerFilter.setOnItemSelectedListener { position ->
            val selectedFilter = filterOptions[position]
            filterDepartments(selectedFilter)
        }
    }

    private fun filterDepartments(filter: String) {
        if (filter == "Tất cả") {
            adapter.filter(mockDepartments)
            currDatas = mockDepartments
        } else {
            val filtered = mockDepartments.filter { it.type == filter }.toMutableList()
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

    private suspend fun createMockData(): MutableList<Department> {
        val departmentService = DepartmentServiceImpl()
        val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjMwYjIyMWFiNjU2MTdiY2Y4N2VlMGY4NDYyZjc0ZTM2NTIyY2EyZTQiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzA2ODg5NCwidXNlcl9pZCI6IjFoMXdZZ0R1c3hWdWZ4MVFXOUNlVmFCamlnejEiLCJzdWIiOiIxaDF3WWdEdXN4VnVmeDFRVzlDZVZhQmppZ3oxIiwiaWF0IjoxNzQzMDY4ODk0LCJleHAiOjE3NDMwNzI0OTQsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.T3VMRaMF3jWrtgv-F2vv15McnXjf_JU_-zkE4VxqbK3wqCQoZ-8h3WcZTpL7mdK88rqE5GL1iiCSQfEe-0Q9-PPvmSIYqUF_GKVhHFvqT9eVY_BSESWTdJh8p2cqSP1mgg_mnB_bQNcyiS86YbP1RpwVXA-T8DNOqI3BuCjHkQcuw10dZaCi9tgMdM8cVXNGkIanWksCip-7oVXHKDQHrH38cKRoFV_fYhNj_t0rdP0zKCcvS1dMY1GZXmlyLdZ-bRx5Xl2c5bimwYSykTGCEGrdVVYblicPTphwm34WKhyw0IcQv1E8b2N0D8y0O0OjL1UtWDYBf24vKqWss4RTgg"

        Toast.makeText(requireContext(), token, Toast.LENGTH_SHORT).show()
        return departmentService.getDepartments(token, 0, 10) ?: mutableListOf()

//        return mutableListOf(
//            Department("1", "CNTT", "Khoa Công nghệ thông tin", "Nhà C1", "", "0243.8522201", "cntt@tlu.edu.vn", "0243.8522201", "Trường Đại học Thủy lợi", "Khoa", R.drawable.cho),
//            Department("2", "KTXD", "Khoa Kỹ thuật xây dựng", "Nhà C2", "", "0243.8522202", "ktxd@tlu.edu.vn", "0243.8522202", "Trường Đại học Thủy lợi", "Khoa", R.drawable.cho),
//            Department("3", "KTTC", "Khoa Kinh tế và Quản lý", "Nhà B1", "", "0243.8522203", "kttc@tlu.edu.vn", "0243.8522203", "Trường Đại học Thủy lợi", "Khoa", R.drawable.cho),
//            Department("4", "KTHH", "Khoa Kỹ thuật tài nguyên nước", "Nhà A5", "", "0243.8522204", "kthh@tlu.edu.vn", "0243.8522204", "Trường Đại học Thủy lợi", "Khoa", R.drawable.cho),
//            Department("5", "DAOTAO", "Phòng Đào tạo", "Nhà A1", "", "0243.8522205", "daotao@tlu.edu.vn", "0243.8522205", "Trường Đại học Thủy lợi", "Phòng ban", R.drawable.cho),
//            Department("6", "CTSV", "Phòng Công tác sinh viên", "Nhà A1", "", "0243.8522206", "ctsv@tlu.edu.vn", "0243.8522206", "Trường Đại học Thủy lợi", "Phòng ban", R.drawable.cho),
//            Department("7", "TCHC", "Phòng Tổ chức hành chính", "Nhà A1", "", "0243.8522207", "tchc@tlu.edu.vn", "0243.8522207", "Trường Đại học Thủy lợi", "Phòng ban", R.drawable.cho),
//            Department("8", "TTCNTT", "Trung tâm Công nghệ thông tin", "Nhà C1", "", "0243.8522208", "ttcntt@tlu.edu.vn", "0243.8522208", "Trường Đại học Thủy lợi", "Trung tâm", R.drawable.cho),
//            Department("9", "TTNN", "Trung tâm Ngoại ngữ", "Nhà A2", "", "0243.8522209", "ttnn@tlu.edu.vn", "0243.8522209", "Trường Đại học Thủy lợi", "Trung tâm", R.drawable.cho),
//            Department("10", "TTDVTH", "Trung tâm Dịch vụ tổng hợp", "Nhà A3", "", "0243.8522210", "ttdvth@tlu.edu.vn", "0243.8522210", "Trường Đại học Thủy lợi", "Trung tâm", R.drawable.cho)
//        )
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