package com.redbomba.arena.carddetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.urls.Urls;

import java.util.ArrayList;

/**
 * Created by Sangmok on 2014. 9. 18..
 */
public class RuleFragment extends Fragment {

    View test;
    private View getList(String title, String contents, int i){
        View v = new View(getActivity());

        // Layout Inflater 객체 참조
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
        v = (View) inflater.inflate(R.layout.rule_list, null);

        LinearLayout rule_head = (LinearLayout) v.findViewById(R.id.rule_head);
        TextView rule_title = (TextView) v.findViewById(R.id.rule_title);
        TextView rule_contents = (TextView) v.findViewById(R.id.rule_contents);
        ImageView rule_expander = (ImageView) v.findViewById(R.id.rule_expander);
        if (i > 0) {
            rule_contents.setVisibility(View.GONE);
        }
        else {
            rule_expander.setBackgroundResource(R.drawable.ic_arrow_up);
        }
        rule_head.setOnClickListener(new Expander(rule_contents, rule_expander));

        rule_title.setText(title);
        rule_contents.setText(Html.fromHtml(contents));
        return v;
    }

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_rule, vg, false);
        LinearLayout contentsLayout = (LinearLayout) rootView.findViewById(R.id.contentsLayout);

        AQuery aq = new AQuery(getActivity());
        Bundle bundle = getActivity().getIntent().getExtras();
        String rule = bundle.getString("rule");
        String poster = bundle.getString("poster");
        String leagueName = bundle.getString("league_name");
        sortRules(rule);
        ArrayList<String> rules = sortRules(rule);

        aq.id(R.id.rule_league_poster).image(Urls.MIDEA + poster);
        aq.id(R.id.rule_league_name).text(leagueName);





        for (int i=0; i<rules.size(); i++) {
            String row = rules.get(i);
            String title = row.substring(0, row.indexOf('\n'));
            String contents = row.substring(row.indexOf('\n')+1);
            contentsLayout.addView(getList(title, contents, i));
        }
        return rootView;
    }
    public ArrayList<String> sortRules(String rules) {
        ArrayList<String> result = new ArrayList<String>();
        String[] tnc = {"", ""};
        String[] rulelines = rules.split(System.getProperty("line.separator"));
        int a;
        a = -1;
        if (!rulelines[0].contains("#")) {
            return result;
        }
        for (int i=0; i<rulelines.length; i++) {
            if (rulelines[i].contains("##")) {
                String s = rulelines[i].replace("##", "");
                result.set(a, result.get(a) + "<strong>" + s + "</strong>\n");
            } else if (rulelines[i].contains("#")) {
                a++;
                String s = rulelines[i].replace("#", "");
                result.add(s.trim() + "\n");
            } else {
                result.set(a, result.get(a) + "<p>" + rulelines[i] + "</p>\n");
            }
        }
        return result;
    }

    class Expander implements View.OnClickListener {
        TextView tv;
        ImageView iv;
        AQuery aq = new AQuery(getActivity());
        Expander (TextView textview, ImageView expander) {
            tv = textview;
            iv = expander;
        }
        @Override
        public void onClick(View v) {
            if (tv.getVisibility() == View.VISIBLE) {
                tv.setVisibility(View.GONE);
                ImageView i = (ImageView) v.findViewById(R.id.rule_expander);
                iv.setBackgroundResource(R.drawable.ic_arrow_down);
            }
            else {
                tv.setVisibility(View.VISIBLE);
                iv.setBackgroundResource(R.drawable.ic_arrow_up);
            }
        }
    }

    class MyView extends View {
        View test;
        public MyView(Context context) {
            super(context);

            LinearLayout contentsLayout = (LinearLayout) findViewById(R.id.contentsLayout);
            // Layout Inflater 객체 참조
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
            test = inflater.inflate(R.layout.rule_list, contentsLayout, true);

            TextView rule_title = (TextView) findViewById(R.id.rule_title);
            TextView rule_contents = (TextView) findViewById(R.id.rule_contents);
        }

        private View getView() {
            return test;
        }
    }
}
