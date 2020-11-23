package com.example.ishopping.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ishopping.Data.ConstantValues;
import com.example.ishopping.Data.Product;
import com.example.ishopping.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewProductFragment extends Fragment {


    private EditText newProductNameEdittext;
    private String newProductName,newProductCategory;
    private Spinner newProductCategorySpinner;
    private CircleImageView saveNewProductButton;
    private ArrayList<String> categories,existingProducts;
    private FirebaseDatabase firebaseDatabase;
    private static DatabaseReference productsRef;
    private Product newProduct;


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
        categories= new ArrayList<String>();
        existingProducts= new ArrayList<String>();
        saveNewProductButton= view.findViewById(R.id.add_new_product_completed_button);
        newProductNameEdittext= view.findViewById(R.id.new_product_name_edittext);
        newProductCategorySpinner = view.findViewById(R.id.new_product_category_spinner);

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
            if(checkInput()){

                switch (v.getId()){
                    case R.id.add_new_product_completed_button:
                        // TODO save in Database
                        break;
                }
            }

        }

        private boolean checkInput() {
            if(newProductName.equals("")||existingProducts.contains(newProductName)){
                Toast.makeText(getActivity(), "שם לא תקין או המוצר קיים כבר", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }
}