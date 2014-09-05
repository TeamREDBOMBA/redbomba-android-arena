package com.redbomba.arena.db;

import android.os.StrictMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DB {

    public static int user_id = 0;

    public static String get_league_info(int index, String column) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        String json = null;

        try {
            json = Jsoup.connect("http://redbomba.net/mobile/?mode=getLeagueInfo").ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray ja = new JSONArray(json);
            return ja.getJSONObject(index).getString(column);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int get_length_of_jsonArray() {

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        String json = null;

        try {
            json = Jsoup.connect("http://redbomba.net/mobile/?mode=getLeagueInfo").ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray ja = new JSONArray(json);
            return ja.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String d_day(int index) {
        String result = null;
        String start_apply = get_league_info(index, "start_apply");
        Date currentTime = new Date();

        //change format
        char[] ca = start_apply.toCharArray();
        StringBuilder sb = new StringBuilder();
        sb.append(ca);
        sb.deleteCharAt(22);
        start_apply = sb.toString();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        Date convertedDate = null;
        try {
            convertedDate = df.parse(start_apply);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long dday = convertedDate.getTime() - currentTime.getTime();
        long aday = (60*60*24*1000);
        dday = -(dday / aday);

        if (dday > 0) {
            result = "D+" + Long.toString(dday);
        }
        else {
            result = "D" + Long.toString(dday);
        }
        return result;
    }

    public static String get_league_round(String var){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        String json = null;

        try {
            json = Jsoup.connect("http://redbomba.net/mobile/?mode=getLeagueRound&id="+var).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray ja = new JSONArray(json);
            return ja.getJSONObject(0).getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int get_league_team(String var) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        String json = null;

        try {
            json = Jsoup.connect("http://redbomba.net/mobile/?mode=getLeagueTeam&id="+var).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray ja = new JSONArray(json);
            return ja.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getJson(String url) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        String json;

        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
            return json;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonObj_as_String(String jsonArray, int i, String name) {
        try {
            JSONArray ja = new JSONArray(jsonArray);
            return ja.getJSONObject(i).getString(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name+" is not an object of the JSONArray";
    }
}
