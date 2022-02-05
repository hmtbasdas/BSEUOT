package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hmtbasdas.bseuot.Services.BSEUNotificationService;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityEditProfileBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    private PreferenceManager preferenceManager;

    private StorageReference storageReference;
    private StorageReference referenceImage;

    private String uploadID;
    private Uri imageUri;

    private DocumentReference documentReference;

    private Boolean privateNotification;
    private Boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
        setUserValues();
    }

    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());

        privateNotification = preferenceManager.getBoolean(Constants.KEY_UNI_PRIVATE_STATUS);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        documentReference = database.collection(Constants.KEY_STUDENT_COLLECTIONS).document(preferenceManager.getString(Constants.KEY_STUDENT_ID));
        storageReference = FirebaseStorage.getInstance().getReference("user_images");
        referenceImage = firebaseStorage.getReferenceFromUrl(preferenceManager.getString(Constants.KEY_STUDENT_IMAGE));
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.goActivity(getApplicationContext(), new NewsActivity());
                finish();
            }
        });

        binding.layoutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editName.getText().toString().isEmpty() && !binding.editSurname.getText().toString().isEmpty()){
                    binding.editProfileButton.setVisibility(View.INVISIBLE);
                    binding.progressItems.setVisibility(View.VISIBLE);

                    binding.backImage.setVisibility(View.INVISIBLE);
                    status = false;

                    uploadFile();
                }
                else {
                    Toast.makeText(EditProfileActivity.this, "gerçekten mi ya, boş bırakamazsın!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        binding.privateNotificationSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privateNotification = binding.privateNotificationSetting.isChecked();
            }
        });
    }

    private void setUserValues(){
        uploadID = preferenceManager.getString(Constants.KEY_STUDENT_IMAGE);
        Picasso.get().load(preferenceManager.getString(Constants.KEY_STUDENT_IMAGE)).into(binding.imageProfile);
        binding.editName.setText(preferenceManager.getString(Constants.KEY_STUDENT_NAME));
        binding.editSurname.setText(preferenceManager.getString(Constants.KEY_STUDENT_SURNAME));
        binding.privateNotificationSetting.setChecked(preferenceManager.getBoolean(Constants.KEY_UNI_PRIVATE_STATUS));
    }

    private void updateProfile(Boolean imageStatus){
        serviceStatus();

        if(imageStatus){
            referenceImage.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if(task.isSuccessful()){
                        updateUser();
                    }
                    else {
                        Toast.makeText(EditProfileActivity.this, "Başarısız", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            updateUser();
        }
    }

    private void updateUser(){
        documentReference.update(Constants.KEY_STUDENT_IMAGE, uploadID);
        documentReference.update(Constants.KEY_STUDENT_NAME, binding.editName.getText().toString().trim());
        documentReference.update(Constants.KEY_STUDENT_SURNAME, binding.editSurname.getText().toString().trim());

        preferenceManager.putString(Constants.KEY_STUDENT_IMAGE, uploadID);
        preferenceManager.putString(Constants.KEY_STUDENT_NAME, binding.editName.getText().toString().trim());
        preferenceManager.putString(Constants.KEY_STUDENT_SURNAME, binding.editSurname.getText().toString().trim());

        Toast.makeText(getApplicationContext(), "Güncellendi", Toast.LENGTH_SHORT).show();
        Intents.goActivity(getApplicationContext(), new NewsActivity());
        finish();
    }

    private void serviceStatus(){
        preferenceManager.putBoolean(Constants.KEY_UNI_PRIVATE_STATUS, privateNotification);
        if(!privateNotification){
            stopService(new Intent(this, BSEUNotificationService.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(binding.imageProfile);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile(){
        if(imageUri != null){
            StorageReference reference = storageReference.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            reference.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            return reference.getDownloadUrl();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull  Task<Uri> task) {
                            if(task.isSuccessful()){
                                uploadID = Objects.requireNonNull(task.getResult()).toString();
                                updateProfile(true);
                            }
                        }
                    });

        }
        else {
            updateProfile(false);
        }
    }

    @Override
    public void onBackPressed() {
        if(status){
            Intents.goActivity(getApplicationContext(), new NewsActivity());
            finish();
        }
    }
}