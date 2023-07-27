package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewDoB,textViewGender,textViewMobile;
    private ProgressBar progressBar;
    private String fullname,email,dob,gender ,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private SwipeRefreshLayout swipeContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        swipeToRefresh();

        textViewWelcome =findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail =findViewById(R.id.textView_show_email);
        textViewDoB =findViewById(R.id.textView_show_dob);
        textViewGender =findViewById(R.id.textView_show_gender);
        textViewMobile =findViewById(R.id.textView_show_mobile);
        progressBar =findViewById(R.id.progressbar);

        //set onclicklistener on ImageView to open uploadprofileActivity
        imageView=findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserProfileActivity.this,UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        authProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =authProfile.getCurrentUser();
        if(firebaseUser==null)
        {
            Toast.makeText(UserProfileActivity.this, "Something went wrong !user details are not available at the moment ", Toast.LENGTH_SHORT).show();


        }
        else
        {
            checkifEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);

            
        }


    }

    private void swipeToRefresh() {
        //look for swipe container
        swipeContainer =findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);

            }
        });

        //configure colours
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
    }

    //users coming to userprofile activity after succesfull registration
    private void checkifEmailVerified(FirebaseUser firebaseUser) {
        if(firebaseUser.isEmailVerified()){
//            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //setup alert builder
        AlertDialog.Builder builder= new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify Your email now.You cannot login without email verification next time");
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


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //extracting user refrences from database to "registerd users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue((ReadWriteUserDetails.class));
                if(readUserDetails!=null)
                {
                    fullname =firebaseUser.getDisplayName();
                    email = firebaseUser.getEmail();
                    dob= readUserDetails.Dob;
                    gender=readUserDetails.gender;
                    mobile=readUserDetails.mobile;

                    textViewWelcome.setText("Welcome,"+fullname+"!");
                    textViewFullName.setText(fullname);
                    textViewEmail.setText(email);
                    textViewDoB.setText(dob);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                    //set user DP(after uploading)
                    Uri uri=firebaseUser.getPhotoUrl();
                    //ImageViewer setImageURI() should not be used with regular uris ,so we are using picasso
                    Picasso.get().load(uri).into(imageView);


                }
                else{
                    Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    //creating action bar menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.menu_refresh)
        {
            //refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }
        /*else if(id==R.id.menu_update_profile)
        {
            Intent intent=new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.menu_update_email)
        {
            Intent intent=new Intent(UserProfileActivity.this,UpdateEmailActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.menu_settings)
        {
            Toast.makeText(UserProfileActivity.this, "menu settings", Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.menu_change_password)
        {
            Intent intent=new Intent(UserProfileActivity.this,UpdatePasswordActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.menu_delete_profile)
        {
            Intent intent=new Intent(UserProfileActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
        }

         */
        else if(id==R.id.menu_logout)
        {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);

            //Clear stack to prevent user from coming back on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close UserProfileActivity

        }
        else{
            Toast.makeText(UserProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);

    }
}
