package com.muchlish.scan_ai.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.dashboard.DashboardActivity;
import com.muchlish.scan_ai.activity.entity.Auth;
import com.muchlish.scan_ai.activity.entity.MyResponse;
import com.muchlish.scan_ai.activity.entity.User;
import com.muchlish.scan_ai.activity.singlescan.SingleScanActivity;
import com.muchlish.scan_ai.service.ApiClient;
import com.muchlish.scan_ai.service.AuthService;
import com.muchlish.scan_ai.utils.SharedUserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
    }

    private boolean checkForm(){
        if(username.getText().toString().equals("")){
            username.setError("Please fill this field");
            return false;
        }
        if(password.getText().toString().equals("")){
            password.setError("Please fill this field");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v==loginButton){
            if(checkForm()) {
                AuthService authService = ApiClient.getRetrofitInstance().create(AuthService.class);
                User user = new User(username.getText().toString(),password.getText().toString());
                Call<MyResponse<Auth>> call = authService.login(user);
                call.enqueue(new Callback<MyResponse<Auth>>() {
                    @Override
                    public void onResponse(Call<MyResponse<Auth>> call, retrofit2.Response<MyResponse<Auth>> response) {
                        if(response.body()!=null && response.isSuccessful()){
                            DashboardActivity.sp.saveToken(response.body().getData().getToken());
                            DashboardActivity.sp.setIsLogin(true);
                            onBackPressed();
                        }else{
                            Log.e(this.getClass().getName(),response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse<Auth>> call, Throwable t) {
                        Log.e(this.getClass().getName(),t.getLocalizedMessage());
                    }
                });
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.10.248:7742/v1/auth/login",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    if(jsonObject.getBoolean("status")){
//                                        JSONObject data = jsonObject.getJSONObject("data");
//                                        DashboardActivity.sp.saveToken(data.getString("token"));
//                                        DashboardActivity.sp.saveRefreshToken(data.getString("refreshToken"));
//                                        DashboardActivity.sp.setIsLogin(true);
//                                        onBackPressed();
//                                    }else{
//                                        Toast.makeText(LoginActivity.this, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
//                                        System.out.println(jsonObject.get("message").toString());
//                                    }
//                                } catch (JSONException e) {
//                                    System.out.println("Error catch" + e.toString());
//                                    System.out.println("Error resp\n" + response);
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                System.out.println("Errornrs \n" + error.networkResponse);
//                                System.out.println("Errortstr \n" + error.toString());
//                                System.out.println("Errormsg \n" + error.getMessage());
//                                System.out.println("Errorjst \n" + error);
//                            }
//                        }) {
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<>();
//                        params.put("username", username.getText().toString());
//                        params.put("password", password.getText().toString());
//                        return params;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(this);
//                requestQueue.add(stringRequest);
            }
        }
    }
}