package com.example.healthsnsproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_post extends AppCompatActivity {
    Post_item post_item;
    private TextView textView_count;
    private ImageButton imageButton_like;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("SetTextI18n")
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

        CircleImageView circleImageView_postProfileImage = findViewById(R.id.postProfileImage);
        ImageView imageView_postImage = findViewById(R.id.postImage);
        TextView textView_postUsername = findViewById(R.id.textView_postUsername);
        TextView textView_date = findViewById(R.id.textView_date);
        TextView textView_postContent = findViewById(R.id.textView_postContent);
        textView_count = findViewById(R.id.textView_count);
        imageButton_like = findViewById(R.id.imageButton_like);
        ImageButton button_follow = findViewById(R.id.button_follow);


        textView_postUsername.setText(post_item.getPostUsername());

        //팔로우 버튼 이벤트
        button_follow.setOnClickListener(v -> {
            //팔로우 버튼 클릭 시
            Toast.makeText(activity_post.this, post_item.getPostUsername() + " 팔로우 버튼 클릭됨", Toast.LENGTH_SHORT).show();

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


        // Firestore에서 데이터를 가져와 UI를 설정
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference postRef = db.collection("postings").document(post_item.getPostId());

        postRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Object likedPeopleObj = document.get("likedPeople");
                    List<String> likedPeople = likedPeopleObj instanceof List<?> ? (List<String>) likedPeopleObj : new ArrayList<>();
                    int likeCount = likedPeople.size();

                    // 좋아요 수와 댓글 수를 반영하기
                    textView_count.setText("좋아요 " + likeCount + "명 댓글 " + post_item.getCommentCount() + "개");

                    // 좋아요 여부에 따른 이미지 변경
                    if (likedPeople.contains(user.getUid())) {
                        // 만약 사용자의 UID가 likedPeople 배열에 있으면 이미지를 heart2로 변경
                        imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart2));
                    } else {
                        // 사용자의 UID가 likedPeople 배열에 없으면 이미지를 heart1로 변경
                        imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart1));
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.d("Firestore", "Error getting document: ", task.getException());
            }
        });

        imageButton_like.setOnClickListener(v -> {
            // Firestore에서 현재 likedPeople 리스트 가져오기
            postRef.get().addOnCompleteListener(likeTask -> {
                if (likeTask.isSuccessful()) {
                    DocumentSnapshot likeDocument = likeTask.getResult();
                    if (likeDocument.exists()) {
                        Object likedPeopleObj = likeDocument.get("likedPeople");
                        List<String> likedPeople = likedPeopleObj instanceof List<?> ? (List<String>) likedPeopleObj : new ArrayList<>();
                        boolean isLiked = likedPeople.contains(user.getUid());
                        int likeCount = likedPeople.size();

                        if (isLiked) {
                            // 좋아요가 되어있는 경우 좋아요 취소로 처리
                            imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart1));

                            // likedPeople 배열에서 사용자의 UID 제거
                            postRef.update("likedPeople", FieldValue.arrayRemove(user.getUid()))
                                    .addOnSuccessListener(aVoid -> {
                                        // Firestore 업데이트가 성공한 후 UI 업데이트
                                        textView_count.setText("좋아요 " + (likeCount - 1) + "명 댓글 " + post_item.getCommentCount() + "개");
                                    });
                        } else {
                            // 좋아요가 안되어있는 경우 좋아요로 처리
                            imageButton_like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.heart2));

                            // likedPeople 배열에 사용자의 UID 추가
                            postRef.update("likedPeople", FieldValue.arrayUnion(user.getUid()))
                                    .addOnSuccessListener(aVoid -> {
                                        // Firestore 업데이트가 성공한 후 UI 업데이트
                                        textView_count.setText("좋아요 " + (likeCount + 1) + "명 댓글 " + post_item.getCommentCount() + "개");
                                    });
                        }
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "Error getting document: ", likeTask.getException());
                }
            });
        });

        // 프로필 이미지, 이름 클릭 이벤트 -> 프로필 페이지 들어가기
        View.OnClickListener profile_event = v -> {
            // 해당 게시자 프로필 페이지로 이동
        };

        circleImageView_postProfileImage.setOnClickListener(profile_event);
        textView_postUsername.setOnClickListener(profile_event);
    }
}