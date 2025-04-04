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
                    //val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzMxMjY0NSwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzMzEyNjQ1LCJleHAiOjE3NDMzMTYyNDUsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.iMF765ggQQE5TUPcuqFUl5MNZXwwXJ6HFzf1tpKU60lloFKal6Knn_da5ZtZwQGDk3GbcABSfN-74pp27lF95nyoXl-byWn0JTN3vB_osePOw5WXzlUTVcDCo8yELbCAODWi9DnrD6jRsGUFLGFfcpcnNCQkboBeEqKa_Wmqen0KvHSnxhhNHp4fLj-y6rpbar-QkML9B8M_4TgNa4HGJnuGkD1ZNhTwFJtTyVWItg_jKS_GHsBWvWdP0MtOQiD0Hi6pN_nuwNGSG2JacxHsQpZ-OjFCQ4JCFrO_5KmF0aMr2e_iTM-7OC6oJsu8xo983Co9yOWRckY2ZXPWin5XbA"
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0Mzc0ODgwOSwidXNlcl9pZCI6ImprQTJFWkZXYjJXNGsweDNEQVlWVmZhUDc2TjIiLCJzdWIiOiJqa0EyRVpGV2IyVzRrMHgzREFZVlZmYVA3Nk4yIiwiaWF0IjoxNzQzNzQ4ODEwLCJleHAiOjE3NDM3NTI0MTAsImVtYWlsIjoiMjI1MTE3MjM2N0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMTcyMzY3QGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.cCOyfIOLVGCDGHKy_KyWMjZwhM_H7ZG2aWdktIVx3v9AZiyPamoi1PH_BDYQk7X74TprO9d9V1tWEl72kuQPYXvhWpU3ZOWEXSgez-0DdVNqtNGOgOEIzSbCzH89gqZUY3XwTKDKkV_0scctAnbIabViyq_5GO_FjN9gzgvVOvnLsWJpv4HD5uFX5cybKW-zvw-Mbh7vfIWtaK9DNBL9-1v9FCGeXEvofm3AGATJ3ScNheX9mseHvWyVBbA9iSKhRX4ZtcW8WY7d7eKNzMqW7Om-cATzpAZWgBlrmFy0VtKhI5YXXquCoJHjrKohCb7hpAxlOpQyXoIjJOpRlCc1ew"
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
                    //2025-04-03 16:10:36.223  9322-9322  SharedPreferences       com.mobile.group.tlucontact          D  Auth Token: eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzY3Mzk5NCwidXNlcl9pZCI6ImprQTJFWkZXYjJXNGsweDNEQVlWVmZhUDc2TjIiLCJzdWIiOiJqa0EyRVpGV2IyVzRrMHgzREFZVlZmYVA3Nk4yIiwiaWF0IjoxNzQzNjczOTk1LCJleHAiOjE3NDM2Nzc1OTUsImVtYWlsIjoiMjI1MTE3MjM2N0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMTcyMzY3QGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.OGVmd0vrhPqJ3Vzs7RNc3UL_dzaK8rXeg8C2t_kYjVwhcmprSumPhV4XCJCw-8XRazlt2GX_dLZNgNUPcbRQY08cIQbM_uTmqUF4KFT9OGZ4Y_jvGjZVgXJPw4YoyInxtytNibdI4rirX6ozfwqyGCDTg0eggk9VeMWEghGIxEYLXOVC-QViI4OqJ2hA3DSPZcMJAZhGfU-QdY7OLXHH_x5hVbLWOXGGWQcrts1qwv-UGaSpIRixr6r39ICjY2eyrYaihJKZ0V-UVD705wnolfFzak1gx7qzwqckJxXYhodORs9cnms5NNlx7IHe3k7wbdQNWmSUD7endGSB0x4aug
                    val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0Mzc0ODgwOSwidXNlcl9pZCI6ImprQTJFWkZXYjJXNGsweDNEQVlWVmZhUDc2TjIiLCJzdWIiOiJqa0EyRVpGV2IyVzRrMHgzREFZVlZmYVA3Nk4yIiwiaWF0IjoxNzQzNzQ4ODEwLCJleHAiOjE3NDM3NTI0MTAsImVtYWlsIjoiMjI1MTE3MjM2N0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMTcyMzY3QGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.cCOyfIOLVGCDGHKy_KyWMjZwhM_H7ZG2aWdktIVx3v9AZiyPamoi1PH_BDYQk7X74TprO9d9V1tWEl72kuQPYXvhWpU3ZOWEXSgez-0DdVNqtNGOgOEIzSbCzH89gqZUY3XwTKDKkV_0scctAnbIabViyq_5GO_FjN9gzgvVOvnLsWJpv4HD5uFX5cybKW-zvw-Mbh7vfIWtaK9DNBL9-1v9FCGeXEvofm3AGATJ3ScNheX9mseHvWyVBbA9iSKhRX4ZtcW8WY7d7eKNzMqW7Om-cATzpAZWgBlrmFy0VtKhI5YXXquCoJHjrKohCb7hpAxlOpQyXoIjJOpRlCc1ew"

                    //val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImE5ZGRjYTc2YzEyMzMyNmI5ZTJlODJkOGFjNDg0MWU1MzMyMmI3NmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vdGx1LWNvbnRhY3QtYzBjNjkiLCJhdWQiOiJ0bHUtY29udGFjdC1jMGM2OSIsImF1dGhfdGltZSI6MTc0MzMxMjY0NSwidXNlcl9pZCI6IkdmZ0o3M1RqVmpoSHB6c0tKbUZhNzNRdUJEZjIiLCJzdWIiOiJHZmdKNzNUalZqaEhwenNLSm1GYTczUXVCRGYyIiwiaWF0IjoxNzQzMzEyNjQ1LCJleHAiOjE3NDMzMTYyNDUsImVtYWlsIjoiMjI1MTA2MTc2M0BlLnRsdS5lZHUudm4iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyIyMjUxMDYxNzYzQGUudGx1LmVkdS52biJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.iMF765ggQQE5TUPcuqFUl5MNZXwwXJ6HFzf1tpKU60lloFKal6Knn_da5ZtZwQGDk3GbcABSfN-74pp27lF95nyoXl-byWn0JTN3vB_osePOw5WXzlUTVcDCo8yELbCAODWi9DnrD6jRsGUFLGFfcpcnNCQkboBeEqKa_Wmqen0KvHSnxhhNHp4fLj-y6rpbar-QkML9B8M_4TgNa4HGJnuGkD1ZNhTwFJtTyVWItg_jKS_GHsBWvWdP0MtOQiD0Hi6pN_nuwNGSG2JacxHsQpZ-OjFCQ4JCFrO_5KmF0aMr2e_iTM-7OC6oJsu8xo983Co9yOWRckY2ZXPWin5XbA"
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