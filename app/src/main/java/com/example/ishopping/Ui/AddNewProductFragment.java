package com.example.ishopping.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ishopping.Data.Category;
import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewProductFragment extends Fragment {


    private EditText newProductNameEdittext;
    private Category newProductCategory;
    private String newProductName,newProductCategoryName;
    private Spinner newProductCategorySpinner;
    private CircleImageView saveNewProductButton;
    private ArrayList<Category> categories;
    private CategoryAdapter categoryAdapter;
    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference productsRef;

    public static AddNewProductFragment newInstance(String param1, String param2) {
        AddNewProductFragment fragment = new AddNewProductFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories= new ArrayList<Category>();
        initListOfCategories();
        categoryAdapter= new CategoryAdapter(getContext(),categories);
        newProductCategorySpinner = view.findViewById(R.id.new_product_category_spinner);
        newProductCategorySpinner.setAdapter(categoryAdapter);

        //
        saveNewProductButton= view.findViewById(R.id.add_new_product_completed_button);
        newProductNameEdittext= view.findViewById(R.id.new_product_name_edittext);
        firebaseDatabase= FirebaseDatabase.getInstance();
        productsRef= firebaseDatabase.getReference().child(ConstantValues.products);
        setListeners();
    }

    void setListeners(){
        AddNewProductFragment.ClickListener clickListener=new AddNewProductFragment.ClickListener();
        saveNewProductButton.setOnClickListener(clickListener);
    }

    private class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            newProductName=newProductNameEdittext.getText().toString().trim();
            newProductCategory= (Category)newProductCategorySpinner.getSelectedItem();
            newProductCategoryName=newProductCategory.getCategoryName();
            if(checkInput()){

                switch (v.getId()){
                    case R.id.add_new_product_completed_button:
                        Product newProduct= new Product(newProductName,null,newProductCategoryName,"",null,null);
                        saveNewProductInDb(newProduct);
                        break;
                }
            }

        }

        private boolean checkInput() {
            boolean isValid=true;
            if(newProductName.equals("")){
                Toast.makeText(getActivity(), "שם המוצר לא תקין", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
            else if(newProductCategoryName.equals("")){
                Toast.makeText(getActivity(), "בחר קטגוריה", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
            else if(ShoppingListsFragment.getExistingProducts().containsKey(newProductName)){
                Toast.makeText(getActivity(), "המוצר קיים כבר במערכת", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
            return isValid;
        }
    }

    void saveNewProductInDb(Product newProduct){
        productsRef.child(newProductName).setValue(newProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "המוצר נוסף בהצלחה", Toast.LENGTH_SHORT).show();
                ShoppingListsFragment.getExistingProducts().put(newProductName,newProductCategoryName);
            }
        });

    }
    private void initListOfCategories(){
        categories.add(new Category("",0));
        categories.add(new Category("ירקות ופירות",R.drawable.vegetables));
        categories.add(new Category("כללי",R.drawable.general));
        categories.add(new Category("ניקיון",R.drawable.cleaning));
        categories.add(new Category("שתיה",R.drawable.drinking));
        categories.add(new Category("בשר",R.drawable.meat));
        categories.add(new Category("מוצרי חלב",R.drawable.milk));
    }



}