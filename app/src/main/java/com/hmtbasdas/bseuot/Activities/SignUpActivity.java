package com.hmtbasdas.bseuot.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hmtbasdas.bseuot.R;
import com.hmtbasdas.bseuot.Utilities.Constants;
import com.hmtbasdas.bseuot.databinding.ActivitySignUpBinding;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SignUpActivity extends BaseActivity {

    private ActivitySignUpBinding binding;
    private FirebaseAuth auth;

    private FirebaseFirestore database;
    private FirebaseStorage firebaseStorage;

    private DatabaseReference databaseReference;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private ArrayList<String> departments;
    private ArrayList<String> departmentsLink;

    private StorageReference storageReference;
    private StorageReference referenceImage;

    private String uploadID;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();

        database = FirebaseFirestore.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference("user_images");
        databaseReference = firebaseDatabase.getReference("departments");

        departments = new ArrayList<>();
        departmentsLink = new ArrayList<>();

        getDepartments();

        builder = new AlertDialog.Builder(this);
    }

    private void setListeners() {
        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidSignUpDetails()) {
                    binding.signUpPageLayout.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.loadingMessage.setVisibility(View.VISIBLE);
                    uploadFile();
                }
            }
        });

        binding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

        binding.selectUserSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserSC();
            }
        });

        binding.whyMail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                final View popup = getLayoutInflater().inflate(R.layout.sign_up_input_alert, null);

                TextView textView = popup.findViewById(R.id.infoValue);
                textView.setText("Mail adresine *sofra.bilecik.edu.tr* adresinden ulaşabilirsin.");

                builder.setView(popup);
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void createUser(String mail, String password, String uploadID) {
        auth.createUserWithEmailAndPassword(mail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveUserFF(mail, password, uploadID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ToastMessage("Tekrar Deneyiniz");

                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.loadingMessage.setVisibility(View.INVISIBLE);
                        binding.signUpPageLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void getDepartments(){
        departments.add("Bölümüm Yok");
        departmentsLink.add("");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> keys = snapshot.getChildren();

                for(DataSnapshot key:keys){
                    departments.add((String) key.child("name").getValue());
                    departmentsLink.add((String) key.child("link").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void saveUserFF(String mail, String password, String uploadID) {
        HashMap<String, Object> studentValue = new HashMap<>();
        HashMap<String, Object> studentSecretValue = new HashMap<>();

        studentValue.put(Constants.KEY_STUDENT_ID, auth.getUid());
        studentValue.put(Constants.KEY_STUDENT_NAME, binding.inputUserName.getText().toString());
        studentValue.put(Constants.KEY_STUDENT_SURNAME, binding.inputUserSurname.getText().toString());
        studentValue.put(Constants.KEY_STUDENT_IMAGE, uploadID);

        studentSecretValue.put(Constants.KEY_STUDENT_NO, binding.inputUserNo.getText().toString());
        studentSecretValue.put(Constants.KEY_STUDENT_MAIL, mail);
        studentSecretValue.put(Constants.KEY_STUDENT_PASS, password);
        studentSecretValue.put(Constants.KEY_STUDENT_DEPARTMENT, binding.selectUserSc.getText());


        for(int i=0;i<departmentsLink.size();i++){
            if(binding.selectUserSc.getText().equals(departments.get(i))){
                studentSecretValue.put(Constants.KEY_STUDENT_DEPARTMENT_LINK, departmentsLink.get(i));
            }
        }

        database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                .document(Objects.requireNonNull(auth.getUid()))
                .set(studentValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        database.collection(Constants.KEY_STUDENT_COLLECTIONS)
                                .document(Objects.requireNonNull(auth.getUid()))
                                .collection(Constants.KEY_STUDENT_SECRET_COLLECTIONS)
                                .document(Objects.requireNonNull(auth.getUid()))
                                .set(studentSecretValue)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ToastMessage("kaydettim..");

                                        binding.progressBar.setVisibility(View.INVISIBLE);
                                        binding.loadingMessage.setVisibility(View.INVISIBLE);
                                        binding.signUpPageLayout.setVisibility(View.VISIBLE);

                                        onBackPressed();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                ToastMessage("Oops kayıt edemedim tekrar dene :/");
                                Objects.requireNonNull(auth.getCurrentUser()).delete();

                                referenceImage = firebaseStorage.getReferenceFromUrl(uploadID);
                                referenceImage.delete();

                                binding.progressBar.setVisibility(View.INVISIBLE);
                                binding.loadingMessage.setVisibility(View.INVISIBLE);
                                binding.signUpPageLayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        ToastMessage("Oops kayıt edemedim tekrar dene :/");
                        Objects.requireNonNull(auth.getCurrentUser()).delete();

                        referenceImage = firebaseStorage.getReferenceFromUrl(uploadID);
                        referenceImage.delete();

                        binding.progressBar.setVisibility(View.INVISIBLE);
                        binding.loadingMessage.setVisibility(View.INVISIBLE);
                        binding.signUpPageLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            binding.textAddImage.setVisibility(View.INVISIBLE);
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
                                createUser(binding.inputEmail.getText().toString(), binding.inputPass.getText().toString(), uploadID);
                            }
                        }
                    });

        }
    }

    private void selectUserSC() {
        final View popup = getLayoutInflater().inflate(R.layout.uni_department, null);

        ListView listView = popup.findViewById(R.id.listDepartment);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, departments);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.selectUserSc.setText(departments.get(position));
                dialog.dismiss();
            }
        });

        builder.setView(popup);
        dialog = builder.create();
        dialog.show();
    }

    private boolean isValidSignUpDetails() {
        StringBuilder stringBuilder = new StringBuilder(binding.inputEmail.getText().toString().trim());
        if (binding.inputUserName.getText().toString().trim().isEmpty()) {
            ToastMessage("İsminizi rica edebilir miyim ?");
            return false;
        } else if (binding.inputUserSurname.getText().toString().trim().isEmpty()) {
            ToastMessage("Soy isminizi rica edebilir miyim ?");
            return false;
        }  else if (binding.inputUserNo.getText().toString().trim().isEmpty()) {
            ToastMessage("Öğrenci numaranı atladın");
            return false;
        } else if (binding.inputUserNo.getText().length() != 11) {
            ToastMessage("Öğrenci numaran eksik sanki");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            ToastMessage("Mail adresini göremedim");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().length() <= 22 || !stringBuilder.reverse().substring(0, 22).trim().equals("rt.ude.kicelib.icnergo")) {
            ToastMessage("Mail adresini kontrol et!");
            return false;
        } else if (binding.inputPass.getText().toString().trim().isEmpty()) {
            ToastMessage("Şifreni girmedinn!");
            return false;
        } else if (binding.inputPass.getText().toString().trim().length() < 6) {
            ToastMessage("Şifren an az 6 karakter içermeli");
            return false;
        } else if (binding.selectUserSc.getText().toString().trim().equals("Bölüm Seç")) {
            ToastMessage("Bölümünü seçmeyi unuttun!");
            return false;
        } else if(imageUri == null){
            ToastMessage("Resimsiz olmaaaaz");
            return false;
        } else if(!binding.agreement.isChecked()){
            ToastMessage("Söz vermelisin :/");
            return false;
        } else {
            return true;
        }
    }

    private void ToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}