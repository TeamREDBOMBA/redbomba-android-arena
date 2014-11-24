package com.redbomba.arena.carddetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.redbomba.arena.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Created by Sangmok on 2014. 9. 17..
 */
public class RuleFAQActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    private static final String[] pageTitle = {"자세한 대회 룰", "FAQ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rule_faq);




        AppSectionsPagerAdapter pagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.rule_pager);
        mViewPager.setAdapter(pagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.rule_indicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(this);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

//        onPageSelected(1);
        Bundle bundle = getIntent().getExtras();
        indicator.setCurrentItem(bundle.getInt("pageNum"));


//        final ActionBar actionBar = getActionBar();
//        actionBar.setHomeButtonEnabled(false);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        mViewPager = (ViewPager) findViewById(R.id.rule_pager);
//        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                // When swiping between different app sections, select the corresponding tab.
//                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
//                // Tab.
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });
//
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            // Create a tab with text corresponding to the page title defined by the adapter.
//            // Also specify this Activity object, which implements the TabListener interface, as the
//            // listener for when this tab is selected.
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener(this));
//        }
    }

    private class AppSectionsPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        //        implements IconPagerAdapter
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new RuleFragment();
                case 1:
                    return new Fragment();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @Override
        public int getCount() {
            return pageTitle.length;
        }

        @Override
        public CharSequence getPageTitle(int position) { return pageTitle[position]; }

    }

//    @Override
//    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//        mViewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }
//
//    @Override
//    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

//    private class AppSectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public AppSectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int i) {
//            switch (i) {
//                case 0:
//                    return new RuleFragment();
//                default:
//                    return new Fragment();
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//
//        public CharSequence getPageTitle(int position) {
//            return pageTitle[position];
//        }
//    }
}
