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

import com.example.ishopping.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewProductFragment extends Fragment {


    private EditText newProductName;
    private Spinner newProductCategory;
    private CircleImageView saveNewProductButton;


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
        saveNewProductButton= view.findViewById(R.id.add_new_product_completed_button);
        newProductName= view.findViewById(R.id.new_product_name_edittext);
        newProductCategory = view.findViewById(R.id.new_product_category_spinner);
    }
}