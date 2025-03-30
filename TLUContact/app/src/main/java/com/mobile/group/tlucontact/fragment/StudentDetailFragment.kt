package com.mobile.group.tlucontact.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.activities.DepartmentDetailActivity
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.models.Student
import com.mobile.group.tlucontact.utils.ContactActionHelper

class StudentDetailFragment : Fragment() {
    private var student: Student? = null
    private lateinit var tvCode: TextView
    private lateinit var tvPhoneNumber: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvClass: TextView
    private lateinit var tvAddress: TextView
    private lateinit var btnTextChat: CardView
    private lateinit var btnCall: CardView
    private lateinit var btnSendEmail: CardView
    private lateinit var btnCopyPhoneNumber: ImageView
    private lateinit var btnCopyEmail: ImageView
    private lateinit var tvContactName: TextView
    private lateinit var tvSecondContactInfo: TextView
    private lateinit var btnView: ImageView
    private lateinit var cvAddress: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViews()
        setListeners()
    }

    private fun setListeners() {
        val phoneNumber = tvPhoneNumber.text.toString()
        val email = tvEmail.text.toString()
        btnTextChat.setOnClickListener { ContactActionHelper.sendSms(requireContext(), phoneNumber) }
        btnCall.setOnClickListener { ContactActionHelper.call(requireContext(), phoneNumber) }
        btnSendEmail.setOnClickListener { ContactActionHelper.sendEmail(requireContext(), email) }
        btnCopyPhoneNumber.setOnClickListener { ContactActionHelper.copyToClipboard(requireContext(), "Số điện thoại", phoneNumber) }
        btnCopyEmail.setOnClickListener { ContactActionHelper.copyToClipboard(requireContext(), "Email", email) }
        btnView.setOnClickListener { viewClass() }
    }

    private fun viewClass() {
        val intent = Intent(requireContext(), DepartmentDetailActivity::class.java).apply {
            putExtra("departmentSelected", student!!.department)
        }
        startActivity(intent)
    }

    private fun setViews() {
        getViewReferences()
        getIntentData()
        tvSecondContactInfo.visibility = View.GONE
        if (student == null) return

        tvContactName.text = student!!.fullName
        tvCode.text = student!!.code
        tvPhoneNumber.text = student!!.phone
        tvEmail.text = student!!.email
        tvClass.text = student!!.department.name
        val address = student!!.address

        if (address.isNullOrBlank()) {
            cvAddress.visibility = View.GONE
        } else {
            tvAddress.text = address
        }
    }

    private fun getIntentData() {
        student = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireActivity().intent.getParcelableExtra("studentSelected", Student::class.java)
        } else {
            @Suppress("DEPRECATION")
            requireActivity().intent.getParcelableExtra("studentSelected")
        }
    }

    private fun getViewReferences() {
        tvContactName = requireView().findViewById(R.id.tv_contact_name)
        tvSecondContactInfo = requireView().findViewById(R.id.tv_second_contact_info)
        tvCode = requireView().findViewById(R.id.tv_code)
        tvPhoneNumber = requireView().findViewById(R.id.tv_phone_number)
        tvEmail = requireView().findViewById(R.id.tv_email)
        tvClass = requireView().findViewById(R.id.tv_class)
        tvAddress = requireView().findViewById(R.id.tv_address)
        btnTextChat = requireView().findViewById(R.id.text_chat_btn)
        btnCall = requireView().findViewById(R.id.call_btn)
        btnSendEmail = requireView().findViewById(R.id.send_email_btn)
        btnCopyPhoneNumber = requireView().findViewById(R.id.btn_copy_phone_number)
        btnCopyEmail = requireView().findViewById(R.id.btn_copy_email)
        btnView = requireView().findViewById(R.id.btn_view)
        cvAddress = requireView().findViewById(R.id.cv_address)
    }
}