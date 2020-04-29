package com.fikkarnot.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedTab extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private EditText editText;
    private Button sharebtn;
    Bitmap receivedImages;


    public FeedTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed_tab, container, false);

        imageView = view.findViewById(R.id.imgShare);
        editText = view.findViewById(R.id.editText2);
        sharebtn = view.findViewById(R.id.btnShare);

        imageView.setOnClickListener(FeedTab.this);
        sharebtn.setOnClickListener(FeedTab.this);


        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgShare:
                if (Build.VERSION.SDK_INT>=23 && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    
                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{
                    
                    getChosenImage();
                }

                break;
                case R.id.btnShare:

                    if(receivedImages != null){

                        if (editText.getText().toString().equals("")){

                            FancyToast.makeText(getContext(),"you must add decription",Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                        }else{

                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            receivedImages.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] bytes = byteArrayOutputStream.toByteArray();
                            ParseFile parseFile = new ParseFile("pic.png",bytes);
                            ParseObject parseObject = new ParseObject("Photo");
                            parseObject.put("picture",parseFile);
                            parseObject.put("imgDes",editText.getText().toString());
                            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                            final ProgressDialog progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("submiting your post");
                            progressDialog.show();
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {

                                    if (e==null){

                                        FancyToast.makeText(getContext(),"Posted!!!",Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                                    }
                                    else {

                                        FancyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                                    }
                                    progressDialog.dismiss();
                                }
                            });

                        }


                    }else{

                        FancyToast.makeText(getContext(),"You must select post images",Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                    }
                    break;
        }

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

                    imageView.setImageBitmap(receivedImages);

                }catch (Exception e){

                        FancyToast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                }
            }
        }
    }
}
