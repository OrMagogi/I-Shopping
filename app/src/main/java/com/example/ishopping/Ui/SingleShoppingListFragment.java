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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.Data.ShoppingList;
import com.example.ishopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class SingleShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private Spinner searchProductsSpinner;
    private NavController navController;
    private ImageButton addNewProductButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference productsRef;
    private String selectedProductName;
    private HashMap<String,String> existingProducts;
    private static ShoppingList shoppingList;
    private ArrayList<Product> currentProductsList;
    private TextView listHeadline;

    public static SingleShoppingListFragment newInstance() {
        return new SingleShoppingListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_shopping_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShoppingListViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        existingProducts = ShoppingListsFragment.getExistingProducts();
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        addNewProductButton= view.findViewById(R.id.add_new_product_button);
        listHeadline= view.findViewById(R.id.product_list_headline);
        searchProductsSpinner= view.findViewById(R.id.search_product_spinner);
        searchProductsSpinner.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.products_spinner_dropdown_item,ShoppingListsFragment.getExistingProducts().keySet().toArray()));
        firebaseDatabase= FirebaseDatabase.getInstance();
        productsRef = firebaseDatabase.getReference().child(ConstantValues.shoppingLists).child(shoppingList.getDate()).child("productList");
        listHeadline.setText(listHeadline.getText()+" - "+shoppingList.getDate());
        currentProductsList=new ArrayList<Product>();
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    currentProductsList.add((Product) data.getValue(Product.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setListeners();
    }

    private void setListeners(){
        ItemSelectionListener itemSelectionListener=new ItemSelectionListener();
        SingleShoppingListFragment.ClickListener clickListener=new SingleShoppingListFragment.ClickListener();
        searchProductsSpinner.setOnItemSelectedListener(itemSelectionListener);
        addNewProductButton.setOnClickListener(clickListener);
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_new_product_button:
                    navController.navigate(R.id.action_singleShoppingListFragment_to_addNewProductFragment);
                    break;
            }

        }
    }

    private class ItemSelectionListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedProductName= parent.getSelectedItem().toString();
            Product newProduct=new Product(selectedProductName,"1",existingProducts.get(selectedProductName),null,null);
            currentProductsList.add(newProduct);

            //Toast.makeText(getActivity(), selectedProductName +" "+ ShoppingListsFragment.getExistingProducts().get(selectedProductName), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public static ShoppingList getShoppingList() {
        return shoppingList;
    }

    public static void setShoppingList(ShoppingList shoppingList) {
        SingleShoppingListFragment.shoppingList = shoppingList;
    }

    private void updateProducts(ArrayList<Product> newProducts){
        productsRef.setValue(newProducts);
    }

    private void showProductsList(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productsRef.setValue(currentProductsList);
    }
}