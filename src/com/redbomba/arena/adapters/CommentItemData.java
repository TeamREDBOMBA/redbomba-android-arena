package com.redbomba.arena.adapters;

/**
 * Created by Sangmok on 2014. 10. 8..
 */
public class CommentItemData {

    private String m_text1;
    private String m_text2;
    private String m_text3;
    private String m_text4;

    public CommentItemData(String text1, String text2, String text3, String text4)
    {
        m_text1 = text1;
        m_text2 = text2;
        m_text3 = text3;
        m_text4 = text4;
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
}
