package com.example.ishopping.Ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ishopping.Data.Category;
import com.example.ishopping.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private ImageView categoryInSpinnerImage;
    private TextView categoryInSpinnerName;
    private Category currentCategory;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArray){
        super(context,0,categoryArray);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, @NonNull ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.categories_spinner_dropdown_item,parent,false);
        }
        categoryInSpinnerImage = convertView.findViewById(R.id.product_image_in_spinner);
        categoryInSpinnerName = convertView.findViewById(R.id.product_name_in_spinner);
        currentCategory= getItem(position);

        if(currentCategory!=null){
            categoryInSpinnerName.setText(currentCategory.getCategoryName());
            categoryInSpinnerImage.setImageResource(currentCategory.getCategoryImage());
        }

        return convertView;
    }
}
