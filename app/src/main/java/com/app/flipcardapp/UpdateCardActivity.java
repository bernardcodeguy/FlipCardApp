package com.app.flipcardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.flipcardapp.db.CardDataBaseHelper;
import com.app.flipcardapp.model.Card;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateCardActivity extends AppCompatActivity {
    private RadioGroup radGroup;
    private EditText backText;
    private Button addUpdate,cancelButton;
    private Card c;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUET_LOAD_PICTURE = 2;
    private String mCurrentPhotoPath;
    private Bitmap picture;
    private CardDataBaseHelper cardDataBaseHelper;
    private SharedPreferences preferences;
    public static final String mypreferences = "mypref";
    public static final String userName = "nameKey";
    public static final String userDOB = "dobKey";
    public static final String userMobile = "mobileKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_card);

       /* Intent intent = getIntent();
        c = (Card) intent.getSerializableExtra("c");*/

        preferences =   getSharedPreferences(mypreferences,
                Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString("c", "");
        c = gson.fromJson(json, Card.class);


        radGroup = findViewById(R.id.radGroup);
        backText = findViewById(R.id.backText);
        addUpdate = findViewById(R.id.addUpdate);
        cancelButton = findViewById(R.id.cancelButton);
        cardDataBaseHelper = new CardDataBaseHelper(this);


        backText.setText(c.getBackText());



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

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateCardActivity.this,MainActivity.class);
                intent .putExtra("tag",true);
                finish();
                startActivity(intent);
            }
        });

        addUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bText = backText.getText().toString();

                if(picture != null){
                    Bitmap p = getResizedBitmap(picture,300);
                    c.setFrontImg(p);

                }
                c.setBackText(bText);
                cardDataBaseHelper.updateCard(c);
                Intent intent = new Intent(UpdateCardActivity.this,MainActivity.class);
                intent .putExtra("tag",true);
                finish();
                startActivity(intent);
            }
        });


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
            /*Card c = list.get(i);
            c.setFrontImg(picture);
            c.setBackText(backText1);*/

            Toast.makeText(this, c.getBackText(), Toast.LENGTH_SHORT).show();

        }

        if(requestCode == REQUET_LOAD_PICTURE && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                picture = BitmapFactory.decodeStream(imageStream);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
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
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,
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
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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