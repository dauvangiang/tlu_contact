package com.mobile.group.tlucontact.fragment

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.DepartmentDetailActivity
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.models.DependentDepartment
import com.mobile.group.tlucontact.utils.ContactActionHelper
import com.mobile.group.tlucontact.utils.DepartmentHelper

class DepartmentDetailFragment : Fragment() {
    private var department: Department? = null
    private lateinit var tvCode: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvFax: TextView
    private lateinit var tvParentDepartment: TextView
    private lateinit var btnTextChat: CardView
    private lateinit var btnCall: CardView
    private lateinit var btnSendEmail: CardView
    private lateinit var btnCopyPhoneNumber: ImageView
    private lateinit var btnCopyEmail: ImageView
    private lateinit var btnCopyFax: ImageView
    private lateinit var tvContactName: TextView
    private lateinit var tvSecondContactInfo: TextView
    private lateinit var cvFax: CardView
    private lateinit var cvParentDepartment: CardView
    private lateinit var cvDependentDepartments: CardView
    private lateinit var btnViewParentDepartment: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_department_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViews()
        setListeners()
    }

    private fun setListeners() {
        val phoneNumber = tvPhoneNumber.text.toString()
        val email = tvEmail.text.toString()
        val fax = tvFax.text.toString()
        btnTextChat.setOnClickListener { ContactActionHelper.sendSms(requireContext(), phoneNumber) }
        btnCall.setOnClickListener { ContactActionHelper.call(requireContext(), phoneNumber) }
        btnSendEmail.setOnClickListener { ContactActionHelper.sendEmail(requireContext(), email) }
        btnCopyPhoneNumber.setOnClickListener { ContactActionHelper.copyToClipboard(requireContext(), "Số điện thoại", phoneNumber) }
        btnCopyEmail.setOnClickListener { ContactActionHelper.copyToClipboard(requireContext(), "Email", email) }
        btnCopyFax.setOnClickListener { ContactActionHelper.copyToClipboard(requireContext(), "Fax", fax) }
        btnViewParentDepartment.setOnClickListener {
            val departmentCode = it.tag as? String
            departmentCode?.let { code -> viewParentDepartment(code) }
        }
    }

    private fun viewParentDepartment(code: String) {
        DepartmentHelper.getDepartment(viewLifecycleOwner, requireContext(), code)
    }

    private fun setViews() {
        getViewReferences()
        getIntentData()
        tvSecondContactInfo.visibility = View.GONE
        if (department == null) return

        tvContactName.text = department!!.name
        tvCode.text = department!!.code
        tvPhoneNumber.text = department!!.phone
        tvEmail.text = department!!.email

        val fax = department!!.fax
        if (fax.isNullOrBlank()) {
            cvFax.visibility = View.GONE
        } else {
            tvFax.text = fax
        }

        val parentDepartment = department!!.parentDepartment
        if (parentDepartment == null) {
            cvParentDepartment.visibility = View.GONE
        } else {
            tvParentDepartment.text = parentDepartment.name
            btnViewParentDepartment.tag = parentDepartment.code
        }

        val dependentDepartments = department!!.dependentDepartments
        if (dependentDepartments.isNullOrEmpty()) {
            cvDependentDepartments.visibility = View.GONE
        } else {
            setDependentDepartments(dependentDepartments)
        }
    }

    private fun setDependentDepartments(departments: List<DependentDepartment>) {
        val listDependentDepartments = requireView().findViewById<LinearLayout>(R.id.list_dependent_departments)
        for ((idx, dep) in departments.withIndex()) {
            val constraintLayout = ConstraintLayout(requireContext()).apply {
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                setPadding(0, 0, 0, 12)
            }

            // Tạo textview để hiện tên
            val textView = TextView(requireContext()).apply {
                id = View.generateViewId()
                text = (idx+1).toString() + ". " + dep.name
                setTextColor(ContextCompat.getColor(context, R.color.primary_black))
                textSize = 18f
                layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    startToStart = ConstraintLayout.LayoutParams.PARENT_ID
                }
            }

            // Tạo icon xem
            val imageView = ImageView(requireContext()).apply {
                id = View.generateViewId()
                setImageResource(R.drawable.ic_visibility)
                contentDescription = getString(R.string.view)
                setColorFilter(ContextCompat.getColor(context, R.color.primary_blue))
                layoutParams = ConstraintLayout.LayoutParams(dpToPx(20), dpToPx(20)).apply {
                    marginStart = dpToPx(8)
                    topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
                    endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
                }
                tag = dep.code

                setOnClickListener {
                    val departmentCode = it.tag as? String
                    departmentCode?.let { code -> viewDependentDepartment(code) }
                }
            }

            constraintLayout.addView(textView)
            constraintLayout.addView(imageView)
            listDependentDepartments.addView(constraintLayout)
        }
    }

    private fun viewDependentDepartment(code: String) {
        DepartmentHelper.getDepartment(viewLifecycleOwner, requireContext(), code)
    }

    private fun dpToPx(dp: Int): Int = (dp * Resources.getSystem().displayMetrics.density).toInt()

    private fun getIntentData() {
        department = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().intent.getParcelableExtra("departmentSelected", Department::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireActivity().intent.getParcelableExtra("departmentSelected")
        }
    }

    private fun getViewReferences() {
        tvContactName = requireView().findViewById(R.id.tv_contact_name)
        tvSecondContactInfo = requireView().findViewById(R.id.tv_second_contact_info)
        tvCode = requireView().findViewById(R.id.tv_code)
        tvPhoneNumber = requireView().findViewById(R.id.tv_phone_number)
        tvEmail = requireView().findViewById(R.id.tv_email)
        tvFax = requireView().findViewById(R.id.tv_fax)
        tvParentDepartment = requireView().findViewById(R.id.tv_parent_department)
        btnTextChat = requireView().findViewById(R.id.text_chat_btn)
        btnCall = requireView().findViewById(R.id.call_btn)
        btnSendEmail = requireView().findViewById(R.id.send_email_btn)
        btnCopyPhoneNumber = requireView().findViewById(R.id.btn_copy_phone_number)
        btnCopyEmail = requireView().findViewById(R.id.btn_copy_email)
        btnCopyFax = requireView().findViewById(R.id.btn_copy_fax)
        cvFax = requireView().findViewById(R.id.cv_fax)
        cvParentDepartment = requireView().findViewById(R.id.cv_parent_department)
        cvDependentDepartments = requireView().findViewById(R.id.cv_dependent_departments)
        btnViewParentDepartment = requireView().findViewById(R.id.btn_view_parent_department)
    }
}