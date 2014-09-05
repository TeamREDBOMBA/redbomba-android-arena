package com.redbomba.arena;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static String[] pageTitle = {"모든 대회", "대회 티켓"};
    static int uid;

    /**
     * Called when the activity is first created.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences sharedpreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        uid = sharedpreferences.getInt("uid", 0);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
//
//    public void fragmentReplace() {
//
//        Fragment newFragment = null;
//
//        newFragment = new ArenaCardList();
//
//        // replace fragment
//        final android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//
//        transaction.replace(R.id.ll_fragment, newFragment);
//
//        // Commit the transaction
//        transaction.commit();
//
//    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new ArenaListFragment();
                default:
                    return new MyArenaFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }
//    public static class TestCardList extends Fragment {
//
//        @Override
//        public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
//            View rootView = lf.inflate(R.layout.fragment_cardlist, vg, false);
//            ListView m_list = (ListView) rootView.findViewById(R.id.arena_cardlist);
//
//            m_list.addHeaderView(new View(getActivity()));
//            m_list.addFooterView(new View(getActivity()));
//
//            BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
//            for (int i = 0; i < DB.get_length_of_jsonArray(); i++)
//            {
//                //add items on a card
//                CardItemData data = new CardItemData(
//                        DB.get_league_info(i, "name"),
//                        "참가자 레벨: "+DB.get_league_info(i, "level"),
//                        DB.d_day(i),
//                        "http://redbomba.net"+DB.get_league_info(i, "poster"),
//                        100*DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id")))/Integer.parseInt(DB.get_league_info(i, "max_team")),
//                        "참가자 현황: "+String.valueOf(DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id"))))+"/"+DB.get_league_info(i, "max_team")
//                );
//                adapter.addItem(data, false);
//            }
//            m_list.setAdapter(adapter);
//
//            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position,
//                                        long id) {
//                    Intent intent = new Intent(getActivity(), CardDetailActivity.class);
//                    startActivity(intent);
//                }
//            });
//            m_list.setItemsCanFocus(true);
//            m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
////
////            if(vg == null) {
////                return null;
////            }
//            return rootView;
//        }
//    }
//
//        m_list.addHeaderView(new View(this));
//        m_list.addFooterView(new View(this));
//
//        BaseInflaterAdapter<CardItemData> adapter = new BaseInflaterAdapter<CardItemData>(new CardInflater());
//        for (int i = 0; i < DB.get_length_of_jsonArray(); i++)
//        {
//            //add items on a card
//            CardItemData data = new CardItemData(
//                    DB.get_league_info(i, "name"),
//                    "참가자 레벨: "+DB.get_league_info(i, "level"),
//                    DB.d_day(i),
//                    "http://redbomba.net"+DB.get_league_info(i, "poster"),
//                    100*DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id")))/Integer.parseInt(DB.get_league_info(i, "max_team")),
//                    "참가자 현황: "+String.valueOf(DB.get_league_team(DB.get_league_round(DB.get_league_info(i, "id"))))+"/"+DB.get_league_info(i, "max_team")
//            );
//            adapter.addItem(data, false);
//        }
//
//        m_list.setAdapter(adapter);
//
//        m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position,
//                                    long id) {
//                Intent intent = new Intent(MainActivity.this, CardDetailActivity.class);
//                startActivity(intent);
//            }
//        });
//        m_list.setItemsCanFocus(true);
//
//        for (int i = 0; i < 3; i++) {
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText("Tab " + (i + 1))
//                            .setTabListener(this));
//        }
//        fragmentReplace();

}
