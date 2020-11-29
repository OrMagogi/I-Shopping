package com.example.ishopping.Ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.Data.ShoppingList;
import com.example.ishopping.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ShoppingListsFragment extends Fragment {

    private ShoppingListsViewModel mViewModel;
    private FloatingActionButton addNewListButton;
    private NavController navController;
    private static RecyclerView shoppingListsRecyclerView;
    private ArrayList<String> existingShoppingListsValidation;
    private ArrayList<String> existingShoppingListsDates;
    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference productsRef,shoppingListsRef;
    private Product productFromDb;
    private ShoppingList shoppingListFromDb,newShoppingList;
    private static HashMap<String,String> existingProducts;
    private FirebaseRecyclerAdapter<ShoppingList, ShoppingListsFragment.ShoppingListsViewHolder> firebaseRecyclerAdapter;


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
        shoppingListsRecyclerView = view.findViewById(R.id.shopping_lists_recycler);
        shoppingListsRecyclerView.setHasFixedSize(true);
        firebaseDatabase= FirebaseDatabase.getInstance();
        existingShoppingListsValidation = new ArrayList<String>();
        existingShoppingListsDates = new ArrayList<String>();
        existingProducts= new HashMap<String,String>();
        shoppingListsRef = firebaseDatabase.getReference().child(ConstantValues.shoppingLists);
        productsRef= firebaseDatabase.getReference().child(ConstantValues.products);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        shoppingListsRecyclerView.setLayoutManager(layoutManager);
        //showShoppingLists();
        setProductsListener();
        setShoppingListsListener();
        setClickListeners();
    }

    private void setClickListeners(){
        ClickListener clickListener=new ClickListener();
        addNewListButton.setOnClickListener(clickListener);
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_new_list_button:
                    if (existingShoppingListsValidation.contains("true")) {
                        Toast.makeText(getActivity(), "יש קניה לא סגורה", Toast.LENGTH_SHORT).show();
                    } else {
                        newShoppingList = new ShoppingList();
                        SingleShoppingListFragment.setShoppingList(newShoppingList);
                        shoppingListsRef.child(newShoppingList.getDate()).setValue(newShoppingList);
                        navController.navigate(R.id.action_shoppingListsFragment_to_singleShoppingListFragment);
                    }
                    break;
            }
        }
    }

    public static HashMap<String,String> getExistingProducts() {
        return existingProducts;
    }

    public void setProductsListener(){
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    productFromDb = (Product) data.getValue(Product.class);
                    existingProducts.put(productFromDb.getProductName(),productFromDb.getProductCategory());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setShoppingListsListener(){
        shoppingListsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    shoppingListFromDb = (ShoppingList) data.getValue(ShoppingList.class);
                    existingShoppingListsValidation.add(shoppingListFromDb.getIsOpen());
                    existingShoppingListsDates.add(shoppingListFromDb.getDate());
                }
                showShoppingLists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showShoppingLists() {
        FirebaseRecyclerOptions<ShoppingList> options = new FirebaseRecyclerOptions
                .Builder<ShoppingList>()
                .setQuery(shoppingListsRef, ShoppingList.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ShoppingList, ShoppingListsViewHolder>(options) {
            @NonNull
            @Override
            public ShoppingListsFragment.ShoppingListsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_in_recycler, parent, false);
                ShoppingListsFragment.ShoppingListsViewHolder viewHolder = new ShoppingListsFragment.ShoppingListsViewHolder(view);
                return viewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final ShoppingListsViewHolder holder, int position, @NonNull final ShoppingList model) {
                holder.shoppingListDate.setText(model.getDate());
                String status;
                if (model.getIsOpen().equals("true")) {
                    status = "פתוח";
                } else {
                    status = "סגור";
                }
                holder.shoppingListStatus.setText(status);
                holder.shoppingListLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SingleShoppingListFragment.setShoppingList(model);
                        navController.navigate(R.id.action_shoppingListsFragment_to_singleShoppingListFragment);
                    }
                });
            }
        };

        RecyclerView newShoppingListsRecyclerView = shoppingListsRecyclerView;
        newShoppingListsRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class ShoppingListsViewHolder extends RecyclerView.ViewHolder{
        TextView shoppingListDate;
        TextView shoppingListStatus;
        LinearLayout shoppingListLayout;

        public ShoppingListsViewHolder(@NonNull View itemView) {
            super(itemView);
            shoppingListDate = itemView.findViewById(R.id.shopping_list_in_recycler_date);
            shoppingListStatus = itemView.findViewById(R.id.shopping_list_in_recycler_status);
            shoppingListLayout = itemView.findViewById(R.id.shopping_list_in_recycler_layout);

        }
    }
}