package com.example.healthsnsproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Fragment_main_1 extends Fragment {

    RecyclerView recyclerView;
    Post_adapter adapter;
    ArrayList<Post_item> postList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fragment_main_1() {
        // Required empty public constructor
    }

    public static Fragment_main_1 newInstance(String param1, String param2) {
        Fragment_main_1 fragment = new Fragment_main_1();
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
        View view = inflater.inflate(R.layout.fragment_main_1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        adapter = new Post_adapter();
        //예시용 아이템 넣기
        for (int i=1; i<6; i++){
            adapter.addItem(new Post_item(null, "이름 " + i, "2025-06-06 12:30:30",
                    "본문 " + i + " aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    (i%2 == 1), i, i));

        }



        recyclerView.setAdapter(adapter);

        adapter.setOnPostItemClickListener(new Post_adapter.OnPostItemClickListener() {
            @Override
            public void onItemClick(Post_item post_item) {
                Toast.makeText(getActivity().getApplicationContext(), post_item.name, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}