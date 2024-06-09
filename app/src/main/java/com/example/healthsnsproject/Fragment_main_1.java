package com.example.healthsnsproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class Fragment_main_1 extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Post_adapter adapter;
    private ArrayList<Post_item> postList;

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

        // 어댑터 초기화
        postList = new ArrayList<>();
        adapter = new Post_adapter(getContext());
        adapter.setList(postList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //리사이클러 뷰 구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 리프레시 레이아웃 리스너 설정
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //리스트 비움
            postList.clear();

            // 새로고침 이벤트 처리
            loadData();

            // 데이터 로드 완료 후 리프레시 상태 해제
            swipeRefreshLayout.setRefreshing(false);
        });

        // 처음 로딩을 리프레시 레이아웃 새로고침으로 하도록 함
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            loadData();

            swipeRefreshLayout.setRefreshing(false);
        });

        // 아이템 클릭 리스너 설정
        //adapter.setOnPostItemClickListener(post_item -> Toast.makeText(requireActivity().getApplicationContext(), post_item.getPostUsername(), Toast.LENGTH_SHORT).show());
        adapter.setOnPostItemClickListener(post_item -> {
            // 게시물 클릭 시 게시물 화면으로 이동하는 부분
            Intent intent = new Intent(getActivity(), activity_post.class);
            intent.putExtra("post_item", post_item);
            startActivity(intent);

        });

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("postings")
                .orderBy("date", Query.Direction.DESCENDING) // 내림차순 정렬
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Post_item post = new Post_item();

                        String postId = document.getId();
                        post.setPostId(postId);

                        int likeCount = Objects.requireNonNull(document.getLong("likeCount")).intValue();
                        post.setLikeCount(likeCount);

                        if (likeCount == 0 || likeCount == 1) {
                            post.setPrevLikeCount(0);
                        }else{
                            post.setPrevLikeCount(likeCount-1);
                        }

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
                        // post.setCommentCount(post.getCommentCount());

                        postList.add(post);
                    }

                    adapter.setList(postList);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "게시글을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show());
    }

}