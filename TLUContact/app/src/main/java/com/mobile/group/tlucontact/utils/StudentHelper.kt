package com.mobile.group.tlucontact.utils

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mobile.group.tlucontact.adapter.StudentAdapter
import com.mobile.group.tlucontact.models.Student
import com.mobile.group.tlucontact.services.student.StudentServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentHelper {
    companion object {
        fun fetchStudents(viewLifecycleOwner: LifecycleOwner, students: MutableList<Student>, adapter: StudentAdapter) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val studentService = StudentServiceImpl()
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzMxMjY0NSwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzMzEyNjQ1LCJleHAiOjE3NDMzMTYyNDUsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.iMF765ggQQE5TUPcuqFUl5MNZXwwXJ6HFzf1tpKU60lloFKal6Knn_da5ZtZwQGDk3GbcABSfN-74pp27lF95nyoXl-byWn0JTN3vB_osePOw5WXzlUTVcDCo8yELbCAODWi9DnrD6jRsGUFLGFfcpcnNCQkboBeEqKa_Wmqen0KvHSnxhhNHp4fLj-y6rpbar-QkML9B8M_4TgNa4HGJnuGkD1ZNhTwFJtTyVWItg_jKS_GHsBWvWdP0MtOQiD0Hi6pN_nuwNGSG2JacxHsQpZ-OjFCQ4JCFrO_5KmF0aMr2e_iTM-7OC6oJsu8xo983Co9yOWRckY2ZXPWin5XbA"

                    val result = withContext(Dispatchers.IO) {
                        studentService.getStudents(token) ?: mutableListOf()
                    }
                    students.clear()
                    students.addAll(result)
                    adapter.filter(result)
                } catch (e: Exception) {
                    Log.e("DepartmentListFragment", "Error fetching departments", e)
                }
            }
        }

    }
}