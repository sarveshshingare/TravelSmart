package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.number.Scale;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class activity_register extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDoB, editTextRegisterMobile,
            editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelcted;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        Toast.makeText(activity_register.this, "You can register now", Toast.LENGTH_SHORT).show();

        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_confirm_password);
        progressBar = findViewById(R.id.progressbar);
        TextView textViewHaveanAccount=findViewById(R.id.textView_haveAnAccount);
        //RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        //
        textViewHaveanAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity_register.this,MainActivity.class));
            }
        });

        //setting  up date picker on edittext

        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker dialogue box
                picker = new DatePickerDialog(activity_register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view ,int year ,int month ,int dayofmonth) {
                        editTextRegisterDoB.setText(dayofmonth+"/"+(month + 1)+"/"+ year);
                    }
                },year,month,day);
                picker.show();
            }
        });
        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelcted = findViewById(selectedGenderId);

                //obtain information entered by user
                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;
                //can't obtain the value before verifying if any button was selected or not

                //validate mobile number using matcher and pattern (regular expression)
                String mobileRegex="[6-9][0-9]{9}";
                Matcher mobileMatcher ;
                Pattern mobilePattern =Pattern.compile(mobileRegex);
                mobileMatcher=mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(activity_register.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();

                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(activity_register.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(activity_register.this, "Please Re-enter your Email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("Valid email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(activity_register.this, "Please enter your DoB", Toast.LENGTH_SHORT).show();
                    editTextRegisterDoB.setError("DoB is required");
                    editTextRegisterDoB.requestFocus();

                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(activity_register.this, "Please select your Gender", Toast.LENGTH_SHORT).show();
                    radioButtonRegisterGenderSelcted.setError("Gender is required ");
                    radioButtonRegisterGenderSelcted.requestFocus();

                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(activity_register.this, "Please enter your Mobile no.", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. is required");
                    editTextRegisterMobile.requestFocus();

                } else if (textMobile.length() != 10) {
                    Toast.makeText(activity_register.this, "Please re-enter your mobile", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no. should be 10 digits");
                    editTextRegisterMobile.requestFocus();

                } else if(!mobileMatcher.find())
                {
                    Toast.makeText(activity_register.this, "Please re-enter your mobile", Toast.LENGTH_SHORT).show();
                    editTextRegisterMobile.setError("Mobile no.is not valid");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(activity_register.this, "Please enter your Password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();

                } else if (textPwd.length() < 6) {
                    Toast.makeText(activity_register.this, "Password should be atleast 6 digits", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("Password to weak ");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(activity_register.this, "Please confirm your Password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(activity_register.this, "Please enter same password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirmPwd.setError("Password confirmation is required");
                    editTextRegisterConfirmPwd.requestFocus();
                    //clear the entered passwords
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                } else {
                    textGender = radioButtonRegisterGenderSelcted.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDoB, textGender, textMobile, textPwd);


                }


            }
        });

    }

    //register user using the given credentials
    private void registerUser(String textFullName, String textEmail, String textDoB, String textGender, String textMobile, String textPwd) {

        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(activity_register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(activity_register.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser firebaseUser =auth.getCurrentUser();

                    //update display name of the user
                    UserProfileChangeRequest profileChangeRequest= new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);


                    //store the data into firebase realtime database
                    ReadWriteUserDetails writeUserDetails =new ReadWriteUserDetails(textDoB,textGender,textMobile);

                    //extracting user refrence from database for "registered users"
                    DatabaseReference refrenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                    refrenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                //open user profile after successful regestration
                                firebaseUser.sendEmailVerification();


                                Toast.makeText(activity_register.this, "user registered successfully,Please verify your email", Toast.LENGTH_SHORT).show();
                    //open user profile after successful regestration
                    Intent intent=new Intent(activity_register.this,UserProfileActivity.class);
                    //to prevent the user from returning back to register activity  after pressing back button on successful registration

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();       //to close the Register Activity


                            }else
                            {
                                Toast.makeText(activity_register.this, "user registered failed,Please please try again", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);



                        }
                    });

                    //open user profile after successful regestration

                    /* Intent intent=new Intent(RegisterActivity.this,UserProfileActivity.class);
                    //to prevent the user from returning back to register activity  after pressing back button on successful registration

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(intent);
                    finish();       //to close the Register Activity */
                }
                else
                {
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e)
                    {
                        editTextRegisterPwd.setError("Your Password is too weak ");
                        editTextRegisterPwd.requestFocus();
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        editTextRegisterEmail.setError("Email is invalid or already in use ,Please re-enter your email ");
                        editTextRegisterEmail.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e)
                    {
                        editTextRegisterEmail.setError("user is already registered with this email,use another email ");
                        editTextRegisterEmail.requestFocus();
                    }catch (Exception e)
                    {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(activity_register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }


}