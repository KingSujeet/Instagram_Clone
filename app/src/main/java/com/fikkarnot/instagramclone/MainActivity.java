package com.fikkarnot.instagramclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button signUpBtn, loginBtn;
    EditText sUsername,sPassword,LUsername,LPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      signUpBtn =  findViewById(R.id.signUp);
      loginBtn =  findViewById(R.id.SignUpBtn);
//
//      sUsername = findViewById(R.id.username);
//      sPassword = findViewById(R.id.password);
//
    LUsername =  findViewById(R.id.Email);
    LPassword = findViewById(R.id.password);
        TextView signUpActivity = findViewById(R.id.SignUP);

    LPassword.setOnKeyListener(new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN){

                onClick(loginBtn);
            }
            return false;
        }
    });

    loginBtn.setOnClickListener(this);
    signUpActivity.setOnClickListener(this);

    if (ParseUser.getCurrentUser() != null){

        transitionToActivity();
        finish();
    }


//
//      signUpBtn.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//
//              ParseUser appUser = new ParseUser();
//              appUser.setUsername(sUsername.getText().toString());
//              appUser.setPassword(sPassword.getText().toString());
//              appUser.signUpInBackground(new SignUpCallback() {
//                  @Override
//                  public void done(ParseException e) {
//                      if (e==null) {
//                          Toast.makeText(getApplicationContext(), sUsername.getText().toString() + " succesfully signed up ", Toast.LENGTH_SHORT).show();
//                      }else{
//
//                          Toast.makeText(getApplicationContext(),"failed to sign up",Toast.LENGTH_SHORT).show();
//                      }
//                  }
//              });
//          }
//      });
//
//      loginBtn.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//
//                  ParseUser.logInInBackground(LUsername.getText().toString(), LPassword.getText().toString(), new LogInCallback() {
//                  @Override
//                  public void done(ParseUser user, ParseException e) {
//
//                      if (user != null && e==null){
//
//                          Toast.makeText(getApplicationContext(),"Logged in succesfully",Toast.LENGTH_SHORT).show();
//
//                          Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                          startActivity(intent);
//                      }else {
//
//                          Toast.makeText(getApplicationContext(),"failed to Login",Toast.LENGTH_SHORT).show();
//                      }
//
//                  }
//
//
//              });
//
//          }
//      });

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.SignUpBtn:
                if (LUsername.getText().toString().equals("") || LPassword.getText().toString().equals("")){

                    Toast.makeText(getApplicationContext(),"You Cannot leave Username or password blank",Toast.LENGTH_SHORT).show();
                }else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Authenticating....." +
                            "\n"+"Please Wait");
                    progressDialog.show();

                    ParseUser.logInInBackground(LUsername.getText().toString(), LPassword.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            if (user != null && e == null) {

                                Toast.makeText(getApplicationContext(), "Logged in succesfully", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {

                                Toast.makeText(getApplicationContext(), "failed to Login", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();

                        }


                    });

                }


                break;

            case R.id.SignUP:
                Intent intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                break;

        }
    }

    public void layoutTapped(View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void transitionToActivity(){

        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);

    }
}
