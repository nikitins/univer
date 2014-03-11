package ru.sgu.univer.app.activity;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Stack;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.providers.GroupProvider;

public class GroupListActivity extends ListActivity {
    public static final String COURSE_ID_EXTRA = "course_id_extra";
    private ArrayAdapter<Group> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int groupId = getIntent().getIntExtra(COURSE_ID_EXTRA, 0);
        setContentView(R.layout.activity_group_list);
        adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, GroupProvider.getGroupsByCourseId(groupId));
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_list, menu);
        return true;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        parent = findViewById(R.layout.activity_group_list);
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this, GroupActivity.class);
        intent.putExtra(GroupActivity.GROUP_ID_EXTRA, adapter.getItem(position).getId());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
