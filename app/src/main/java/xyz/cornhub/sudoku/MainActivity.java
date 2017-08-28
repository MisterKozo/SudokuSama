package xyz.cornhub.sudoku;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void main_play(View v) {
        startActivity(new Intent(this, PlayActivity.class));
    }

    public void main_scores(View v) {
        startActivity(new Intent(this, ScoresActivity.class));
    }

    public void main_credits(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.creditsTitle))
                .setMessage(getString(R.string.creditsText))
                .setNeutralButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss
                    }
                })
                .setIcon(R.drawable.icon)
                .show();
    }

    public void main_leave(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.leaveTitle))
                .setMessage(getString(R.string.leaveText))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // dismiss
                    }
                })
                .setIcon(R.drawable.icon)
                .show();
    }

}
