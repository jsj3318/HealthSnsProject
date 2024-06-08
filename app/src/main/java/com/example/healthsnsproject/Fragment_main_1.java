package com.example.healthsnsproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_main_1 extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Post_adapter adapter;

    public Fragment_main_1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_main_1.xml 레이아웃을 인플레이트하여 뷰를 생성합니다.
        View view = inflater.inflate(R.layout.fragment_main_1, container, false);

        // 뷰를 초기화합니다.
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        // 어댑터 초기화 및 예제 데이터 추가
        ArrayList<Post_item> postList = new ArrayList<>();
        adapter = new Post_adapter(getContext());
        adapter.setList(postList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadData();

        // 아이템 클릭 리스너 설정
        adapter.setOnPostItemClickListener(post_item -> Toast.makeText(requireActivity().getApplicationContext(), post_item.getPostUsername(), Toast.LENGTH_SHORT).show());

        // 리프레시 레이아웃 리스너 설정
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 새로고침 이벤트 처리
            loadData();

            // 데이터 로드 완료 후 리프레시 상태 해제
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("postings")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post_item> postList = new ArrayList<>();
                    Post_item post = new Post_item();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String postProfileImageUri = document.getString("postProfileImageUri");
                        String postImageUri = document.getString("postImageUrl");
                        String postUsername = document.getString("postUsername");
                        String date = document.getString("date");
                        String postContent = document.getString("postContent");

                        post.setPostProfileImageUri(postProfileImageUri);
                        post.setPostImageUri(postImageUri);
                        post.setPostUsername(postUsername);
                        post.setDate(date);
                        post.setPostContent(postContent);
                        post.setLikeState(post.getLikeState());
                        post.setLikeCount(post.getLikeCount());
                        post.setCommentCount(post.getCommentCount());

                        postList.add(post);
                    }

                    adapter.setList(postList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "게시글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show());
    }
}