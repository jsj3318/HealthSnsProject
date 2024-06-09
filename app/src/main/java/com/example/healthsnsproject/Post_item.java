package com.example.healthsnsproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Post_item implements Parcelable {
    private String postProfileImageUri = "";
    private String postImageUri = "";
    private String postUsername = "";
    private String date = "";
    private String postContent = "";

    private String commentProfileImageUri = "";
    private String commentUsername = "";
    private String comment = "";
    private Boolean likeState = false;
    private int likeCount = 0;
    private int commentCount = 0;

    public Post_item(String postProfileImageUri, String postImageUri, String postUsername, String date, String postContent) {
        setPostProfileImageUri(postProfileImageUri);
        setPostImageUri(postImageUri);
        setPostUsername(postUsername);
        setDate(date);
        setPostContent(postContent);

        setLikeState(false);
        setLikeCount(0);
        setCommentCount(0);
    }

    public Post_item() {
        /*
        setPostProfileImageUri(getPostProfileImageUri());
        setPostImageUri(getPostImageUri());
        setPostUsername(getPostUsername());
        setDate(getDate());
        setPostContent(getPostContent());
        setCommentProfileImageUri(getCommentProfileImageUri());
        setCommentUsername(getCommentUsername());
        setComment(getComment());
        */
        //setLikeState(getLikeState());
        //setLikeCount(getLikeCount());
        //setCommentCount(getCommentCount());
    }

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

    public String getCommentProfileImageUri() {
        return commentProfileImageUri;
    }
    public void setCommentProfileImageUri(String commentProfileImageUri) {
        this.commentProfileImageUri = commentProfileImageUri;
    }
    public String getCommentUsername() {
        return commentUsername;
    }
    public void setCommentUsername(String commentUsername) {
        this.commentUsername = commentUsername;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Boolean getLikeState() {
        return likeState;
    }
    public void setLikeState(Boolean likeState) {
        this.likeState = likeState;
    }
    public int getLikeCount() {
        return likeCount;
    }
    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
    public int getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }


    protected Post_item(Parcel in) {
        postProfileImageUri = in.readString();
        postImageUri = in.readString();
        postUsername = in.readString();
        date = in.readString();
        postContent = in.readString();
        commentProfileImageUri = in.readString();
        commentUsername = in.readString();
        comment = in.readString();
        byte tmpLikeState = in.readByte();
        likeState = tmpLikeState == 0 ? null : tmpLikeState == 1;
        likeCount = in.readInt();
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
        dest.writeString(commentProfileImageUri);
        dest.writeString(commentUsername);
        dest.writeString(comment);
        dest.writeByte((byte) (likeState == null ? 0 : likeState ? 1 : 2));
        dest.writeInt(likeCount);
        dest.writeInt(commentCount);
    }


}
