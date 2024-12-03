package com.example.croissant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;
    private OnUserStatusChangeListener listener;

    public UserAdapter(List<User> users, OnUserStatusChangeListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvEmail.setText(user.getEmail());
        holder.tvStatus.setText(user.getIsActive() ? "Active" : "Inactive");

        holder.itemView.setOnClickListener(v -> listener.onUserStatusChange(user));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmail;
        TextView tvStatus;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvStatus = itemView.findViewById(R.id.tvUserStatus);
        }
    }

    interface OnUserStatusChangeListener {
        void onUserStatusChange(User user);
    }
}

