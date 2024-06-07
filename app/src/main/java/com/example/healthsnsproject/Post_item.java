package com.example.healthsnsproject;

public class Post_item {
    private String postProfileImageUri;
    private String postImageUri;
    private String postUsername;
    private String date;
    private String postContent;

    private String commentProfileImageUri;
    private String commentUsername;
    private String comment;
    private Boolean likeState;
    private int likeCount;
    private int commentCount;

    public Post_item(String postProfileImageUri, String postImageUri, String postUsername, String date, String postContent,
        String commentProfileImageUri, String commentUsername, String comment, Boolean likeState, int likeCount, int commentCount) {

        setPostProfileImageUri(postProfileImageUri);
        setPostImageUri(postImageUri);
        setPostUsername(postUsername);
        setDate(date);
        setPostContent(postContent);
        setCommentProfileImageUri(commentProfileImageUri);
        setCommentUsername(commentUsername);
        setComment(comment);
        setLikeState(likeState);
        setLikeCount(likeCount);
        setCommentCount(commentCount);
    }

    public Post_item(String postProfileImageUri, String postImageUri, String postUsername, String date, String postContent) {
        setPostProfileImageUri(postProfileImageUri);
        setPostImageUri(postImageUri);
        setPostUsername(postUsername);
        setDate(date);
        setPostContent(postContent);
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
}
