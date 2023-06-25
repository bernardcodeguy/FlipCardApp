package com.app.flipcardapp.fragment;

import static android.app.Activity.RESULT_OK;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.flipcardapp.ConfirmDeleteActivity;
import com.app.flipcardapp.R;
import com.app.flipcardapp.UpdateCardActivity;
import com.app.flipcardapp.db.CardDataBaseHelper;
import com.app.flipcardapp.model.Card;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CardFragment extends Fragment {
    private CardView card1,card2;
    private TextView backText,count,total,WelcomeText;
    private ImageView frontImg,next_button,pre_button,delete_button,edit_button;
    private List<Card> list;
    AnimatorSet animFront;
    AnimatorSet animBack;
    private CardDataBaseHelper cardDataBaseHelper;
    private String selectType,backText1;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUET_LOAD_PICTURE = 2;
    private String mCurrentPhotoPath;
    private Bitmap picture;
    private SharedPreferences preferences;
    public static final String mypreferences = "mypref";
    public static final String userName = "nameKey";
    public static final String userDOB = "dobKey";
    public static final String userMobile = "mobileKey";



    private boolean mIsBackVisible = false; //keeping card flip state
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_card, container, false);


        card1 = v.findViewById(R.id.card1);
        card2 = v.findViewById(R.id.card2);

        backText = v.findViewById(R.id.backText);
        WelcomeText = v.findViewById(R.id.WelcomeText);
        count = v.findViewById(R.id.count);
        total = v.findViewById(R.id.total);
        edit_button = v.findViewById(R.id.edit_button);
        delete_button = v.findViewById(R.id.delete_button);

        preferences = getActivity().getSharedPreferences(mypreferences,
                Context.MODE_PRIVATE);

        frontImg = v.findViewById(R.id.frontImg);
        pre_button = v.findViewById(R.id.pre_button);
        next_button = v.findViewById(R.id.next_button);
        loadAnimations();
        changeCameraDistance();
        cardDataBaseHelper = new CardDataBaseHelper(getContext());
        list = cardDataBaseHelper.getAllCards();

        i = 0;
        if(list.isEmpty()) {
            next_button.setVisibility(View.GONE);
            pre_button.setVisibility(View.GONE);
            card1.setVisibility(View.GONE);
            card2.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No card available\nAdd Card to view", Toast.LENGTH_SHORT).show();
        }else{
            WelcomeText.setVisibility(View.GONE);
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            next_button.setVisibility(View.VISIBLE);
            pre_button.setVisibility(View.VISIBLE);
            total.setText(list.size() + "");
            count.setText((i + 1) + "");
            frontImg.setImageBitmap(list.get(i).getImageDataInBitmap());
            backText.setText(list.get(i).getBackText());

        }

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(list.isEmpty())) {
                   Card c = list.get(i);




                    SharedPreferences.Editor prefsEditor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(c);
                    Toast.makeText(getContext(), "Loading... Please Wait", Toast.LENGTH_SHORT).show();
                    prefsEditor.putString("c", json);
                    prefsEditor.commit();

                    Intent intent = new Intent(getContext(), UpdateCardActivity.class);
                    //intent.putExtra("c", c);

                    startActivity(intent);
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.isEmpty()) {

                }else{
                    Card c = list.get(i);
                    SharedPreferences.Editor prefsEditor = preferences.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(c);
                    Toast.makeText(getContext(), "Loading... Please Wait", Toast.LENGTH_SHORT).show();
                    prefsEditor.putString("c", json);
                    prefsEditor.commit();
                    Intent intent = new Intent(getContext(), ConfirmDeleteActivity.class);
                    //intent.putExtra("c", c);

                    startActivity(intent);
                }
            }
        });



        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
            }
        });


        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
            }
        });

        pre_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.isEmpty()) {

                }else{
                    if (i <= 0) {
                        i = 0;

                    } else {
                        i = i - 1;

                        count.setText((i + 1) + "");


                        //final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                        final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                        rightInAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // this method is called when the animation is finished playing


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });

                        if (!(i <= -1)) {
                            card1.startAnimation(rightInAnim);
                            animBack.setTarget(card2);
                            animFront.setTarget(card1);
                            animBack.start();
                            animFront.start();
                            mIsBackVisible = false;
                            frontImg.setImageBitmap(list.get(i).getImageDataInBitmap());
                            backText.setText(list.get(i).getBackText());

                        }


                    }
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (list.isEmpty()) {

                }else{
                    if (i > (list.size() - 1)) {
                    } else {
                        i = i + 1;
                        if (!(i > list.size() - 1)) {
                            count.setText((i + 1) + "");
                        }

                        final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                        //final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                        leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                // this method is called when the animation is finished playing


                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // we don't need to worry about this method
                            }
                        });

                        if (!(i > (list.size() - 1))) {
                            card1.startAnimation(leftOutAnim);
                            animBack.setTarget(card2);
                            animFront.setTarget(card1);
                            animBack.start();
                            animFront.start();
                            mIsBackVisible = false;
                            frontImg.setImageBitmap(list.get(i).getImageDataInBitmap());
                            backText.setText(list.get(i).getBackText());
                        }


                    }
                }
            }
        });


        return v;
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        card1.setCameraDistance(scale);
        card2.setCameraDistance(scale);
    }

    private void loadAnimations() {
        animBack = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_back);
        animFront = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_front);
    }

    public void flipCard() {
        if (!mIsBackVisible) {

            animBack.setTarget(card1);
            animFront.setTarget(card2);
            animBack.start();
            animFront.start();
            mIsBackVisible = true;

        } else {

            animBack.setTarget(card2);
            animFront.setTarget(card1);
            animBack.start();
            animFront.start();
            mIsBackVisible = false;

        }
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
            Bitmap p = getResizedBitmap(picture,300);

            Card c = list.get(i);
            c.setFrontImg(p);
            c.setBackText(backText1);

        }

        if(requestCode == REQUET_LOAD_PICTURE && resultCode == RESULT_OK){
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                picture = BitmapFactory.decodeStream(imageStream);

                Bitmap p = getResizedBitmap(picture,300);

                Card c = list.get(i);
                c.setFrontImg(p);
                c.setBackText(backText1);


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