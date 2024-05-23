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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_main_1#newInstance} factory method to
 * create an instance of this fragment.
 */
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