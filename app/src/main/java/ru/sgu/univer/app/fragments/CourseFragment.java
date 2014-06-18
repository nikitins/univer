package ru.sgu.univer.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.activity.BrouseActivity;
import ru.sgu.univer.app.activity.GroupListActivity;
import ru.sgu.univer.app.activity.LoginActivity;
import ru.sgu.univer.app.activity.RatingActivity;
import ru.sgu.univer.app.activity.SyncGroupListActivity;
import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.OperationType;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.MegaProvider;
import ru.sgu.univer.app.providers.PerevodProvider;
import ru.sgu.univer.app.providers.RatingProvider;
import ru.sgu.univer.app.providers.StudentProvider;
import ru.sgu.univer.app.writers.ExcelParser;

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
    private int operation;

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
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAdapter = new ArrayAdapter<Course>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, CourseProvider.getCourses());

        operation = getActivity().getIntent().getIntExtra(BrouseActivity.OPERATION_EXTRA, -1);
        String path = getActivity().getIntent().getStringExtra(RatingActivity.PATH_RATING_PARAM);

        if (operation == OperationType.BACKUP.ordinal()) {
            backUp(path);
        }
        if (operation == OperationType.RESTORE.ordinal()) {
            restore(path);
        }
    }

    private void restore(String path) {
        MegaProvider.clearAll();
        ExcelParser.fromBackUp(path);
        mAdapter.notifyDataSetChanged();
    }

    private void backUp(final String path) {
        Log.d("LOG", "Start backup");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Имя файла");
        final EditText input = new EditText(getActivity());
//        input.setInputType(InputType.);
        builder.setView(input);
        builder.setPositiveButton("Ок",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Log.d("LOG", "start to dialog to back up");
                        if (!"".equals(input.getText().toString())) {
                            String name = input.getText().toString();
                            Log.d("LOG", "back up to " + name);
                            ExcelParser.toBackUp(path + "/" + name + ".backup");
                            Log.d("LOG", "back end");
                            showMessage("Backup сохранен в " + name + ".backup");
                            dialog.dismiss();
                        }
                    }
                }
        );
        builder.show();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_course: {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Введите имя");
                builder.setCancelable(false);
                final EditText input = new EditText(getActivity());
                builder.setView(input);
                builder.setPositiveButton("Добавить",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String name = input.getText().toString();
                                if("".equals(name)) {
                                    showMessage("Имя не может быть пустым.");
                                    dialog.dismiss();
                                    return;
                                }
                                if(CourseProvider.hasCourse(name)) {
                                    showMessage("Имя " + name + " уже зенято.");
                                    dialog.dismiss();
                                    return;
                                }
                                addCourse(name);
                            }

                        });
                builder.setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
                return true;
            }
            case R.id.action_login: {
                if(CourseProvider.logged){
                    Intent intent = new Intent(getActivity(), SyncGroupListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.action_clear: {
                MegaProvider.clearAll();
                mAdapter.notifyDataSetChanged();
                showMessage("База Данных очищена");
                return true;
            }
            case R.id.action_backup: {
                Intent intent = new Intent(getActivity(), BrouseActivity.class);
                intent.putExtra(BrouseActivity.FIND_FILE_EXTRA, false);
                intent.putExtra(BrouseActivity.FILE_END_EXTRA, "");
//                intent.putExtra(RatingActivity.GROUP_ID_RATING_PARAM, groupId);
//                intent.putExtra(GroupListActivity.COURSE_ID_EXTRA, courseId);
                intent.putExtra(BrouseActivity.OPERATION_EXTRA, OperationType.BACKUP.ordinal());
                startActivity(intent);
                return true;
            }
            case R.id.action_restore: {
                Intent intent = new Intent(getActivity(), BrouseActivity.class);
                intent.putExtra(BrouseActivity.FIND_FILE_EXTRA, true);
                intent.putExtra(BrouseActivity.FILE_END_EXTRA, ".backup");
                intent.putExtra(BrouseActivity.OPERATION_EXTRA, OperationType.RESTORE.ordinal());
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
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
            clickListener.onCourseFragmentItemClick(CourseProvider.getByPosition(position).id);
        }
    }

    public void addCourse(String name) {
        CourseProvider.add(name);
        mAdapter.notifyDataSetChanged();
    }

    public void removeCourse(int id) {
        CourseProvider.removeById(id);
        mAdapter.notifyDataSetChanged();
    }

    public void renameCourse(int id, String newName) {
        CourseProvider.renameById(id, newName);
        mAdapter.notifyDataSetChanged();
    }

    public interface OnCourseFragmentItemClickListener {
        public void onCourseFragmentItemClick(int id);
    }
}
