package com.mobile.group.tlucontact.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mobile.group.tlucontact.R
import com.mobile.group.tlucontact.fragment.DepartmentDetailFragment

class DepartmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_detail)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DepartmentDetailFragment()) // Load Fragment vào container
            .commit()
    }

    fun goBack(view: View) {
        finish()
    }
}