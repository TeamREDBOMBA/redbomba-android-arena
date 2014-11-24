package com.redbomba.arena.carddetail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.redbomba.arena.R;

/**
 * Created by Sangmok on 2014. 11. 4..
 */
public class TimeCompleteFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle b) {
        View rootView = lf.inflate(R.layout.fragment_step2_complete, vg, false);
        return rootView;
    }
}
