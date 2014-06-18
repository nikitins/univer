package ru.sgu.univer.app.activity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.RatingProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class GroupListActivity extends ListActivity {
    public static final String COURSE_ID_EXTRA = "course_id_extra";
    private ArrayAdapter<Group> adapter;
    private int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseId = getIntent().getIntExtra(COURSE_ID_EXTRA, 0);
        setContentView(R.layout.activity_group_list);
        adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, GroupProvider.getGroupsByCourseId(courseId));
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
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(StudentActivity.GROUP_ID_PARAM, adapter.getItem(position).getId());
        intent.putExtra(COURSE_ID_EXTRA, courseId);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.group_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_remove: {
                removeGroup(item);
                break;
            }
            case R.id.action_group_rename: {
                renameGroup(item);
                break;
            }
        }
        return super.onContextItemSelected(item);
    }

    private void renameGroup(MenuItem item) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Group group = adapter.getItem(aMenuInfo.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Введите новое имя");
        builder.setCancelable(false);
        final EditText input = new EditText(this);
        input.setText(group.getName());
        builder.setView(input);
        builder.setPositiveButton("Переименовать",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        String newName = input.getText().toString();
                        if("".equals(newName)) {
                            showMessage("Имя не может быть пустым.");
                            dialog.dismiss();
                            return;
                        }
                        if(GroupProvider.hasGroup(newName) && !newName.equals(group.getName())) {
                            showMessage("Имя " + newName + " уже зенято.");
                            dialog.dismiss();
                            return;
                        }
                        GroupProvider.renameGroup(group.getId(), newName);
                        adapter.notifyDataSetChanged();
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

    }

    private void removeGroup(MenuItem item) {
        AdapterView.AdapterContextMenuInfo aMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Group group = adapter.getItem(aMenuInfo.position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы хотите удалить " + group.getName() + "?");
        builder.setCancelable(false);
        builder.setPositiveButton("Да",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        GroupProvider.removeGroup(group.getId());
                        StudentProvider.removeStudentByGroupId(group.getId());
                        adapter.notifyDataSetChanged();
                        showMessage("Группа " + group.getName() + " удалена");
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Нет",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    private void addGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Введите имя");
        builder.setCancelable(false);
        final EditText input = new EditText(this);
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
                        if(GroupProvider.hasGroup(name)) {
                            showMessage("Имя " + name + " уже зенято.");
                            dialog.dismiss();
                            return;
                        }
                        int groupId = GroupProvider.addGroup(courseId, name).getId();
                        RatingProvider.add(courseId, groupId);
                        adapter.notifyDataSetChanged();
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
    }

    private void showMessage(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_group) {
            addGroup();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
