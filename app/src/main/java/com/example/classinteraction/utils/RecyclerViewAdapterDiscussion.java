package com.example.classinteraction.utils;

import android.graphics.Movie;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classinteraction.R;

import java.util.ArrayList;

public class RecyclerViewAdapterDiscussion extends RecyclerView.Adapter<RecyclerViewAdapterDiscussion.ViewHolder>{
        //TODO create XML of inflate
        private final ArrayList<ChatMessage> messageList;

        public RecyclerViewAdapterDiscussion(ArrayList<ChatMessage> items) {
            messageList = items;
            //mListener = listener; mListener = OnClickListener
        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_item, parent, false) ;
        //view.setOnClickListener(rvListener);
        ViewHolder viewholder = new ViewHolder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage newMsg = messageList.get(position);
        //position is index of item on the list

        holder.nameTv.setText(newMsg.getName());
        holder.text.setText(newMsg.getText());
        //viewHolder.icon.setImageBitmap(getMovieIcon(movie.getName(), movie.getRating()));

    }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView nameTv;
            public final TextView text;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                nameTv = (TextView) mView.findViewById(R.id.text_message_name);
                text = (TextView) mView.findViewById(R.id.text_message_body);
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }


}
