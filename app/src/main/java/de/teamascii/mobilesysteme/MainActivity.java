package de.teamascii.mobilesysteme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeMessageTV = new TextView(this);
        welcomeMessageTV.setText("Hello CodeYourApp World!");
        setContentView(welcomeMessageTV);

    }
}
