package com.example.ishopping.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ishopping.R;
import com.example.ishopping.Ui.ShoppingListsFragment;

public class ShoppingActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ShoppingListsFragment.newInstance())
                    .commitNow();
        }
    }
}