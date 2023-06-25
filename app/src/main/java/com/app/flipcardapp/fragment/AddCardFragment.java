package com.app.flipcardapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.app.flipcardapp.R;
import com.app.flipcardapp.db.CardDataBaseHelper;
import com.app.flipcardapp.model.Card;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddCardFragment extends Fragment {
    private RadioGroup radGroup;
    private EditText backText;
    private Button btnCreate;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUET_LOAD_PICTURE = 2;
    private String mCurrentPhotoPath;
    private Bitmap picture;
    private CardDataBaseHelper cardDataBaseHelper;
    Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getContext();

        cardDataBaseHelper = new CardDataBaseHelper(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_add_card, container, false);


        radGroup = v.findViewById(R.id.radGroup);
        backText = v.findViewById(R.id.backText);
        btnCreate = v.findViewById(R.id.btnCreate);




        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int rdID) {
                switch (rdID){
                    case R.id.rdCamera:
                        dispatchTakePicture();
                        break;
                    case R.id.rdGallery:
                        loadImageFromGallery();
                        break;
                    default:
                        break;
                }
            }
        });


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Card  c = new Card();
                String bText = backText.getText().toString();

                if(picture != null && !bText.isEmpty()){
                    c.setFrontImg(picture);
                    Bitmap p = getResizedBitmap(picture,300);
                    c.setFrontImg(p);
                    c.setBackText(bText);

                    Boolean b = cardDataBaseHelper.addOne(c);
                    if(b){
                        Toast.makeText(getContext(), "Card created successfully", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getContext(), "Failed to create card", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }



            }
        });

        return v;
    }

    private void loadImageFromGallery() {
        Intent loadIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(loadIntent,REQUET_LOAD_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK){
            picture = BitmapFactory.decodeFile(mCurrentPhotoPath);


        }

        if(requestCode == REQUET_LOAD_PICTURE && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                picture = BitmapFactory.decodeStream(imageStream);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }


        }

    }

    public static String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }


    private void dispatchTakePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (Exception e){
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.app.flipcardapp.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,REQUEST_TAKE_PHOTO);
            }


        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imgFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
