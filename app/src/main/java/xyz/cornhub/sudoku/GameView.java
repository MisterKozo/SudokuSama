package xyz.cornhub.sudoku;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.InputDeviceCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import java.lang.reflect.Array;
import xyz.cornhub.sudoku.Sudoku.Game;

public class GameView extends View {
    private int Ax;
    private int Ay;
    private int Bx;
    private int By;
    private Canvas canvas;
    public SudokuCell[][] cells = ((SudokuCell[][]) Array.newInstance(SudokuCell.class, new int[]{9, 9}));
    private int height;
    private int pX = -1;
    private int pY = -1;
    private ProgressDialog progress = new ProgressDialog(getContext());
    private Paint recPaint;
    public Game sudoku = new Game();
    private Paint thick;
    private Paint thin;
    private Paint txtPaint;
    private int width;

    public GameView(Context context) {
        super(context);
        this.progress.setIcon(R.drawable.icon);
        this.progress.setTitle("Please Wait");
        this.progress.setMessage("Loading...");
        this.progress.show();
        this.sudoku.newGame();
        this.progress.dismiss();
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (this.sudoku.getNumber(x, y) == 0) {
                    this.cells[x][y] = new SudokuCell(this.sudoku.getNumber(x, y), x, y, false);
                } else {
                    this.cells[x][y] = new SudokuCell(this.sudoku.getNumber(x, y), x, y, true);
                }
            }
        }
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        int x;
        super.onDraw(canvas);
        canvas.save();
        this.canvas = canvas;
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.recPaint = new Paint();
        this.txtPaint = new Paint();
        this.thick = new Paint();
        this.thin = new Paint();
        for (int y = 0; y < 9; y++) {
            for (x = 0; x < 9; x++) {
                this.cells[x][y].Redraw(canvas, this.width);
            }
        }
        this.recPaint.setColor(-1);
        this.txtPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.txtPaint.setTextSize(70.0f);
        this.thick.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.thin.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.thick.setStrokeWidth(10.0f);
        this.thin.setStrokeWidth(5.0f);
        this.Ax = 45;
        this.Ay = 45;
        this.Bx = (((this.width - 90) / 9) * 9) + 45;
        this.By = (((this.width - 90) / 9) * 9) + 45;
        for (x = 0; x < 9; x++) {
            int startVer = this.By + 45;
            int endVer = (this.By + 45) + ((this.width - 90) / 9);
            int startHor = (((this.width - 90) / 9) * x) + 45;
            int endHor = (((this.width - 90) / 9) * (x + 1)) + 45;
            int textY = (startVer + 45) + ((endVer - startVer) / 4);
            int textX = startHor + ((endHor - startHor) / 4);
            canvas.drawRect((float) startHor, (float) startVer, (float) endHor, (float) endVer, this.recPaint);
            canvas.drawText(String.valueOf(x + 1), (float) textX, (float) textY, this.txtPaint);
            canvas.drawLine(45.0f, (float) ((((this.width - 90) / 9) * x) + 45), (float) (this.width - 45), (float) ((((this.width - 90) / 9) * x) + 45), this.thin);
            canvas.drawLine((float) ((((this.width - 90) / 9) * x) + 45), 45.0f, (float) ((((this.width - 90) / 9) * x) + 45), (float) ((((this.width - 90) / 9) * 9) + 45), this.thin);
        }
        canvas.drawLine((float) ((((this.width - 90) / 9) * 3) + 45), 45.0f, (float) ((((this.width - 90) / 9) * 3) + 45), (float) ((((this.width - 90) / 9) * 9) + 45), this.thick);
        canvas.drawLine((float) ((((this.width - 90) / 9) * 6) + 45), 45.0f, (float) ((((this.width - 90) / 9) * 6) + 45), (float) ((((this.width - 90) / 9) * 9) + 45), this.thick);
        canvas.drawLine(45.0f, (float) ((((this.width - 90) / 9) * 3) + 45), (float) ((((this.width - 90) / 9) * 9) + 45), (float) ((((this.width - 90) / 9) * 3) + 45), this.thick);
        canvas.drawLine(45.0f, (float) ((((this.width - 90) / 9) * 6) + 45), (float) ((((this.width - 90) / 9) * 9) + 45), (float) ((((this.width - 90) / 9) * 6) + 45), this.thick);
        Drawable back = getResources().getDrawable(R.drawable.back);
        Drawable help = getResources().getDrawable(R.drawable.help);
        Drawable solve = getResources().getDrawable(R.drawable.solve);
        back.setBounds(45, this.height - 245, 245, this.height - 45);
        help.setBounds((this.width / 2) - 100, this.height - 245, (this.width / 2) + 100, this.height - 45);
        solve.setBounds(this.width - 245, this.height - 245, this.width - 45, this.height - 45);
        back.draw(canvas);
        help.draw(canvas);
        solve.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case 0:
                AnalTouch(x, y);
                break;
        }
        return true;
    }

    public void RedrawBoard() {
        invalidate();
    }

    public void AnalTouch(float touchX, float touchY) {
        int ly;
        int lx;
        boolean freezone = true;
        if (touchX >= ((float) this.Ax) && touchX <= ((float) this.Bx) && touchY >= ((float) this.Ay) && touchY <= ((float) this.By)) {
            int intX = (int) ((touchX - 45.0f) / ((float) ((this.width - 90) / 9)));
            int intY = (int) ((touchY - 45.0f) / ((float) ((this.width - 90) / 9)));
            Log.i("tripaloski", "TOUCH: " + String.valueOf(intX) + "," + String.valueOf(intY));
            this.cells[intX][intY].Press(this.cells);
            RedrawBoard();
            this.pX = intX;
            this.pY = intY;
            freezone = false;
        }
        if (touchX >= ((float) this.Ax) && touchX <= ((float) this.Bx) && touchY >= ((float) (this.By + 45)) && touchY <= ((float) (this.By + 155))) {
            freezone = false;
            int intX = (int) ((touchX - 45.0f) / ((float) ((this.width - 90) / 9)));
            Log.i("tripaloski", "NUMROW: " + String.valueOf(intX + 1));
            for (ly = 0; ly < 9; ly++) {
                for (lx = 0; lx < 9; lx++) {
                    if (this.cells[lx][ly].IsPressed()) {
                        this.cells[lx][ly].SetNumber(intX + 1);
                        this.cells[lx][ly].Paint(-16711681);
                        Log.i("tripaloski", "Detected: " + String.valueOf(lx) + "," + String.valueOf(ly));
                    }
                }
            }
            if (!(this.pX == -1 || this.pY == -1 || this.cells[this.pX][this.pY].IsHard() || !BoardFull())) {
                if (IsValidBoard()) {
                    for (ly = 0; ly < 9; ly++) {
                        for (lx = 0; lx < 9; lx++) {
                            this.cells[lx][ly].Paint(-65281);
                        }
                    }
                    Toast.makeText(getContext(), "Congratulations!", 1).show();
                } else {
                    Toast.makeText(getContext(), "Please check the board! Something is wrong with your solution.", 1).show();
                }
            }
            invalidate();
        }
        if (touchY >= ((float) (this.height - 245)) && touchY <= ((float) (this.height - 45))) {
            if (touchX >= 45.0f && touchX <= 245.0f) {
                Log.i("tripaloski", "BACK");
                freezone = false;
                ly = 0;
                while (ly < 9) {
                    lx = 0;
                    while (lx < 9) {
                        if (!this.cells[lx][ly].IsHard() && this.cells[lx][ly].IsPressed()) {
                            this.cells[lx][ly].SetNumber(0);
                            this.cells[lx][ly].Paint(-16711681);
                        }
                        lx++;
                    }
                    ly++;
                }
                invalidate();
            }
            if (touchX >= ((float) ((this.width / 2) - 100)) && touchX <= ((float) ((this.width / 2) + 100))) {
                Log.i("tripaloski", "HELP");
                freezone = false;
                for (ly = 0; ly < 9; ly++) {
                    for (lx = 0; lx < 9; lx++) {
                        if (this.cells[lx][ly].IsPressed()) {
                            if (this.cells[lx][ly].GetNumber() == 0) {
                                this.cells[lx][ly].Paint(InputDeviceCompat.SOURCE_ANY);
                            } else if (IsPossible(lx, ly)) {
                                this.cells[lx][ly].Paint(-16711936);
                            } else {
                                this.cells[lx][ly].Paint(SupportMenu.CATEGORY_MASK);
                            }
                        }
                    }
                }
                invalidate();
            }
            if (touchX >= ((float) (this.width - 245)) && touchX <= ((float) (this.width - 45))) {
                Log.i("tripaloski", "CLEAN");
                freezone = false;
                for (ly = 0; ly < 9; ly++) {
                    for (lx = 0; lx < 9; lx++) {
                        if (!this.cells[lx][ly].IsHard()) {
                            this.cells[lx][ly].SetNumber(0);
                        }
                        if (this.cells[lx][ly].IsPressed()) {
                            this.cells[lx][ly].Depress();
                        }
                    }
                }
                invalidate();
            }
        }
        if (freezone) {
            Log.i("tripaloski", "free");
            if (this.pX != -1 && this.pY != -1) {
                this.cells[this.pX][this.pY].Depress();
                invalidate();
            }
        }
    }

    public boolean IsPossible(int cx, int cy) {
        Log.i("tripaloski", "Checking if possible");
        int y = 0;
        while (y < 9) {
            if (y != cy && this.cells[cx][y].GetNumber() == this.cells[cx][cy].GetNumber()) {
                return false;
            }
            y++;
        }
        int x = 0;
        while (x < 9) {
            if (x != cx && this.cells[x][cy].GetNumber() == this.cells[cx][cy].GetNumber()) {
                return false;
            }
            x++;
        }
        y = (cy / 3) * 3;
        while (y < ((cy / 3) + 1) * 3) {
            x = (cx / 3) * 3;
            while (x < ((cx / 3) + 1) * 3) {
                if ((cx != x || cy != y) && this.cells[x][y].GetNumber() == this.cells[cx][cy].GetNumber()) {
                    return false;
                }
                x++;
            }
            y++;
        }
        return true;
    }

    public boolean BoardFull() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (this.cells[x][y].GetNumber() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean IsValidBoard() {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                if (!IsPossible(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
