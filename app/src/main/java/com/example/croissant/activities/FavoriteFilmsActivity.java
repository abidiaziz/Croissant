package com.example.croissant.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.entities.Film;
import com.example.croissant.adapters.FilmAdapter;
import com.example.croissant.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFilmsActivity extends BaseActivity implements FilmAdapter.OnFilmClickListener {
    private RecyclerView recyclerView;
    private FilmAdapter adapter;
    private List<Film> favoriteFilms;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_films);

        setHeaderTitle("Favorite Films");
        setupToolbar();

        recyclerView = findViewById(R.id.recyclerViewFavoriteFilms);
        favoriteFilms = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        adapter = new FilmAdapter(favoriteFilms, this, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFavoriteFilms();
    }

    private void loadFavoriteFilms() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("favorites")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String filmId = document.getString("filmId");
                        if (filmId != null) {
                            loadFilmDetails(filmId);
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading favorite films", Toast.LENGTH_SHORT).show());
    }

    private void loadFilmDetails(String filmId) {
        db.collection("films").document(filmId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Film film = documentSnapshot.toObject(Film.class);
                    if (film != null) {
                        film.setId(documentSnapshot.getId());
                        favoriteFilms.add(film);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading film details", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onFilmClick(Film film) {
        // Handle film click if needed
    }

    @Override
    public void onDeleteFilm(Film film) {
        // Not applicable for favorite films
    }

    @Override
    protected boolean isUserAdmin() {
        return false;
    }
}

