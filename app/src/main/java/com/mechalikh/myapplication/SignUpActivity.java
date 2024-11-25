package com.mechalikh.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends HttpActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText ageEditText;
    private EditText addressEditText;
    private EditText familyEditText;
    private EditText firstEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        PAGE = "signup.php";
        familyEditText = findViewById(R.id.family_name);
        firstEditText = findViewById(R.id.first_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        ageEditText = findViewById(R.id.age);
        addressEditText = findViewById(R.id.address);
        signUpButton = findViewById(R.id.signup_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailEditText.getText().toString());
                params.put("family_name", familyEditText.getText().toString());
                params.put("first_name", firstEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());
                params.put("age", ageEditText.getText().toString());
                params.put("address", addressEditText.getText().toString());
                send(params);
            }
        });
    }
    @Override
    protected void responseReceived(String response, Map<String, String> params) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("success")) {
                // Get user information
                String sessionToken = jsonObject.getString("session_token");
                String sessionId = jsonObject.getString("session_id");

                // Save user information to shared preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignUpActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("family_name", params.get("family_name"));
                editor.putString("first_name", params.get("first_name"));
                editor.putString("email", params.get("email"));
                editor.putInt("age", Integer.parseInt(params.get("age")));
                editor.putString("address", params.get("address"));
                editor.putString("session_token", sessionToken);
                editor.putString("session_id", sessionId);
                editor.apply();

                // Start HomeActivity
                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                String errorMessage = jsonObject.getString("message");
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
