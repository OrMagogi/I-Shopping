package com.example.ishopping.Ui;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ishopping.Activities.ShoppingActivity;
import com.example.ishopping.R;

import java.util.ArrayList;

public class SingleShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private ArrayList<String> previousProducts;
    private Spinner searchProductsSpinner;

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
        searchProductsSpinner= view.findViewById(R.id.search_product_spinner);
        previousProducts=new ArrayList<String>();
        previousProducts.add("milk");
        previousProducts.add("bread");
        previousProducts.add("butter");
        searchProductsSpinner.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.products_spinner_dropdown_item,previousProducts));

    }
}