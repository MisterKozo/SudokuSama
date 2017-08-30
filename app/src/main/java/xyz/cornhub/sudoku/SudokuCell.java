package xyz.cornhub.sudoku;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;

public class SudokuCell {
    private boolean active;
    private boolean hard;
    private int number;
    private int posX;
    private int posY;
    private Paint recPaint;
    private Paint txtPaint;

    public SudokuCell() {
        this.recPaint = new Paint();
        this.txtPaint = new Paint();
        this.number = 0;
        this.active = false;
        this.posX = 0;
        this.posY = 0;
        this.recPaint.setColor(-1);
        this.txtPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.txtPaint.setTextSize(70.0f);
    }

    public SudokuCell(int number, int posX, int posY, boolean hard) {
        this.recPaint = new Paint();
        this.txtPaint = new Paint();
        this.number = number;
        this.active = false;
        this.posX = posX;
        this.posY = posY;
        this.hard = hard;
        this.recPaint.setColor(-1);
        this.txtPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.txtPaint.setTextSize(70.0f);
    }

    public boolean IsPressed() {
        return this.active;
    }

    public void SetNumber(int number) {
        if (!this.hard) {
            this.number = number;
        }
    }

    public void Press(SudokuCell[][] cells) {
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 9; x++) {
                cells[x][y].Depress();
            }
        }
        this.active = true;
        this.recPaint.setColor(-16711681);
        this.txtPaint.setColor(-1);
    }

    public void Depress() {
        this.active = false;
        this.recPaint.setColor(-1);
        this.txtPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public void Redraw(Canvas canvas, int width) {
        String tNumber = String.valueOf(this.number);
        int startVer = (((width - 90) / 9) * this.posY) + 45;
        int endVer = (((width - 90) / 9) * (this.posY + 1)) + 45;
        int startHor = (((width - 90) / 9) * this.posX) + 45;
        int endHor = (((width - 90) / 9) * (this.posX + 1)) + 45;
        int textY = (startVer + 45) + ((endVer - startVer) / 4);
        int textX = startHor + ((endHor - startHor) / 2);
        canvas.drawRect((float) startHor, (float) startVer, (float) endHor, (float) endVer, this.recPaint);
        this.txtPaint.setTextAlign(Align.CENTER);
        if (this.hard) {
            this.txtPaint.setTypeface(Typeface.create(Typeface.DEFAULT, 1));
        }
        if (this.number != 0) {
            canvas.drawText(tNumber, (float) textX, (float) textY, this.txtPaint);
        }
    }

    public int GetNumber() {
        return this.number;
    }

    public boolean IsHard() {
        return this.hard;
    }

    public void Paint(int color) {
        this.recPaint.setColor(color);
    }
}
