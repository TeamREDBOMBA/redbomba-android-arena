package com.redbomba.arena.carddetail.feeds;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
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

import static com.redbomba.arena.db.DB.getJson;
import static com.redbomba.arena.db.DB.getJsonObj_as_String;

/**
 * Created by Sangmok on 2014. 10. 13..
 */
public class WriteFeedActivity extends Activity {

    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = (Settings) getApplicationContext();
        setContentView(R.layout.write_feed);

        new UserProfile().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_write_feed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = getIntent().getExtras();
        String lid = bundle.getString("lid");
        EditText feed = (EditText) findViewById(R.id.write_feed2);
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_write_feed:
                writeFeed(String.valueOf(settings.user_id), lid, "l", feed.getText().toString());
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String writeFeed(String uid, String uto, String utotype, String txt) {
        ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
        post.add(new BasicNameValuePair("uid", uid));
        post.add(new BasicNameValuePair("uto", uto));
        post.add(new BasicNameValuePair("utotype", utotype));
        post.add(new BasicNameValuePair("feedtype", "1"));
        post.add(new BasicNameValuePair("txt", txt));
        post.add(new BasicNameValuePair("tag", null));
        post.add(new BasicNameValuePair("img", null));
        post.add(new BasicNameValuePair("vid", null));
        post.add(new BasicNameValuePair("log", null));
        post.add(new BasicNameValuePair("hyp", null));

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost("http://redbomba.net/mobile/?mode=postLeagueFeed");

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

    public class UserProfile extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... params) {
            return getJson(Urls.USER_PROFILE + String.valueOf(settings.user_id));
        }

        @Override
        protected void onPostExecute(String result) {
            AQuery aq = new AQuery(WriteFeedActivity.this);
            ImageOptions options = new ImageOptions();
            options.round = 1000;
            aq.id(R.id.write_feed_user_icon).image(Urls.MIDEA +
                    getJsonObj_as_String(result, 0, "usericon"), options);
            aq.id(R.id.write_feed_user_name).text(getJsonObj_as_String(result, 0, "username"));
        }
    }
}
