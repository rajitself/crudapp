package com.example.mark10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterAactivity extends AppCompatActivity {
EditText email,password;
Button register;
FirebaseAuth auth;

    private boolean strongPasswordCheckerII(String password) {
        if (password.length() < 8) return false;

        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSpecial = false;
        String specials = "!@#$%^&*()-+";

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (i > 0 && ch == password.charAt(i - 1)) return false;

            if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (specials.indexOf(ch) >= 0) hasSpecial = true;
        }

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_aactivity);
        email = findViewById(R.id.edEmail);
        password = findViewById(R.id.edPassword);
        register = findViewById(R.id.btnRegister);
        TextView passwordHint = findViewById(R.id.password_hint);
        passwordHint.setVisibility(View.VISIBLE);  // You can toggle it if needed

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(RegisterAactivity.this, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!strongPasswordCheckerII(passwordText)) {
                    Toast.makeText(RegisterAactivity.this, "Password must be strong:\n• ≥8 chars\n• 1 upper, lower, digit, special (!@#...)\n• No repeat chars", Toast.LENGTH_LONG).show();
                    return;
                }
                auth = FirebaseAuth.getInstance();
                auth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterAactivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterAactivity.this, loginActivity.class));
                        }
                        else{
                            Toast.makeText(RegisterAactivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterAactivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
                //startActivity(new Intent(RegisterAactivity.this, MainActivity.class));
            }
        });
    }
}