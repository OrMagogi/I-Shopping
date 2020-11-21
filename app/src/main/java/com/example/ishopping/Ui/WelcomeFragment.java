package com.example.ishopping.Ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ishopping.Activities.ShoppingActivity;
import com.example.ishopping.R;

public class WelcomeFragment extends Fragment {

    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading... Please wait");
        progressDialog.show();
        sendUserToShoppingActivity();
    }


    private void sendUserToShoppingActivity() {

        progressDialog.dismiss();
        Intent intent = new Intent(getActivity(), ShoppingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }


}