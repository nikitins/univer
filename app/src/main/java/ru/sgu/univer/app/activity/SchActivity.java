package ru.sgu.univer.app.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.sgu.univer.app.R;
import ru.sgu.univer.app.providers.TempProvider;

public class SchActivity extends ActionBarActivity {

    int v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = getIntent().getIntExtra("qq", 0);
        setContentView(R.layout.activity_sch);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        if (v == 0) {
            if(TempProvider.view1 == null) {
                TempProvider.view1 = getLayoutInflater().inflate(R.layout.activity_sch, null);
            }
            return TempProvider.view1;
        } else {
            if(TempProvider.view2 == null) {
                TempProvider.view2 = getLayoutInflater().inflate(R.layout.activity_sch, null);
            }
            return TempProvider.view2;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
