package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {

    // variables
    RelativeLayout field;
    Button login,register;
    TextInputEditText firstName,lastName,email,password,confirmPassword;
    FirebaseDatabase root;
    DatabaseReference userRef;
    FirebaseAuth auth;
    String getFirstName,getLastName,getEmail,getPassword,getConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // find id
        field = findViewById(R.id.field);
        login = findViewById(R.id.to_login);
        register = findViewById(R.id.register);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm);

        // set position for animation
        field.setTranslationY(500f);
        field.setAlpha(0f);

        // set animation
        field.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(300).start();

        // onclick button to register
        register.setOnClickListener(v -> {

//            String userTask = "nothing";

//            // set value to helper class
//            getUserData getUserData = new getUserData(getFirstName,getLastName,getEmail,getPassword);

            getRegister();
        });

        // onclick button to login page
        login.setOnClickListener(v -> toLogin());
    }

    public void toLogin(){
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check auth then to dashboard
        if(auth.getCurrentUser() != null){
            toMainApp();
        }
    }

    public void toMainApp(){
        startActivity(new Intent(Register.this,TaskDashboard.class));
        finish();
    }

    public void getRegister(){
        // get string from tex edit
        getFirstName = firstName.getEditableText().toString();
        getLastName = lastName.getEditableText().toString();
        getEmail = email.getEditableText().toString();
        getPassword = password.getEditableText().toString();
        getConfirmPassword = confirmPassword.getEditableText().toString();

        // check field
        if(getFirstName.isEmpty()){
            firstName.setError("Nama depan masih kosong");
            firstName.requestFocus();
        } else if(getLastName.isEmpty()){
            lastName.setError("Nama belakang masih kosong");
            lastName.requestFocus();
        } else if(getPassword.isEmpty()){
            password.setError("Password masih kosong");
            password.requestFocus();
        } else if(getEmail.isEmpty()){
            email.setError("Email masih kosong");
            email.requestFocus();
        } else if(getConfirmPassword.isEmpty()){
            confirmPassword.setError("konfirmasi password anda");
            confirmPassword.requestFocus();
        } else {
            createUser(getFirstName,getLastName,getEmail,getPassword);
        }
    }

    public void createUser(String firstName,String lastName,String email,String password){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                uploadData(firstName,lastName,email);
            } else {
                Toast.makeText(Register.this,"Error: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(Register.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }

    public void uploadData(String firstName,String lastName,String email){
        // get DB references
        root = FirebaseDatabase.getInstance();
        userRef = root.getReference("users");

        // get request id
        String userID;
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        userID = user.getUid();

        HashMap<String,String> userData = new HashMap<>();
        userData.put("id",userID);
        userData.put("first name",firstName);
        userData.put("last name",lastName);
        userData.put("email",email);

        userRef.child(userID).setValue(userData).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Register.this,"Daftar berhasil",Toast.LENGTH_SHORT).show();
                toMainApp();
            } else {
                Toast.makeText(Register.this,"Error: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> Toast.makeText(Register.this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show());
    }
}