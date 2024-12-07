package com.example.croissant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.croissant.R;
import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    protected FirebaseAuth mAuth;
    protected TextView headerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupHeader();
        setupToolbar();
    }

    protected void setupHeader() {
        headerTitle = findViewById(R.id.headerTitle);
    }

    protected void setHeaderTitle(String title) {
        if (headerTitle != null) {
            headerTitle.setText(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isAdmin = isUserAdmin();
        menu.findItem(R.id.action_add_film).setVisible(isAdmin);
        menu.findItem(R.id.action_manage_users).setVisible(isAdmin);
        menu.findItem(R.id.action_user_comparisons).setVisible(!isAdmin);
        menu.findItem(R.id.action_favorite_films).setVisible(!isAdmin);
        menu.findItem(R.id.action_add_admin).setVisible(isAdmin);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add_film) {
            if (this instanceof MainActivity) {
                ((MainActivity) this).showAddFilmDialog();
            }
            return true;
        } else if (itemId == R.id.action_manage_users) {
            startActivity(new Intent(this, UserManagementActivity.class));
            return true;
        } else if (itemId == R.id.action_add_admin) {
            startActivity(new Intent(this, AdminRegistrationActivity.class));
            return true;
        } else if (itemId == R.id.action_user_comparisons) {
            startActivity(new Intent(this, UserComparisonActivity.class));
            return true;
        } else if (itemId == R.id.action_favorite_films) {
            startActivity(new Intent(this, FavoriteFilmsActivity.class));
            return true;
        } else if (itemId == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected boolean isUserAdmin() {
        return false;
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

