package com.redbomba.arena;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sangmok on 2014. 10. 27..
 */
public class Functions {

    private static Typeface mTypeface = null;

    public static Typeface setFont(Context con){
        if(mTypeface == null){
            if (Integer.parseInt(Build.VERSION.SDK) > Build.VERSION_CODES.FROYO) {
                mTypeface = Typeface.createFromAsset(con.getAssets(), "font.ttf");
            }else{
                mTypeface = Typeface.SANS_SERIF;
            }
        }

        return mTypeface;
    }

    public static JSONArray GET(String var){
        String jsonStr = null;
        jsonStr = downloadHtml(var);
        jsonStr = jsonStr.trim();
        try{
            JSONArray ja = new JSONArray(jsonStr);
            return ja;
        }catch(Exception e){
            Log.i("JSON ERROR", "" + e.getMessage());
        }
        return null;
    }

    public static String downloadHtml(String addr){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StringBuilder html = new StringBuilder();
        addr = "http://redbomba.net/mobile/?"+addr;
        try{
            URL url = new URL(addr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if(conn!=null){
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
                    while(true){
                        String line = br.readLine();
                        if(line==null) break;
                        html.append(line+"\n");
                    }
                    br.close();
                }
                conn.disconnect();
            }
        }catch(Exception ex){}
        return html.toString();
    }

    public static String stripHTML(String htmlStr) {
        Pattern p = Pattern.compile("<(?:.|\\s)*?>");
        Matcher m = p.matcher(htmlStr);
        return m.replaceAll("");
    }

//	public static void setBadge(Context context, int count) {
//	    String launcherClassName = getLauncherClassName(context);
//	    if (launcherClassName == null) {
//	        return;
//	    }
//	    Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
//	    intent.putExtra("badge_count", count);
//	    intent.putExtra("badge_count_package_name", context.getPackageName());
//	    intent.putExtra("badge_count_class_name", launcherClassName);
//	    context.sendBroadcast(intent);
//	}

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }
}
