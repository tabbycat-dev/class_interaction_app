package com.example.classinteraction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classinteraction.utils.ChatMessage;
import com.example.classinteraction.utils.RecyclerViewAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

/*
* Live Discussion is for each class session
* User can't enter class discussion when class is closed
*
* */
public class DiscussionActivity extends AppCompatActivity {
    private DatabaseReference ref;
    private Button sendBtn, sendnameBtn;
    private EditText messageTv, usernameEt;
    private String name;
    private ArrayList<ChatMessage> messageList;
    private ArrayAdapter<ChatMessage> adapter;
    private ListView listViewMsg;
    private final String ID_KEY = "user_id";
    private final String NAME_KEY = "user_name";
    private final String CLASS_KEY = "class_code";
    private String user_id, user_name, class_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        initUI();
    }
    private void initUI(){
        messageTv = findViewById(R.id.etMessage);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeToFirebase();
            }
        });

        extractBundle();

        listViewSetUp();
        realTimeChat();

    }
    /* to get class code and user display name */
    private void extractBundle(){
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            user_id = extras.getString(ID_KEY);
            user_name = extras.getString(NAME_KEY);
            class_code = extras.getString(CLASS_KEY);
            updateToast(user_id+user_name+class_code);
        }
        // database reference
        ref = FirebaseDatabase.getInstance().getReference("discussion").child(class_code);
    }

    //validate message input is empty
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
    //write chat message to live database DISCUSSION

    public void writeToFirebase() {
        //TODO send chat to firebase
        if (!isEmptyInput() && (!user_name.equals(""))) {
            String text = messageTv.getText().toString();
            final ChatMessage messageObj = new ChatMessage(user_id, user_name, text, new Date());
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

        setUpAdapter();
    }
    /*error handing incase chatlist is null*/
    public void setUpAdapter(){

        listViewMsg = (ListView)findViewById(R.id.chatLisView);

        if (messageList !=null ) {listViewMsg.setAdapter(adapter);  }

        else {listViewMsg.setAdapter(null); }
        }

    private void updateToast(String text){
        Toast.makeText(this ,text, Toast.LENGTH_SHORT).show();
    }

}
