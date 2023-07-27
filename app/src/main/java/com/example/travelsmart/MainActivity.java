package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText editTextLoginEmail,editTextLoginPwd;
    private ProgressBar progressbar;
    FirebaseAuth authProfile;

    private static final String TAG= "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextLoginEmail=findViewById(R.id.editText_login_email);
        editTextLoginPwd=findViewById(R.id.editText_login_pwd);
        progressbar=findViewById(R.id.progressbar);
        TextView textViewRegister=findViewById(R.id.textView_Accounut);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,activity_register.class));
            }
        });


        //Reset password

        TextView textViewForgotPassword=findViewById(R.id.textView_ForgetPassword);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You can now reset your password!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,forgotPasswordActivity.class));
            }
        });

        authProfile=FirebaseAuth.getInstance();
/*
        //show hide password using eye icon
        ImageView imageViewShowHidePwd =findViewById(R.id.imageview_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if password is visible then hide it
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);

                }else
                {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);

                }
            }
        });
*/
        //Login user
        Button ButtonLogin = findViewById(R.id.Button_login);
        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textEmail =editTextLoginEmail.getText().toString();
                String textPwd =editTextLoginPwd.getText().toString();



                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(MainActivity.this, "please enter your email ", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("email is required");
                    editTextLoginEmail.requestFocus();

                }
                else if (TextUtils.isEmpty(textPwd))
                {
                    Toast.makeText(MainActivity.this, "please enter your Password ", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Password is required");
                    editTextLoginEmail.requestFocus();
                }
                else
                {
                    progressbar.setVisibility(view.VISIBLE);
                    loginUser(textEmail,textPwd);
                }
            }
        });



    }

    private void loginUser(String Email, String Pwd) {
        authProfile.signInWithEmailAndPassword(Email ,Pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "you are logged in now", Toast.LENGTH_SHORT).show();
                    //get instance of the current user
                    FirebaseUser firebaseUser= authProfile.getCurrentUser();
                    //check if email is verified or not before accesing their profile
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(MainActivity.this, "you are logged in now", Toast.LENGTH_SHORT).show();
                        //open user profile activity
                        //start the user profileActivity
                        startActivity(new Intent(MainActivity.this,homePage.class));
                        finish();   //close loginActivity
                    }else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut(); //sign out user
                        showAlertDialog();

                    }


                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        editTextLoginEmail.setError("user does not exist ");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editTextLoginEmail.setError("Invalid crdentials ");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }


                } progressbar.setVisibility(View.GONE);
            }
        });

    }

    private void showAlertDialog() {
        //setup alert builder
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify Your email now.You cannot login without email verification");
        //open email apps if user clicks/taps continue button
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(Intent.ACTION_MAIN);
                intent.addCategory((Intent.CATEGORY_APP_EMAIL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);     //to launch email app in new window and not within our own app
                startActivity(intent);

            }
        });
        //create the alertdialog
        AlertDialog alertDialog = builder.create();

        //show alertdialog
        alertDialog.show();
    }
    //check if user is already logged in . in such case,straight away take the user to the  the users profile


    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null)
        {
            Toast.makeText(MainActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();

            //start the user profileActivity
            startActivity(new Intent(MainActivity.this,homePage.class));
            finish();   //close loginActivity
        }
        else
        {
            Toast.makeText(MainActivity.this, "You can login now!", Toast.LENGTH_SHORT).show();
        }
    }


}