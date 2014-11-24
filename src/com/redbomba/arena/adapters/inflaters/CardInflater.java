package com.redbomba.arena.adapters.inflaters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.androidquery.AQuery;
import com.redbomba.arena.R;
import com.redbomba.arena.adapters.BaseInflaterAdapter;
import com.redbomba.arena.adapters.CardItemData;
import com.redbomba.arena.adapters.IAdapterViewInflater;

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
            AQuery aq = new AQuery(m_rootView);
            aq.id(R.id.league_name).text(item.getText1());
            aq.id(R.id.player_level).text(item.getText2());
            d_dayColor(item.getText3());
            aq.id(R.id.bitmap).image(item.getBitmap());
            m_bitmap.setScaleType(ImageView.ScaleType.CENTER_CROP);
            m_join.setProgress(item.getJoin());
            m_join.getProgressDrawable().setColorFilter(Color.rgb(0x34,0x98,0xdb), PorterDuff.Mode.SRC_IN);
            aq.id(R.id.joined_user).text(item.getJoinedUser());
            aq.id(R.id.joined_user_total).text(item.getJoinedUserTotal());
//            m_joined_user.setText(item.getJoinedUser());
		}

        private void d_dayColor(String[] calcDay) {
            AQuery aq = new AQuery(m_rootView);
            if (calcDay[0].equals("OK")) {
                aq.id(R.id.d_day).background(R.drawable.d_day_shape);
                m_text3.setText(calcDay[1]);
            }
            else {
                aq.id(R.id.d_day).background(R.drawable.d_day_shape2);
                m_text3.setText(calcDay[1]);
            }
        }
	}
//
//
//    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            bmImage.setImageBitmap(result);
//        }
//    }


}
