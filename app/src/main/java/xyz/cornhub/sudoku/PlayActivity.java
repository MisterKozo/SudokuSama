package xyz.cornhub.sudoku;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    private GameView GameLayout;

    protected void onCreate(Bundle savedInstanceState) {
        this.GameLayout = new GameView(this);
        super.onCreate(savedInstanceState);
        setContentView(this.GameLayout);
    }
}
