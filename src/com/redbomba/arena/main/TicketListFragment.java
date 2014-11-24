package com.redbomba.arena.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.IAdapterViewInflater;
import com.redbomba.arena.adapters.TicketItemData;
import com.redbomba.arena.carddetail.CardDetailActivity;
import com.redbomba.arena.urls.Urls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 9. 3..
 */
public class TicketListFragment extends Fragment {


    private String leagueTeam;
    private ListView ticket_list;
//    private TextView check_ticket_exist;
//    private ImageView check_ticket_exist_image;
    LinearLayout checker;

    Settings settings;

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_myarena, vg, false);
        settings = (Settings) getActivity().getApplicationContext();

        ticket_list = (ListView) rootView.findViewById(R.id.ticket_list);
//        check_ticket_exist = (TextView) rootView.findViewById(R.id.check_ticket_exist);
//        check_ticket_exist_image = (ImageView) rootView.findViewById(R.id.check_ticket_exist_image);

        checker = (LinearLayout) rootView.findViewById(R.id.check_ticket_exist);
        new Tickets().execute();




//        AQuery aq = new AQuery(getActivity());
//        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
//
//            @Override
//            public void callback(String url, JSONArray json, AjaxStatus status) {
//                try {
//                    jo = json.getJSONObject(0).toString();
//                    System.out.println(jo);
//                } catch (Exception e) {
//                }
//            }
//        });


        return rootView;
    }

    public class TicketInflater implements IAdapterViewInflater<TicketItemData> {

        @Override
        public View inflate(final BaseInflaterAdapter<TicketItemData> adapter, final int pos, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.list_item_ticket, parent, false);
                holder = new ViewHolder(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final TicketItemData item = adapter.getTItem(pos);
            holder.updateDisplay(item);

            return convertView;
        }

        private class ViewHolder {
            private View m_rootView;
            private TextView ticket_leage_name;
            private TextView ticket_team_name;
//            private TextView ticket_round;
            private ImageView ticket_user_icon;

            public ViewHolder(View rootView) {
                m_rootView = rootView;
                ticket_leage_name = (TextView) m_rootView.findViewById(R.id.ticket_league_name);
                ticket_team_name = (TextView) m_rootView.findViewById(R.id.ticket_team_name);
//                ticket_round = (TextView) m_rootView.findViewById(R.id.join_complete);
                ticket_user_icon = (ImageView) m_rootView.findViewById(R.id.ticket_user_icon);

            }

            public void updateDisplay(TicketItemData item) {
                AQuery aq = new AQuery(m_rootView);
                ticket_leage_name.setText(item.getText1());
                ticket_team_name.setText(item.getText2());
//                ticket_user_icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageOptions options = new ImageOptions();
                options.round = 200;
                aq.id(ticket_user_icon).image(item.getText3(), options);
                aq.id(R.id.opgroup_name).text(item.getText4());
                aq.id(R.id.opgroup_icon).image(item.getText5(), options);
                aq.id(R.id.match_date).text(item.getText6());
                aq.id(R.id.next_match).text(item.getText7());
                aq.id(R.id.ticket_state).text(item.getText8());
//                ticket_user_icon.setBackgroundResource(R.drawable.ticket_layout);
//                ticket_user_icon.i(item.getText3());
//                aq.id(ticket_user_icon).image(item.getText3(), true, true, 0, 0,
//                    new BitmapAjaxCallback() {
//
//                        @Override
//                        public void callback(String url, ImageView iv,
//                                             Bitmap bm,
//                                             com.androidquery.callback.AjaxStatus status) {
////                                iv.setImageBitmap(bm);
//
//                            CircleDrawable circle = new CircleDrawable(bm, true);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                iv.setBackground(circle);
//                            }
//                            else {
//                                iv.setBackgroundDrawable(circle);
//                            }
//                        }
//
//                    });
            }
        }
    }

    private class Tickets extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            String url = "http://redbomba.net/mobile/?mode=getArenaTicket&id="+settings.user_id;
            leagueTeam = getJson(url);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AQuery aq = new AQuery(getActivity());

            ticket_list.addHeaderView(new View(getActivity()));
            ticket_list.addFooterView(new View(getActivity()));

            BaseInflaterAdapter<TicketItemData> adapter = new BaseInflaterAdapter<TicketItemData>(new TicketInflater());
            int lenOfJa = getJsonLength(leagueTeam);
            if (lenOfJa == 0) {
//                aq.id(R.id.check_ticket_exist_image).visible();
                checker.setVisibility(View.VISIBLE);
//                aq.id(R.id.check_ticket_exist).visible();
//                check_ticket_exist.setText("There is no ticket for you");
            }
            else {
                for (int i = 0; i < lenOfJa; i++) {
                    //add items on a card
                    TicketItemData data = new TicketItemData(
                            getJsonObj_as_String(leagueTeam, i, "league_name"),
                            "@ "+ getJsonObj_as_String(leagueTeam, i, "group"),
                            Urls.GROUP_ICON + getJsonObj_as_String(leagueTeam, i, "groupicon"),
//                            "http://redbomba.net/media/poster/poster_redbomba.png",
                            getJsonObj_as_String(leagueTeam, i, "opgroup"),
                            Urls.GROUP_ICON + getJsonObj_as_String(leagueTeam, i, "opgroupicon"),
                            convertDateTime(getJsonObj_as_String(leagueTeam, i, "next_match")),
                            getJsonObj_as_String(leagueTeam, i, "round") + "R",
                            getState(Integer.parseInt(getJsonObj_as_String(leagueTeam, i, "state")))
                    );
                    adapter.addItem(data, false);
                }
                ticket_list.setAdapter(adapter);
                ticket_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), CardDetailActivity.class);
                        intent.putExtra("leagueId", getJsonObj_as_String(leagueTeam, (int) id, "league_id"));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public String convertDateTime(String date) {
        if (date.equals("0")) {
            return "일정 등록 전";
        }
        else {
            char[] ca = date.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(ca);
            sb.deleteCharAt(22);
            date = sb.toString();

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
            Date convertedDate = null;
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
            try {
                convertedDate = df.parse(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return format.format(convertedDate);
        }
    }

    public String getState(int state) {
        switch(state) {
            case 1:
                return "신청 완료";
            case 2:
                return "경기 대기";
            case 3:
                return "입장 가능";
            case 4:
                return "라운드 탈락";
            case 5:
                return "우승/준우승";
            default:
                return "알 수 없음";
        }
    }

//    public class CircleDrawable extends Drawable {
//
//        private final BitmapShader mBitmapShader;
//        private final Paint mPaint;
//        private Paint mWhitePaint;
//        int circleCenterX;
//        int circleCenterY;
//        int mRadus;
//        private boolean mUseStroke = false;
//        private int mStrokePadding = 0;
//
//        public CircleDrawable(Bitmap bitmap) {
//
//            mBitmapShader = new BitmapShader(bitmap,
//                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//            mPaint = new Paint();
//            mPaint.setAntiAlias(true);
//            mPaint.setShader(mBitmapShader);
//
//        }

//        public CircleDrawable(Bitmap bitmap, boolean mUseStroke) {
//            this(bitmap);
//
//            if (mUseStroke) {
//                this.mUseStroke = true;
//                mStrokePadding = 4;
//                mWhitePaint = new Paint();
//                mWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
//                mWhitePaint.setStrokeWidth(0.75f);
//                mWhitePaint.setColor(Color.WHITE);
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//                Rect rect = new Rect(0, 0, width, height);
////                bitmap.getWidth();
////                bitmap.getHeight();
//                onBoundsChange(rect);
//            }
//        }
//
//        @Override
//        protected void onBoundsChange(Rect bounds) {
//            super.onBoundsChange(bounds);
//            circleCenterX = bounds.width() / 2;
//            circleCenterY = bounds.height() / 2;
//
//            if (bounds.width() >= bounds.height())
//                mRadus = bounds.width() / 2;
//            else
//                mRadus = bounds.height() / 2;
//        }
//
//        @Override
//        public void draw(Canvas canvas) {
//            if (mUseStroke) {
//                canvas.drawCircle(circleCenterX, circleCenterY, mRadus, mWhitePaint);
//            }
//            canvas.drawCircle(circleCenterX, circleCenterY, mRadus - mStrokePadding, mPaint);
//        }
//
//        @Override
//        public int getOpacity() {
//            return PixelFormat.TRANSLUCENT;
//        }
//
//        @Override
//        public void setAlpha(int alpha) {
//            mPaint.setAlpha(alpha);
//        }
//
//        @Override
//        public void setColorFilter(ColorFilter cf) {
//            mPaint.setColorFilter(cf);
//        }
//
//        public boolean ismUseStroke() {
//            return mUseStroke;
//        }
//
//        public void setmUseStroke(boolean mUseStroke) {
//            this.mUseStroke = mUseStroke;
//        }
//
//    }
}
