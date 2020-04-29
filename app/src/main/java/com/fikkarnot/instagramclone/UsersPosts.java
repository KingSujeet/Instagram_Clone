package com.fikkarnot.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity{
    Toolbar toolbar;
    LinearLayout linearLayout;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        String receviedUsername = getIntent().getStringExtra("username");
        setTitle(receviedUsername+"'s Posts");
        toolbar = findViewById(R.id.my_toolbar);
        linearLayout = findViewById(R.id.LinearLayout);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        progressBar = findViewById(R.id.progressBar2);

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username", receviedUsername);
        parseQuery.orderByDescending("createdAt");

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size()>0 && e==null){

                    for (ParseObject post : objects){

                        final TextView imgDes = new TextView(getApplicationContext());
                        imgDes.setText(post.get("imgDes") + "");
                        ParseFile postPictures = (ParseFile) post.get("picture");
                        postPictures.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (data !=null && e==null){

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                    ImageView postImageView = new ImageView(getApplicationContext());
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                                    params.setMargins(5,5,5,0);
                                    postImageView.setLayoutParams(params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams params_des = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params_des.setMargins(5,0,5,20);
                                    imgDes.setLayoutParams(params_des);
                                    imgDes.setGravity(Gravity.CENTER);
                                    imgDes.setBackgroundColor(Color.GRAY);
                                    imgDes.setTextColor(Color.WHITE);
                                    imgDes.setTextSize(24f);

                                    View view = new View(getApplicationContext());
                                    LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2);
                                    params_view.setMargins(0,30,0,30);
                                    view.setLayoutParams(params_view);
                                    view.setBackgroundColor(Color.BLACK);

                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(imgDes);
                                    linearLayout.addView(view);
                                }

                            }
                        });
                        linearLayout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }
                }
                else {

                    progressBar.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(),"This user doesn't have any posts", Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

                }
            }
        });

    }
}
