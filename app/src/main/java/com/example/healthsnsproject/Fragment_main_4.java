package com.example.healthsnsproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;

public class Fragment_main_4 extends Fragment {
    private ProgressDialog progressDialog; //프로필 사진 업로드 로딩 다이얼로그
    private Profile_view profileView;
    private  OnFragmentInteractionListener mListener;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public Fragment_main_4() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 프로필 이미지 선택하는 인텐트 반환 받기
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                //업로드 로딩 다이얼로그 표시
                progressDialog.show();

                profileView.setImage(selectedImageUri);
                // 이미지 uri 파이어 베이스에 업로드
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference mountainsRef = storageRef.child("images/"+user.getDisplayName()+"/"+"profile/"+selectedImageUri.getLastPathSegment());//.child("user.jpg");
                UploadTask uploadTask = mountainsRef.putFile(selectedImageUri);

                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }

                    // Continue with the task to get the download URL
                    return mountainsRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    //업로드 다이얼로그 종료
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(downloadUri)
                                .build();

                        user.updateProfile(profileUpdates);

                        Toast.makeText(getContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_4, container, false);

        profileView = view.findViewById(R.id.profile_view_in_fragment4);

        //파이어베이스 유저 정보 불러와서 프로필 뷰 메서드로 넣기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            Uri photoUrl = user.getPhotoUrl();
            String name = user.getDisplayName();
            String email = user.getEmail();

            if(profileView != null){
                profileView.setImage(photoUrl);
                profileView.setName(name);
                profileView.setId(email);

            }
        }

        //다이얼로그 초기화
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("프로필 이미지 업로드 중...");

        //프로필 이미지 클릭 리스너
        profileView.setImageView_clickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("프로필 이미지 변경")
                    .setMessage("프로필 이미지를 변경하시겠습니까?")
                    .setPositiveButton("예", (dialog, which) -> {
                        // 갤러리에서 이미지 선택하는 인텐트 시작
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);


                    })
                    .setNegativeButton("아니오", (dialog, which) -> {
                        // 사용자가 이미지 변경을 취소한 경우 아무 작업도 하지 않음
                    })
                    .show();
        });

        //로그아웃 버튼 이벤트
        Button button = view.findViewById(R.id.button_logout);
        button.setOnClickListener(v -> mListener.Logout());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void Logout();
    }

}