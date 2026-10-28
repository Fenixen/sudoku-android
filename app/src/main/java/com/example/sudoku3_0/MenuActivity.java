package com.example.sudoku3_0;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // I menu bude v cernem kabatu
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_menu);

        Button btnEasy = findViewById(R.id.btnEasy);
        Button btnMedium = findViewById(R.id.btnMedium);
        Button btnHard = findViewById(R.id.btnHard);

        //nastaveni klikani
        btnEasy.setOnClickListener(v -> startGame(30)); //30 prázdných políček
        btnMedium.setOnClickListener(v -> startGame(45)); //45 prázdných políček
        btnHard.setOnClickListener(v -> startGame(55)); //55 prázdných políček
    }

    //metada, ktera spusti samotnou hru a preda ji cislo obtiznosti
    private void startGame(int emptyCells){
        Intent intent = new Intent(this, MainActivity.class);
        //pribalime si do Intent hodnotu pod klicem "Difficulty"
        intent.putExtra("DIFFICULTY", emptyCells);
        startActivity(intent);
    }
}