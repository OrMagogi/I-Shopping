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

import com.example.ishopping.Data.ShoppingList;
import com.example.ishopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SingleShoppingListFragment extends Fragment {

    private ShoppingListViewModel mViewModel;
    private Spinner searchProductsSpinner;
    private NavController navController;
    private ImageButton addNewProductButton;
    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference productsRef;
    private String selectedProductName;
    private ShoppingList shoppingList;
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
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        addNewProductButton= view.findViewById(R.id.add_new_product_button);
        listHeadline= view.findViewById(R.id.product_list_headline);
        searchProductsSpinner= view.findViewById(R.id.search_product_spinner);
        searchProductsSpinner.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.products_spinner_dropdown_item,ShoppingListsFragment.getExistingProducts()));
        firebaseDatabase= FirebaseDatabase.getInstance();
        shoppingList=new ShoppingList();
        listHeadline.setText(listHeadline.getText()+" - "+shoppingList.getDate());
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
            Toast.makeText(getActivity(), selectedProductName, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


}