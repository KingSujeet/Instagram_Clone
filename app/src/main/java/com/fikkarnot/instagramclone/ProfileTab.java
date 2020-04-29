package com.fikkarnot.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.parse.Parse.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    TextView username;
    EditText name,bio,profession,hobbies;
    Button updateInfo;
    Bitmap receivedImages;
    CircleImageView profileImage;


    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        username = view.findViewById(R.id.username);
        name = view.findViewById(R.id.name);
        bio = view.findViewById(R.id.bio);
        profession = view.findViewById(R.id.profession);
        hobbies = view.findViewById(R.id.hobbies);
        updateInfo = view.findViewById(R.id.updateInfo);
        profileImage = view.findViewById(R.id.profileImage);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{

                    getChosenImage();
                }
            }
        });



        username.setText(ParseUser.getCurrentUser().get("username")+"");

        final ParseUser user = ParseUser.getCurrentUser();

        if (user.get("FullName")==null){

            name.setText("");
            bio.setText("");
            profession.setText("");
            hobbies.setText("");
        }else {

            name.setText(user.get("FullName").toString());
            bio.setText(user.get("Bio").toString());
            profession.setText(user.get("Profession").toString());
            hobbies.setText(user.get("Hobbies").toString());

        }

        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Storing your info....");
                progressDialog.show();
                user.put("FullName", name.getText().toString());
                user.put("Bio", bio.getText().toString());
                user.put("Profession", profession.getText().toString());
                user.put("Hobbies", hobbies.getText().toString());

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            FancyToast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        } else {


                            FancyToast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        }
                        progressDialog.dismiss();

                    }
                });


//                if (receivedImages != null) {
//
//
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                    receivedImages.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//                    byte[] bytes = byteArrayOutputStream.toByteArray();
//                    ParseFile parseFile = new ParseFile("pic.png", bytes);
//                    ParseObject parseObject = new ParseObject("ProfilePhoto");
//                    parseObject.put("picture", parseFile);
//                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
//
//                    parseObject.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//
//                            if (e == null) {
//
//                                FancyToast.makeText(getContext(), "Changes Saved", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
//                            } else {
//
//                                FancyToast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
//                            }
//
//                        }
//                    });
//
//
//                }






            }
        });



        Button logout = view.findViewById(R.id.button3);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;

    }

    private void getChosenImage() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2000){

            if (resultCode == Activity.RESULT_OK){

                try {

                    Uri selectedImages = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImages,
                            filePathColumn,null,null,null
                    );
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    receivedImages = BitmapFactory.decodeFile(picturePath);

                    profileImage.setImageBitmap(receivedImages);

                }catch (Exception e){

                    FancyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
            }
        }
    }


}
