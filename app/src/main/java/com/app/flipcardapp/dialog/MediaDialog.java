package com.app.flipcardapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.app.flipcardapp.R;

public class MediaDialog extends DialogFragment {
    private RadioGroup radGroup;
    private String selectType;
    private MediaDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.media_dialog_layout,null);

        radGroup = v.findViewById(R.id.radGroup);


        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int rdID) {
                switch (rdID){
                    case R.id.rdCamera:
                        selectType = "Camera";
                        break;
                    case R.id.rdGallery:
                        selectType = "Gallery";
                        break;
                    default:
                        break;
                }
            }
        });


        builder.setView(v)
                .setTitle("Choose Media Type")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(selectType.equals("")){

                        }else{
                            listener.mediaType(selectType);
                        }

                    }
                });
        return  builder.create();


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (MediaDialogListener) getTargetFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface MediaDialogListener {
        void mediaType(String selectType);

    }
}
