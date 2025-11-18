package io.github.some.BlockBreaker;  

public class GameSession {

    private static GameSession instance;

    //puntaje total del jugador en la partida actual
    private int score;

    //cantidad de vidas que le quedan al jugador
    private int lives;


    //Constructor privado: así nadie puede hacer new GameSession desde fuera
    private GameSession() {
        reset(); 
    }

    public static GameSession getInstance() {
        // si todavía no se ha creado la instancia
        if (instance == null) {
            //la creamos usando el constructor privado
            instance = new GameSession();
        }
        // devolvemos siempre la misma instancia
        return instance;
    }


    public void reset() {
        score = 0;   //puntaje parte en 0
        lives = 3;   //vidas iniciales 
    }

    //suma puntos al puntaje total.
    public void addScore(int points) {
        score += points;  //acumulamos los puntos 
    }

    //resta una vida al jugador.
    public void loseLife() {
        if (lives > 0) {// evitamos que baje de 0
            lives--;
        }
    }

    // Agrega una vida 
    public void addLife() {
        lives++;
    }

    //getter para mostrr en pantalla

    //devuelve el puntaje actual
    public int getScore() {
        return score;
    }

    //devuelve la cantidad de vidas actuales
    public int getLives() {
        return lives;
    }
}
