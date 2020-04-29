package com.fikkarnot.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText sUsername,sPassword,sEmail;
    Button SignUPBtn;
    TextView loginAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sUsername = findViewById(R.id.username);
        sPassword = findViewById(R.id.password);
        sEmail = findViewById(R.id.Email);
        SignUPBtn = findViewById(R.id.SignUpBtn);
        loginAct = findViewById(R.id.loginAct);

        SignUPBtn.setOnClickListener(this);
        loginAct.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.SignUpBtn:
                if (sUsername.getText().toString().equals("") || sPassword.getText().toString().equals("") || sEmail.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"You Cannot leave Email, Username or password blank",Toast.LENGTH_SHORT).show();
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Creating Your Account....");
                        progressDialog.show();
                        ParseUser appUser = new ParseUser();
                        appUser.setUsername(sUsername.getText().toString());
                        appUser.setPassword(sPassword.getText().toString());
                        appUser.signUpInBackground(new SignUpCallback() {
                          @Override
                          public void done(ParseException e) {
                              if (e==null) {
                                  Toast.makeText(getApplicationContext(), sUsername.getText().toString() + " succesfully signed up ", Toast.LENGTH_SHORT).show();
                                  Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                  startActivity(intent);
                                  finish();
                              }else{

                                  Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                              }
                              progressDialog.dismiss();
                          }
                      });}
                break;

            case R.id.loginAct:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;

        }
    }
    public void layoutTapped1(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
