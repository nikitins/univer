package ru.sgu.univer.app.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.objects.Student;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class EditStudentActivity extends ActionBarActivity {
    public static final String GROUP_ID_EDIT_STUDENT = "group_id_edit_student";
    public static final String STUDENT_ID_EDIT_STUDENT = "student_id_edit_student";
    int groupId;
    int studentId;

    EditText editFamilia;
    EditText editName;
    EditText editOtchestvo;
    EditText editTel;
    EditText editEmail;
    TextView textGroup;
    Button callButton;
    Button mailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(GROUP_ID_EDIT_STUDENT, 0);
        studentId = getIntent().getIntExtra(STUDENT_ID_EDIT_STUDENT, 0);
        setContentView(R.layout.activity_edit_student);
        editFamilia = (EditText) findViewById(R.id.Show_edit_familia);
        editName = (EditText) findViewById(R.id.Show_edit_imya);
        editOtchestvo = (EditText) findViewById(R.id.Show_edit_Otchestvo);
        editTel = (EditText) findViewById(R.id.Show_edit_Telefon_mob);
        editEmail = (EditText) findViewById(R.id.Show_edit_Email);
        textGroup = (TextView) findViewById(R.id.Show_edit_student_group);

        Student student = StudentProvider.getById(studentId);
        editFamilia.setText(student.getLastname());
        editName.setText(student.getName());
        editOtchestvo.setText(student.getSurname());
        editEmail.setText(student.email);
        editTel.setText(student.telefon);
        textGroup.setText(GroupProvider.getGroupById(groupId).getName());

        callButton = (Button) findViewById(R.id.Button_Call_mobe);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + editTel.getText().toString())));
            }
        });
        mailButton = (Button) findViewById(R.id.Button_maile);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                String[] arrayOfString = new String[1];
                arrayOfString[0] = editEmail.getText().toString();
                localIntent.putExtra("android.intent.extra.EMAIL", arrayOfString);
                localIntent.putExtra("android.intent.extra.TEXT", "");
                localIntent.putExtra("android.intent.extra.SUBJECT", "");
                startActivity(localIntent);
            }
        });
        textGroup.setText(GroupProvider.getGroupById(groupId).getName());
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void editStudent(View view) {
        StudentProvider.edit(studentId, editName.getText().toString(), editFamilia.getText().toString(),
                editOtchestvo.getText().toString(), editTel.getText().toString(),
                editEmail.getText().toString(), groupId);
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(StudentActivity.GROUP_ID_PARAM, groupId);
        startActivity(intent);
    }
}
