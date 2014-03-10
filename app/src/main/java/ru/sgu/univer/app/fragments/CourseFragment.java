package ru.sgu.univer.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.providers.DataBase;

public class CourseFragment extends ListFragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnCourseFragmentItemClickListener clickListener;
    private AbsListView mListView;
    private ArrayAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public CourseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        for (int i = 0; i < 5; i++) {
            DataBase.putCourse(getActivity(), "Курс " + Integer.toString(i + 1));
        }
        mAdapter = new ArrayAdapter<Course>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DataBase.getCourses(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course_list, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnCreateContextMenuListener(getActivity());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            clickListener = (OnCourseFragmentItemClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnCourseFragmentItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        clickListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (null != clickListener) {
//            clickListener.onCourseFragmentItemClick(CourseProvider.getByPosition(position).id);
            clickListener.onCourseFragmentItemClick("1");
        }
    }

    public void addCourse(String name) {
//        CourseProvider.add(name);
        mAdapter.notifyDataSetChanged();
    }

    public void removeCourse(String id) {
//        CourseProvider.removeById(id);
        mAdapter.notifyDataSetChanged();
    }

    public void renameCourse(String id, String newName) {
//        CourseProvider.renameById(id, newName);
        mAdapter.notifyDataSetChanged();
    }

    public interface OnCourseFragmentItemClickListener {
        public void onCourseFragmentItemClick(String id);
    }
}
