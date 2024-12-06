package com.example.croissant.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.croissant.R;
import com.example.croissant.entities.UserComparison;

import java.util.List;

public class UserComparisonAdapter extends RecyclerView.Adapter<UserComparisonAdapter.UserComparisonViewHolder> {
    private List<UserComparison> userComparisons;

    public UserComparisonAdapter(List<UserComparison> userComparisons) {
        this.userComparisons = userComparisons;
    }

    @NonNull
    @Override
    public UserComparisonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_comparison, parent, false);
        return new UserComparisonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserComparisonViewHolder holder, int position) {
        UserComparison comparison = userComparisons.get(position);
        holder.tvUserName.setText(comparison.getFirstName() + " " + comparison.getLastName());
        holder.tvMatchPercentage.setText(String.format("%.1f%%", comparison.getMatchPercentage()));
    }

    @Override
    public int getItemCount() {
        return userComparisons.size();
    }

    static class UserComparisonViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvMatchPercentage;

        UserComparisonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvMatchPercentage = itemView.findViewById(R.id.tvMatchPercentage);
        }
    }
}

