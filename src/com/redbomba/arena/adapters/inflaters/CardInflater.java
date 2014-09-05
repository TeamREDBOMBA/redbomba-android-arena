package com.redbomba.arena.adapters.inflaters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CardItemData;
import com.redbomba.arena.adapters.IAdapterViewInflater;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Justin
 * Date: 10/6/13
 * Time: 12:47 AM
 */
public class CardInflater implements IAdapterViewInflater<CardItemData>
{

    @Override
	public View inflate(final BaseInflaterAdapter<CardItemData> adapter, final int pos, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.list_item_card, parent, false);
			holder = new ViewHolder(convertView);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		final CardItemData item = adapter.getTItem(pos);
		holder.updateDisplay(item);

		return convertView;
	}

	private class ViewHolder
	{
		private View m_rootView;
		private TextView m_text1;
		private TextView m_text2;
		private TextView m_text3;
        private ImageView m_bitmap;
        private ProgressBar m_join;
        private TextView m_joined_user;

		public ViewHolder(View rootView)
		{
			m_rootView = rootView;
			m_text1 = (TextView) m_rootView.findViewById(R.id.league_name);
			m_text2 = (TextView) m_rootView.findViewById(R.id.player_level);
			m_text3 = (TextView) m_rootView.findViewById(R.id.d_day);
            m_bitmap = (ImageView) m_rootView.findViewById(R.id.bitmap);
            m_join = (ProgressBar) m_rootView.findViewById(R.id.how_many_join);
            m_joined_user = (TextView) m_rootView.findViewById(R.id.joined_user);
			rootView.setTag(this);
		}

		public void updateDisplay(CardItemData item)
		{
			m_text1.setText(item.getText1());
			m_text2.setText(item.getText2());
			m_text3.setText(item.getText3());
            new DownloadImageTask(m_bitmap)
                    .execute(item.getBitmap());
            m_bitmap.setScaleType(ImageView.ScaleType.CENTER_CROP);
            m_join.setProgress(item.getJoin());
            m_joined_user.setText(item.getJoinedUser());
		}
	}


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
