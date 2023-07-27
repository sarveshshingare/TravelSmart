package com.example.travelsmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePicActivity extends AppCompatActivity {
private ProgressBar progressBar;
private ImageView imageViewUploadPic;
private FirebaseAuth authProfile;
private StorageReference storageRefrence;
private FirebaseUser firebaseUser;
private static final int PIC_IMAGE_REQUEST=1;
private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        authProfile=FirebaseAuth.getInstance();
        Button buttonUploadPicChoose= findViewById(R.id.upload_pic_choose_button);
        Button buttonUploadPic=findViewById(R.id.upload_pic_button);
        progressBar =findViewById(R.id.progressbar);
        imageViewUploadPic=findViewById(R.id.imageView_profile_dp);
        firebaseUser = authProfile.getCurrentUser();

        storageRefrence= FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri=firebaseUser.getPhotoUrl();

        //set user's current DP in ImageView(if uploaded already).we will picasso since ImageViewer setimageUri
        //Regular URIs.
        Picasso.get().load(uri).into(imageViewUploadPic);
        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //upload image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();
            }
        });



    }

    private void UploadPic() {
        if(uriImage!=null)
        {
            //save the image with uid of the currently logedin user
            StorageReference fileRefrence=storageRefrence.child(authProfile.getCurrentUser().getUid()+"."+getFileExtension(uriImage));

            //upload image to storage
            fileRefrence.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri =uri;
                            firebaseUser=authProfile.getCurrentUser();

                            //finally set the display image of the current user after upload

                            UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePicActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                    Intent intent =new Intent(UploadProfilePicActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePicActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    
                }
            });

        }else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "no file is selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime =MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void openFileChooser() {

        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PIC_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PIC_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            uriImage=data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
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
            Toast.makeText(UploadProfilePicActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(UploadProfilePicActivity.this,MainActivity.class);

            //Clear stack to prevent user from coming back on pressing back button after Logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close UserProfileActivity

        }
        else{
            Toast.makeText(UploadProfilePicActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);

    }
}