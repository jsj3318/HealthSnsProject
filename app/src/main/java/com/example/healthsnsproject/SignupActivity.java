package com.example.healthsnsproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignupActivity extends AppCompatActivity {
    EditText editText_name;
    EditText editText_id;
    EditText editText_pw;
    EditText editText_pw_re;

    TextView textView_name_error;
    TextView textView_id_error;
    TextView textView_pw_error;
    TextView textView_pw_re_error;

    Button button_create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
        });

        editText_name = findViewById(R.id.editText_name);
        editText_id = findViewById(R.id.editText_id);
        editText_pw = findViewById(R.id.editText_pw);
        editText_pw_re = findViewById(R.id.editText_pw_re);

        textView_name_error = findViewById(R.id.textView_name_error);
        textView_id_error = findViewById(R.id.textView_id_error);
        textView_pw_error = findViewById(R.id.textView_pw_error);
        textView_pw_re_error = findViewById(R.id.textView_pw_re_error);


        button_create_account = findViewById(R.id.button_create_account);
        button_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isError = false;

                //닉네임 비어있는지 검사
                if (editText_name.getText().toString().isEmpty()) {
                    textView_name_error.setText("닉네임을 입력하세요.");
                    isError = true;
                }
                //닉네임 공백 있는지 검사
                else if (editText_name.getText().toString().contains(" ")) {
                    //공백 있음
                    textView_name_error.setText("공백은 입력할 수 없습니다.");
                    isError = true;
                }
                //닉네임 중복 검사

                //닉네임 통과
                else {
                    textView_name_error.setText("");
                }

                //아이디 비어있는지 검사
                if (editText_id.getText().toString().isEmpty()) {
                    textView_id_error.setText("아이디를 입력하세요.");
                    isError = true;
                }
                //아이디 중복 검사

                //아이디 통과
                else {
                    textView_id_error.setText("");
                }

                //패스워드 비어있는지 검사
                if (editText_pw.getText().toString().isEmpty()) {
                    textView_pw_error.setText("패스워드를 입력하세요.");
                    isError = true;
                }
                //패스워드 통과
                else {
                    textView_pw_error.setText("");
                }

                //패스워드 확인 비어있는지 검사
                if (editText_pw_re.getText().toString().isEmpty()) {
                    textView_pw_re_error.setText("패스워드 확인을 입력하세요.");
                    isError = true;
                }
                //패스워드 확인 일치 검사
                else if (!editText_pw.getText().toString().equals(editText_pw_re.getText().toString())) {
                    //틀렸음
                    textView_pw_re_error.setText("패스워드가 일치하지 않습니다.");
                    isError = true;
                }
                //패스워드 확인 통과
                else {
                    textView_pw_re_error.setText("");
                }


                if (!isError) {
                    //에러 없으므로 데이터베이스에 계정 추가하고 회원가입 액티비티 종료
                    //데이터 베이스 계정 정보 추가 부분


                    //회원가입 액티비티 종료
                    Toast.makeText(getApplicationContext(), "계정 생성 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });


    }
}