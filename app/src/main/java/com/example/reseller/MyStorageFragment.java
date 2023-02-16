package com.example.reseller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.example.reseller.Adapters.TabFragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MyStorageFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabaseRef;
    private List<Book> mUploads;
    private TabLayout mTabs;
    private View mIndicator;
    private ViewPager mViewPager;
    private int indicatorWidth;
    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_mystorage, container, false);
        mTabs = rootview.findViewById(R.id.tab);
        mIndicator = rootview.findViewById(R.id.indicator);
        mViewPager = rootview.findViewById(R.id.viewPager);
        TabFragmentAdapter adapter = new TabFragmentAdapter(getFragmentManager());
        adapter.addFragment(new FragmentForMyStorageOne(), "All");
        adapter.addFragment(new FragmentForMyStorageSec(), "Private");
        add= rootview.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm2 = getFragmentManager();
                FragmentTransaction tr2 = fm2.beginTransaction();
                tr2.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr2.replace(R.id.fragment_container, new AddNewItemFragment()).commit();

                MoovingActivity.toolbar.setTitle("Add new lot");
            }
        });
        //Set up the view pager and fragments
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);

        //Determine indicator width at runtime
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return rootview;
    }
}
