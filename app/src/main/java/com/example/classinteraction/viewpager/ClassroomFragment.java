package com.example.classinteraction.viewpager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.classinteraction.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ClassroomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassroomFragment extends Fragment {
    private boolean status = false;
    private String user_name, user_id, class_code, class_name ;
    private static String ID_KEY = "user_id";
    private static String UNAME_KEY = "user_name";
    private static String CLASS_KEY = "class_code";
    private static String CNAME_KEY = "class_name";

    @BindView(R.id.tvName)
    TextView tvName;
    private DatabaseReference ref ;

    @BindView(R.id.etClassName)
    TextView etClassName ;

    @BindView(R.id.etClassCode)
    TextView etClassCode ;

    @BindView(R.id.etActive)
    TextView etActive ;

    public ClassroomFragment() {
        // Required empty public constructor
    }

    public static ClassroomFragment newInstance(String class_code, String class_name, String user_id, String user_name) {
        ClassroomFragment fragment = new ClassroomFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_KEY, class_code);
        args.putString(CNAME_KEY, class_name);
        args.putString(ID_KEY, user_id);
        args.putString(UNAME_KEY, user_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            class_code = getArguments().getString(CLASS_KEY);
            class_name = getArguments().getString(CNAME_KEY);
            user_id = getArguments().getString(ID_KEY);
            user_name = getArguments().getString(UNAME_KEY);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classroom, container, false);

        ButterKnife.bind(this,view);
        readLiveClass();        // display details of class user is in
        updateUI();
        return view;
    }
    private void updateUI(){
        etClassCode.setText(class_code);
        etClassName.setText(class_name);
        tvName.setText("Hello "+ user_name+" ("+ user_id+")");
        status = true;
        etActive.setText("active");
    }
    private void readLiveClass(){
        Query query = FirebaseDatabase.getInstance().getReference("class")
                .orderByChild("class_code")
                .equalTo(class_code);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is added");

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is changed");

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("FIREBASE","- child is remvoed");
                updateClassClose();

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("FIREBASE","- child is moved");

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("FIREBASE","- child is cancel");

            }
        });

    }
    private void updateClassClose(){
        status = false;
        etActive.setText("closed");
    }




}
