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
    Button callButton;
    Button mailButton;

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
        callButton = (Button) findViewById(R.id.Button_Call_mob);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + editTel.getText().toString())));
            }
        });
        mailButton = (Button) findViewById(R.id.Button_mail);
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

    public void saveStudent(View view) {
        StudentProvider.add(editName.getText().toString(), editFamilia.getText().toString(),
                editOtchestvo.getText().toString(), editTel.getText().toString(),
                editEmail.getText().toString(),  groupId);
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra(StudentActivity.GROUP_ID_PARAM, groupId);
        startActivity(intent);
    }
}
