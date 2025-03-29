package com.mobile.group.tlu_contact_be.repositories;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
@Getter
public class StudentRepository {
    private final CollectionReference collection = FirestoreClient.getFirestore().collection("students");

    public List<QueryDocumentSnapshot> getStudents(Integer page, Integer size, Boolean sort, String search, Boolean deleted) throws ExecutionException, InterruptedException {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filter.equalTo("deleted", Objects.requireNonNullElse(deleted, false)));

        Query query = collection.where(Filter.and(filters.toArray(new Filter[0])));

        if (sort != null && sort) {
            query = query.orderBy("fullName");
        }
        if (page != null && size != null) {
            int offset = page * size;
            query = query.limit(size).offset(offset);
        }

        ApiFuture<QuerySnapshot> future = query.get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> docs = querySnapshot.getDocuments();

        if (search != null && !search.isEmpty()) {
            String searchLower = search.toLowerCase();
            docs = docs.stream()
                    .filter(doc -> Objects.requireNonNull(doc.getString("fullName")).toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        return docs;
    }

    public QueryDocumentSnapshot getStudent(String id) throws ExecutionException, InterruptedException {
        Filter filter = Filter.and(
                Filter.equalTo("deleted", false),
                Filter.equalTo("studentId", id)
        );

        QuerySnapshot querySnapshot = collection.where(filter).get().get();
        return querySnapshot.isEmpty() ? null : querySnapshot.getDocuments().getFirst();
    }
}
