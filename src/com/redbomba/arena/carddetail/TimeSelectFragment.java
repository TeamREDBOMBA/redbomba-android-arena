package com.redbomba.arena.carddetail;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.redbomba.arena.R;

import java.util.ArrayList;

/**
 * Created by Sangmok on 2014. 10. 31..
 */
public class TimeSelectFragment extends Fragment {

    LinearLayout contentsLayout_am;
    LinearLayout contentsLayout_am2;
    LinearLayout contentsLayout_pm;
    LinearLayout contentsLayout_pm2;

    private View getList(ArrayList<Integer> time, final int i){
        View v = new View(getActivity());

        // Layout Inflater 객체 참조
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
        v = (View) inflater.inflate(R.layout.list_item_time_selection2, null);

        final TextView hour = (TextView) v.findViewById(R.id.time_hour);
        CheckBox checkbox = (CheckBox) v.findViewById(R.id.time_checkbox);
//        if (i > 0) {
//            rule_contents.setVisibility(View.GONE);
//        }
//        else {
//            rule_expander.setBackgroundResource(R.drawable.ic_arrow_up);
//        }
        if (time.contains(i)) {
            checkbox.setClickable(true);
            checkbox.setButtonDrawable(R.drawable.checkbox_green);
        }

        hour.setText(i + "시");

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    hour.setPaintFlags(hour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    int key = ApplyTimeActivity.result.indexOf(i + 24*getArguments().getInt("pageNum"));
                    ApplyTimeActivity.result.remove(key);
                }
                else {
                    hour.setPaintFlags(hour.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    ApplyTimeActivity.result.add(i + 24*getArguments().getInt("pageNum"));
                }
            }
        });
        return v;
    }

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_step2, vg, false);
        contentsLayout_am = (LinearLayout) rootView.findViewById(R.id.time_container_am);
        contentsLayout_am2 = (LinearLayout) rootView.findViewById(R.id.time_container_am2);
        contentsLayout_pm = (LinearLayout) rootView.findViewById(R.id.time_container_pm);
        contentsLayout_pm2 = (LinearLayout) rootView.findViewById(R.id.time_container_pm2);
//
//        Bundle bundle = getActivity().getIntent().getExtras();
//        final String start = bundle.getString("start");
//        final String end = bundle.getString("end");



//        ArrayList<Integer> arrayList = timeCalc(start, end);
//        int p = ApplyTimeActivity.page;

//        System.out.println(+arrayList.size());
//        for (int i=0; i < arrayList.size(); i++) {
//            System.out.println("=!-"+arrayList.get(i) + ",");
//        }


        for(int i=0; i<6; i++) {
            contentsLayout_am.addView(getList(ApplyTimeActivity.times, i));
        }

        for(int i=6; i<12; i++) {
            contentsLayout_am2.addView(getList(ApplyTimeActivity.times, i));
        }

        for(int i=12; i<18; i++) {
            contentsLayout_pm.addView(getList(ApplyTimeActivity.times, i));
        }

        for(int i=18; i<24; i++) {
            contentsLayout_pm2.addView(getList(ApplyTimeActivity.times, i));
        }

        return rootView;
    }
//
//    private ArrayList<Integer> timeCalc(String start, String end) {
//        char[] chStart = start.toCharArray();
//        char[] chEnd = end.toCharArray();
//        StringBuilder sb = new StringBuilder();
//        StringBuilder sb2 = new StringBuilder();
//
//        sb.append(chStart);
//        sb2.append(chEnd);
//        sb.deleteCharAt(22);
//        sb.deleteCharAt(22);
//
//        start = sb.toString();
//        end = sb2.toString();
//
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
//        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
//        Date convertedStart = null;
//        Date convertedEnd = null;
//
//        try {
//            convertedStart = df.parse(start);
//            convertedEnd = df2.parse(end);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        Calendar calendar = Calendar.getInstance();
//        Calendar calendar2 = Calendar.getInstance();
//        calendar.setTime(convertedStart);
//        calendar2.setTime(convertedEnd);
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//        int hours2 = calendar2.get(Calendar.HOUR_OF_DAY);
//
//        long timediff = convertedEnd.getTime() - convertedStart.getTime();
//        long daydiff = timediff/86400000;
//        ArrayList<Integer> result = new ArrayList<Integer>();
//
//        for (int i=0; i<=daydiff; i++) {
//            for (int j=hours; j<=hours2; j++) {
//                result.add(24 * i + j);
//            }
//        }
//        return result;
//    }
}
