package com.example.home.hajjability;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class home extends AppCompatActivity implements View.OnClickListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView bar;
        bar = (TextView) findViewById(R.id.textView_toolbar);
        bar.setText("Home");

        new navbar().create((DrawerLayout) this.findViewById(R.id.drawer_layout), home.this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                Intent int1 = new Intent(this, home.class);
                startActivity(int1);
                break;
            case R.id.settings:
                Intent int2 = new Intent(this, settings.class);
                startActivity(int2);
                break;
            case R.id.logoff:
                Intent int3 = new Intent(this, login.class);
                startActivity(int3);
                break;

        }
    }
}
