package com.redbomba.arena.login;
import com.redbomba.arena.Functions;
import com.redbomba.arena.main.MainActivity;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

public class LoadingActivity extends Activity {

    private SharedPreferences prefs_system;

    Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);

        settings = (Settings) getApplicationContext();

        prefs_system = getSharedPreferences("system", 0);

        new LoginTask().execute(null, null, null);
    }

    class LoginTask extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... Void) {
            if(prefs_system.getInt("uid", 0) != 0){
                settings.user_id = prefs_system.getInt("uid", 0);
                try {
                    settings.user_info = Functions.GET("mode=getUserInfo&uid=" + settings.user_id).getJSONObject(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    return 2;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 1;
            }else{
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return 2;
            }
        }

        protected void onPostExecute(Integer result) {
            if(result.intValue() == 1){
                startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                LoadingActivity.this.finish();
            }else if(result.intValue() == 2){
                startActivity(new Intent(LoadingActivity.this, LandingActivity.class));
                LoadingActivity.this.finish();
            }
            return;
        }
    }

}