package xyz.cornhub.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ScoresActivity extends AppCompatActivity {
    TextView tv_onlines, tv_offlines;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        tv_onlines  = (TextView) findViewById(R.id.tv_onlines);
        tv_offlines = (TextView) findViewById(R.id.tv_offlines);
    }

    public void scores_back(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
}