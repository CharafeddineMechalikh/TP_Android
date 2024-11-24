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

public class LoginActivity extends HttpActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        PAGE = "login.php";
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("email", emailEditText.getText().toString());
                params.put("password", passwordEditText.getText().toString());

                //Send empail and
                // password to server
                send(params);
            }
        });
    }

    @Override
    protected void responseReceived(String response, Map<String, String> params) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
            if (status.equals("success")) {
                JSONObject userObject = jsonObject.getJSONObject("user_info");
                String family_name = userObject.getString("family_name");
                String first_name = userObject.getString("first_name");
                String email = userObject.getString("email");
                int age = userObject.getInt("age");
                String address = userObject.getString("address");

                // Save user information to shared preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("family_name", family_name);
                editor.putString("first_name", first_name);
                editor.putString("email", email);
                editor.putInt("age", age);
                editor.putString("address", address);
                editor.apply();

                // Start HomeActivity
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                String errorMessage = jsonObject.getString("message");
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLastStatus() {
        return this.status;
    }
}