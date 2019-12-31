package com.example.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.taskmanager.api.UserAPI;
import com.example.taskmanager.model.Detail;
import com.example.taskmanager.serverresponse.SignUpResponse;
import com.example.taskmanager.url.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
EditText fname,lname,username,password,cpassword;
Button signbtn;
ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname=findViewById(R.id.etFname);
        lname=findViewById(R.id.etLname);
        username=findViewById(R.id.etusername);
        password=findViewById(R.id.etPassword);
        cpassword=findViewById(R.id.etCPassword);
        signbtn=findViewById(R.id.btnSignUp);
        img=findViewById(R.id.imgView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image();
            }
        });
        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Register();
            }
        });
    }

    public void image()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(data==null)
            {
                Toast.makeText(this,"Please select an image",Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri=data.getData();
        img.setImageURI(uri);
    }

    private String getRealPathFromUri(Uri uri)
    {
        String[] projection={MediaStore.Images.Media.DATA};
        CursorLoader loader=new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int colIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result=cursor.getString(colIndex);
        cursor.close();
        return result;
    }


    public void Register()
    {
        String fn=fname.getText().toString();
        String ln=lname.getText().toString();
        String user=username.getText().toString();
        String pass=password.getText().toString();
        String cpass=cpassword.getText().toString();
        String image=img.toString();
        if(!pass.equals(cpass))
        {
            Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT).show();
            cpassword.setText("");
            cpassword.requestFocus();
            return;
        }
        Detail detail =new Detail(fn,ln,user,pass,image);
        UserAPI userAPI= URL.getInstance().create(UserAPI.class);
        Call<SignUpResponse> signUpCall= userAPI.registerUser(detail);
        signUpCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

                Toast.makeText(SignUpActivity.this,"User Saved",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"Code" + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
