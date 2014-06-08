package ru.sgu.univer.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Lesson;
import ru.sgu.univer.app.objects.MegaRatingTable;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.CookieProvider;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;
import ru.sgu.univer.app.providers.SyncRatingProvider;
import ru.sgu.univer.app.utils.Parser;

public class SyncRatingActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String GROUP_ID_EXTRA = "sync_group_id_extra";

    private int groupId;
    private int courseId;
    private TableLayout table;
    private TableRow tableHead;
    private Map<Integer, TableRow> rowMap;
    private Map<Integer, TextView> oMap;
    public static Map<TextView, Integer> viewStudentMap;
    public static Map<TextView, Integer> viewColMap;
    MegaRatingTable rating;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId = getIntent().getIntExtra(GROUP_ID_EXTRA, 0);
        courseId = SyncGroupListActivity.SYNC_COURSE_ID;

        setUp();
    }

    private void setUp() {
        rowMap = new HashMap<Integer, TableRow>();
        oMap = new HashMap<Integer, TextView>();
        viewStudentMap = new HashMap<TextView, Integer>();
        viewColMap = new HashMap<TextView, Integer>();

        rating = SyncRatingProvider.map.get(groupId);
        setContentView(R.layout.activity_sync_rating);

        table = (TableLayout) findViewById(R.id.sync_rating_table_layout);
        tableHead = (TableRow) findViewById(R.id.sync_table_head);

        List<TableRow> rows = new ArrayList<TableRow>();
        List<Student> students = StudentProvider.getStudentsByGroupId(groupId);

        for (Lesson lesson : rating.lessons) {
            TextView headView = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
            headView.setText(lesson.toString());
            tableHead.addView(headView);
        }


        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);


            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);

            TextView textView = (TextView) row.getChildAt(0);
            textView.setText(student.toString());

            for (int j = 0; j < rating.getRatingByStudent(student.lastname).size(); j++) {

                int mark = rating.getRatingByStudent(student.lastname).get(j);
                TextView text = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
                String stringMark = String.valueOf(mark);
                text.setText(stringMark);
                text.setOnClickListener(this);
                viewStudentMap.put(text, student.id);
                viewColMap.put(text, j);
                row.addView(text);
            }
            TextView sumText = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
            sumText.setText(String.valueOf(rating.finMap.get(student.lastname)));
            sumText.setOnClickListener(getViewListener(this));
            viewStudentMap.put(sumText, student.id);
            viewColMap.put(sumText, rating.lessons.size() - 1);
            row.addView(sumText);

            rowMap.put(student.id, row);
            rows.add(row);
        }

//        TextView sumHead = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
//        sumHead.setText("Сумма");
//        tableHead.addView(sumHead);
//
//        TextView ocheHead = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
//        ocheHead.setText("Оценка");
//        tableHead.addView(ocheHead);

        for (TableRow row : rows) {
            table.addView(row);
        }
    }

    public View.OnClickListener getViewListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Оценка");
                final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.mark_dialog, null);

                final Spinner spinner = (Spinner) linearLayout.findViewById(R.id.mark_spinner);
                List<String> marks = Arrays.asList("", "2", "3", "4", "5", "неявка");
                final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, marks);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

                builder.setView(linearLayout);

                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                String mark = (String) spinner.getSelectedItem();
                                ((TextView) v).setText(mark);
                                TextView tv = (TextView) v;
                                int id = viewStudentMap.get(tv);
                                rating.finMap.put(StudentProvider.getById(id).lastname, mark);
                                dialog.dismiss();
                            }
                        }
                );
                builder.show();
            }
        };
    }

    @Override
    public void onClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Баллы");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Ок",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        ((TextView) v).setText(input.getText().toString());
                        TextView tv = (TextView) v;
                        int id = viewStudentMap.get(tv);
                        int pos = viewColMap.get(tv);
                        if (!"".equals(input.getText().toString())) {
                            int ball = Integer.valueOf(input.getText().toString());
                            for (Map.Entry<TextView, Integer> entry : viewColMap.entrySet()) {
                                Log.d("LOG", "entry col map: " + entry.getKey() + ": " + entry.getValue());
                            }
                            rating.put(StudentProvider.getById(id).lastname, pos, ball);
                            dialog.dismiss();
                        }
                    }
                }
        );
//        builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ((TextView) v).setText("Н");
//                rating.put(viewStudentMap.get(v), viewColMap.get(v), -1);
//                refreshSum();
//                dialog.dismiss();
//            }
//        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync_rating, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.download) {
            showMessage("Обновление...");
            GroupGetTask groupGetTask = new GroupGetTask(groupId);
            groupGetTask.execute((Void) null);
            return true;
        }
        if (item.getItemId() == R.id.action_upload) {
            showMessage("Загрузка данных на сервер...");
            GroupPostTask groupPostTask = new GroupPostTask(groupId);
            groupPostTask.execute((Void) null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
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
                get.setHeader("Cookie", CookieProvider.cookie);

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
                        Log.d("Log", ss.get(ss.size() - 1));

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
            setUp();
        }

        @Override
        protected void onCancelled() {
//            showProgress(false);
        }
    }

    public class GroupPostTask extends AsyncTask<Void, Void, Boolean> {

        private final int groupId;

        GroupPostTask(int groupId) {
            this.groupId = groupId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            MegaRatingTable table = SyncRatingProvider.map.get(groupId);

            try {
                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost post = new HttpPost("http://requestb.in/y4iuh1y4");
                HttpPost post = new HttpPost("http://cdobars.sgu.ru/Teacher/groupPoints.xhtml");

                // Add your data
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                post.setHeader("Cookie", CookieProvider.cookie);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                for (Map.Entry<String, List<Integer>> entry : table.rating.entrySet()) {
                    String fam = entry.getKey();
                    int pos = table.getStudentPosByFam(fam);
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        String col = table.getStringByPosition(i);
                        int val = entry.getValue().get(i);
                        nameValuePairs.add(new BasicNameValuePair("table:" + pos + ":" + col, String.valueOf(val)));
                    }
                    String fin = table.getFinToRequest(fam);
                    nameValuePairs.add(new BasicNameValuePair("table:" + pos + ":grade", String.valueOf(fin)));
                }

                nameValuePairs.add(new BasicNameValuePair("form", "form"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.ViewState", table.requestId));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.source", "btn"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.partial.event", "click"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.partial.execute", "btn form"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.partial.render", "form"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.behavior.event", "action"));
                nameValuePairs.add(new BasicNameValuePair("javax.faces.partial.ajax", "true"));


                UrlEncodedFormEntity en = new UrlEncodedFormEntity(nameValuePairs);
                en.setContentType("application/x-www-form-urlencoded");


                post.setHeader("User-Agent", "Apache-HttpClient/4.0.1 (java 1.5)");
                AbstractHttpEntity ent = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                ent.setContentType("application/x-www-form-urlencoded; charset=UTF-8");

                post.setEntity(ent);

                HttpResponse response = httpclient.execute(post);
                Log.d("LOG", response.getStatusLine().toString());
                response = httpclient.execute(post);
                Log.d("LOG", response.getStatusLine().toString());

            } catch (ClientProtocolException e) {
                showMessage(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                showMessage(e.getMessage());
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
            setUp();
            showMessage("Данные загружены");
        }

        @Override
        protected void onCancelled() {
//            showProgress(false);
        }
    }
}
