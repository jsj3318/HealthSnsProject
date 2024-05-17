package com.example.healthsnsproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static Activity loginActivity;

    EditText editText_id;
    EditText editText_pw;
    Button button_login;
    Button button_signup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginActivity = LoginActivity.this;

        editText_id = findViewById(R.id.editText_name);
        editText_pw = findViewById(R.id.editText_pw);
        button_login = findViewById(R.id.button_login);
        button_signup = findViewById(R.id.button_signup);

        mAuth = FirebaseAuth.getInstance();


        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(editText_id.getText().toString(),editText_pw.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();
                            //로그인 정보 (자신에 대한 정보) 메인 액티비티로 보내줘야함



                            //메인 액티비티로 이동
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            //로그인 액티비티는 제거
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {   //회원가입 버튼 클릭 시 회원가입 액티비티로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            }
        });


    }

    //테스트 버튼에 넣을 메인으로 넘어가는 함수
    public void test_button_func(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}