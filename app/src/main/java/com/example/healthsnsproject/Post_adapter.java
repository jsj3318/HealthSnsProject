package com.example.healthsnsproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_adapter extends RecyclerView.Adapter<Post_adapter.Post_viewHolder> {
    private Context context;
    private List<Post_item> postList;

    OnPostItemClickListener onPostItemClickListener;

    public Post_adapter(Context context){
        this.context = context;
    }

    public interface OnPostItemClickListener {
        void onItemClick(Post_item post_item);
    }

    public void setOnPostItemClickListener(OnPostItemClickListener listener) {
        this.onPostItemClickListener = listener;
    }

    public void setList(List<Post_item> postList) {
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
        ImageButton imageButton_like, imageButton_comment;

        public Post_viewHolder(@NonNull View view) {
            super(view);

            circleImageView_postProfileImage = view.findViewById(R.id.postProfileImage);
            imageView_postImage = view.findViewById(R.id.postImage);
            textView_postUsername = view.findViewById(R.id.textView_postUsername);
            textView_date = view.findViewById(R.id.textView_date);
            textView_postContent = view.findViewById(R.id.textView_postContent);
            textView_count = view.findViewById(R.id.textView_count);
            imageButton_like = view.findViewById(R.id.imageButton_like);
            imageButton_comment = view.findViewById(R.id.imageButton_comment);

            //댓글부분
            //circleImageView_commentProfileImage = view.findViewById(R.id.commentProfileImage);
            //textView_commentUsername = view.findViewById(R.id.textView_commentUsername);
            //textView_comment = view.findViewById(R.id.textView_comment);
        }

        @SuppressLint("SetTextI18n")
        public void bind(final Post_item post) {
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
            else{
                imageView_postImage.setImageDrawable(null);
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

            // 좋아요 수와 댓글 수를 반영하기
            textView_count.setText("좋아요 " + post.getLikeCount() + "명 댓글 " + post.getCommentCount() + "개");

            // 좋아요 이미 눌렀을 경우 좋아요 버튼 이미지 바꾸기
            if (post.getLikeState()) {
                imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));
            }

            // 좋아요 버튼 동작
            imageButton_like.setOnClickListener(v -> {
                if (post.getLikeState()) {
                    // 좋아요가 되어있는 경우
                    post.setLikeCount(post.getLikeCount() - 1);
                    imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart1));
                } else {
                    // 좋아요 안되어있던 경우
                    post.setLikeCount(post.getLikeCount() + 1);
                    imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));
                }
                post.setLikeState(!post.getLikeState());
                // 카운트 텍스트 뷰 다시 반영하기
                textView_count.setText("좋아요 " + post.getLikeCount() + "명 댓글 " + post.getCommentCount() + "개");
            });

            // 댓글 버튼 클릭 이벤트
            imageButton_comment.setOnClickListener(v -> {
                // 해당 게시글 댓글 창으로 이동
            });

            // 프로필 이미지, 이름 클릭 이벤트 -> 프로필 페이지 들어가기
            View.OnClickListener profile_event = v -> {
                // 해당 게시자 프로필 페이지로 이동
            };

            circleImageView_postProfileImage.setOnClickListener(profile_event);
            textView_postUsername.setOnClickListener(profile_event);
        }
    }
}
