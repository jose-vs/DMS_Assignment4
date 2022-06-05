package com.example.app.screens.login;

import static com.example.app.data.Variables.API_URL;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.R;
import com.example.app.api.LoginApiService;
import com.example.app.data.model.LoginRequest;
import com.example.app.screens.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText usernameView, passwordView;
    private TextView noAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        usernameView = findViewById(R.id.et_email);
        passwordView = findViewById(R.id.et_password);
        noAccount = findViewById(R.id.tvCreateAccount);

        noAccount.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(view -> {
            if(TextUtils.isEmpty(usernameView.getText().toString()) || TextUtils.isEmpty( passwordView.getText().toString()) ) {
                String message = "All inputs required ..";
                Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
            }else{
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setUsername(usernameView.getText().toString());
                loginRequest.setPassword( passwordView.getText().toString());

                loginUser(loginRequest);
            }
        });
    }

    public void loginUser(LoginRequest loginRequest){
        LoginApiService task = new LoginApiService(loginRequest, this);
        task.execute(API_URL + "users/" + loginRequest.getUsername());
    }

}
