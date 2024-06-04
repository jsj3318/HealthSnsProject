package com.example.healthsnsproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

public class Fragment_main_4 extends Fragment {

    private Profile_view profileView;

    private  OnFragmentInteractionListener mListener;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Fragment_main_4() {
        // Required empty public constructor
    }

    public static Fragment_main_4 newInstance(String param1, String param2) {
        Fragment_main_4 fragment = new Fragment_main_4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 프로필 이미지 선택하는 인텐트 반환 받기
        if (requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                profileView.setImage(selectedImageUri);
                // 이미지 uri 파이어 베이스에 업로드
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference mountainsRef = storageRef.child("images/"+user.getDisplayName()+"/"+"profile/"+selectedImageUri.getLastPathSegment());//.child("user.jpg");
                UploadTask uploadTask = mountainsRef.putFile(selectedImageUri);


                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return mountainsRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
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

        //프로필 이미지 클릭 리스너
        profileView.setImageView_clickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("프로필 이미지 변경")
                        .setMessage("프로필 이미지를 변경하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 갤러리에서 이미지 선택하는 인텐트 시작
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, 1);
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 사용자가 이미지 변경을 취소한 경우 아무 작업도 하지 않음
                            }
                        })
                        .show();
            }
        });




        //로그아웃 버튼 이벤트
        Button button = view.findViewById(R.id.button_logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.Logout();
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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