package com.example.healthsnsproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Activity loginActivity;
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


        button_login.setOnClickListener(v -> {
            // 빈칸 있을 때 동작
            if (editText_id.getText().toString().isEmpty() || editText_pw.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "계정 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(editText_id.getText().toString(),editText_pw.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();

                                //FirebaseUser user = mAuth.getCurrentUser();
                                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                                //DatabaseReference myRef = database.getReference("message");

                                //myRef.setValue(user.getPhotoUrl().toString() + "    " + user.getDisplayName().toString());
                                //url 출력 테스트용

                                //메인 액티비티로 이동
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                //로그인 액티비티는 제거
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                            }
                        });

        });

        //회원가입 버튼 클릭 시 회원가입 액티비티로 이동
        button_signup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });


    }


}