package com.app.flipcardapp.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.app.flipcardapp.R;
import com.app.flipcardapp.model.Card;
import com.app.flipcardapp.model.Profile;

public class EditDialog extends DialogFragment {

    private EditText edtName,edtDOB,edtMobile;
    private String name,dOB,mobile;
    private EditDialogListener listener;

    public EditDialog(Profile p){
        this.name = p.getName();
        this.dOB = p.getdOfBirth();
        this.mobile = p.getMobileNumber();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_dialog_layout,null);


        edtName = v.findViewById(R.id.edtName);
        edtDOB = v.findViewById(R.id.edtDOB);
        edtMobile = v.findViewById(R.id.edtMobile);



        edtName.setText(name);
        edtDOB.setText(dOB);
        edtMobile.setText(mobile);



        builder.setView(v)
                .setTitle("Update Profile")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(edtName.getText().toString().isEmpty() || edtDOB.getText().toString().isEmpty() || edtMobile.getText().toString().isEmpty()){
                            Toast.makeText(getContext(), "One of the fields was empty", Toast.LENGTH_SHORT).show();
                        }else{
                            String name = edtName.getText().toString();
                            String dob = edtDOB.getText().toString();
                            String mobile = edtMobile.getText().toString();

                            listener.editValue(name,dob,mobile);
                        }

                    }
                });
        return  builder.create();


    }

    @Override
    public void onAttach(@NonNull Activity context) {
        super.onAttach(context);
        try {
            listener = (EditDialog.EditDialogListener) getTargetFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface EditDialogListener{
        void editValue(String name,String dob,String mobile);
    }



}
