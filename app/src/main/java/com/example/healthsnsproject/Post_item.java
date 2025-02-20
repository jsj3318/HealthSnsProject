package com.example.healthsnsproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Post_item implements Parcelable {
    private String postProfileImageUri = "";
    private String postImageUri = "";
    private String postUsername = "";
    private String date = "";
    private String postContent = "";

    private String postId = "";
    private int commentCount = 0;

    private List<String> likedPeople = new ArrayList<>();


    public Post_item() {}

    // getters & setters 방식으로 변경
    public String getPostProfileImageUri() {
        return postProfileImageUri;
    }
    public void setPostProfileImageUri(String postProfileImageUri) { this.postProfileImageUri = postProfileImageUri; }
    public String getPostImageUri() { return postImageUri; }
    public void setPostImageUri(String postImageUri) {
        this.postImageUri = postImageUri;
    }
    public String getPostUsername() {
        return postUsername;
    }
    public void setPostUsername(String postUsername) {
        this.postUsername = postUsername;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getPostContent() {
        return postContent;
    }
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }



    public List<String> getLikedPeople() {
        return likedPeople;
    }
    public void setLikedPeople(List<String> likedPeople) {
        this.likedPeople = likedPeople;
    }


    protected Post_item(Parcel in) {
        postProfileImageUri = in.readString();
        postImageUri = in.readString();
        postUsername = in.readString();
        date = in.readString();
        postContent = in.readString();

        postId = in.readString();
        commentCount = in.readInt();

    }

    public static final Creator<Post_item> CREATOR = new Creator<Post_item>() {
        @Override
        public Post_item createFromParcel(Parcel in) {
            return new Post_item(in);
        }

        @Override
        public Post_item[] newArray(int size) {
            return new Post_item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postProfileImageUri);
        dest.writeString(postImageUri);
        dest.writeString(postUsername);
        dest.writeString(date);
        dest.writeString(postContent);

        dest.writeString(postId);
        dest.writeInt(commentCount);
    }
}
