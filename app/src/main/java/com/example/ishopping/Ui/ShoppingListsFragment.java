package com.example.ishopping.Ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingListsFragment extends Fragment {

    private ShoppingListsViewModel mViewModel;
    private FloatingActionButton addNewListButton;
    private NavController navController;
    //
    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference productsRef;
    private Product productFromDb;
    private static ArrayList<String> existingProducts;

    //

    public static ShoppingListsFragment newInstance() {
        return new ShoppingListsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_lists_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShoppingListsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addNewListButton= view.findViewById(R.id.add_new_list_button);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        firebaseDatabase= FirebaseDatabase.getInstance();
        existingProducts= new ArrayList<String>();
        productsRef= firebaseDatabase.getReference().child(ConstantValues.products);
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    productFromDb = (Product) data.getValue(Product.class);
                    existingProducts.add(productFromDb.getProductName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setListeners();
    }

    private void setListeners(){
        ClickListener clickListener=new ClickListener();
        addNewListButton.setOnClickListener(clickListener);
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_new_list_button:
                    navController.navigate(R.id.action_shoppingListsFragment_to_singleShoppingListFragment);
                    break;
            }

        }
    }

    public static ArrayList<String> getExistingProducts() {
        return existingProducts;
    }
}