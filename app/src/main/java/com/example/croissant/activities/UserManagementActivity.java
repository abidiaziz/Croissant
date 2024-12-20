package com.example.croissant.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.R;
import com.example.croissant.adapters.UserAdapter;
import com.example.croissant.entities.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends BaseActivity implements UserAdapter.OnUserStatusChangeListener {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> users;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        setHeaderTitle("Users management");
        setupToolbar();

        recyclerView = findViewById(R.id.recyclerViewUsers);
        users = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        adapter = new UserAdapter(users, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();
    }

    private void loadUsers() {
        db.collection("users")
                .whereEqualTo("isAdmin", false)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    users.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        User user = doc.toObject(User.class);
                        user.setId(doc.getId());
                        users.add(user);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onUserStatusChange(User user) {
        db.collection("users").document(user.getId())
                .update("isActive", !user.getIsActive())
                .addOnSuccessListener(aVoid -> {
                    user.setIsActive(!user.getIsActive());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "User status updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update user status", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected boolean isUserAdmin() {
        return true;
    }
}

