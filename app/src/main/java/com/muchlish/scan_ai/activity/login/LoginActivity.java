package com.muchlish.scan_ai.activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.muchlish.scan_ai.utils.OcrCaptureActivity;
import com.muchlish.scan_ai.R;
import com.muchlish.scan_ai.activity.dashboard.DashboardActivity;
import com.muchlish.scan_ai.activity.entity.Auth;
import com.muchlish.scan_ai.activity.entity.MyResponse;
import com.muchlish.scan_ai.activity.entity.User;
import com.muchlish.scan_ai.service.ApiClient;
import com.muchlish.scan_ai.service.AuthService;


import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button loginButton;
//    private SharedPreferences sp;
//    private static final String SHARED_PREF_ID = "sharedPrefs";
//    private static final String TEXT = "text";
//
//    private String text ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
//        loadData();
//        updateViews();

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
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
        //nothing
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
                            // save id
                            //saveid(username.getText().toString());
                            //onBackPressed();
                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                            i.putExtra(OcrCaptureActivity.AutoFocus, true);
                            startActivity(i);
                        }else{
                            Log.e(this.getClass().getName(),response.errorBody().toString());
                            Toast.makeText(LoginActivity.this,"Please Check Your UserId and Password Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse<Auth>> call, Throwable t) {
                        Log.e(this.getClass().getName(),t.getLocalizedMessage());
                    }
                });
            }
        }
    }

//    private void saveid(String _userid)
//    {
//        sp = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//
//        editor.putString(TEXT,_userid);
//        editor.apply();
//    }
//
//    private void loadData()
//    {
//        sp = getSharedPreferences(SHARED_PREF_ID,MODE_PRIVATE);
//        text = sp.getString(TEXT,"");
//
//    }
//
//    private void updateViews()
//    {
//        username.setText(text);
//    }
}