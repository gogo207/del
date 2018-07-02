package com.delex.a_introduction;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.delex.customer.R;
import com.delex.customer.databinding.ActivityIntroductionBinding;

import org.w3c.dom.ProcessingInstruction;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.ArrayList;

public class IntroductionActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private boolean isMovePageThread = true;
    private PagerAdapter mPagerAdapter;
    private ActivityIntroductionBinding binding;
    private static final int TAB_COUNT = 6;
    public static final String TAG = IntroductionActivity.class.getSimpleName();
    private boolean isMovePaging = true;
    private Thread mMovePageThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_introduction);

        binding.viewpager.setOnTouchListener(this);

        setTabLayout();
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.delex.customer", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setTabLayout() {

        // Creating TabPagerAdapter adapter
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), TAB_COUNT);
        binding.viewpager.setAdapter(mPagerAdapter);
        binding.viewpager.addOnPageChangeListener(this);
        binding.viewpager.setCurrentItem(1);

        movePage();
    }

    public void movePage() {
        final Handler handler = new Handler();
        //현재 위치
//이동할 위치
        mMovePageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isMovePageThread) {
                    try {
                        Thread.sleep(4000);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                int getSelectedPosition = binding.viewpager.getCurrentItem();  //현재 위치
                                int movePosition = getSelectedPosition + 1;  //이동할 위치
                                binding.viewpager.setCurrentItem(movePosition);
                                Log.d(TAG, "run: movePosition");
                            }
                        });
                    } catch (InterruptedException e) {
                        Log.d(TAG, "run: catch ddddd");
//                        e.printStackTrace();
                        mMovePageThread = null;
                        isMovePageThread = false;

                    }
                }
            }
        });
        mMovePageThread.start();
    }

//    public class PageStateChangedException extends Exception {
//
//        public PageStateChangedException() {
//            super("PageStateChangedException");
//        }
//    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch: " + event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "onTouch: ");
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            isMovePageThread = true;
            movePage();
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mMovePageThread != null) {
//                try {
                mMovePageThread.interrupt();
//                    mMovePageThread.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        Log.d(TAG, "onPageScrollStateChanged: " + state);
        if (state == 0) {
            int currentItem = binding.viewpager.getCurrentItem();
            if (currentItem == 5) {
                binding.viewpager.setCurrentItem(1, false);
            } else if (currentItem == 0) {
                binding.viewpager.setCurrentItem(4, false);
            }
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {

        // Count number of tabs
        private int tabCount;
        private IntroductionFragment tabFragment;


        public PagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    tabFragment = IntroductionFragment.newInstance("4");
                    return tabFragment;
                case 1:
                    tabFragment = IntroductionFragment.newInstance("1");
                    return tabFragment;
                case 2:
                    tabFragment = IntroductionFragment.newInstance("2");
                    return tabFragment;
                case 3:
                    tabFragment = IntroductionFragment.newInstance("3");
                    return tabFragment;
                case 4:
                    tabFragment = IntroductionFragment.newInstance("4");
                    return tabFragment;
                case 5:
                    tabFragment = IntroductionFragment.newInstance("1");
                    return tabFragment;
                default:
                    return tabFragment;
            }
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
