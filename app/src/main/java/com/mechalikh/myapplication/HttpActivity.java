package com.mechalikh.myapplication;

import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public abstract class HttpActivity extends AppCompatActivity {
    protected final String ADDRESS = "http://192.168.50.185/"; // Don't forget to change the ip here
    protected String PAGE;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

    }

    protected void send(Map<String, String> params) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.ADDRESS + this.PAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responseReceived(response, params);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error occurred, show error message
                        Toast.makeText(HttpActivity.this, getResources().getString(R.string.error) + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        // Add request to queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    protected abstract void responseReceived(String response, Map<String, String> params);

    @Override
    public Resources getResources() {
        return super.getResources();
    }


}