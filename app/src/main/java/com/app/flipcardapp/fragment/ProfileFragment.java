package com.app.flipcardapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.flipcardapp.R;
import com.app.flipcardapp.dialog.EditDialog;
import com.app.flipcardapp.model.Profile;


public class ProfileFragment extends Fragment implements EditDialog.EditDialogListener{
    private ImageView edit_button;
    private EditText edtName,edtDOB,edtMobile;
    private Profile p;
    private ImageView img;
    private SharedPreferences preferences;
    private ImageButton btnCameraRoll;
    public static final String mypreferences = "mypref";
    public static final String userName = "nameKey";
    public static final String userDOB = "dobKey";
    public static final String userMobile = "mobileKey";
    public static final String imageUrl = "imageUrlKey";
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUET_LOAD_PICTURE = 2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        edtName = v.findViewById(R.id.edtName);
        edtDOB = v.findViewById(R.id.edtDOB);
        edtMobile = v.findViewById(R.id.edtMobile);
        edit_button = v.findViewById(R.id.edit_button);
        btnCameraRoll = v.findViewById(R.id.cameraRoll);
        img = v.findViewById(R.id.img);

        p = new Profile();

        p.setName(edtName.getText().toString());
        p.setdOfBirth(edtDOB.getText().toString());
        p.setMobileNumber(edtMobile.getText().toString());





        preferences = getActivity().getSharedPreferences(mypreferences,
                Context.MODE_PRIVATE);

        if(preferences.contains(userName)){
            edtName.setText(preferences.getString(userName,"Anonymous"));
        }

        if(preferences.contains(userDOB)){
            edtDOB.setText(preferences.getString(userDOB,"N/A"));
        }

        if(preferences.contains(userMobile)){
            edtMobile.setText(preferences.getString(userMobile,"N/A"));
        }



        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditDialog editDialog = new EditDialog(p);
                editDialog.show(getActivity().getSupportFragmentManager(),"edit dialog");
                editDialog.setTargetFragment(ProfileFragment.this,1);
            }
        });

        btnCameraRoll.setVisibility(View.GONE);

        /*btnCameraRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaDialog mediaDialog = new MediaDialog();
                mediaDialog.show(getActivity().getSupportFragmentManager(),"media dialog");
                mediaDialog.setTargetFragment(ProfileFragment.this,1);
            }
        });*/



        return v;
    }

    @Override
    public void editValue(String name, String dob, String mobile) {
        if(name.isEmpty() || dob.isEmpty() || mobile.isEmpty()){

        }else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(userName,name);
            editor.putString(userDOB,dob);
            editor.putString(userMobile,mobile);
            editor.commit();

            preferences = getActivity().getSharedPreferences(mypreferences,Context.MODE_PRIVATE);
            if(preferences.contains(userName)){

                edtName.setText(preferences.getString(userName,"Anonymous"));
            }

            if(preferences.contains(userDOB)){

                edtDOB.setText(preferences.getString(userDOB,"N/A"));
            }

            if(preferences.contains(userMobile)){

                edtMobile.setText(preferences.getString(userMobile,"N/A"));
            }

        }
    }




}