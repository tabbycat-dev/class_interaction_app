package com.example.classinteraction.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.classinteraction.ChatMessage;
import com.example.classinteraction.mvp.FirebaseQueryLiveData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DiscussionViewModel extends AndroidViewModel {
    private static final DatabaseReference DISCUSSION_REF = FirebaseDatabase.getInstance().getReference("/discussion");
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(DISCUSSION_REF);

    public DiscussionViewModel(@NonNull Application application) {
        super(application);
    }
    public LiveData<DataSnapshot> getDataSnapshotLiveData(){
        return liveData;
    }
}
