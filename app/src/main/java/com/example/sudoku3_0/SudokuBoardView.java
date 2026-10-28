package com.example.sudoku3_0;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SudokuBoardView extends View {
    private Paint thickLinePaint, thinLinePaint, textPaint, highlightPaint, userTextPaint;
    private int cellSize;

    // Proměnné pro uložení aktuálně vybraného políčka (-1 znamená, že není vybráno nic)
    private int selectedRow = -1;
    private int selectedCol = -1;

    private boolean[][] isGiven;

    // Zde bude uložena herní mřížka, kterou nám pošle SudokuLogic
    private int[][] board;

    public SudokuBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        thickLinePaint = new Paint();
        thickLinePaint.setStyle(Paint.Style.STROKE);
        thickLinePaint.setColor(Color.WHITE);
        thickLinePaint.setStrokeWidth(8f);

        thinLinePaint = new Paint();
        thinLinePaint.setStyle(Paint.Style.STROKE);
        thinLinePaint.setColor(Color.GRAY);
        thinLinePaint.setStrokeWidth(3f);

        // Nový štětec pro vykreslení čísel
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER); // Číslo se bude kreslit od svého středu

        // Nový štětec pro zvýraznění políčka (tmavší modro-šedá, šetrná k očím)
        highlightPaint = new Paint();
        highlightPaint.setColor(Color.parseColor("#bd7e02"));

        userTextPaint = new Paint();
        userTextPaint.setColor(Color.parseColor("#334455"));
        userTextPaint.setTextAlign(Paint.Align.CENTER); // Číslo se bude kreslit od svého středu
    }

    // Tuto metodu zavoláme z MainActivity, abychom plátnu předali hrací plochu
    public void setBoard(int[][] board) {
        this.board = board;
        invalidate(); // DŮLEŽITÉ: Říká Androidu "Něco se změnilo, překresli plátno!"
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = size / 9;

        // Nyní víme, jak velké je políčko, můžeme nastavit velikost písma (na 70% velikosti políčka)
        textPaint.setTextSize(cellSize * 0.7f);
        userTextPaint.setTextSize(cellSize * 0.7f);


        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 1. ZVÝRAZNĚNÍ: Nakreslíme barevný čtverec, pokud hráč na něco klikl
        if (selectedRow != -1 && selectedCol != -1) {
            canvas.drawRect(
                    selectedCol * cellSize,         // levý okraj (X)
                    selectedRow * cellSize,         // horní okraj (Y)
                    (selectedCol + 1) * cellSize,   // pravý okraj (X)
                    (selectedRow + 1) * cellSize,   // spodní okraj (Y)
                    highlightPaint
            );
        }

        // 2. MŘÍŽKA: Vykreslení čar (tvůj původní kód)
        for (int i = 0; i <= 9; i++) {
            Paint paintToUse = (i % 3 == 0) ? thickLinePaint : thinLinePaint;
            canvas.drawLine(i * cellSize, 0, i * cellSize, getWidth(), paintToUse);
            canvas.drawLine(0, i * cellSize, getWidth(), i * cellSize, paintToUse);
        }

        // 3. ČÍSLA: Projdeme celé 2D pole a vykreslíme čísla, která nejsou 0
        if (board != null) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (board[r][c] != 0) {
                        String text = String.valueOf(board[r][c]);

                        // Výpočet dokonalého středu pro text (X a Y souřadnice)
                        float x = (c * cellSize) + (cellSize / 2f);
                        float y = (r * cellSize) + (cellSize / 2f) - ((textPaint.descent() + textPaint.ascent()) / 2f);

                        Paint currentPaint = (isGiven[r][c]) ? textPaint : userTextPaint;
                        canvas.drawText(text, x, y, textPaint);
                    }
                }
            }
        }
    }

    // NOVÉ: Zachycení doteku prstem na obrazovku
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Zajímají nás jen doteky, kdy prst dopadne na obrazovku (ACTION_DOWN)
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            // Vydělíme pixelovou souřadnici velikostí buňky = zjistíme index (0 až 8)
            selectedCol = (int) (x / cellSize);
            selectedRow = (int) (y / cellSize);

            // Pojistka, kdyby uživatel klikl těsně mimo (aby aplikace nespadla)
            if (selectedCol >= 9 || selectedRow >= 9) {
                return false;
            }

            // DŮLEŽITÉ: Nahlásíme změnu a donutíme systém znovu zavolat onDraw()
            invalidate();
            return true;
        }
        return false;
    }

    public int getSelectedRow(){
        return selectedRow;
    }

    public int getSelectedCol(){
        return selectedCol;
    }

    public void setIsGiven(boolean[][] isGiven) {
        this.isGiven = isGiven;
    }
}