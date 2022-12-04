package com.gmastik.tanfi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {

    // variables
    RelativeLayout filed;
    Button login,register;
    TextInputEditText email,password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // find id
        filed = findViewById(R.id.space);
        login = findViewById(R.id.login);
        register = findViewById(R.id.to_register);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password_login);

        // set position for animation
        filed.setTranslationY(500f);
        filed.setAlpha(0f);

        // set animation
        filed.animate().translationY(0f).alpha(1f).setDuration(800).setStartDelay(300).start();

        register.setOnClickListener(v -> toReg());

        login.setOnClickListener(v -> {
            doLogin();
        });
    }

    public void toReg(){
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
    }

    public void doLogin(){
        // get value from field
        String getEmail = email.getEditableText().toString();
        String getPassword = password.getEditableText().toString();

        // check field
        if(getEmail.isEmpty()){
            email.setError("Isi email anda");
            email.requestFocus();
        } else if(getPassword.isEmpty()) {
            password.setError("Isi password anda");
            password.requestFocus();
        } else {
            getLogin(getEmail,getPassword);
        }
    }

    public void getLogin(String email,String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(Login.this,"Login berhasil",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this,TaskDashboard.class));
            } else {
                Toast.makeText(Login.this,"Login Error: "+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}