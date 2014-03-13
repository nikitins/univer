package ru.sgu.univer.app.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Lesson;
import ru.sgu.univer.app.objects.LessonType;
import ru.sgu.univer.app.objects.RatingTable;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.LessonTypeProvider;
import ru.sgu.univer.app.providers.RatingProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class RatingActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String GROUP_ID_RATING_PARAM = "group_id_rating_param";

    private int groupId;
    private int courseId;
    private TableLayout table;
    private TableRow tableHead;
    private Map<Integer, TableRow> rowMap = new HashMap<Integer, TableRow>();
    private Map<Integer, TextView> sumMap = new HashMap<Integer, TextView>();
    public static Map<TextView, Integer> viewStudentMap = new HashMap<TextView, Integer>();
    public static Map<TextView, Integer> viewColMap = new HashMap<TextView, Integer>();
    private int DATA_DIALOG_ID = 1;
    private int DATA_TEXT_ID = 111;
    TextView dateTextView;
    RatingTable rating;
    int myYear = 2014;
    int myMonth = 3;
    int myDay = 13;
    String dataString = myDay + "." + myMonth + "." + myYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupId = getIntent().getIntExtra(GROUP_ID_RATING_PARAM, 0);
        courseId = getIntent().getIntExtra(GroupListActivity.COURSE_ID_EXTRA, 0);
        rating = RatingProvider.getById(courseId, groupId);
        setContentView(R.layout.fragment_rating);

        table = (TableLayout) findViewById(R.id.rating_table_layout);
        tableHead = (TableRow) findViewById(R.id.table_head);

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

            for (Integer mark : rating.getRatingByStudentId(student.id)) {
                TextView text = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
                String stringMark = String.valueOf(mark);
                if(mark == -1) {
                    stringMark = "Н";
                }
                if(mark == -2) {
                    stringMark = "";
                }
                text.setText(stringMark);
                text.setOnClickListener(this);
                viewStudentMap.put(text, student.id);
                viewColMap.put(text, i);
                row.addView(text);
            }
            TextView sumText = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
            sumText.setText(String.valueOf(rating.getSumStudentId(student.id)));
            sumMap.put(student.id, sumText);
            row.addView(sumText);

            rowMap.put(student.id, row);
            rows.add(row);
        }

        TextView sumHead = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
        sumHead.setText("Сумма");
        tableHead.addView(sumHead);

        for (TableRow row : rows) {
            table.addView(row);
        }

    }

    public void addColumn(String date, LessonType type) {

        TextView headView = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
        headView.setText(type.toString() + " " + date);
        rating.addColumn(type.id, date);

        tableHead.addView(headView, tableHead.getChildCount() - 1);
        for (Map.Entry<Integer, TableRow> entry : rowMap.entrySet()) {
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
            textView.setOnClickListener(this);
            viewStudentMap.put(textView, entry.getKey());
            viewColMap.put(textView, rating.lessons.size() - 1);
            entry.getValue().addView(textView, entry.getValue().getChildCount() - 1);
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
                        rating.put(1,0,4);
                        TextView tv = (TextView)v;
                        int id = viewStudentMap.get(tv);
                        int pos = viewColMap.get(tv);
                        int ball = Integer.valueOf(input.getText().toString());
                        rating.put(id, pos, ball);
                        refreshSum();
                        dialog.dismiss();
                    }
                }
        );
        builder.setNeutralButton("Отсутствовал", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TextView) v).setText("Н");
                rating.put(viewStudentMap.get(v), viewColMap.get(v), -1);
                refreshSum();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rating, menu);
        return true;
    }

    public void refreshSum() {
        for (Map.Entry<Integer, TextView> entry : sumMap.entrySet()) {
            entry.getValue().setText(String.valueOf(rating.getSumStudentId(entry.getKey())));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_column) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Добавить занятие");
            final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.event_dialog, null);

            dateTextView = (TextView) linearLayout.findViewById(R.id.data_event_textView);
            dateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(DATA_DIALOG_ID);
                }
            });
            dateTextView.setText(dataString);

            final Spinner spinner = (Spinner) linearLayout.findViewById(R.id.add_event_spinner);
            ArrayAdapter<LessonType> dataAdapter = new ArrayAdapter<LessonType>(this,
                    android.R.layout.simple_spinner_item, LessonTypeProvider.lessonTypes);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);

            builder.setView(linearLayout);

            builder.setPositiveButton("Добавить",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            LessonType type = (LessonType) spinner.getSelectedItem();
                            String date = dateTextView.getText().toString();
                            addColumn(date, type);
                            dialog.dismiss();
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
        return super.onOptionsItemSelected(item);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == DATA_DIALOG_ID) {
            return new DatePickerDialog(this, myCallBack, myYear, myMonth, myDay);
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dataString = dayOfMonth + "." + monthOfYear + "." + year;
            dateTextView.setText(dataString);
        }
    };

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
