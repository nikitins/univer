package ru.sgu.univer.app.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class StudentActivity extends ListActivity {
    public static final String GROUP_ID_PARAM = "group_id__student_param";
    private int groupId;
    private int courseId;
    public ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(GROUP_ID_PARAM, 0);
        courseId = getIntent().getIntExtra(GroupListActivity.COURSE_ID_EXTRA, 0);
        setContentView(R.layout.activity_student);
        adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, StudentProvider.getStudentsByGroupId(groupId));
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, EditStudentActivity.class);
        intent.putExtra(EditStudentActivity.GROUP_ID_EDIT_STUDENT, adapter.getItem(position).getGroupId());
        intent.putExtra(EditStudentActivity.STUDENT_ID_EDIT_STUDENT, adapter.getItem(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_student) {
            Intent intent = new Intent(this, CreateStudentActivity.class);
            intent.putExtra(CreateStudentActivity.GROUP_ID_CREATE_STUDENT, groupId);
            startActivity(intent);
            return true;
        }
        if(id == R.id.action_show_rating) {
            Intent intent = new Intent(this, RatingActivity.class);
            intent.putExtra(RatingActivity.GROUP_ID_RATING_PARAM, groupId);
            intent.putExtra(GroupListActivity.COURSE_ID_EXTRA, courseId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        parent = findViewById(R.layout.activity_student);
        return super.onCreateView(parent, name, context, attrs);
    }
}
