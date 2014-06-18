package ru.sgu.univer.app.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.MyFile;
import ru.sgu.univer.app.objects.OperationType;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.writers.MyFilter;

public class BrouseActivity extends ListActivity {
    public static final String FIND_FILE_EXTRA = "find_file_extra";
    public static final String FILE_END_EXTRA = "file_end_extra";
    public static final String OPERATION_EXTRA = "operation_extra";
    private ArrayAdapter<MyFile> adapter;
    private int groupId;
    private int courseId;
    private int operation;
    private String fileEnd = "";
    private boolean findFile;
    private File file1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileEnd = getIntent().getStringExtra(FILE_END_EXTRA);
        findFile = getIntent().getBooleanExtra(FIND_FILE_EXTRA, true);
        groupId = getIntent().getIntExtra(RatingActivity.GROUP_ID_RATING_PARAM, 0);
        courseId = getIntent().getIntExtra(GroupListActivity.COURSE_ID_EXTRA, 0);
        operation = getIntent().getIntExtra(OPERATION_EXTRA, -1);

        setContentView(R.layout.activity_brouse);

        adapter = new ArrayAdapter<MyFile>(this, android.R.layout.simple_list_item_1, getFiles("/"));
        setListAdapter(adapter);
    }

    private List<MyFile> getFiles(String path) {
        List<MyFile> files = new ArrayList<MyFile>();
        if(!"/".equals(path)) {
            files.add(new MyFile(new File(path).getParentFile(), true));
        }
        File rootFile = new File(path);
        for (File file : rootFile.listFiles(new MyFilter(findFile, fileEnd))) {
            files.add(new MyFile(file));
        }
        return files;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        file1 = adapter.getItem(position).file;

        if(file1.isFile()) {
            Intent intent = new Intent(this, RatingActivity.class);
            if (operation == OperationType.BACKUP.ordinal() || operation == OperationType.RESTORE.ordinal()) {
                intent = new Intent(this, MainActivity.class);
            }
            intent.putExtra(RatingActivity.GROUP_ID_RATING_PARAM, groupId);
            intent.putExtra(GroupListActivity.COURSE_ID_EXTRA, courseId);
            intent.putExtra(RatingActivity.PATH_RATING_PARAM, file1.getAbsolutePath());
            intent.putExtra(OPERATION_EXTRA, operation);

            startActivity(intent);
            return;
        }

        adapter = new ArrayAdapter<MyFile>(this, android.R.layout.simple_list_item_1, getFiles(file1.getAbsolutePath()));
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.brouse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_chouse_dir) {
            Intent intent = new Intent(this, RatingActivity.class);
            if (operation == OperationType.BACKUP.ordinal() || operation == OperationType.RESTORE.ordinal()) {
                intent = new Intent(this, MainActivity.class);
            }
            intent.putExtra(RatingActivity.GROUP_ID_RATING_PARAM, groupId);
            intent.putExtra(GroupListActivity.COURSE_ID_EXTRA, courseId);
            intent.putExtra(RatingActivity.PATH_RATING_PARAM, file1.getAbsolutePath());
            intent.putExtra(OPERATION_EXTRA, operation);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
