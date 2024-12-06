package com.example.croissant.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.R;
import com.example.croissant.adapters.UserComparisonAdapter;
import com.example.croissant.entities.UserComparison;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserComparisonActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private UserComparisonAdapter adapter;
    private List<UserComparison> userComparisons;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_comparison);

        setHeaderTitle("Users with same favorites");
        setupToolbar();

        recyclerView = findViewById(R.id.recyclerViewComparison);
        userComparisons = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

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
                        String firstName = userDoc.getString("firstName");
                        String lastName = userDoc.getString("lastName");
                        UserComparison comparison = new UserComparison(userDoc.getId(), firstName, lastName, percentage);
                        userComparisons.add(comparison);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    protected boolean isUserAdmin() {
        return false;
    }
}

