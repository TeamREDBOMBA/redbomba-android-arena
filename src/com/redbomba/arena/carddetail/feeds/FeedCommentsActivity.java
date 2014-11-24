package com.redbomba.arena.carddetail.feeds;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CommentItemData;
import com.redbomba.arena.adapters.inflaters.CommentInflater;
import com.redbomba.arena.urls.Urls;
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

import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 10. 13..
 */
public class FeedCommentsActivity extends Activity {

    String comments;


    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = (Settings) getApplicationContext();
        setContentView(R.layout.feed_comments);
        LinearLayout contentsLayout = (LinearLayout) findViewById(R.id.feed_in_comment);
        Bundle bundle = getIntent().getExtras();
        final String feed = bundle.getString("feed");
        final String fid = bundle.getString("fid");
        contentsLayout.addView(getFeed(feed));

        settings = (Settings) getApplicationContext();
        AQuery aq = new AQuery(FeedCommentsActivity.this);
        ImageOptions options = new ImageOptions();
        options.round = 1000;
        aq.id(R.id.feed_user_icon).image(Urls.MIDEA + getValue_fromObject(feed, "usericon"), options);
        aq.id(R.id.feed_user_name).text(getValue_fromObject(feed, "username"));
        aq.id(R.id.feed_group_name).text(" @ "+getValue_fromObject(feed, "groupname"));
        aq.id(R.id.feed_content).text(getValue_fromObject(feed, "con"));
        aq.id(R.id.feed_update).text(FeedActivity.timeCalc(getValue_fromObject(feed, "update")));
        aq.id(R.id.feed_replynum).text(getValue_fromObject(feed, "replynum") + "개의 댓글");
        aq.id(R.id.feed_like_len).text("좋아요 "+getValue_fromObject(feed, "smile_len")+"개");

        ImageView like_thumb = (ImageView) findViewById(R.id.feed_like_thumb);
        like_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeedSmile(String.valueOf(settings.user_id), getValue_fromObject(feed, "id"));
                onResume();
            }
        });

        new Comments().execute();

        ImageButton confirm = (ImageButton) findViewById(R.id.comment_confirm);
        final EditText comment = (EditText) findViewById(R.id.comment_con);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeComment(String.valueOf(settings.user_id), fid, comment.getText().toString());
                onResume();
            }
        });
    }

    private View getFeed(String feed){
        View v = new View(this);
        // Layout Inflater 객체 참조
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // button.xml에 정의된 레이아웃에 대한 인플레이션 수행
        v = (View) inflater.inflate(R.layout.list_item_feed, null);

//        ImageView mImageView = (ImageView) v.findViewById(R.id.feed_user_icon);
//        TextView mTextView = (TextView) v.findViewById(R.id.feed_user_name);
//        TextView mTextView2 = (TextView) v.findViewById(R.id.feed_group_name);
//        TextView mTextView3 = (TextView) v.findViewById(R.id.feed_content);
//        TextView mTextView4 = (TextView) v.findViewById(R.id.feed_update);
//
//
//
//
//        mTextView.setText(getValue_fromObject(feed, "username"));
        return v;
    }

    private class Comments extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            leagueTeam = DB.getJson("http://redbomba.net/mobile/?mode=getArenaTicket&id="+uid);
        }

        @Override
        protected String doInBackground(Void... params) {
//            int uid = MainActivity.sharedpreferences.getInt("uid", 0);
            Bundle bundle = getIntent().getExtras();
            String fid = bundle.getString("fid");


            comments = getJson("http://redbomba.net/mobile/?mode=getFeedComments&fid="+fid);
            return comments;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            BaseInflaterAdapter<CommentItemData> adapter = new BaseInflaterAdapter<CommentItemData>(new CommentInflater());
            int lenOfJa = getJsonLength(result);
            for (int i = 0; i < lenOfJa; i++) {
                //add items on a card
                CommentItemData data = new CommentItemData(
                        Urls.MIDEA + getJsonObj_as_String(comments, i, "user_icon"),
                        getJsonObj_as_String(comments, i, "user_name"),
                        FeedActivity.timeCalc(getJsonObj_as_String(comments, i, "update")),
                        getJsonObj_as_String(comments, i, "con")
                );
                adapter.addItem(data, false);
            }
            ListView m_list = (ListView) findViewById(R.id.feed_comment_list);
            m_list.setAdapter(adapter);
//            m_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });
//            m_list.setItemsCanFocus(true);
//            m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }
    @Override
    protected void onResume() {

        super.onResume();
        this.onCreate(null);
    }

    public String writeComment(String uid, String fid, String txt) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("fid", fid));
        post.add(new BasicNameValuePair("txt", txt));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postFeedReply");

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

    public String postFeedSmile(String uid, String fid) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("fid", fid));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postFeedSmile");
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
}
