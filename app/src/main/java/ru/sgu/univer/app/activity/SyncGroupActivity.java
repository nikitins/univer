package ru.sgu.univer.app.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.fragments.GroupFragment;
import ru.sgu.univer.app.fragments.RatingFragment;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class SyncGroupActivity extends ListActivity {
    public static final String SYNC_GROUP_ID_EXTRA = "sync_group_id_extra";
    public static int SYNC_COURSE_ID = 99999999;
    private int groupId;
    public ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(SYNC_GROUP_ID_EXTRA, 0);
        setContentView(R.layout.activity_sync_group);
        adapter = new ArrayAdapter<Student>(this, android.R.layout.simple_list_item_1, StudentProvider.getStudentsByGroupId(groupId));
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//        Intent intent = new Intent(this, EditStudentActivity.class);
//        intent.putExtra(EditStudentActivity.GROUP_ID_EDIT_STUDENT, adapter.getItem(position).getGroupId());
//        intent.putExtra(EditStudentActivity.STUDENT_ID_EDIT_STUDENT, adapter.getItem(position).getId());
//        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_group_context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_show_rating) {

            Intent intent = new Intent(this, SyncRatingActivity.class);
            intent.putExtra(SyncRatingActivity.GROUP_ID_EXTRA, groupId);
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