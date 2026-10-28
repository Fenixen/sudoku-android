package com.example.sudoku3_0;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    // Vytvoříme si instance našich tříd
    private SudokuLogic sudokuLogic;
    private SudokuBoardView boardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        // 1. Najdeme naše plátno z XML rozhraní pomocí jeho ID
        boardView = findViewById(R.id.sudokuBoard);

        // 2. Vytvoříme novou herní logiku
        sudokuLogic = new SudokuLogic();

        //vygenerujeme hru podle zvolene obtiznosti
        sudokuLogic.generateNewGame(getEmptyCells());

        // 4. Předáme čísla z logiky do našeho vizuálu
        boardView.setBoard(sudokuLogic.getBoard());
        boardView.setIsGiven(sudokuLogic.getIsGiven());

        setupButtons();
    }

    private void setupButtons(){
        //vytvorime si pole s ID vsech nasich tlacitek
        int[] buttonIds = {
                R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5,
                R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };

        //pomoci cyklu priradime vsem 9 tlacitkum stejne chovani
        for (int i = 0; i < buttonIds.length; i++){
            final int number = i + 1; // na indexu 0 je tlacitko pro cislo 1

            findViewById(buttonIds[i]).setOnClickListener(v -> {
                //zjistime jake policko ma hrac nakliknute
                int r = boardView.getSelectedRow();
                int c = boardView.getSelectedCol();

                //pokud ma neco vybrane, -1 znamena ze nic neni vybrane
                if (r != -1 && c != -1){
                    //posleme pozadavek logice, validator zkontroluje pravidla
                    if(sudokuLogic.placeNumber(r, c, number)){
                        //pokud byl tah platny a cislo se zapsalo, naridime na vykresleni noveho stavu
                        boardView.invalidate();
                        checkWinCondition();
                    }
                }
            });

            findViewById(R.id.btnDelete).setOnClickListener(v -> {
                int r = boardView.getSelectedRow();
                int c = boardView.getSelectedCol();

                if (r != -1 && c != -1) {
                    // Pokusíme se číslo smazat
                    if (sudokuLogic.deleteNumber(r, c)) {
                        boardView.invalidate(); // Překreslíme, pokud se smazání povedlo
                    }
                }
            });
        }
    }

    private void checkWinCondition(){
        if(sudokuLogic.isGameWon()){
            //hrac vyhral
            new AlertDialog.Builder(this)
                    .setTitle("Congratulations!")
                    .setMessage("The Game is Won!")
                    // Zabranime tomu, aby si hrac okno nezvrel kliknutim mimo nej
                    .setCancelable(false)
                    .setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //kdyz hrac klikne na tlacitko New Game, vygenerujeme novou hru
                            sudokuLogic.generateNewGame(getEmptyCells());
                            //posleme nova data do platna a prekreslime to
                            boardView.setBoard(sudokuLogic.getBoard());
                            boardView.setIsGiven(sudokuLogic.getIsGiven());
                            boardView.invalidate();
                        }
                    })
                    .show(); //zobrazeni okna
        }
    }

    private int getEmptyCells(){
        //vyzvedneme si hodnotu z menu (pokud by zadna neprisla, jako pojistka se provede 45)
        return getIntent().getIntExtra("DIFFICULTY", 45);
    }
}

