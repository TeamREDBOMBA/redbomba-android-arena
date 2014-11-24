package com.redbomba.arena.adapters;

/**
 * Created by Sangmok on 2014. 10. 8..
 */
public class FeedItemData {

    private String m_text1;
    private String m_text2;
    private String m_text3;
    private String m_text4;
    private String m_text5;
    private String m_text6;
    private String m_text7;

    public FeedItemData(String text1, String text2, String text3, String text4, String text5, String text6, String text7)
    {
        m_text1 = text1;
        m_text2 = text2;
        m_text3 = text3;
        m_text4 = text4;
        m_text5 = text5;
        m_text6 = text6;
        m_text7 = text7;
    }

    public String getText1()
    {
        return m_text1;
    }

    public String getText2()
    {
        return m_text2;
    }

    public String getText3()
    {
        return m_text3;
    }

    public String getText4() { return m_text4; }

    public String getText5() { return m_text5; }

    public String getText6() { return m_text6; }

    public String getText7() { return m_text7; }

}
