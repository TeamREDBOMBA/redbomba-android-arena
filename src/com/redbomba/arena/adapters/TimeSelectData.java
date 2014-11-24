package com.redbomba.arena.adapters;

/**
 * Created by Sangmok on 2014. 9. 4..
 */
public class TimeSelectData {
    private String m_text1;
    private int m_text2;

    public TimeSelectData(String text1, int text2)
    {
        m_text1 = text1;
        m_text2 = text2;
    }

    public String getText1()
    {
        return m_text1;
    }

    public int getText2()
    {
        return m_text2;
    }

}