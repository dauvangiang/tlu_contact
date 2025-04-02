package com.mobile.group.tlucontact.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.mobile.group.tlucontact.activities.DepartmentDetailActivity
import com.mobile.group.tlucontact.adapter.DepartmentAdapter
import com.mobile.group.tlucontact.models.Department
import com.mobile.group.tlucontact.services.department.DepartmentServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DepartmentHelper {
    companion object {
        fun fetchDepartments(viewLifecycleOwner: LifecycleOwner, departments: MutableList<Department>, adapter: DepartmentAdapter) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val departmentService = DepartmentServiceImpl()
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzMxMjY0NSwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzMzEyNjQ1LCJleHAiOjE3NDMzMTYyNDUsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.iMF765ggQQE5TUPcuqFUl5MNZXwwXJ6HFzf1tpKU60lloFKal6Knn_da5ZtZwQGDk3GbcABSfN-74pp27lF95nyoXl-byWn0JTN3vB_osePOw5WXzlUTVcDCo8yELbCAODWi9DnrD6jRsGUFLGFfcpcnNCQkboBeEqKa_Wmqen0KvHSnxhhNHp4fLj-y6rpbar-QkML9B8M_4TgNa4HGJnuGkD1ZNhTwFJtTyVWItg_jKS_GHsBWvWdP0MtOQiD0Hi6pN_nuwNGSG2JacxHsQpZ-OjFCQ4JCFrO_5KmF0aMr2e_iTM-7OC6oJsu8xo983Co9yOWRckY2ZXPWin5XbA"

                    val result = departmentService.getDepartments(token) ?: mutableListOf()
                    departments.clear()
                    departments.addAll(result)
                    adapter.filter(result)
                } catch (e: Exception) {
                    Log.e("DepartmentListFragment", "Error fetching departments", e)
                }
            }
        }

        fun getDepartment(viewLifecycleOwner: LifecycleOwner, context: Context, code: String) {
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val departmentService = DepartmentServiceImpl()
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzU2MTU5NiwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzNTYxNTk2LCJleHAiOjE3NDM1NjUxOTYsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.lfafRLM4nJHfekTrSytvZUaBRycUtSKppYPYuqMZILTdsDOLMfalOH1vv-ss_lrlXAmATEX9oziGmrEqXp2pQsn-YLu4dUA2HfpQU1ElxfBu7ZuUiWyVF-pgG4U7MWcWA1_-WWMfu6VnjvGiOEPwE9Fhl_BbPu35zO9DZ7qSrETABR03iwfY4jLh4d-xd-wRrv5QyBqnDnGpkTxGK5PvfqXTlQceivkArM6pu2ecM9g9xuuiufeK13Ka6cZCknPeAbcsuCcszQpFZx69JVRVWgamKecDi5TFg3UmnbwpT6Stny7u2fpdEQn59Rhzv2uRzWg1nIZMIkotAtLRwJzZ5w"
                    val result = departmentService.getDepartment(token, code)
                    if (result != null) {
                        val intent = Intent(context, DepartmentDetailActivity::class.java).apply {
                            putExtra("departmentSelected", result)
                        }
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Đã có lỗi xảy ra. Vui lòng thử lại sau!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("DepartmentListFragment", "Error fetching departments", e)
                }
            }
        }
    }
}