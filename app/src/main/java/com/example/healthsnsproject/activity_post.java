package com.example.healthsnsproject;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_post extends AppCompatActivity {

    Post_item post_item;
    private CircleImageView circleImageView_postProfileImage;
    private ImageView imageView_postImage;
    private TextView textView_postUsername, textView_date, textView_postContent, textView_count;
    private ImageButton imageButton_like, button_follow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        post_item = intent.getParcelableExtra("post_item");

        circleImageView_postProfileImage = findViewById(R.id.postProfileImage);
        imageView_postImage = findViewById(R.id.postImage);
        textView_postUsername = findViewById(R.id.textView_postUsername);
        textView_date = findViewById(R.id.textView_date);
        textView_postContent = findViewById(R.id.textView_postContent);
        textView_count = findViewById(R.id.textView_count);
        imageButton_like = findViewById(R.id.imageButton_like);
        button_follow = findViewById(R.id.button_follow);


        textView_postUsername.setText(post_item.getPostUsername());

        //팔로우 버튼 이벤트
        button_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //팔로우 버튼 클릭 시
                Toast.makeText(activity_post.this, post_item.getPostUsername() + " 팔로우 버튼 클릭됨", Toast.LENGTH_SHORT).show();


            }
        });

        //프로필 이미지 뷰 이미지 넣기
        if(post_item.getPostProfileImageUri() != null){
            Glide.with(this)
                    .load(Uri.parse(post_item.getPostProfileImageUri()))
                    .placeholder(R.drawable.ic_loading) // 이미지 로딩중 보여주는 이미지
                    .error(R.drawable.ic_unknown)       // 이미지 로딩 실패 시 보여주는 이미지
                    .fallback(R.drawable.ic_unknown)    // 이미지가 없을 시 보여주는 이미지
                    .into(circleImageView_postProfileImage);
        }

        //본문 이미지 뷰 이미지 넣기
        if(post_item.getPostImageUri() != null){
            Glide.with(this)
                    .load(Uri.parse(post_item.getPostImageUri()))
                    .placeholder(R.drawable.ic_loading) // 이미지 로딩중 보여주는 이미지
                    .error(R.drawable.ic_unknown)       // 이미지 로딩 실패 시 보여주는 이미지
                    //.fallback(R.drawable.ic_unknown)    // 이미지가 없을 시 보여주는 이미지 -> 이미지 없으면 표시 안함
                    .into(imageView_postImage);
        }

        // 본문 표시
        textView_postContent.setText(post_item.getPostContent());

        // 날짜 문자열 포맷팅 하는 파트
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat targetFormat = new SimpleDateFormat("yy년 M월 d일 a h시 m분 s초", Locale.getDefault());
        try {
            Date date = originalFormat.parse(post_item.getDate());
            String formattedDate = targetFormat.format(date);
            textView_date.setText(formattedDate);
        } catch (ParseException e) {
            Toast.makeText(this, "날짜 포맷팅 실패", Toast.LENGTH_SHORT).show();
            textView_date.setText(post_item.getDate());  // 포맷 실패 시 원본 날짜를 표시
        }

        // 좋아요 수와 댓글 수를 반영하기
        textView_count.setText("좋아요 " + post_item.getLikeCount() + "명 댓글 " + post_item.getCommentCount() + "개");

        // 좋아요 여부에 따른 이미지 변경
        if (post_item.getLikeState()) {
            imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart2));
        }
        else {
            imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart1));
        }

        // 좋아요 버튼 동작
        imageButton_like.setOnClickListener(v -> {
            if (post_item.getLikeState()) {
                // 좋아요가 되어있는 경우
                post_item.setLikeCount(post_item.getLikeCount() - 1);
                imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart1));
            } else {
                // 좋아요 안되어있던 경우
                post_item.setLikeCount(post_item.getLikeCount() + 1);
                imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart2));
            }
            post_item.setLikeState(!post_item.getLikeState());
            // 카운트 텍스트 뷰 다시 반영하기
            textView_count.setText("좋아요 " + post_item.getLikeCount() + "명 댓글 " + post_item.getCommentCount() + "개");
        });

        // 프로필 이미지, 이름 클릭 이벤트 -> 프로필 페이지 들어가기
        View.OnClickListener profile_event = v -> {
            // 해당 게시자 프로필 페이지로 이동
        };

        circleImageView_postProfileImage.setOnClickListener(profile_event);
        textView_postUsername.setOnClickListener(profile_event);


    }
}