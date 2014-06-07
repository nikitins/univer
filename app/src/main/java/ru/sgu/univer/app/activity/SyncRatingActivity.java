package ru.sgu.univer.app.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Group;
import ru.sgu.univer.app.objects.Lesson;
import ru.sgu.univer.app.objects.LessonType;
import ru.sgu.univer.app.objects.MegaRatingTable;
import ru.sgu.univer.app.objects.OperationType;
import ru.sgu.univer.app.objects.RatingTable;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.LessonTypeProvider;
import ru.sgu.univer.app.providers.PerevodProvider;
import ru.sgu.univer.app.providers.RatingProvider;
import ru.sgu.univer.app.providers.StudentProvider;
import ru.sgu.univer.app.providers.SyncRatingProvider;
import ru.sgu.univer.app.utils.Parser;
import ru.sgu.univer.app.writers.ExcelParser;

public class SyncRatingActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String GROUP_ID_EXTRA = "sync_group_id_extra";
    public static final String GROUP_ID_RATING_PARAM = "group_id_rating_param";

    private int groupId;
    private int courseId;
    private TableLayout table;
    private TableRow tableHead;
    private Map<Integer, TableRow> rowMap;
    private Map<Integer, TextView> oMap;
    public static Map<TextView, Integer> viewStudentMap;
    public static Map<TextView, Integer> viewColMap;
    private int DATA_DIALOG_ID = 12;
    TextView dateTextView;
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


        for(int i = 0; i < students.size(); i++) {
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
                        TextView tv = (TextView)v;
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
        if(item.getItemId() == R.id.download) {
            GroupGetTask groupGetTask = new GroupGetTask(groupId);
            groupGetTask.execute((Void) null);
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
            setUp();
        }

        @Override
        protected void onCancelled() {
//            showProgress(false);
        }
    }
}
