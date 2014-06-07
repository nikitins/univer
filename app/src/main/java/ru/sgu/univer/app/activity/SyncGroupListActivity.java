package ru.sgu.univer.app.activity;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.MegaRatingTable;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;
import ru.sgu.univer.app.providers.SyncRatingProvider;
import ru.sgu.univer.app.utils.Parser;

public class SyncGroupListActivity extends ListActivity {
    public static final String COURSE_ID_EXTRA = "course_id_extra";
    private ArrayAdapter<Group> adapter;
    public static int SYNC_COURSE_ID = 99999999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_group_list);
        adapter = new ArrayAdapter<Group>(this, android.R.layout.simple_list_item_1, GroupProvider.getGroupsByCourseId(SYNC_COURSE_ID));
        setListAdapter(adapter);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        parent = findViewById(R.layout.activity_sync_group_list);
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        int grId = adapter.getItem(position).getId();
        GroupGetTask groupGetTask = new GroupGetTask(grId);
        groupGetTask.execute((Void) null);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        getMenuInflater().inflate(R.menu.group_context, menu);
//        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_group_remove: {
//                removeGroup(item);
//                break;
//            }
//            case R.id.action_group_rename: {
//                renameGroup(item);
//                break;
//            }
//        }
//        return super.onContextItemSelected(item);
        return false;
    }

    public void showGroup(int groupId){
        Intent intent = new Intent(this, SyncGroupActivity.class);
        intent.putExtra(SyncGroupActivity.SYNC_GROUP_ID_EXTRA, groupId);
        startActivity(intent);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mProgressView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
    }

    public class GroupGetTask extends AsyncTask<Void, Void, Boolean> {

        private final int groupId;

        GroupGetTask(int groupId) {
            this.groupId = groupId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {

                Group group = GroupProvider.getGroupById(groupId);
                HttpClient hc = new DefaultHttpClient();
                HttpGet get = new HttpGet(group.link);
                get.setHeader("Cookie", "\t__utma=84755787.1464350218.1401944832.1401990220.1402150946.4; __utmz=84755787.1401944832.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); JSESSIONID=891458594F2F0F9952CF75070B15489F; __utmb=84755787.2.10.1402150946; __utmc=84755787; SPRING_SECURITY_REMEMBER_ME_COOKIE=cG96ZG55YWtvdnZhOjE0MDMzNjA1NjA1Mjg6ZTJjODhmOGNhODcyNDAxMWQwM2QwYTIyZWJlM2Q2YTg");

                MegaRatingTable m = null;
                boolean ok = true;
                int i = 0;
                do {
                    Log.d("LOG", String.valueOf(i++));
                    ok = true;
                    HttpResponse r = hc.execute(get);
                    Scanner s = new Scanner(r.getEntity().getContent());
                    List<String> ss = new ArrayList<String>();
                    while (s.hasNext()) {
                        String st = s.nextLine();
                        if (ss.size() > 0 && !ss.get(ss.size() - 1).endsWith(">")) {
                            ss.set(ss.size() - 1, ss.get(ss.size() - 1).concat(st));
                        } else {
                            ss.add(st);
                        }
//                        Log.d("Log", ss.get(ss.size()-1));

                    }
                    try {
                        m = Parser.parseRating(ss);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        ok = false;
                    }
                } while (!ok);

                for (Student student : m.students) {
                    StudentProvider.putIfNotExist(student, groupId);
                }
                SyncRatingProvider.map.put(groupId, m);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        private void showMessage(String message) {
            Toast.makeText(getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showGroup(groupId);
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }
}


