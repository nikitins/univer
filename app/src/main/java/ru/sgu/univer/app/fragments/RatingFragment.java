package ru.sgu.univer.app.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.StudentProvider;

public class RatingFragment extends Fragment {
    public static final String GROUP_ID_RATING_PARAM = "group_id_rating_param";

    private int groupId;
    private TableLayout table;
    private OnFragmentInteractionListener mListener;

    public static RatingFragment newInstance(int groupId) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putInt(GROUP_ID_RATING_PARAM, 0);
        fragment.setArguments(args);
        return fragment;
    }
    public RatingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupId = getArguments().getInt(GROUP_ID_RATING_PARAM);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.rating_table_layout);

        List<TableRow> rows = new ArrayList<TableRow>();
        List<Student> students = StudentProvider.getStudentsByGroupId(groupId);
        for (Student student : students) {
            TableRow row = (TableRow) inflater.inflate(R.layout.table_row, null);

            TextView textView = (TextView) row.getChildAt(0);
            textView.setText(student.toString());
            for(int i = 0; i < 4; i++) {
                EditText editText = (EditText) inflater.inflate(R.layout.table_cell, null);
                editText.setText(String.valueOf(new Random().nextInt(7)));
                row.addView(editText);

            }
            rows.add(row);
        }

        for (TableRow row : rows) {
            tableLayout.addView(row);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.rating, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onTableFragmentInteraction(Uri uri);
    }

}
