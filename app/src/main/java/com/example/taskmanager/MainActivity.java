package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanager.api.UserAPI;
import com.example.taskmanager.model.DetailCheck;
import com.example.taskmanager.serverresponse.SignUpResponse;
import com.example.taskmanager.url.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
TextView create;
EditText user,pass;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        create=findViewById(R.id.txtCreate);
        user=findViewById(R.id.etUser);
        pass=findViewById(R.id.etPass);
        button=findViewById(R.id.btnLog);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
    }

    public void login()
    {
        String name=user.getText().toString();
        String password=pass.getText().toString();

        DetailCheck detailCheck=new DetailCheck(name,password);
        UserAPI userAPI= URL.getInstance().create(UserAPI.class);
        Call<SignUpResponse> loginCall=userAPI.checkUser(name,password);
        loginCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG);
            }
        });

    }
}
