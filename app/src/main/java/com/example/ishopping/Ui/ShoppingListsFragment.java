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

import com.example.ishopping.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ShoppingListsFragment extends Fragment {

    private ShoppingListsViewModel mViewModel;
    private FloatingActionButton addNewListButton;
    private NavController navController;

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
}