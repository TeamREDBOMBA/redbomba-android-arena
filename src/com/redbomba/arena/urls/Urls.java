package com.redbomba.arena.urls;

/**
 * Created by Sangmok on 2014. 10. 11..
 */
public class Urls {
    public static String REDBOMBA = "http://redbomba.net";
    public static String MIDEA = "http://redbomba.net/media/";
    public static String GROUP_ICON = "http://redbomba.net/media/group_icon/";
    public static String SIMPLE_LEAGUE_INFO = "http://redbomba.net/mobile/?mode=getSimpleLeagueInfo";
    public static String REWARDS = "http://redbomba.net/mobile/?mode=getReward&lid=";
    public static String USER_PROFILE = "http://redbomba.net/mobile/?mode=getUserProfile&id=";
    public static String LINKED_GAMES = "http://redbomba.net/mobile/?mode=getLinkedGames&id=";
    public static String LEAGUE_TEAM = "http://redbomba.net/mobile/?mode=getLeagueTeam&id=";

    public static String DETAILED_LEAGUE_INFO(String uid, String lid) {
        return "http://redbomba.net/mobile/?mode=getDetailLeagueInfo&uid=" + uid + "&lid=" + lid;
    }
    public String LEAGUE_CONDITION(String uid, String lid){
        return "http://redbomba.net/mobile/?mode=getCondition&uid=" + uid + "&lid=" + lid;
    }
}
