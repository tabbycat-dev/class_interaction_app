package com.example.classinteraction.viewpager;

import android.os.Bundle;

import com.example.classinteraction.R;
import com.example.classinteraction.utils.ClassCode;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    private String user_name, user_id, class_code, class_name;
    private FirebaseAuth mAuth ;
    private String parcelName = "CC_01";
    private ArrayList<ClassCode> classCodeList;

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
                    return CheckinFragment.newInstance(class_code, user_id, user_name);
                case 2:
                    return DiscussionFragment.newInstance();//class_code, user_id, user_name);; //ChildFragment3 at position 2
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

}