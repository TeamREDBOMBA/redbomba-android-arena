package com.redbomba.arena.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.redbomba.arena.*;
import com.redbomba.arena.functions.BackPressCloseHandler;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private BackPressCloseHandler backPressCloseHandler;
    private int pageNum = 0;
    private static final String[] CONTENT = {"대회목록", "참가중", "좋아요", "기타"};
    private static final int[] ICONS = {
            R.drawable.colosseum_selector,
            R.drawable.selector_action_joined,
            R.drawable.selector_action_like,
            R.drawable.selector_action_etc
    };

    /**
     * Called when the activity is first created.
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu items for use in the action bar
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_activity_action, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            Intent intent = new Intent(this, NoInternetActivity.class);
            startActivity(intent);
            this.finish();
        }

        Bundle bundle = getIntent().getExtras();

        onPageSelected(pageNum);

        AppSectionsPagerAdapter pagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager2);
        mViewPager.setAdapter(pagerAdapter);

        TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        indicator.setOnPageChangeListener(this);

//        indicator.setCurrentItem(Settings.currentViewPage);
//
//        System.out.println("-=-=-="+Settings.currentViewPage);
//        if (Settings.currentViewPage != 0) {
//            indicator.setCurrentItem(Settings.currentViewPage);
//            Settings.currentViewPage = 0;
//        }
        try {
            if (bundle.containsKey("MyArena")) {
                indicator.setCurrentItem(bundle.getInt("MyArena"));
            }
        }
        catch (Exception e) {

        }

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        setTitle(CONTENT[i]);
        pageNum = i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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
                    return new LeagueListFragment();
                case 1:
                    return new TicketListFragment();
                case 3:
                    return new EtcFragment();
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) { return null; }

        @Override
        public int getIconResId(int index) {
            return ICONS[index];
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//
//    private class SendPost extends AsyncTask<Void, Void, String> {
//        protected String doInBackground(Void... unused) {
//            String content = executeClient("uid", "uto", "utotype", "txt");
//            return content;
//        }
//
//        protected void onPostExecute(String result) {
//            // 모두 작업을 마치고 실행할 일 (메소드 등등)
////            setTitle(result);
//        }
//
//        // 실제 전송하는 부분
//        public String executeClient(String uid, String uto, String utotype, String txt) {
//            ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
//            post.add(new BasicNameValuePair("uid", uid));
//            post.add(new BasicNameValuePair("uto", uto));
//            post.add(new BasicNameValuePair("utotype", utotype));
//            post.add(new BasicNameValuePair("feedtype", "1"));
//            post.add(new BasicNameValuePair("txt", txt));
//            post.add(new BasicNameValuePair("tag", "0"));
//            post.add(new BasicNameValuePair("img", "0"));
//            post.add(new BasicNameValuePair("vid", "0"));
//            post.add(new BasicNameValuePair("log", "0"));
//            post.add(new BasicNameValuePair("hyp", "0"));
//
//            // 연결 HttpClient 객체 생성
//            HttpClient client = new DefaultHttpClient();
//
//            // 객체 연결 설정 부분, 연결 최대시간 등등
//            HttpParams params = client.getParams();
//            HttpConnectionParams.setConnectionTimeout(params, 5000);
//            HttpConnectionParams.setSoTimeout(params, 5000);
//
//            // Post객체 생성
//            HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=TestPost");
//
//            try {
//                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
//                httpPost.setEntity(entity);
//                client.execute(httpPost);
//                System.out.println("====="+EntityUtils.getContentCharSet(entity));
//                System.out.println("====="+client.execute(httpPost));
////                System.out.println("=====2" + EntityUtils.toString(entity, "godcomplex"));
//                return EntityUtils.getContentCharSet(entity);
//            } catch (ClientProtocolException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


}
