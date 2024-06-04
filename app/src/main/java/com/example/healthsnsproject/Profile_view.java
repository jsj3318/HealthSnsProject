package com.example.healthsnsproject;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


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



    }

    public void setImage(Uri photoUri) {   //이미지 uri 전달 해서 프로필 바 이미지 변경 하는 함수
        if(photoUri != null)
            imageView_profile.setImageURI(photoUri); //?
    }

    public void setName(String name) {  //스트링 전달 해서 프로필 바 닉네임 변경 하는 함수
        if(name != null)
            textView_profile_name.setText(name);
    }

    public void setId(String id) {  //스트링 전달 해서 프로필 바 아이디(이메일) 변경 하는 함수
        if(id != null)
            textView_profile_id.setText(id);
    }

    public void setImageView_clickListener(OnClickListener listener){
        imageView_profile.setOnClickListener(listener);
    }

}
