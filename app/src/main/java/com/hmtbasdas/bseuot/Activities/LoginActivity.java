package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.Utilities.Intents;
import com.hmtbasdas.bseuot.Utilities.PreferenceManager;
import com.hmtbasdas.bseuot.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private PreferenceManager preferenceManager;

    private FirebaseFirestore database;
    private DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void setListeners(){
        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.buttonSignIn.setVisibility(View.INVISIBLE);
                binding.progressBar.setVisibility(View.VISIBLE);
                login(binding.inputEmail.getText().toString(), binding.inputPass.getText().toString());
            }
        });

        binding.textCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intents.goActivity(LoginActivity.this, new SignUpActivity());
            }
        });

        binding.welcomeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(auth.getUid() != null){
                    ToastMessage(auth.getUid());
                }
            }
        });

        binding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.forgotPass.setVisibility(View.INVISIBLE);
                binding.passwordProgress.setVisibility(View.VISIBLE);

                if(!binding.inputEmail.getText().toString().isEmpty()){
                    auth.sendPasswordResetEmail(binding.inputEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    binding.passwordProgress.setVisibility(View.INVISIBLE);
                                    binding.forgotPass.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        ToastMessage("Şifre sıfırlama bağlantısını mail adresine gönderdim, kontrol ett!");
                                    }
                                    else {
                                        ToastMessage("Mail adresin kayıtlı değil :/");
                                    }
                                }
                            });
                }
                else {
                    binding.passwordProgress.setVisibility(View.INVISIBLE);
                    binding.forgotPass.setVisibility(View.VISIBLE);
                    ToastMessage("Yukarıdaki bölüme mail adresini yazmalısın!");
                }
            }
        });
    }

    private void login(String mail, String password){
        if(isValidSignInDetails()){
            binding.inputEmail.setEnabled(false);
            binding.passwordProgress.setEnabled(false);
            auth.signInWithEmailAndPassword(mail, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if(Objects.requireNonNull(authResult.getUser()).isEmailVerified()){
                                saveUser(mail,password);
                            }
                            else{
                                ToastMessage("mail adresine onay maili gönderdim onaylaa");
                                authResult.getUser().sendEmailVerification();

                                binding.inputEmail.setEnabled(true);
                                binding.passwordProgress.setEnabled(true);

                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.buttonSignIn.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ToastMessage("bilgilerini kontrol eder misin ?");

                            binding.inputEmail.setEnabled(true);
                            binding.passwordProgress.setEnabled(true);

                            binding.progressBar.setVisibility(View.INVISIBLE);
                            binding.buttonSignIn.setVisibility(View.VISIBLE);
                        }
                    });
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    private void saveUser(String mail, String password){
        database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                .document(Objects.requireNonNull(auth.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult() != null){
                            preferenceManager.putString(Constants.KEY_STUDENT_ID, task.getResult().getString(Constants.KEY_STUDENT_ID));
                            preferenceManager.putString(Constants.KEY_STUDENT_NAME, task.getResult().getString(Constants.KEY_STUDENT_NAME));
                            preferenceManager.putString(Constants.KEY_STUDENT_SURNAME, task.getResult().getString(Constants.KEY_STUDENT_SURNAME));
                            preferenceManager.putString(Constants.KEY_STUDENT_IMAGE, task.getResult().getString(Constants.KEY_STUDENT_IMAGE));
                            preferenceManager.putBoolean(Constants.KEY_UNI_PRIVATE_STATUS, true);

                            database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                                    .document(Objects.requireNonNull(auth.getUid()))
                                    .collection(Constants.KEY_STUDENT_SECRET_COLLECTIONS)
                                    .document(Objects.requireNonNull(auth.getUid()))
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {

                                            if(task.isSuccessful() && task.getResult() != null){
                                                preferenceManager.putString(Constants.KEY_STUDENT_NO, task.getResult().getString(Constants.KEY_STUDENT_NO));
                                                preferenceManager.putString(Constants.KEY_STUDENT_MAIL, mail);
                                                preferenceManager.putString(Constants.KEY_STUDENT_PASS, password);
                                                preferenceManager.putString(Constants.KEY_STUDENT_DEPARTMENT, task.getResult().getString(Constants.KEY_STUDENT_DEPARTMENT));
                                                preferenceManager.putString(Constants.KEY_STUDENT_DEPARTMENT_LINK, task.getResult().getString(Constants.KEY_STUDENT_DEPARTMENT_LINK));

                                                documentReference = database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                                                        .document(Objects.requireNonNull(auth.getUid()))
                                                        .collection(Constants.KEY_STUDENT_SECRET_COLLECTIONS)
                                                        .document(Objects.requireNonNull(auth.getUid()));
                                                documentReference.update(Constants.KEY_STUDENT_PASS, password);

                                                Intents.goActivity(LoginActivity.this, new NewsActivity());
                                                finish();
                                            }

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            ToastMessage(auth.getUid());
                                            ToastMessage("bilgilerine ulaşamadım tekrar dene :/");
                                            auth.signOut();

                                            binding.progressBar.setVisibility(View.INVISIBLE);
                                            binding.buttonSignIn.setVisibility(View.VISIBLE);
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        ToastMessage(auth.getUid());
                        ToastMessage("bilgilerine ulaşamadım tekrar dene :/");
                        auth.signOut();

                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.buttonSignIn.setVisibility(View.VISIBLE);
                    }
                });
    }

    private boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            ToastMessage("Mail adresini göremedim");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            ToastMessage("Geçersiz formatta mail adresi girdin");
            return false;
        } else if (binding.inputPass.getText().toString().trim().isEmpty()) {
            ToastMessage("Şifreni girmedin");
            return false;
        } else if (binding.inputPass.getText().toString().trim().length() < 6) {
            ToastMessage("Şifren en az 6 haneli olacak :)");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void ToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}