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
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzU2MTU5NiwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzNTYxNTk2LCJleHAiOjE3NDM1NjUxOTYsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.lfafRLM4nJHfekTrSytvZUaBRycUtSKppYPYuqMZILTdsDOLMfalOH1vv-ss_lrlXAmATEX9oziGmrEqXp2pQsn-YLu4dUA2HfpQU1ElxfBu7ZuUiWyVF-pgG4U7MWcWA1_-WWMfu6VnjvGiOEPwE9Fhl_BbPu35zO9DZ7qSrETABR03iwfY4jLh4d-xd-wRrv5QyBqnDnGpkTxGK5PvfqXTlQceivkArM6pu2ecM9g9xuuiufeK13Ka6cZCknPeAbcsuCcszQpFZx69JVRVWgamKecDi5TFg3UmnbwpT6Stny7u2fpdEQn59Rhzv2uRzWg1nIZMIkotAtLRwJzZ5w"

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