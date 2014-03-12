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
import java.util.List;
import java.util.Random;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Course;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.CourseProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class RatingActivity extends ActionBarActivity implements View.OnClickListener {
    public static final String GROUP_ID_RATING_PARAM = "group_id_rating_param";

    private int groupId;
    private TableLayout table;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(GROUP_ID_RATING_PARAM, 0);
        setContentView(R.layout.fragment_rating);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.rating_table_layout);

        List<TableRow> rows = new ArrayList<TableRow>();
        List<Student> students = StudentProvider.getStudentsByGroupId(groupId);
        for (Student student : students) {
            TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);

            TextView textView = (TextView) row.getChildAt(0);
            textView.setText(student.toString());
            for (int i = 0; i < 4; i++) {
                TextView editText = (TextView) getLayoutInflater().inflate(R.layout.table_cell, null);
                editText.setText(String.valueOf(new Random().nextInt(7)));
                editText.setOnClickListener(this);
                row.addView(editText);
            }
            rows.add(row);
        }

        for (TableRow row : rows) {
            tableLayout.addView(row);
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
//        builder.setNegativeButton("Отмена",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog,
//                                        int which) {
//                        dialog.cancel();
//                    }
//                });
        builder.show();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
