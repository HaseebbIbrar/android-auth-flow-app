package com.example.task16oct;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView textAvatarInitial, textGreeting, textEmail, textVisitCount, textLoginMethod, textQuote;
      Button buttonLogout, buttonNewQuote;
      SharedPreferences sharedPreferences;

    private static final String[] QUOTES = {
                  "Small steps every day lead to big results.",
                  "Discipline is choosing between what you want now and what you want most.",
                  "Done is better than perfect.",
                  "Every expert was once a beginner.",
                  "Your only limit is the one you set yourself.",
                  "Progress, not perfection.",
                  "Stay consistent, the results will follow.",
                  "Great things take time, keep building."
    };

    private final Random random = new Random();

    @Override
      protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

          textAvatarInitial = findViewById(R.id.textAvatarInitial);
                textGreeting = findViewById(R.id.textGreeting);
                textEmail = findViewById(R.id.textEmail);
                textVisitCount = findViewById(R.id.textVisitCount);
                textLoginMethod = findViewById(R.id.textLoginMethod);
                textQuote = findViewById(R.id.textQuote);
                buttonLogout = findViewById(R.id.buttonLogout);
                buttonNewQuote = findViewById(R.id.buttonNewQuote);

          sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);

          String userName = sharedPreferences.getString("userName", null);
                String userEmail = sharedPreferences.getString("userEmail", "");
                boolean isGoogleUser = sharedPreferences.getBoolean("isGoogleUser", false);

          String displayName = (userName != null && !userName.trim().isEmpty()) ? userName.trim() : userEmail;
                if (displayName == null || displayName.isEmpty()) {
                              displayName = "there";
                }

          // Avatar initial
          String initial = displayName.substring(0, 1).toUpperCase();
                textAvatarInitial.setText(initial);

          // Time-based greeting
          int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                String timeGreeting;
                if (hour < 12) {
                              timeGreeting = "Good morning";
                } else if (hour < 17) {
                              timeGreeting = "Good afternoon";
                } else {
                              timeGreeting = "Good evening";
                }
                String firstName = displayName.contains(" ") ? displayName.split(" ")[0] : displayName;
                textGreeting.setText(timeGreeting + ", " + firstName + "!");
                textEmail.setText(userEmail);

          // Login method badge
          textLoginMethod.setText(isGoogleUser ? "Google" : "Email");

          // Visit counter, persisted via SharedPreferences
          int visitCount = sharedPreferences.getInt("visitCount", 0) + 1;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("visitCount", visitCount);
                editor.commit();
                textVisitCount.setText(String.valueOf(visitCount));

          // Quote of the moment
          showRandomQuote();
                buttonNewQuote.setOnClickListener(v -> showRandomQuote());

          buttonLogout.setOnClickListener(v -> logout());
      }

    private void showRandomQuote() {
              String quote = QUOTES[random.nextInt(QUOTES.length)];
              textQuote.setText("“" + quote + "”");
    }

    private void logout() {
              boolean wasGoogleUser = sharedPreferences.getBoolean("isGoogleUser", false);

          SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putBoolean("isLoggedIn", false);
              editor.commit();

          if (wasGoogleUser) {
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
                        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
                        client.signOut();
          }

          startActivity(new Intent(MainActivity.this, LoginActivity.class));
              finish();
    }
}
