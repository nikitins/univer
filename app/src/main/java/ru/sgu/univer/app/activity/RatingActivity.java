package ru.sgu.univer.app.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class RatingActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String GROUP_ID_RATING_PARAM = "group_id_rating_param";

    private int groupId;
    private int courseId;
    private TableLayout table;
    private TableRow tableHead;
    private Map<Integer, TableRow> rowMap = new HashMap<Integer, TableRow>();
    private Map<Integer, TextView> sumMap = new HashMap<Integer, TextView>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(GROUP_ID_RATING_PARAM, 0);
        courseId = getIntent().getIntExtra(GroupListActivity.COURSE_ID_EXTRA, 0);
        setContentView(R.layout.fragment_rating);

        table = (TableLayout) findViewById(R.id.rating_table_layout);
        tableHead = (TableRow) findViewById(R.id.table_head);
        TextView sumHead = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
        sumHead.setText("Сумма");
        tableHead.addView(sumHead);

        List<TableRow> rows = new ArrayList<TableRow>();
        List<Student> students = StudentProvider.getStudentsByGroupId(groupId);
        for (Student student : students) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);

            TextView textView = (TextView) row.getChildAt(0);
            textView.setText(student.toString());
            for (int i = 0; i < 4; i++) {
                TextView editText = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
                int num = new Random().nextInt(7);
                editText.setText(String.valueOf(num));
                editText.setOnClickListener(this);
                row.addView(editText);
            }
            TextView sumText = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
            sumText.setText("0");
            sumMap.put(student.id, sumText);
            row.addView(sumText);

            rowMap.put(student.id, row);
            rows.add(row);
        }

        for (TableRow row : rows) {
            table.addView(row);
        }

    }

    public void addColumn(String name) {
        TextView headView = (TextView) getLayoutInflater().inflate(R.layout.head_table_cell, null);
        headView.setText(name);
        tableHead.addView(headView, tableHead.getChildCount() - 1);
        for (Map.Entry<Integer, TableRow> entry : rowMap.entrySet()) {
            TextView textView = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
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
                        dialog.dismiss();
                    }
                }
        );
        builder.setNeutralButton("Отсутствовал", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((TextView) v).setText("Н");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add_column) {
            addColumn("name");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
