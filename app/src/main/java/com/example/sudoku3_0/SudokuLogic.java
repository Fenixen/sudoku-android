package com.example.sudoku3_0;
import java.util.Random;

public class SudokuLogic {
    private int[][] board;
    private boolean[][] isGiven; //pamatuje si puvodni zadani
    private Random random  = new Random();

    // Inicializace prazdne mrizky 9x9
    public SudokuLogic() {
        board = new int[9][9];
        isGiven = new boolean[9][9];
    }

    public int[][] getBoard() {
        return board;
    }

    //metoda pro zapsani cisla do mrizky, pokud je tah platny
    public boolean placeNumber(int row, int col, int number){
        //pokud je policko soucastni puvodniho zadani, tah se rovnou zamitne
        if (isGiven[row][col]){
            return false;
        }

        if(isValidMove(row, col, number)){
            board[row][col] = number;
            return true;
        }
        return false; // tah neni povolen
    }

    //validator, zda je zadano cislo spravne
    private boolean isValidMove(int row, int col, int number){
        for (int i = 0; i < 9; i++){
            if (board[row][i] == number) {
                return false; // cislo je v radku
            }
        }
        for (int i = 0; i < 9; i++){
            if (board[i][col] == number) {
                return false; // cislo je v sloupci
            }
        }

        //kontrola ctverce
        //vypocitame kde zacina ctverec
        int startRow = row - (row % 3); // TODO udelat lepsi pocitani!!!!!
        int startCol = col - (col % 3);

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[startRow + i][startCol + j] == number) {
                    return false; // cislo je v tom ctverci
                }
            }
        }

        return true;
    }

    public boolean fillBoard() {
        for(int row = 0; row < 9; row++){
            for(int col = 0; col < 9; col++){
                if (board[row][col] == 0) {
                    //pripravime si cisla 1-9 a zamichame je
                    //aby kazda vygenerovana hra byla uplne jina
                    int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
                    shuffleArray(numbers);

                    for (int number : numbers){
                        if (isValidMove(row, col, number)) {
                            board[row][col] = number;

                            //rekurzivne zavolame fillBoard() pro zbytek mrizky
                            if (fillBoard()) {
                                return true; // podarilo se vyplnit celou plochu
                            } else {
                                //slepa ulicka krok zpet
                                board[row][col] = 0;
                            }

                        }
                    }
                    return false; //do tohoto policka nepasuje zadne cislo, musime se vratit

                }
            }
        }
        return true; //podarilo se vyplnit celou plochu
    }

    //metoda pro zamichavani pole
    private void shuffleArray(int[] numbers){
        for (int i = numbers.length - 1; i > 0; i--){
            int index = random.nextInt(i + 1);
            int temp = numbers[index];
            numbers[index] = numbers[i];
            numbers[i] = temp;
        }
    }

    public void generateNewGame(int emptyCells){
        // radsi vycistime plochu, pokud by uz hrac hral predchozi hru
        board = new int[9][9];
        isGiven = new boolean[9][9];


        //nove reseni hry
        fillBoard();

        //vymazeme pozadovany pocet policek
        int count = 0;
        while (count < emptyCells){
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            //pokud na policku jeste je cislo, vymazeme ho
            if (board[row][col] != 0){
                board[row][col] = 0;
                count++;
            }
        }

        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (board[r][c] != 0){
                    isGiven[r][c] = true;
                }
            }
        }
    }

    public boolean deleteNumber(int row, int col){
        //puvodni zadani smazat nejde
        if(isGiven[row][col]){
            return false;
        }
        board[row][col] = 0; //nastavime na 0, prazdne policko
        return true;
    }

    public boolean[][] getIsGiven(){
        return isGiven;
    }

    public boolean isGameWon(){
        for (int r = 0; r < 9; r++){
            for (int c = 0; c < 9; c++){
                if (board[r][c] == 0){
                    return false; //nasli jsme prazdne policko a hra neni vyhrana
                }
            }
        }
        return true; // nenasli jsme nulu, hra je vyhrana
    }
}
