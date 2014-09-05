package com.redbomba.arena;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import org.json.JSONArray;

/**
 * Created by Sangmok on 2014. 9. 1..
 */
public class LoginActivity extends Activity {

    private EditText username = null;
    private EditText password = null;
    private Button login_btn;
    int counter = 3;
    AQuery aq = new AQuery(this);
    private SharedPreferences sharedpreferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.user_password);
        login_btn = (Button) findViewById(R.id.login_btn);

    }

//    public void login(View view) {
//        if (username.getText().toString().equals("") &&
//                password.getText().toString().equals("")) {
//            login();
//        } else {
//            Toast.makeText(getApplicationContext(), "Wrong Credentials",
//                    Toast.LENGTH_SHORT).show();
//            counter--;
//            if (counter == 0) {
//                login_btn.setEnabled(false);
//            }
//
//        }
//    }
    public void login(View view){
        String email = username.getText().toString();
        String pw = password.getText().toString();
        final Intent intent = new Intent(this, MainActivity.class);
        if (email.equals("") && pw.equals("")) {
            Toast.makeText(aq.getContext(), "아이디와 비밀번호를 입력해 주세요.", Toast.LENGTH_LONG).show();
        }
        else {
            String url = "http://redbomba.net/mobile/?mode=1&email="+ email +"&password=" + pw;
            aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {

                @Override
                public void callback(String url, JSONArray json, AjaxStatus status) {
                    try {
                        int jo = json.getJSONObject(0).getInt("uid");
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putInt("uid", jo);
                        editor.commit();
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } catch (Exception e) {
                        Toast.makeText(aq.getContext(), "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        sharedpreferences=getSharedPreferences("login",
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains("uid"))
        {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        super.onResume();
    }
}