package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityChangePasswordBinding;

import org.jetbrains.annotations.NotNull;

public class ChangePasswordActivity extends AppCompatActivity {

    private ActivityChangePasswordBinding binding;

    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private DocumentReference documentReference;

    private AuthCredential authCredential;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        database = FirebaseFirestore.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());

        documentReference = database.collection(Constants.KEY_STUDENT_COLLECTIONS).document(preferenceManager.getString(Constants.KEY_STUDENT_ID));

        authCredential = EmailAuthProvider.getCredential(preferenceManager.getString(Constants.KEY_STUDENT_MAIL), preferenceManager.getString(Constants.KEY_STUDENT_PASS));
    }

    private void setListeners(){
        binding.backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.newPass.getText().toString().equals(binding.newPass2.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "Şifreler Eşleşmeli", Toast.LENGTH_SHORT).show();
                }
                else {
                    binding.changeButton.setVisibility(View.INVISIBLE);
                    binding.progressItems.setVisibility(View.VISIBLE);
                    updatePass();
                }
            }
        });
    }

    private void updatePass(){
        firebaseUser.reauthenticate(authCredential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            firebaseUser.updatePassword(binding.newPass.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                documentReference.update(Constants.KEY_STUDENT_PASS, binding.newPass.getText().toString());
                                                preferenceManager.putString(Constants.KEY_STUDENT_PASS, binding.newPass.getText().toString());
                                                Toast.makeText(ChangePasswordActivity.this, "Güncellendi", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}