package com.example.healthsnsproject;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_item {

    String profile_picture_uri;
    String name;
    String date;
    //페이저 내용물 추가 필요

    String text;
    Boolean likeState;
    int likeCount;
    int commentCount;

    public Post_item(String profile_picture_uri, String name, String date, String text, Boolean likeState, int likeCount, int commentCount) {
        this.profile_picture_uri = profile_picture_uri;
        this.name = name;
        this.date = date;
        this.text = text;
        this.likeState = likeState;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
