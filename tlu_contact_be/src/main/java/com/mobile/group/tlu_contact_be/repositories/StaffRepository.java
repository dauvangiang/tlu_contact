package com.mobile.group.tlu_contact_be.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.dto.response.staff.StaffRes;
import com.mobile.group.tlu_contact_be.model.Department;
import com.mobile.group.tlu_contact_be.model.Staff;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
@Getter
public class StaffRepository {

    private final CollectionReference collection;
    private final CollectionReference departmentCollection;

    public StaffRepository(Firestore firestore) {
        collection = firestore.collection("staffs");
        departmentCollection = firestore.collection("departments");
    }
    
    public List<Staff> findAll(Integer page, Integer size, Boolean sort, String search, Boolean deleted) throws ExecutionException, InterruptedException {
        Query query = collection;
        
        // Filter by deleted status
        if (deleted != null) {
            query = query.whereEqualTo("deleted", deleted);
        }
        
        // Search by name, email, or phone
        if (search != null && !search.trim().isEmpty()) {
            // Firebase không hỗ trợ tìm kiếm text trực tiếp, nên phải lấy tất cả docs và lọc ở client
            List<Staff> allStaff = getAllDocuments(query);
            
            String searchLower = search.toLowerCase();
            return allStaff.stream()
                    .filter(staff -> 
                            (staff.getFullName() != null && staff.getFullName().toLowerCase().contains(searchLower)) ||
                            (staff.getEmail() != null && staff.getEmail().toLowerCase().contains(searchLower)) ||
                            (staff.getPhone() != null && staff.getPhone().contains(search)))
                    .skip((page != null && size != null) ? (long) (page - 1) * size : 0)
                    .limit(size != null ? size : Integer.MAX_VALUE)
                    .collect(Collectors.toList());
        }
        
        // Sort by name
        if (sort != null) {
            query = query.orderBy("fullName", sort ? Query.Direction.ASCENDING : Query.Direction.DESCENDING);
        }
        
        // Pagination
        if (page != null && size != null) {
            query = query.limit(size);
            
            // If not the first page, we need to skip previous items
            if (page > 1) {
                // This is a simplified approach - for production, cursor-based pagination is better
                List<Staff> allStaff = getAllDocuments(query.orderBy("fullName"));
                return allStaff.stream()
                        .skip((long) (page - 1) * size)
                        .limit(size)
                        .collect(Collectors.toList());
            }
        }
        
        return getAllDocuments(query);
    }
    
    public Staff findById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = collection.document(id).get().get();
        if (document.exists()) {
            return document.toObject(Staff.class);
        }
        return null;
    }
    
    public boolean existsByEmail(String email) throws ExecutionException, InterruptedException {
        return !collection.whereEqualTo("email", email).limit(1).get().get().isEmpty();
    }
    
    public boolean existsByPhone(String phone) throws ExecutionException, InterruptedException {
        return !collection.whereEqualTo("phone", phone).limit(1).get().get().isEmpty();
    }
    
    public String save(Staff staff) throws ExecutionException, InterruptedException {
        ApiFuture<WriteResult> future = collection.document(staff.getStaffId()).set(staff);
        future.get();
        return staff.getStaffId();
    }
    
    public void delete(String id) throws ExecutionException, InterruptedException {
        collection.document(id).delete().get();
    }
    
    public long count(Boolean deleted) throws ExecutionException, InterruptedException {
        Query query = collection;
        if (deleted != null) {
            query = query.whereEqualTo("deleted", deleted);
        }
        return query.get().get().size();
    }
    
    private List<Staff> getAllDocuments(Query query) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = query.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        
        List<Staff> staffList = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            staffList.add(document.toObject(Staff.class));
        }
        return staffList;
    }
    
    public Department findDepartmentById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = departmentCollection.document(id).get().get();
        if (document.exists()) {
            return document.toObject(Department.class);
        }
        return null;
    }
    
    public List<Department> findDepartmentsByIds(List<String> ids) throws ExecutionException, InterruptedException {
        List<Department> departments = new ArrayList<>();
        for (String id : ids) {
            Department department = findDepartmentById(id);
            if (department != null) {
                departments.add(department);
            }
        }
        return departments;
    }
} 