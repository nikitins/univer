package ru.sgu.univer.app.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.sgu.univer.app.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }


}
