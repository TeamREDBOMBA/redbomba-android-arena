package com.redbomba.arena.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.redbomba.arena.R;
import com.redbomba.arena.Settings;
import com.redbomba.arena.TnCActivity;
import com.redbomba.arena.login.LandingActivity;
import com.redbomba.arena.urls.Urls;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.VISIBLE;
import static com.redbomba.arena.db.DB.*;

/**
 * Created by Sangmok on 2014. 10. 7..
 */
public class EtcFragment extends DialogFragment {

    private SharedPreferences prefs_system;
    private SharedPreferences.Editor editor_system;
    private String user_profile;
    private String user_games;
    Settings settings;

    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_etc, vg, false);
        settings = (Settings) getActivity().getApplicationContext();

        Button logout = (Button) rootView.findViewById(R.id.logout);
        TextView tnc = (TextView) rootView.findViewById(R.id.terms_and_conditions);
        tnc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TnCActivity.class);
                startActivity(intent);
            }
        });
        new UserProfile().execute();

        Button exeRedbomba = (Button) rootView.findViewById(R.id.etc_execute_redbomba);
        exeRedbomba.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
//                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                    intent.setComponent(new ComponentName("com.Redbomba.Landing","com.Redbomba.Landing.LoadingActivity"));
//                    startActivity(intent);
                    Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.Redbomba");

                    startActivity(LaunchIntent);
                }
                catch (Exception e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("com.android.vending");
                    intent.setData(Uri.parse("market://details?id=com.Redbomba"));
                    startActivity(intent);
                }


//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("market://search?q=foo"));
//                PackageManager pm = getActivity().getPackageManager();
//                List<ResolveInfo> list = pm.queryIntentActivities(intent, 0);

//                boolean androidMarketExists = false;
//                try{
//                    ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.android.vending", 0 );
//                    if(info.packageName.equals("com.android.vending"))
//                        androidMarketExists = true;
//                    else
//                        androidMarketExists = false;
//                } catch(PackageManager.NameNotFoundException e ){
//                    //application doesn't exist
//                    androidMarketExists = false;
//                }
//                if(!androidMarketExists){
////                    Log.d(LOG_TAG, "No Android Market");
//                    getActivity().finish();
//                }
//                else{
////                    Log.d(LOG_TAG, "Android Market Installed");
//                }
            }
        });




                prefs_system = this.getActivity().getSharedPreferences("system", 0);
        editor_system = prefs_system.edit();

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        editor_system.putInt("uid", 0);
                        editor_system.commit();
                        settings.user_id = 0;
                        Intent intent = new Intent(getActivity(), LandingActivity.class);
                        getActivity().finish();
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("로그아웃 하시겠습니까?").setPositiveButton("예", dialogClickListener)
                        .setNegativeButton("아니오", dialogClickListener).show();
            }
        });

        return rootView;
    }

    static EtcFragment newInstance(int num) {
        EtcFragment f = new EtcFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    public class UserProfile extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
//            AQuery aq = new AQuery(getActivity());
            user_profile = getJson(Urls.USER_PROFILE + String.valueOf(settings.user_id));
            user_games = getJson(Urls.LINKED_GAMES + String.valueOf(settings.user_id));
//            aq.id(R.id.etc_profile_picture).image(USER_ICON_URL + getJsonObj_as_String(user_profile, 0, "usericon"));
//            aq.id(R.id.etc_username).text(getJsonObj_as_String(user_profile, 0, "username"));
//            aq.id(R.id.etc_groupname).text(getJsonObj_as_String(user_profile, 0, "groupname"));
            String[] result = {user_profile, user_games};
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            AQuery aq = new AQuery(getActivity());
            ImageOptions options = new ImageOptions();
            options.round = 1000;
            aq.id(R.id.etc_profile_picture).image(Urls.MIDEA + getJsonObj_as_String(result[0], 0, "usericon"), options);
            aq.id(R.id.etc_username).text(getJsonObj_as_String(result[0], 0, "username"));
            aq.id(R.id.etc_user_email).text(getJsonObj_as_String(result[0], 0, "email"));

            //ListView 형태로 수정 요함
//            if (isItJsonArray(result[1])) {
//                int gametype = Integer.parseInt(getJsonObj_as_String(result[1], 0, "gameid"));
//                switch (gametype) {
//                    case 1:
//                        aq.id(R.id.etc_game_bg).image("http://redbomba.net/static/img/main_link_game1_bg.png");
//                        aq.id(R.id.etc_game_icon).image("http://redbomba.net/static/img/game_leagueoflegends.jpg", options);
//                }
//                aq.id(R.id.etc_game_name).text(getJsonObj_as_String(result[1], 0, "userid"));
//            }

            int lenOfJa = getJsonLength(result[1]);
            if(lenOfJa != 0) {
                int gametype = Integer.parseInt(getJsonObj_as_String(result[1], 0, "gameid"));
//                switch (gametype) {
//                    case 1:
                        aq.id(R.id.etc_game_bg).image("http://redbomba.net/static/img/main_link_game1_bg.png");
                        aq.id(R.id.etc_game_icon).image("http://redbomba.net/static/img/game_leagueoflegends.jpg", options);
//                    default:
//
//                }
                aq.id(R.id.etc_game_name).text(getJsonObj_as_String(result[1], 0, "userid"));
            }
            else {
                aq.id(R.id.etc_no_account).visibility(VISIBLE);
            }

            if (getJsonObj_as_String(result[0], 0, "groupicon").equals("")) {
//                aq.id(R.id.etc_group_container).invisible();
//                aq.id(R.id.etc_no_group).visible();
            }
            else {
                aq.id(R.id.etc_group_container).visible();
                aq.id(R.id.etc_no_group).visibility(GONE);
                aq.id(R.id.etc_group_icon).image(Urls.GROUP_ICON + getJsonObj_as_String(result[0], 0, "groupicon"), options);
                aq.id(R.id.etc_group_name).text(getJsonObj_as_String(result[0], 0, "groupname"));
                aq.id(R.id.etc_group_ini).text(getJsonObj_as_String(result[0], 0, "groupini"));
                aq.id(R.id.etc_group_game).text(getJsonObj_as_String(result[0], 0, "gameid"));
            }

//            String etc_group_game = "테스트";
//            String group_game = getJsonObj_as_String(result[0], 0, "gameid");
//            if (group_game.equals("1")) {
//                etc_group_game = "League of legends";
//            }
//            else {
//                aq.id(R.id.etc_group_game).text(etc_group_game);
//            }
            aq.id(R.id.etc_group_mem).text(getJsonObj_as_String(result[0], 0, "numofmem"));

        }
    }
//
//    public boolean isItJsonArray(String jsonArray) {
//        try {
//            JSONArray ja = new JSONArray(jsonArray);
//            return true;
//        } catch (JSONException e) {
//            return false;
//        }
//    }
}
