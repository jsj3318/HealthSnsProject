package com.example.healthsnsproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_adapter extends RecyclerView.Adapter<Post_adapter.Post_viewHolder> {
    private Context context;
    private ArrayList<Post_item> postList;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    OnPostItemClickListener onPostItemClickListener;

    public Post_adapter(Context context){ this.context = context; }

    public interface OnPostItemClickListener {
        void onItemClick(Post_item post_item);
    }

    public void setOnPostItemClickListener(OnPostItemClickListener listener) {
        this.onPostItemClickListener = listener;
    }

    public void setList(ArrayList<Post_item> postList) {
        this.postList = postList;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public Post_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new Post_viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Post_viewHolder holder, int position) {
        Post_item post = postList.get(position);
        holder.bind(post);
    }

    public class Post_viewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView_postProfileImage; //, circleImageView_commentProfileImage;
        ImageView imageView_postImage;
        TextView textView_postUsername, textView_date, textView_postContent, textView_count; //textView_commentUsername, textView_comment, ;
        ImageButton imageButton_like;


        public Post_viewHolder(@NonNull View view) {
            super(view);

            circleImageView_postProfileImage = view.findViewById(R.id.postProfileImage);
            imageView_postImage = view.findViewById(R.id.postImage);
            textView_postUsername = view.findViewById(R.id.textView_postUsername);
            textView_date = view.findViewById(R.id.textView_date);
            textView_postContent = view.findViewById(R.id.textView_postContent);
            textView_count = view.findViewById(R.id.textView_count);
            imageButton_like = view.findViewById(R.id.imageButton_like);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Post_item post) {
            //일단 사진부터 비움
            imageView_postImage.setImageDrawable(null);

            itemView.setOnClickListener(v -> {
                if (onPostItemClickListener != null) {
                    onPostItemClickListener.onItemClick(post);
                }
            });

            textView_postUsername.setText(post.getPostUsername());

            //프로필 이미지 뷰 이미지 넣기
            if(post.getPostProfileImageUri() != null){
                Glide.with(context)
                        .load(Uri.parse(post.getPostProfileImageUri()))
                        .placeholder(R.drawable.ic_loading) // 이미지 로딩중 보여주는 이미지
                        .error(R.drawable.ic_unknown)       // 이미지 로딩 실패 시 보여주는 이미지
                        .fallback(R.drawable.ic_unknown)    // 이미지가 없을 시 보여주는 이미지
                        .into(circleImageView_postProfileImage);
            }

            //본문 이미지 뷰 이미지 넣기
            if(post.getPostImageUri() != null){
                Glide.with(context)
                        .load(Uri.parse(post.getPostImageUri()))
                        .placeholder(R.drawable.ic_loading) // 이미지 로딩중 보여주는 이미지
                        .error(R.drawable.ic_unknown)       // 이미지 로딩 실패 시 보여주는 이미지
                        //.fallback(R.drawable.ic_unknown)    // 이미지가 없을 시 보여주는 이미지 -> 이미지 없으면 표시 안함
                        .into(imageView_postImage);
            }

            // 본문 내용이 40자가 넘으면 잘라서 표시
            String content = post.getPostContent();
            if (content.length() > 40) {
                content = content.substring(0, 40) + "...";
            }
            textView_postContent.setText(content);

            // 날짜 문자열 포맷팅 하는 파트
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("yy년 M월 d일 a h시 m분 s초", Locale.getDefault());
            try {
                Date date = originalFormat.parse(post.getDate());
                String formattedDate = targetFormat.format(date);
                textView_date.setText(formattedDate);
            } catch (ParseException e) {
                Toast.makeText(itemView.getContext(), "날짜 포맷팅 실패", Toast.LENGTH_SHORT).show();
                textView_date.setText(post.getDate());  // 포맷 실패 시 원본 날짜를 표시
            }



            // Firestore에서 데이터를 가져와 UI를 설정
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference postRef = db.collection("postings").document(post.getPostId());

            postRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Object likedPeopleObj = document.get("likedPeople");
                        List<String> likedPeople = likedPeopleObj instanceof List<?> ? (List<String>) likedPeopleObj : new ArrayList<>();
                        int likeCount = likedPeople.size();

                        // 좋아요 수와 댓글 수를 반영하기
                        textView_count.setText("좋아요 " + likeCount + "명 댓글 " + post.getCommentCount() + "개");

                        // 좋아요 여부에 따른 이미지 변경
                        if (likedPeople.contains(user.getUid())) {
                            // 만약 사용자의 UID가 likedPeople 배열에 있으면 이미지를 heart2로 변경
                            imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));
                        } else {
                            // 사용자의 UID가 likedPeople 배열에 없으면 이미지를 heart1로 변경
                            imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart1));
                        }
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "Error getting document: ", task.getException());
                }
            });

            imageButton_like.setOnClickListener(v -> {
                // Firestore에서 현재 likedPeople 리스트 가져오기
                postRef.get().addOnCompleteListener(likeTask -> {
                    if (likeTask.isSuccessful()) {
                        DocumentSnapshot likeDocument = likeTask.getResult();
                        if (likeDocument.exists()) {
                            Object likedPeopleObj = likeDocument.get("likedPeople");
                            List<String> likedPeople = likedPeopleObj instanceof List<?> ? (List<String>) likedPeopleObj : new ArrayList<>();
                            boolean isLiked = likedPeople.contains(user.getUid());
                            int likeCount = likedPeople.size();

                            if (isLiked) {
                                // 좋아요가 되어있는 경우 좋아요 취소로 처리
                                imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart1));

                                // likedPeople 배열에서 사용자의 UID 제거
                                postRef.update("likedPeople", FieldValue.arrayRemove(user.getUid()))
                                        .addOnSuccessListener(aVoid -> {
                                            // Firestore 업데이트가 성공한 후 UI 업데이트
                                            textView_count.setText("좋아요 " + (likeCount - 1) + "명 댓글 " + post.getCommentCount() + "개");
                                        });
                            } else {
                                // 좋아요가 안되어있는 경우 좋아요로 처리
                                imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));

                                // likedPeople 배열에 사용자의 UID 추가
                                postRef.update("likedPeople", FieldValue.arrayUnion(user.getUid()))
                                        .addOnSuccessListener(aVoid -> {
                                            // Firestore 업데이트가 성공한 후 UI 업데이트
                                            textView_count.setText("좋아요 " + (likeCount + 1) + "명 댓글 " + post.getCommentCount() + "개");
                                        });
                            }
                        } else {
                            Log.d("Firestore", "No such document");
                        }
                    } else {
                        Log.d("Firestore", "Error getting document: ", likeTask.getException());
                    }
                });
            });






            // 프로필 이미지, 이름 클릭 이벤트 -> 프로필 페이지 들어가기
            View.OnClickListener profile_event = v -> {
                // 해당 게시자 프로필 페이지로 이동
            };

            circleImageView_postProfileImage.setOnClickListener(profile_event);
            textView_postUsername.setOnClickListener(profile_event);
        }

        // 좋아요 상태 업데이트(문서수정)

    }
}
