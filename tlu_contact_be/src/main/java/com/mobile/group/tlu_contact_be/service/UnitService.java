package com.mobile.group.tlu_contact_be.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.mobile.group.tlu_contact_be.model.Unit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UnitService {

    private final Firestore db;

    public UnitService(FirebaseApp firebaseApp) {
        this.db = FirestoreClient.getFirestore();
    }

    public String createUnit(Unit unit) {
        try {
            ApiFuture<WriteResult> future = db.collection("units").document(unit.getCode()).set(unit);
            future.get(); // Đợi thao tác hoàn thành
            return unit.getCode();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create unit", e);
        }
    }

    public Unit getUnit(String unitId) {
        try {
            DocumentReference docRef = db.collection("units").document(unitId);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Unit.class);
            }
            return null;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get unit", e);
        }
    }

    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection("units").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                units.add(document.toObject(Unit.class));
            }
            return units;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get all units", e);
        }
    }

    public void updateUnit(String unitId, Unit unit) {
        try {
            db.collection("units").document(unitId).set(unit).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update unit", e);
        }
    }

    public void deleteUnit(String unitId) {
        try {
            db.collection("units").document(unitId).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete unit", e);
        }
    }
}