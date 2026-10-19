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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;
      Button buttonLogin, buttonGoogleSignIn;
      TextView textGoToSignup;

    SharedPreferences sharedPreferences;

    GoogleSignInClient googleSignInClient;
      ActivityResultLauncher<Intent> googleSignInLauncher;

    @Override
      protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);

          editTextEmail = findViewById(R.id.editTextEmail);
                editTextPassword = findViewById(R.id.editTextPassword);
                buttonLogin = findViewById(R.id.buttonLogin);
                buttonGoogleSignIn = findViewById(R.id.buttonGoogleSignIn);
                textGoToSignup = findViewById(R.id.textGoToSignup);

          sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

          buttonLogin.setOnClickListener(v -> attemptLogin());

          textGoToSignup.setOnClickListener(v ->
                                                            startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

          // ---- Google Sign-In setup ----
          GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                googleSignInClient = GoogleSignIn.getClient(this, gso);

          googleSignInLauncher = registerForActivityResult(
                            new ActivityResultContracts.StartActivityForResult(),
                            result -> {
                                                  Task<GoogleSignInAccount> task =
                                                                                GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                                  handleGoogleSignInResult(task);
                            });

          buttonGoogleSignIn.setOnClickListener(v -> {
                        Intent signInIntent = googleSignInClient.getSignInIntent();
                        googleSignInLauncher.launch(signInIntent);
          });
      }

    private void attemptLogin() {
              String email = editTextEmail.getText().toString().trim();
              String password = editTextPassword.getText().toString().trim();

          if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                        return;
          }

          String savedEmail = sharedPreferences.getString("userEmail", null);
              String savedPassword = sharedPreferences.getString("userPassword", null);

          if (email.equals(savedEmail) && password.equals(savedPassword)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.commit();

                  Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        goToMain();
          } else {
                        Toast.makeText(this, "Invalid email or password. Sign up first.", Toast.LENGTH_SHORT).show();
          }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
              try {
                            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

                  SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("userEmail", account.getEmail());
                            editor.putString("userName", account.getDisplayName());
                            editor.putBoolean("isGoogleUser", true);
                            editor.commit();

                  Toast.makeText(this, "Signed in as " + account.getEmail(), Toast.LENGTH_SHORT).show();
                            goToMain();
              } catch (ApiException e) {
                            Toast.makeText(this, "Google sign-in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
              }
    }

    private void goToMain() {
              startActivity(new Intent(LoginActivity.this, MainActivity.class));
              finish();
    }
}
