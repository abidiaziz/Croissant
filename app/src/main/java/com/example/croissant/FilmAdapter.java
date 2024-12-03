package com.example.croissant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tvTitle.setText(film.getTitle());
        holder.tvDirector.setText("Director: " + film.getDirector());
        holder.tvReleaseYear.setText("Year: " + film.getReleaseYear());

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
        TextView tvTitle, tvDirector, tvReleaseYear;
        Button btnDelete;

        FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDirector = itemView.findViewById(R.id.tvDirector);
            tvReleaseYear = itemView.findViewById(R.id.tvReleaseYear);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    interface OnFilmClickListener {
        void onFilmClick(Film film);
        void onDeleteFilm(Film film);
    }
}