package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class DiscussionActivity extends AppCompatActivity {
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("discussion");
    private Button sendBtn, sendnameBtn;
    private EditText messageTv, usernameEt;
    private String name;
    private ArrayList<ChatMessage> messageList;
    private ArrayAdapter<ChatMessage> adapter;
    private ListView listViewMsg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        initUI();
    }
    private void initUI(){
        getUserName();
        messageTv = findViewById(R.id.etMessage);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToFirebase();
            }
        });
        listViewSetUp();
        realTimeChat();
    }



    private void getUserName(){
        //TODO get username
        usernameEt = findViewById(R.id.etName);
        sendnameBtn = findViewById(R.id.sendNameBtn);
        sendnameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usernameEt.getText().toString().isEmpty()){
                    //TODO set username
                    name = usernameEt.getText().toString();
                    usernameEt.setEnabled(false);
                }else{
                    usernameEt.setError("Name is empty!");
                }
            }
        });
    }

        private boolean isEmptyInput(){
        //TODO check messagechat is empty
        if (!messageTv.getText().toString().isEmpty()){
            return false;
        }else{
            Toast.makeText(DiscussionActivity.this, "Message is empty!",
                    Toast.LENGTH_LONG).show();
            return true;
        }
    }
    public void writeToFirebase() {
        //TODO send chat to firebase
        if (!isEmptyInput() && (!name.equals(""))) {
            // create checkin1 or user enter checkin details on screen to perform self-checkin
            String text = messageTv.getText().toString();
            final ChatMessage messageObj = new ChatMessage(name, text);
            //TODO read user input below and construct checkin instance
            ref.push().setValue(messageObj);
            Toast.makeText(DiscussionActivity.this, "Sent!", Toast.LENGTH_LONG).show();
            messageTv.setText("");
        }
    }


        private void realTimeChat(){
            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    ChatMessage chat = dataSnapshot.getValue(ChatMessage.class);
                    messageList.add(chat);
                    adapter.notifyDataSetChanged();
                    //statusTv.setText(readCheckin.toString());
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Log.i("TAG", "Database error", databaseError.toException());
                }
            });

        }
    private void listViewSetUp() {
        messageList = new ArrayList<ChatMessage>();
        adapter = new ArrayAdapter<ChatMessage>(this, android.R.layout.two_line_list_item, messageList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
                if (view ==null){
                    view = getLayoutInflater().inflate(R.layout.list_row_item,parent, false);
                }
                ChatMessage newMsg =messageList.get(position);
                TextView tvForName = (TextView)view.findViewById(R.id.text_message_name);
                TextView tvForMsg = (TextView)view.findViewById(R.id.text_message_body);
                tvForMsg.setText(newMsg.getText());
                tvForName.setText(newMsg.getName());
                return view;
            }
        };

        listViewMsg = (ListView)findViewById(R.id.chatLisView);
        listViewMsg.setAdapter(adapter);
    }


}
