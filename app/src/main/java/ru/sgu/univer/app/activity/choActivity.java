package ru.sgu.univer.app.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.GroupProvider;

public class choActivity extends ListActivity {

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Arrays.asList(new String[]{"Четная неделя", "Нечетная неделя"}));
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(position == 0) {
            Intent intent = new Intent(this, SchActivity.class);
            intent.putExtra("qq", 0);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SchActivity.class);
            intent.putExtra("qq", 1);
            startActivity(intent);
        }

        super.onListItemClick(l, v, position, id);
    }
}
