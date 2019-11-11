package com.example.classinteraction.viewpager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classinteraction.DiscussionActivity;
import com.example.classinteraction.R;
import com.example.classinteraction.RegisterActivity;
import com.example.classinteraction.utils.ChatMessage;
import com.example.classinteraction.utils.Checkin;
import com.example.classinteraction.utils.RecyclerViewAdapter;
import com.example.classinteraction.utils.RecyclerViewAdapterDiscussion;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link DiscussionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscussionFragment extends Fragment {
    private DatabaseReference ref;
    private ArrayList<ChatMessage> messageList;
    private static String ID_KEY = "user_id";
    private static String UNAME_KEY = "user_name";
    private static String CLASS_KEY = "class_code";
    private String user_id, user_name, class_code;
    @BindView(R.id.etMessage)
    EditText messaggeET;

    @BindView(R.id.chatLisView)
    RecyclerView recyclerView;

    private RecyclerViewAdapterDiscussion mAdapter;

    public DiscussionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment DiscussionFragment.
     */
    public static DiscussionFragment newInstance(String class_code, String user_id, String user_name) {
        DiscussionFragment fragment = new DiscussionFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_KEY, class_code);
        args.putString(ID_KEY, user_id);
        args.putString(UNAME_KEY, user_name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initUI(){
        if (getArguments() != null) {
            user_id = getArguments().getString(ID_KEY);
            user_name = getArguments().getString(UNAME_KEY);
            class_code = getArguments().getString(CLASS_KEY);
            ref = FirebaseDatabase.getInstance().getReference("discussion").child(class_code);
            listViewSetUp();
            realTimeChat();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discussion, container, false);
        ButterKnife.bind(this,view);
        initUI();
        return view;
    }


    @OnClick(R.id.sendBtn) void sendChat(){
        writeToFirebase();
    }
    //validate message input is empty
    private boolean isEmptyInput(){
        //TODO check messagechat is empty
        if (!messaggeET.getText().toString().isEmpty()){
            return false;
        }else{
            messaggeET.setError("Please enter some texts!");
            return true;
        }
    }
    //write chat message to live database DISCUSSION

    private void writeToFirebase() {
        //TODO send chat to firebase
        if (!isEmptyInput() && (!user_name.equals(""))) {
            String text = messaggeET.getText().toString();
            final ChatMessage messageObj = new ChatMessage(user_id, user_name, text, new Date());
            //TODO read user input below and construct checkin instance
            ref.push().setValue(messageObj);
            updateToast("Sent!");
            messaggeET.setText("");
        }
    }
    private void realTimeChat(){
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);
                messageList.add(chat);
                mAdapter.notifyDataSetChanged();
                //statusTv.setText(readCheckin.toString());
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    private void listViewSetUp() {
        messageList = new ArrayList<ChatMessage>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        setUpAdapter();
    }
    /*error handing incase chatlist is null*/
    public void setUpAdapter(){
        if (messageList !=null ) {
            mAdapter = new RecyclerViewAdapterDiscussion(messageList);
            recyclerView.setAdapter(mAdapter);  }
        else {recyclerView.setAdapter(null); }
    }
    private void updateToast(String text){
        Toast.makeText(this.getContext() ,text, Toast.LENGTH_SHORT).show();
    }



}
