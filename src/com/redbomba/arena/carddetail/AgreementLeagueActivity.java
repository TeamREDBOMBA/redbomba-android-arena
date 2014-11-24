package com.redbomba.arena.carddetail;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.util.Constants;
import com.redbomba.arena.R;
import com.redbomba.arena.main.MainActivity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sangmok on 2014. 10. 20..
 */
public class AgreementLeagueActivity extends Activity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_league_step3);
        sharedPreferences = getSharedPreferences("system", 0);
        final AQuery aq = new AQuery(AgreementLeagueActivity.this);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Bundle bundle = getIntent().getExtras();
        final String lid = bundle.getString("lid");
        final String start = bundle.getString("start");
        final String end = bundle.getString("end");
        final String ft = bundle.getString("ft");


        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        Intent intent = new Intent(AgreementLeagueActivity.this, CardDetailActivity.class);
                        intent.putExtra("leagueId", lid);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        intent.setFlags(Intent.)
                        startActivity(intent);

                        AgreementLeagueActivity.this.finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        Intent intent2 = new Intent(AgreementLeagueActivity.this, MainActivity.class);
//                        settings.currentViewPage = 1;
//                        System.out.println("-=-=-="+settings.currentViewPage);
                        intent2.putExtra("MyArena", 1);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);

                        AgreementLeagueActivity.this.finish();
                        break;
                }
            }
        };

        Button goback = (Button) findViewById(R.id.step2_back);
        goback.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        aq.id(R.id.step2_back).background(R.drawable.ic_arrow_left_red);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        aq.id(R.id.step2_back).background(R.drawable.ic_arrow_left);
                        onBackPressed();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        CheckBox agreement = (CheckBox) findViewById(R.id.checkbox);
        agreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Button registration = (Button) findViewById(R.id.post_registration);
                    aq.id(R.id.post_registration).background(R.drawable.btn_green);
                    registration.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result","Done");
                            setResult(RESULT_OK,returnIntent);

                            AQuery aq = new AQuery(AgreementLeagueActivity.this);
                            setLeagueTeam(String.valueOf(sharedPreferences.getInt("uid", 0)), lid, "insert", ft);
                            System.out.println("-=-=" + String.valueOf(sharedPreferences.getInt("uid", 0)));
                            aq.id(R.id.apply_step2).visibility(Constants.GONE);
                            aq.id(R.id.apply_step3).visible();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AgreementLeagueActivity.this);
                            builder.setCancelable(false);
                            TextView msg = new TextView(AgreementLeagueActivity.this);
                            msg.setText("대회 참가 신청 완료!\n참가중인대회의 정보는\n\'마이아레나\'에서 살펴보실 수 있어요!");
                            msg.setPadding(10,10,10,10);
                            msg.setGravity(Gravity.CENTER);
                            msg.setTextSize(18);
                            builder.setView(msg).setPositiveButton("나중에", dialogClickListener)
                                    .setNegativeButton("마이아레나로 이동", dialogClickListener).show();
                        }
                    });
                }

                else {
                    Button registration = (Button) findViewById(R.id.post_registration);
                    aq.id(R.id.post_registration).background(R.drawable.unselected_btn_grey);
                    registration.setOnClickListener(null);
                }
            }
        });

    }

    public String setLeagueTeam(String uid, String lid, String action, String ftime) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("id", uid));
        post.add(new BasicNameValuePair("league_id", lid));
        post.add(new BasicNameValuePair("action", action));
        post.add(new BasicNameValuePair("feasible_time", ftime));
//        post.add(new BasicNameValuePair("round", round));
//        post.add(new BasicNameValuePair("is_complete", comp));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postLeagueTeam");
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, "UTF-8");
            httpPost.setEntity(entity);
            client.execute(httpPost);
            return EntityUtils.getContentCharSet(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String timeCalc(String start, String end) {
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
//        String result = "";
//
//        for (int i=0; i<=daydiff; i++) {
//            for (int j=hours; j<=hours2; j++) {
//                result = result + String.valueOf(24*i + j);
//                if (!(i == daydiff && j == hours2)) {
//                    result = result + ",";
//                }
//            }
//        }
//        return result;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AgreementLeagueActivity.this.overridePendingTransition(R.anim.stay, R.anim.stay);
    }
}
