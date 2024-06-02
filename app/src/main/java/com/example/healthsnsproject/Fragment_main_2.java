package com.example.healthsnsproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Fragment_main_2 extends Fragment {

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    ImageView photoView;    //사진 업로드 뷰

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_main_2() {
        // Required empty public constructor
    }


    public static Fragment_main_2 newInstance(String param1, String param2) {
        Fragment_main_2 fragment = new Fragment_main_2();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_2, container, false);

        EditText postContent = view.findViewById(R.id.post_upload_content);
        photoView = view.findViewById(R.id.imageView_upload);
        Button uploadButton = view.findViewById(R.id.button_upload);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지 뷰 눌렀을 때 사진 한장 선택하기
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //업로드 버튼 눌렀을 때 작성한 내용들 게시글 데이터베이스에 업로드


            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //사진 선택 반환 받아오는 곳
        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == getActivity().RESULT_OK) {
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null) {
                    //선택한 사진을 이미지 뷰에 적용해서 표시
                    photoView.setImageURI(selectedImageUri);
                    //업로드 할때 필요하면 추가로 이미지 uri 저장하는 코드 추가 가능


                }
            }
        }

    }
}