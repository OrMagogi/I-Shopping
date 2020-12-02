package com.example.ishopping.Ui;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.Data.ShoppingList;
import com.example.ishopping.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private Button closeShoppingListButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference shoppingListRef,productsRef,statisticsRef;
    private String selectedProductName,costString;
    private HashMap<String,String> existingProducts;
    private static ShoppingList shoppingList;
    private ArrayList<Product> currentProductsList;
    private ArrayList<String> currentProductsNamesList;
    private RecyclerView productsRecycler;
    private int index;
    private LinearLayout listCostLayout;
    private EditText listCostEdittext;
    private TextView listHeadline,closedShoppingListCostTextview;
    private FirebaseRecyclerAdapter<Product, SingleShoppingListFragment.SingleShoppingListViewHolder> firebaseRecyclerAdapter;


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
        listCostLayout= view.findViewById(R.id.singleShoppingListCostLayout);
        listCostEdittext= view.findViewById(R.id.singleShoppingListCostEdittext);
        productsRecycler= view.findViewById(R.id.product_list_recycler);
        closeShoppingListButton= view.findViewById(R.id.closeShoppingListButton);
        addNewProductButton= view.findViewById(R.id.add_new_product_button);
        listHeadline= view.findViewById(R.id.product_list_headline);
        searchProductsSpinner= view.findViewById(R.id.search_product_spinner);
        closedShoppingListCostTextview= view.findViewById(R.id.closedShoppingListCostTextview);
        searchProductsSpinner.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.products_spinner_dropdown_item,ShoppingListsFragment.getExistingProducts().keySet().toArray()));
        firebaseDatabase= FirebaseDatabase.getInstance();
        shoppingListRef=firebaseDatabase.getReference().child(ConstantValues.shoppingLists).child(shoppingList.getDate());
        statisticsRef=firebaseDatabase.getReference().child(ConstantValues.statistics);
        productsRef = shoppingListRef.child("productList");
        listHeadline.setText(listHeadline.getText()+" - "+shoppingList.getDate());
        currentProductsList=new ArrayList<Product>();
        currentProductsNamesList=new ArrayList<String>();
        listCostLayout.setVisibility(View.INVISIBLE);
        closedShoppingListCostTextview.setVisibility(View.INVISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        productsRecycler.setLayoutManager(layoutManager);
        index=0;
        productsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren()){
                    currentProductsList.add((Product) data.getValue(Product.class));
                    currentProductsNamesList.add(currentProductsList.get(index).getProductName());
                    index++;
                }
                showProductsList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(shoppingList.getIsOpen().equals("true")){
            setListeners();
        } else{
            closedShoppingListCostTextview.setVisibility(view.VISIBLE);
            closedShoppingListCostTextview.setText("סכום הקניה:  "+ shoppingList.getCost()+ " שח");
            closeShoppingListButton.setText("הקניה סגורה");
            closeShoppingListButton.setClickable(false);
            searchProductsSpinner.setVisibility(View.INVISIBLE);
            addNewProductButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setListeners(){
        ItemSelectionListener itemSelectionListener=new ItemSelectionListener();
        SingleShoppingListFragment.ClickListener clickListener=new SingleShoppingListFragment.ClickListener();
        searchProductsSpinner.setOnItemSelectedListener(itemSelectionListener);
        closeShoppingListButton.setOnClickListener(clickListener);
        addNewProductButton.setOnClickListener(clickListener);
    }

    private class ClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_new_product_button:
                    navController.navigate(R.id.action_singleShoppingListFragment_to_addNewProductFragment);
                    break;
                case R.id.closeShoppingListButton:
                    if(listCostLayout.getVisibility()== getView().INVISIBLE){
                        listCostLayout.setVisibility(getView().VISIBLE);
                        Toast.makeText(getActivity(), "הכנס את סכום הקניה", Toast.LENGTH_SHORT).show();
                    } else {
                        costString = listCostEdittext.getText().toString();
                        if (costString.equals("")) {
                            Toast.makeText(getActivity(), "סכום הקניה שהוכנס אינו תקין", Toast.LENGTH_SHORT).show();
                        } else {
                            createAlertDialog("סכום הקניה שהוכנס: "+ costString +"\nהאם אתה בטוח שאתה רוצה לסגור את הקניה ?", ConstantValues.closeShoppingListAction, null);
                        }
                    }
                    break;
            }

        }
    }

    private class ItemSelectionListener implements AdapterView.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedProductName= parent.getSelectedItem().toString();
            if(currentProductsNamesList.contains(selectedProductName)){
                Toast.makeText(getActivity(), "המוצר כבר קיים ברשימה", Toast.LENGTH_SHORT).show();
            } else{
                Product newProduct=new Product(selectedProductName,"1",existingProducts.get(selectedProductName),null,null,"false");
                //currentProductsList.add(newProduct);
                currentProductsNamesList.add(selectedProductName);
                //productsRef.setValue(currentProductsList);
                productsRef.child(selectedProductName).setValue(newProduct);
            }
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
        FirebaseRecyclerOptions<Product> options = new FirebaseRecyclerOptions
                .Builder<Product>()
                .setQuery(productsRef, Product.class)
                .build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, SingleShoppingListFragment.SingleShoppingListViewHolder>(options) {
            @NonNull
            @Override
            public SingleShoppingListFragment.SingleShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_in_recycler, parent, false);
                SingleShoppingListFragment.SingleShoppingListViewHolder viewHolder = new SingleShoppingListFragment.SingleShoppingListViewHolder(view);
                return viewHolder;
            }
            @Override
            protected void onBindViewHolder(@NonNull final SingleShoppingListFragment.SingleShoppingListViewHolder holder, int position, @NonNull final Product model) {
                holder.productName.setText(model.getProductName());
                holder.productCheckbox.setChecked(Boolean.parseBoolean(model.getIsChecked()));
                if(shoppingList.getIsOpen().equals("false")){
                    holder.productName.setLongClickable(false);
                    holder.productCheckbox.setClickable(false);
                } else{
                    holder.productCheckbox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newIsChecked = "false";
                            if(model.getIsChecked().equals("false")){
                                newIsChecked="true";
                            }
                            productsRef.child(model.getProductName()).child(ConstantValues.isChecked).setValue(newIsChecked);
                        }
                    });
                    holder.productName.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return createAlertDialog("להסיר מהרשימה את המוצר: "+ model.getProductName()+ "?",ConstantValues.removeProductAction,model);
                        }
                    });
                }
            }
        };
        RecyclerView shoppingListRecyclerView = productsRecycler;
        shoppingListRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class SingleShoppingListViewHolder extends RecyclerView.ViewHolder{
        TextView productName;
        CheckBox productCheckbox;

        public SingleShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name_in_recycler);
            productCheckbox = itemView.findViewById(R.id.product_check_in_recycler);
        }
    }

    public boolean createAlertDialog(String message, final String action, final Product model){
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (action){
                            case ConstantValues.removeProductAction:
                                productsRef.child(model.getProductName()).removeValue();
                                currentProductsNamesList.remove(model.getProductName());
                                break;
                            case ConstantValues.closeShoppingListAction:
                                shoppingListRef.child(ConstantValues.isOpen).setValue("false");
                                shoppingListRef.child(ConstantValues.listCost).setValue(costString);
                                statisticsRef.child(shoppingList.getDate()).child(ConstantValues.listDate).setValue(shoppingList.getDate());
                                statisticsRef.child(shoppingList.getDate()).child(ConstantValues.listCost).setValue(costString);
                                navController.navigate(R.id.action_singleShoppingListFragment_to_shoppingListsFragment);
                                Toast.makeText(getActivity(), "הקניה נסגה בהצלחה", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return false;
    }


/*    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productsRef.setValue(currentProductsList);
    }*/
}