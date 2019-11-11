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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    //TODO create XML of inflate
    private final ArrayList<Checkin> studentList;
    public RecyclerViewAdapter(ArrayList<Checkin> items) {
        studentList = items;
        //mListener = listener; mListener = OnClickListener
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.student_list_row, viewGroup, false) ;
        //view.setOnClickListener(rvListener);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    //TODO: #3b create adapter b/onBindViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Checkin checkin = studentList.get(position);
        //position is index of item on the list
        viewHolder.nameTv.setText(checkin.getName());
        viewHolder.userId.setText(checkin.getUserId());
        viewHolder.locationtv.setText(checkin.getLatitude()+","+checkin.getLongitude());
        //viewHolder.icon.setImageBitmap(getMovieIcon(movie.getName(), movie.getRating()));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView icon;
        public final TextView nameTv;
        public final TextView userId;
        public final TextView locationtv;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            icon = (ImageView) mView.findViewById(R.id.row_icon);
            nameTv = (TextView) mView.findViewById(R.id.nameTv);
            userId = (TextView) mView.findViewById(R.id.userId);
            locationtv = (TextView) mView.findViewById(R.id.locationtv);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
