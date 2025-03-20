package com.mobile.group.tlucontact.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.StudentDetailFragment

class StudentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, StudentDetailFragment())
            .commit()
    }
}