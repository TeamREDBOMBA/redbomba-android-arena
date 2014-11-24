package com.redbomba.arena.carddetail;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TitlePageIndicator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Sangmok on 2014. 10. 31..
 */
public class ApplyTimeActivity extends FragmentActivity {

    public static ArrayList<Integer> times = new ArrayList<Integer>();
    public static ArrayList<Integer> result = new ArrayList<Integer>();
    public static ArrayList<Date> days = new ArrayList<Date>();

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private LinearLayout daysLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_league_step2);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        Bundle bundle = getIntent().getExtras();
        String start = bundle.getString("start");
        String end = bundle.getString("end");

        days = dayCalc(start, end);
        times = timeCalc(start, end);
        result = timeCalc(start, end);

        final Button nextPage = (Button) findViewById(R.id.time_next_step);
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(ApplyTimeActivity.result);
                int size = result.size();
                String ft = "";
                for (int i=0; i<size; i++) {
                    if (i == size-1) {
                        ft = ft + result.get(i);
                    }
                    else {
                        ft = ft + result.get(i) + ",";
                    }
                }
                Bundle bundle = getIntent().getExtras();
                String lid = bundle.getString("lid");
                String start = bundle.getString("start");
                String end = bundle.getString("end");
                Intent intent = new Intent(ApplyTimeActivity.this, AgreementLeagueActivity.class);
                intent.putExtra("lid", lid);
                intent.putExtra("start", start);
                intent.putExtra("end", end);
                intent.putExtra("ft", ft);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent, 1);
                ApplyTimeActivity.this.overridePendingTransition(R.anim.stay, R.anim.stay);
            }
        });
        nextPage.setClickable(false);


        final AQuery aq = new AQuery(ApplyTimeActivity.this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.step2_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOffscreenPageLimit(days.size());

        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.step2_indicator);
        indicator.setViewPager(mPager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int pos) {

                if (pos == days.size()) {
//                    aq.id(R.id.step2_date).text("시간 선택 완료");
                    aq.id(R.id.time_next_step).background(R.drawable.btn_green);
                    nextPage.setClickable(true);

                }
                else {
//                    aq.id(R.id.step2_date).text(dateConverter(days.get(i)));
                    aq.id(R.id.time_next_step).background(R.drawable.unselected_btn_grey);
                    nextPage.setClickable(false);

                }
//                page = i;
//                System.out.println("-=!!!!!"+page);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//        System.out.println("-=-=!!" + mPagerAdapter.getCount());

//        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {
//
//            }
//
//            @Override
//            public void onPageSelected(int pos) {
//
//                if (pos == days.size()) {
////                    aq.id(R.id.step2_date).text("시간 선택 완료");
////                    aq.id(R.id.time_next_step).background(R.drawable.btn_green);
//                    nextPage.setClickable(true);
//
//                }
//                else {
////                    aq.id(R.id.step2_date).text(dateConverter(days.get(i)));
////                    aq.id(R.id.time_next_step).background(R.drawable.unselected_btn_grey);
//                    nextPage.setClickable(false);
//
//                }
////                page = i;
////                System.out.println("-=!!!!!"+page);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//        aq.id(R.id.step2_date).text(dateConverter(days.get(0)));

//        daysLayout = (LinearLayout) findViewById(R.id.time_days);
//        for (int i=0; i<=days.size(); i++) {
//            if (i == days.size()) {
//                daysLayout.addView(getDayList("시간 선택 완료"));
//            }
//            else {
//                daysLayout.addView(getDayList(dateConverter(days.get(i))));
//
//            }
//        }

    }

    private View getDayList(String date){
        View v = new View(this);

        // Layout Inflater 객체 참조
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
        v = (View) inflater.inflate(R.layout.day_of_week, null);

        final TextView day = (TextView) v.findViewById(R.id.step2_date);
        day.setText(date);
        return v;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if (position == days.size()) {
                return new TimeCompleteFragment();
            }
            else {
                TimeSelectFragment tsf = new TimeSelectFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pageNum", position);
                tsf.setArguments(bundle);
                return tsf;
            }
        }

        @Override
        public int getIconResId(int index) {
            return 0;
        }

        @Override
        public int getCount() {
            return days.size()+1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == days.size()) {
                return "시간 선택 완료";
            }
            else {
                return dateConverter(days.get(position));

            }
        }
//
//        @Override
//        public String getTitle(int position) {
//            return CONTENT[position];
//        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private ArrayList<Integer> timeCalc(String start, String end) {
        char[] chStart = start.toCharArray();
        char[] chEnd = end.toCharArray();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        sb.append(chStart);
        sb2.append(chEnd);
        sb.deleteCharAt(22);
        sb.deleteCharAt(22);

        start = sb.toString();
        end = sb2.toString();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date convertedStart = null;
        Date convertedEnd = null;

        try {
            convertedStart = df.parse(start);
            convertedEnd = df2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(convertedStart);
        calendar2.setTime(convertedEnd);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int hours2 = calendar2.get(Calendar.HOUR_OF_DAY);

        long timediff = convertedEnd.getTime() - convertedStart.getTime();
        long daydiff = timediff/86400000;
        ArrayList<Integer> result = new ArrayList<Integer>();

        for (int i=0; i<=daydiff; i++) {
            for (int j=hours; j<=hours2; j++) {
                result.add(24 * i + j);
            }
        }
        return result;
    }

    private static String dateConverter(Date date) {
//        DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd (EEE)");
        return format.format(date);
    }

    private ArrayList<Date> dayCalc(String start, String end) {
        char[] chStart = start.toCharArray();
        char[] chEnd = end.toCharArray();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        sb.append(chStart);
        sb2.append(chEnd);
        sb.deleteCharAt(22);
        sb.deleteCharAt(22);

        start = sb.toString();
        end = sb2.toString();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date convertedStart = null;
        Date convertedEnd = null;

        try {
            convertedStart = df.parse(start);
            convertedEnd = df2.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar.setTime(convertedStart);
        calendar2.setTime(convertedEnd);

        long timediff = convertedEnd.getTime() - convertedStart.getTime();
        long daydiff = timediff/86400000;
        ArrayList<Date> result = new ArrayList<Date>();

        for (int i=0; i<=daydiff; i++) {
            Date date = new Date();
            date.setTime(convertedStart.getTime() + 86400000*i);
            result.add(date);
        }
        return result;
    }
}
