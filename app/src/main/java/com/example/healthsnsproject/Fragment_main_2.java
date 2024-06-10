package com.example.healthsnsproject;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Fragment_main_2 extends Fragment {

    //업로드 로딩화면 다이얼로그
    private ProgressDialog progressDialog;

    private ImageView imageView;
    private EditText editText;
    private Uri imageUri;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final String postUsername = Objects.requireNonNull(user).getDisplayName() + "  (@"
                                    + Objects.requireNonNull(user.getEmail()).split("@")[0]+")";
    private String postProfileImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_2, container, false);

        //다이얼로그 초기화
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("게시글 업로드 중...");
        progressDialog.setCancelable(false);

        imageView = view.findViewById(R.id.imageView_upload);
        editText = view.findViewById(R.id.post_upload_content);
        Button uploadButton = view.findViewById(R.id.button_upload);

        storage = FirebaseStorage.getInstance();  // Firebase Storage 초기화
        firestore = FirebaseFirestore.getInstance();  // Firestore 초기화

        imageView.setOnClickListener(v -> openFileChooser());  // 이미지 선택 클릭 리스너
        uploadButton.setOnClickListener(v -> uploadPost());  // 업로드 버튼 클릭 리스너

        return view;
    }

    // 이미지 선택 런처
    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    imageUri = uri;
                    imageView.setImageURI(imageUri);  // 이미지 설정
                }
            }
    );

    // 파일 선택 메서드
    private void openFileChooser() {
        pickImageLauncher.launch("image/*");  // 이미지 파일 선택
    }

    // 파일 업로드 메서드
    private void uploadPost() {
        //업로드 로딩 다이얼로그 표시
        progressDialog.show();

        String postContent = editText.getText().toString().trim();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 이미지와 텍스트가 모두 없는 경우 처리
        if (imageUri == null && postContent.isEmpty()) {
            Toast.makeText(getContext(), "업로드 할 내용이 없습니다!!", Toast.LENGTH_SHORT).show();

            // 로딩 다이얼로그 취소
            progressDialog.dismiss();
            return;
        }

        // 이미지가 있는 경우
        if (imageUri != null) {
            StorageReference fileReference = storage.getReference()
                    .child("uploads/"+user.getDisplayName()
                            +"/"+"postImages/"+UUID.randomUUID().toString());
            // 계정이름 기준으로 무작위 UUID 경로생성 (충돌 및 덮어쓰기 케어)

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        savePost(imageUrl, postContent, date);  // 이미지와 텍스트 저장
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            savePost(null, postContent, date);  // 이미지 없이 텍스트만 저장
        }
    }

    // Firestore에 게시글 저장 메서드
    private void savePost(String postImageUrl, String postContent, String date) {
        Map<String, Object> post = new HashMap<>();
        post.put("postProfileImageUri", String.valueOf(user.getPhotoUrl()));
        post.put("postUsername", postUsername);
        if (postImageUrl != null) {
            post.put("postImageUrl", postImageUrl);  // 이미지 URL이 있는 경우 추가
        }
        if (!postContent.isEmpty()) {
            post.put("postContent", postContent);  // 텍스트가 있는 경우 추가
        }
        post.put("date", date);
        post.put("likedPeople", null);


        firestore.collection("postings").add(post)
                .addOnCompleteListener(task -> {
                    //업로드 후 다이얼로그 종료
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "게시글 업로드 완료!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "게시글 업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });

        //업로드 후 화면 다시 비우기
        editText.setText("");
        imageUri = null;
        imageView.setImageResource(R.drawable.add_image);

    }
}