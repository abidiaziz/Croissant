package com.example.croissant;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserComparisonActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserComparisonAdapter adapter;
    private List<UserComparison> userComparisons;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comparison);

        recyclerView = findViewById(R.id.recyclerViewUserComparison);
        userComparisons = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        adapter = new UserComparisonAdapter(userComparisons);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        compareUsers();
    }

    private void compareUsers() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(currentUserId).collection("favorites")
                .get()
                .addOnSuccessListener(currentUserFavorites -> {
                    Set<String> currentUserFavoriteIds = new HashSet<>();
                    for (DocumentSnapshot doc : currentUserFavorites) {
                        currentUserFavoriteIds.add(doc.getId());
                    }

                    db.collection("users")
                            .get()
                            .addOnSuccessListener(allUsers -> {
                                for (DocumentSnapshot userDoc : allUsers) {
                                    if (!userDoc.getId().equals(currentUserId)) {
                                        compareUserFavorites(userDoc, currentUserFavoriteIds);
                                    }
                                }
                            });
                });
    }

    private void compareUserFavorites(DocumentSnapshot userDoc, Set<String> currentUserFavoriteIds) {
        db.collection("users").document(userDoc.getId()).collection("favorites")
                .get()
                .addOnSuccessListener(otherUserFavorites -> {
                    Set<String> commonFavorites = new HashSet<>();
                    for (DocumentSnapshot doc : otherUserFavorites) {
                        if (currentUserFavoriteIds.contains(doc.getId())) {
                            commonFavorites.add(doc.getId());
                        }
                    }

                    double percentage = (double) commonFavorites.size() / currentUserFavoriteIds.size() * 100;
                    if (percentage >= 70) {
                        UserComparison comparison = new UserComparison(userDoc.getString("email"), percentage);
                        userComparisons.add(comparison);
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}