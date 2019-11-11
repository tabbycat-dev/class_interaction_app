package com.example.classinteraction.viewpager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.classinteraction.MainActivity;
import com.example.classinteraction.R;
import com.example.classinteraction.utils.ClassCode;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classinteraction.utils.TextDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.OnClick;

public class StudentActivity extends AppCompatActivity implements
        TextDialog.TextDialogListener{

    private String user_name, user_id, class_code, class_name;
    private FirebaseAuth mAuth ;
    private String parcelName = "CC_01";
    private ArrayList<ClassCode> classCodeList;
    public static final String DIALOG_TAG = "dialog_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initUI();

    }
    private void initUI() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        if (getIntent().hasExtra(parcelName)) {
            ArrayList<ClassCode> classCodeList = getIntent().getParcelableArrayListExtra(parcelName);
            ClassCode newClass = classCodeList.get(0);
            class_code = newClass.getClass_code();
            class_name = newClass.getName();
        }
        accessUserInfo();
        //ToolBar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
}


    private void updateStatus(String text){
        Toast.makeText(this ,text, Toast.LENGTH_SHORT).show();
    }
    /*display username and email when activity start*/
    private void accessUserInfo(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            boolean emailVerified = user.isEmailVerified();
            user_id = user.getEmail();
            user_name = user.getDisplayName();
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return ClassroomFragment.newInstance(class_code, class_name, user_id, user_name);
                case 1:
                     //return  CheckinFragment.newInstance(class_code, user_id, user_name);
                    return CheckinFragment.newInstance(class_code, user_id, user_name);
                case 2:
                    return DiscussionFragment.newInstance(class_code, user_id, user_name);//class_code, user_id, user_name);; //ChildFragment3 at position 2
            }
            return null; //does not happen
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position)
            {
                case 0:
                    return "Classroom";
                case 1:
                    return "Check-in";
                case 2:
                    return "Discussion";
            }
            return null; //does not happen
        }
    }
    @Override
    public void onTextDialogOK(boolean agree) {
        Log.d("CHECKIN"," 1 - call TextDialog");
        if(agree){
            signUserOut();
            updateStatus("signUserOut: successful");
        }else{
            updateStatus("Cancel Signout");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case (R.id.action_signout):
                TextDialog dialog = TextDialog.newInstance("Confirm", "Are you sure to sign out?");
                dialog.show(getSupportFragmentManager(), DIALOG_TAG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void signUserOut() {
        // TODO: sign the user out
        mAuth.signOut();
        Intent i = new Intent (getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}