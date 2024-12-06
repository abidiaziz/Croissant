package com.example.croissant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.R;
import com.example.croissant.adapters.FilmAdapter;
import com.example.croissant.entities.Film;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import android.widget.EditText;

public class MainActivity extends BaseActivity implements FilmAdapter.OnFilmClickListener {
    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private List<Film> films;
    private FirebaseFirestore db;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeaderTitle("Film list");
        setContentView(R.layout.activity_main);


        setupToolbar();

        recyclerView = findViewById(R.id.recyclerView);
        films = new ArrayList<>();
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        checkUserRole();
    }

    private void checkUserRole() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("MainActivity", "No user is signed in");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        String uid = currentUser.getUid();
        Log.d("MainActivity", "Checking role for user: " + uid);

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("MainActivity", "Successfully retrieved user document");
                    if (documentSnapshot.exists()) {
                        isAdmin = documentSnapshot.getBoolean("isAdmin");
                        boolean isActive = documentSnapshot.getBoolean("isActive");
                        Log.d("MainActivity", "User is admin: " + isAdmin + ", is active: " + isActive);
                        if (!isActive) {
                            showDeactivatedMessage();
                        } else {
                            setupRecyclerView();
                            loadFilms();
                            invalidateOptionsMenu();
                        }
                    } else {
                        Log.e("MainActivity", "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MainActivity", "Error getting user document", e);
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showDeactivatedMessage() {
        setContentView(R.layout.activity_deactivated);
        TextView tvMessage = findViewById(R.id.tvDeactivatedMessage);
        tvMessage.setText("Your account has been deactivated. Please contact the admin.");
    }

    private void setupRecyclerView() {
        adapter = new FilmAdapter(films, this, isAdmin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFilms() {
        db.collection("films")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading films: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("MainActivity", "Error loading films", error);
                        return;
                    }

                    films.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Film film = doc.toObject(Film.class);
                        film.setId(doc.getId());
                        films.add(film);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public void onFilmClick(Film film) {
        if (!isAdmin) {
            toggleFavorite(film);
        }
    }

    @Override
    public void onDeleteFilm(Film film) {
        if (isAdmin) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Film")
                    .setMessage("Are you sure you want to delete this film? It will also be removed from all users' favorites.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        deleteFilmAndFavorites(film);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    private void deleteFilmAndFavorites(Film film) {
        db.collection("films").document(film.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Film deleted", Toast.LENGTH_SHORT).show();
                    deleteFilmFromAllUsersFavorites(film);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to delete film", Toast.LENGTH_SHORT).show());
    }


    private void deleteFilmFromAllUsersFavorites(Film film) {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot userDoc : queryDocumentSnapshots) {
                        db.collection("users").document(userDoc.getId())
                                .collection("favorites").document(film.getTitle())
                                .delete()
                                .addOnFailureListener(e -> Log.e("MainActivity", "Error deleting favorite for user: " + userDoc.getId(), e));
                    }
                })
                .addOnFailureListener(e -> Log.e("MainActivity", "Error fetching users to delete favorites", e));
    }

    private void toggleFavorite(Film film) {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("users").document(uid).collection("favorites")
                .document(film.getTitle())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            document.getReference().delete();
                            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> favorite = new HashMap<>();
                            favorite.put("filmId", film.getId());
                            db.collection("users").document(uid).collection("favorites").document(film.getTitle()).set(favorite);
                            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void showAddFilmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_add_film, null);
        EditText etTitle = view.findViewById(R.id.etTitle);
        EditText etDirector = view.findViewById(R.id.etDirector);
        EditText etReleaseYear = view.findViewById(R.id.etReleaseYear);

        builder.setView(view)
                .setTitle("Add New Film")
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String director = etDirector.getText().toString().trim();
                    int releaseYear = Integer.parseInt(etReleaseYear.getText().toString().trim());
                    addFilm(title, director, releaseYear);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addFilm(String title, String director, int releaseYear) {
        Film newFilm = new Film(UUID.randomUUID().toString(), title, director, releaseYear);
        db.collection("films").add(newFilm)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Film added successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding film: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected boolean isUserAdmin() {
        return isAdmin;
    }
}

