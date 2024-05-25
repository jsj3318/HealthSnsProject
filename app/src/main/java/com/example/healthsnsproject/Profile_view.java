package com.example.healthsnsproject;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_view extends LinearLayout {
    // 프로필 카드 뷰 클래스
    private ImageView imageView_profile;
    private TextView textView_profile_name;
    private TextView textView_profile_id;

    public Profile_view(Context context) {
        super(context);
        init(context);
    }

    public Profile_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.profile_view, this, true);

        imageView_profile = findViewById(R.id.imageView_profile);
        textView_profile_name = findViewById(R.id.textView_profile_name);
        textView_profile_id = findViewById(R.id.textView_profile_id);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            textView_profile_name.setText(name);
            textView_profile_id.setText(email);
        }

    }

    public void setImage(int resourceId) {   //이미지 리소스 전달 해서 프로필 바 이미지 변경 하는 함수
        imageView_profile.setImageResource(resourceId);
    }

    public void setName(String name) {  //스트링 전달 해서 프로필 바 닉네임 변경 하는 함수
        textView_profile_name.setText(name);
    }

    public void setId(String id) {  //스트링 전달 해서 프로필 바 아이디(이메일) 변경 하는 함수
        textView_profile_id.setText(id);
    }

}
