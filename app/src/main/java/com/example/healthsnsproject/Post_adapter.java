package com.example.healthsnsproject;

import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_adapter extends RecyclerView.Adapter<Post_adapter.Post_viewHolder> {

    private ArrayList<Post_item> postList = new ArrayList<Post_item>();
    OnPostItemClickListener onPostItemClickListener;

    public interface OnPostItemClickListener {
        void onItemClick(Post_item post_item);
    }


    public void setOnPostItemClickListener(OnPostItemClickListener listener) {
        this.onPostItemClickListener = listener;
    }

    public void addItem(Post_item post) {
        postList.add(post);
    }
    public void setList(ArrayList<Post_item> postList) {
        this.postList = postList;
    }

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
        CircleImageView circleImageView_profile_picture;
        TextView textView_name, textView_date, textView_text, textView_count;
        ViewPager2 pager;
        ImageButton imageButton_like, imageButton_comment;

        public Post_viewHolder(@NonNull View view) {
            super(view);

            circleImageView_profile_picture = view.findViewById(R.id.imageView_profile_picture);
            textView_name = view.findViewById(R.id.textView_name);
            textView_date = view.findViewById(R.id.textView_date);
            textView_text = view.findViewById(R.id.textView_text);
            textView_count = view.findViewById(R.id.textView_count);
            pager = view.findViewById(R.id.pager);
            imageButton_like = view.findViewById(R.id.imageButton_like);
            imageButton_comment = view.findViewById(R.id.imageButton_comment);
        }

        public void bind(final Post_item post) {
             itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPostItemClickListener != null) {
                        onPostItemClickListener.onItemClick(post);
                    }
                }
            });


            //holder 뷰를 post의 내용에 맞게 표시 하도록 설정 하기

            //circleImageView_profile_picture.setImageURI(post.profile_picture_uri);
            textView_name.setText(post.name);

            //본문 내용이 40자가 넘으면 잘라서 표시
            String content = post.text;
            if (content.length() > 40) {
                content = content.substring(0, 40) + "...";
            }
            textView_text.setText(content);


            //날짜 문자열 포맷팅 하는 파트
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat targetFormat = new SimpleDateFormat("yy년 M월 d일 a h시 mm분", Locale.getDefault());
            try {
                Date date = originalFormat.parse(post.date);
                String formattedDate = targetFormat.format(date);
                textView_date.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                textView_date.setText(post.date);  // 포맷 실패 시 원본 날짜를 표시
            }


            //좋아요 수와 댓글 수를 반영하기
            textView_count.setText("좋아요 " + post.likeCount + "명 댓글 " + post.commentCount + "개");

            //좋아요 이미 눌렀을 경우 좋아요 버튼 이미지 바꾸기
            if (post.likeState == true) {
                imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));
            }

            //좋아요 버튼 동작
            imageButton_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(post.likeState == true) {
                        //좋아요가 되어있는 경우
                        post.likeCount--;
                        imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart1));

                    }
                    else {
                        //좋아요 안되어있던 경우
                        post.likeCount++;
                        imageButton_like.setImageDrawable(ContextCompat.getDrawable(itemView.getContext(), R.drawable.heart2));

                    }
                    post.likeState = !post.likeState;
                    //카운트 텍스트 뷰 다시 반영하기
                    textView_count.setText("좋아요 " + post.likeCount + "명 댓글 " + post.commentCount + "개");

                }
            });

            //댓글 버튼 클릭 이벤트
            imageButton_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //해당 게시글 댓글 창으로 이동

                }
            });

            //프로필 이미지, 이름 클릭 이벤트 -> 프로필 페이지 들어가기
            View.OnClickListener profile_event = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //해당 게시자 프로필 페이지로 이동

                }
            };

            circleImageView_profile_picture.setOnClickListener(profile_event);
            textView_name.setOnClickListener(profile_event);


        }
    }
}
