package com.example.dgale.picaponto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

   EditText email,pass;
   Button login;
     StringRequest request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });

    }

    private void login(){
        String URL = "http://172.16.186.186/picaponto/api/user.php";
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            if (response.trim().equals("success")){
                Toast.makeText(login.this, "Login successfuly", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(login.this, mainpage.class);
                startActivity(i);
            }else{
                Toast.makeText(login.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login.this, "error" +error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Email", email.getText().toString().trim());
                params.put("Password", pass.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
