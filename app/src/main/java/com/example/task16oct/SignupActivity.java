package com.example.task16oct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword, editTextConfirmPassword;
      Button buttonSignup;
      TextView textGoToLogin;

    SharedPreferences sharedPreferences;

    @Override
      protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

          editTextName = findViewById(R.id.editTextName);
                editTextEmail = findViewById(R.id.editTextEmail);
                editTextPassword = findViewById(R.id.editTextPassword);
                editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
                buttonSignup = findViewById(R.id.buttonSignup);
                textGoToLogin = findViewById(R.id.textGoToLogin);

          sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

          buttonSignup.setOnClickListener(v -> attemptSignup());

          textGoToLogin.setOnClickListener(v -> {
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                        finish();
          });
      }

    private void attemptSignup() {
              String name = editTextName.getText().toString().trim();
              String email = editTextEmail.getText().toString().trim();
              String password = editTextPassword.getText().toString().trim();
              String confirmPassword = editTextConfirmPassword.getText().toString().trim();

          if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                              || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
          }

          if (!password.equals(confirmPassword)) {
                        Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
          }

          // Save the new account using SharedPreferences (simple local demo storage)
          SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putString("userName", name);
              editor.putString("userEmail", email);
              editor.putString("userPassword", password);
              editor.putBoolean("isLoggedIn", true);
              editor.putBoolean("isGoogleUser", false);
              editor.commit();

          Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show();

          // Navigate to another activity (MainActivity) after signup
          startActivity(new Intent(SignupActivity.this, MainActivity.class));
              finish();
    }
}
