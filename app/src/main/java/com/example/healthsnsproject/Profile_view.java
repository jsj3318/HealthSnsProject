package com.example.healthsnsproject;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;


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
            //깃허브 glide 함수 사용 - 이미지 뷰에 이미지 쉽고 부드럽게 넣어주는 라이브러리
        Glide.with(this)
                .load(photoUri)
                .placeholder(R.drawable.ic_loading) // 이미지 로딩중 보여주는 이미지
                .error(R.drawable.ic_unknown)       // 이미지 로딩 실패 시 보여주는 이미지
                .fallback(R.drawable.ic_unknown)    // 이미지가 없을 시 보여주는 이미지
                .into(imageView_profile);

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
