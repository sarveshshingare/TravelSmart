package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class forgotPasswordActivity extends AppCompatActivity {

    private Button buttonPwdReset;
    private EditText editTextPwdResetEmail;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG="forgotPasswordActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextPwdResetEmail=findViewById(R.id.editText_password_reset_email);
        buttonPwdReset=findViewById(R.id.button_password_reset);
        progressBar=findViewById(R.id.progressbar);

        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String email=editTextPwdResetEmail.getText().toString();
                
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(forgotPasswordActivity.this, "Please enter your regiseterd email", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Email is required");
                    editTextPwdResetEmail.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(forgotPasswordActivity.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    editTextPwdResetEmail.setError("Valid email is required");
                    editTextPwdResetEmail.requestFocus();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }

            }
        });


    }

    private void resetPassword(String email) {
        authProfile= FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(forgotPasswordActivity.this, "Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(forgotPasswordActivity.this,MainActivity.class);

                    //Clear stack to prevent user from coming back to reset password
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e){
                        editTextPwdResetEmail.setError("user does not exists or is no longer available.please register again.");
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(forgotPasswordActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}