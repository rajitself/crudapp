package com.example.mark10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {
    EditText email, password;
    Button login, register;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.edEmail);
        password = findViewById(R.id.edPassword);
        login = findViewById(R.id.btnLogin);
        register = findViewById(R.id.btnRegister);

        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(view -> {
            String emailInput = email.getText().toString().trim();
            String passwordInput = password.getText().toString().trim();

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(loginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginActivity.this, "Login Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(loginActivity.this, "User ID or Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(loginActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        register.setOnClickListener(view -> {
            startActivity(new Intent(loginActivity.this, RegisterAactivity.class)); // Fix the class name here
        });
    }
}
