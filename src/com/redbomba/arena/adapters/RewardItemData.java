package com.redbomba.arena.adapters;

/**
 * Created by Sangmok on 2014. 9. 4..
 */
public class RewardItemData {
    private String m_text1;
    private String m_text2;

    public RewardItemData(String text1, String text2)
    {
        m_text1 = text1;
        m_text2 = text2;
    }

    public String getText1()
    {
        return m_text1;
    }

    public String getText2()
    {
        return m_text2;
    }

}