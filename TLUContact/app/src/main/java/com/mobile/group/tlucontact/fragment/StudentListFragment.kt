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
import com.mobile.group.tlucontact.databinding.FragmentStudentListBinding
import com.mobile.group.tlucontact.utils.StudentHelper

class StudentListFragment : Fragment() {

    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

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
    private val students: MutableList<Student> = mutableListOf()
    private lateinit var currDatas: MutableList<Student>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
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

        StudentHelper.fetchStudents(viewLifecycleOwner, students, adapter)

        setupSearch()
        setupFilter()
        setupSort()
        setupHeaderActions()
    }

    private fun setupRecyclerView(context: Context) {
        adapter = StudentAdapter(context, students)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        currDatas = students
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
        val filteredStudents = mutableListOf<Student>()

        students.forEach { student ->
            if (student.fullName.contains(text, ignoreCase = true) ||
                student.code.contains(text) ||
                student.phone.contains(text) ||
                student.email.contains(text, ignoreCase = true) ||
                student.department.name.contains(text, ignoreCase = true)
            ) {
                filteredStudents.add(student)
            }
        }

        currDatas = filteredStudents
        adapter.filter(filteredStudents)
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
            adapter.filter(students)
            currDatas = students
        } else {
            val filtered = students.filter { it.department.name == filter }.toMutableList()
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