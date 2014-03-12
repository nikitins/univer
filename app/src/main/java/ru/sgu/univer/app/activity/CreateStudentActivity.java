package ru.sgu.univer.app.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.providers.GroupProvider;
import ru.sgu.univer.app.providers.StudentProvider;

public class CreateStudentActivity extends ActionBarActivity {

    public static final String GROUP_ID_CREATE_STUDENT = "group_id_create_student";
    int groupId;

    EditText editFamilia;
    EditText editName;
    EditText editOtchestvo;
    EditText editTel;
    EditText editEmail;
    TextView textGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getIntent().getIntExtra(GROUP_ID_CREATE_STUDENT, 0);
        setContentView(R.layout.activity_create_student);
        editFamilia = (EditText) findViewById(R.id.Edit_familia);
        editName = (EditText) findViewById(R.id.Edit_imya);
        editOtchestvo = (EditText) findViewById(R.id.Edit_Otchestvo);
        editTel = (EditText) findViewById(R.id.Edit_Telefon_mob);
        editEmail = (EditText) findViewById(R.id.Edit_Email);
        textGroup = (TextView) findViewById(R.id.edit_student_group);
        textGroup.setText(GroupProvider.getGroupById(groupId).getName());
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public void saveStudent(View view) {
        StudentProvider.add(editName.getText().toString(), editFamilia.getText().toString(),
                editOtchestvo.getText().toString(), editTel.getText().toString(),
                editEmail.getText().toString(),  groupId);
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(StudentActivity.GROUP_ID_PARAM, groupId);
        startActivity(intent);
    }
}
