package com.example.croissant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.R;
import com.example.croissant.entities.Film;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    private List<Film> films;
    private OnFilmClickListener listener;
    private boolean isAdmin;

    public FilmAdapter(List<Film> films, OnFilmClickListener listener, boolean isAdmin) {
        this.films = films;
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = films.get(position);
        holder.title.setText(film.getTitle());
        holder.director.setText("Director: " + film.getDirector());
        holder.releaseYear.setText("Year: " + film.getReleaseYear());

        if (isAdmin) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> listener.onDeleteFilm(film));
        } else {
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onFilmClick(film));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        TextView title, director, releaseYear;
        Button btnDelete;

        FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            director = itemView.findViewById(R.id.director);
            releaseYear = itemView.findViewById(R.id.releaseYear);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnFilmClickListener {
        void onFilmClick(Film film);
        void onDeleteFilm(Film film);
    }
}