package com.mobile.group.tlu_contact_be.repositories;

import com.google.cloud.firestore.*;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
@Getter
public class DepartmentTypeRepository {
    private final CollectionReference typeCollection;
    private final CollectionReference departmentCollection;

    public DepartmentTypeRepository(Firestore firestore) {
        typeCollection = firestore.collection("department_types");
        departmentCollection = firestore.collection("departments");
    }

    public List<QueryDocumentSnapshot> getDepartmentTypes() throws ExecutionException, InterruptedException {
        Query query = typeCollection.whereEqualTo("deleted", false)
                .orderBy("name");
        return query.get().get().getDocuments();
    }

    public List<QueryDocumentSnapshot> getFilterDepartments(String filterId, String parentId) throws ExecutionException, InterruptedException {
        Query query = departmentCollection.whereEqualTo("deleted", false);

        if (parentId == null) {
            query = query.whereEqualTo("typeId", filterId);
        } else {
            query = query.whereEqualTo("parentDepartmentId", parentId);
        }

        return query.orderBy("name").get().get().getDocuments();
    }

    public QueryDocumentSnapshot getTypeByID(String id) throws ExecutionException, InterruptedException {
        Query query = typeCollection.whereEqualTo("id", id)
                .whereEqualTo("deleted", false);
        QuerySnapshot querySnapshot = query.get().get();
        return !querySnapshot.isEmpty() ? query.get().get().getDocuments().getFirst() : null;
    }
}
