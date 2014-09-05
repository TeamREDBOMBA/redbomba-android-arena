package com.redbomba.arena.adapters;

/**
 * Created by Justin on 2/2/14.
 */
public class CardItemData
{
	private String m_text1;
	private String m_text2;
	private String m_text3;
    private String m_bitmap;
    private int m_join;
    private String m_joined_user;

	public CardItemData(String text1, String text2, String text3, String bitmap, int join, String joined_user)
	{
		m_text1 = text1;
		m_text2 = text2;
		m_text3 = text3;
        m_bitmap = bitmap;
        m_join = join;
        m_joined_user = joined_user;
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

    public String getBitmap() { return m_bitmap; }

    public int getJoin() { return m_join; }

    public String getJoinedUser() { return m_joined_user; }
}
