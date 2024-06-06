package com.example.healthsnsproject;

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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fragment_main_2 extends Fragment {
    private ImageView imageView;
    private EditText editText;
    private Uri imageUri;
    private FirebaseStorage storage;
    private FirebaseFirestore firestore;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_2, container, false);

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
        String text = editText.getText().toString().trim();

        // 이미지와 텍스트가 모두 없는 경우 처리
        if (imageUri == null && text.isEmpty()) {
            Toast.makeText(getContext(), "업로드 할 내용이 없습니다!!", Toast.LENGTH_SHORT).show();
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
                        savePost(text, imageUrl);  // 이미지와 텍스트 저장
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "업로드 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            savePost(text, null);  // 이미지 없이 텍스트만 저장
        }
    }

    // Firestore에 게시글 저장 메서드
    private void savePost(String text, String imageUrl) {
        Map<String, Object> post = new HashMap<>();
        if (!text.isEmpty()) {
            post.put("text", text);  // 텍스트가 있는 경우 추가
        }
        if (imageUrl != null) {
            post.put("imageUrl", imageUrl);  // 이미지 URL이 있는 경우 추가
        }

        firestore.collection("postings").add(post)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "게시글 업로드 완료!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "게시글 업로드 실패!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}