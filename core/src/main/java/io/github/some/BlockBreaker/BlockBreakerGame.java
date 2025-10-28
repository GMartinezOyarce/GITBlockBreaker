package io.github.some.BlockBreaker;

// --- Imports de LibGDX ---
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

import Habilidades.AgrandarPaddle;
import Habilidades.Habilidad;
import Habilidades.PaddlePegajoso;
import Habilidades.VelocidadPaddle;
import Habilidades.VidaExtra;

// --- Imports de Java ---
import java.util.ArrayList;
import java.util.Collections;

/**
 * Clase principal del juego (el "Cerebro").
 * Controla la lógica, el dibujado y los estados del juego.
 */
public class BlockBreakerGame extends ApplicationAdapter {

    // --- 1. VARIABLES DE RENDERIZADO (LibGDX) ---
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;

    // --- 2. VARIABLES DE OBJETOS DEL JUEGO ---
    private PingBall ball;
    private Paddle pad;
    private ArrayList<Block> blocks; // Lista de bloques

    // --- 3. VARIABLES DE ESTADO DEL JUEGO ---
    private int vidas;
    private int puntaje;
    private int nivel;

    // --- 4. MÁQUINA DE ESTADOS ---
    /**
     * Define los "modos" en los que puede estar el juego.
     * JUGANDO: La bola se mueve, hay colisiones.
     * SELECCIONANDO_HABILIDAD: El juego se pausa para mostrar el menú de mejoras.
     */
    private enum EstadoJuego {
        JUGANDO,
        SELECCIONANDO_HABILIDAD
    }
    private EstadoJuego estadoActual;

    // --- 5. SISTEMA DE HABILIDADES (SOLID) ---
    // La lista (pool) de todas las habilidades posibles
    private ArrayList<Habilidad> poolDeHabilidades;
    
    // Las 3 habilidades que se ofrecen al jugador
    private Habilidad opcion1;
    private Habilidad opcion2;
    private Habilidad opcion3;

    
    /**
     * Método de CREACIÓN. Se llama UNA VEZ al iniciar el juego.
     * Aquí se inicializan todos los objetos.
     */
    @Override
    public void create() {
        // --- Inicializar LibGDX ---
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); // Tamaño de la ventana
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.5f); // Tamaño de la fuente
        shape = new ShapeRenderer();

        // --- Inicializar Objetos del Juego ---
        blocks = new ArrayList<>();
        // Posiciona el paddle en el centro inferior
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 20, 100, 10);
        // Posiciona la bola sobre el paddle
        ball = new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + 20, 10, 8, 8, true);

        // --- Inicializar Estado del Juego ---
        vidas = 3;
        puntaje = 0;
        nivel = 1;

        // --- Inicializar Sistema de Habilidades ---
        poolDeHabilidades = new ArrayList<>();
        // (Asegúrate de que tus clases se llamen así y que "implements Habilidad")
        poolDeHabilidades.add(new VidaExtra());
        poolDeHabilidades.add(new AgrandarPaddle());
        poolDeHabilidades.add(new VelocidadPaddle());
        poolDeHabilidades.add(new PaddlePegajoso());
        // ¡Puedes seguir añadiendo más habilidades aquí!

        // --- Estado Inicial ---
        estadoActual = EstadoJuego.JUGANDO;
        
        // Carga los bloques del primer nivel
        crearBloques(nivel);
    }

    /**
     * Método RENDER. Se llama 60 veces por segundo (Game Loop).
     * Actúa como un "director de tráfico" basado en el estado actual.
     */
    @Override
    public void render() {
        // 1. Limpiar la pantalla
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        // 2. Lógica de Update (basada en el estado)
        switch (estadoActual) {
            case JUGANDO:
                updateJuego(); // Mueve la bola, revisa colisiones, etc.
                break;
            case SELECCIONANDO_HABILIDAD:
                manejarInputSeleccion(); // Escucha las teclas 1, 2, 3
                break;
        }

        // 3. Lógica de Dibujado (SIEMPRE se ejecuta)

        // --- Dibujado con ShapeRenderer (Formas) ---
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeType.Filled);
        
        ball.draw(shape); // Dibuja la bola
        pad.draw(shape);  // Dibuja el paddle
        
        // Dibuja todos los bloques
        for (Block block : blocks) {
            block.draw(shape);
        }
        
        shape.end();

        // --- Dibujado con SpriteBatch (Textos) ---
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        // Dibuja la UI básica (vidas, puntaje)
        font.draw(batch, "Vidas: " + vidas, 10, 470);
        font.draw(batch, "Puntaje: " + puntaje, Gdx.graphics.getWidth() - 150, 470);

        // Si estamos en el menú, dibuja el menú de habilidades ENCIMA de todo
        if (estadoActual == EstadoJuego.SELECCIONANDO_HABILIDAD) {
            dibujarMenuHabilidades();
        }
        
        batch.end();
    }

    /**
     * Lógica principal del juego. Se llama 60 veces por segundo
     * SOLO si estadoActual es JUGANDO.
     */
    private void updateJuego() {
        
        // --- 1. Input del Paddle ---
        // (Movemos la lógica de input aquí, fuera del draw de Paddle)
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pad.moveLeft(); // Asumiendo que añades este método a Paddle
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pad.moveRight(); // Asumiendo que añades este método a Paddle
        }
        // (Asegúrate de añadir los límites de pantalla en moveLeft/Right de Paddle)

        // --- 2. Iniciar la bola ---
        // Si la bola está quieta y el jugador presiona ESPACIO, la lanza
        if (ball.estaQuieto() && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            ball.setEstaQuieto(false);
            ball.forzarVelocidadArriba();
        }
        
        // Si la bola está quieta, sigue al paddle
        if(ball.estaQuieto()) {
            ball.setXY(pad.getX() + pad.getWidth() / 2 - ball.getSize() / 2, pad.getY() + pad.getHeight() + 5);
        } else {
            // Si no, actualiza su movimiento
            ball.update();
        }
        
        // --- 3. Lógica de Colisiones ---
        
        // Colisión Bola <-> Paddle
        ball.checkCollision(pad);

        // Colisión Bola <-> Bloques
        for (int i = 0; i < blocks.size(); i++) {
            Block block = blocks.get(i);
            if (ball.checkCollision(block)) {
                puntaje += 1; // Gana puntos
                blocks.remove(i); // Destruye el bloque
                i--; // Ajusta el índice del bucle
            }
        }
        
        // --- 4. Comprobar Fin de Nivel ---
        // Si no quedan bloques, pasa al menú de habilidad
        if (blocks.isEmpty()) {
            prepararSeleccionHabilidad();
        }
        
        // --- 5. Comprobar Muerte ---
        if (ball.getY() < 0) {
            vidas--;
            if (vidas <= 0) {
                // Game Over (Implementar esta lógica, ej. reiniciar el juego)
                reiniciarJuego(); 
            } else {
                // Pierde una vida, resetea la bola
                ball.setEstaQuieto(true);
            }
        }
        
    }

    /**
     * Dibuja los bloques en pantalla.
     * (Esta es la lógica que ya tenías en tu .class)
     */
    private void crearBloques(int nivelActual) {
        blocks.clear();
        int blockWidth = 70;
        int blockHeight = 25;
        int filas = 1 + nivelActual; // El nivel añade más filas
        int columnas = 9;
        
        for (int y = 0; y < filas; y++) {
            for (int x = 0; x < columnas; x++) {
                blocks.add(new Block(
                    x * (blockWidth + 10) + 30, // Posición X
                    Gdx.graphics.getHeight() - (y * (blockHeight + 10)) - 50, // Posición Y
                    blockWidth,
                    blockHeight
                ));
            }
        }
    }
    
    // --- ============================================ ---
    // --- MÉTODOS DEL SISTEMA DE HABILIDADES (SOLID) ---
    // --- ============================================ ---

    /**
     * Se llama UNA VEZ al completar un nivel.
     * Pausa el juego y elige 3 habilidades al azar.
     */
    private void prepararSeleccionHabilidad() {
        estadoActual = EstadoJuego.SELECCIONANDO_HABILIDAD;
        ball.setEstaQuieto(true); // Pausa la bola
        
        // Baraja la lista y elige 3
        Collections.shuffle(poolDeHabilidades);
        opcion1 = (poolDeHabilidades.size() > 0) ? poolDeHabilidades.get(0) : null;
        opcion2 = (poolDeHabilidades.size() > 1) ? poolDeHabilidades.get(1) : null;
        opcion3 = (poolDeHabilidades.size() > 2) ? poolDeHabilidades.get(2) : null;

        // Carga los bloques del siguiente nivel
        this.nivel++;
        crearBloques(this.nivel);
    }

    /**
     * Dibuja el menú de habilidades (Se llama desde render).
     */
    private void dibujarMenuHabilidades() {
        float centroX = Gdx.graphics.getWidth() / 2;
        float centroY = Gdx.graphics.getHeight() / 2;

        font.draw(batch, "¡NIVEL " + (nivel-1) + " COMPLETADO!", centroX - 130, centroY + 100);
        font.draw(batch, "Elige una mejora:", centroX - 100, centroY + 50);

        if (opcion1 != null) {
            font.draw(batch, "1. " + opcion1.getNombre(), centroX - 80, centroY);
        }
        if (opcion2 != null) {
            font.draw(batch, "2. " + opcion2.getNombre(), centroX - 80, centroY - 40);
        }
        if (opcion3 != null) {
            font.draw(batch, "3. " + opcion3.getNombre(), centroX - 80, centroY - 80);
        }
    }

    /**
     * Escucha las teclas 1, 2, 3 (Se llama desde render).
     */
    private void manejarInputSeleccion() {
        Habilidad habilidadElegida = null;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && opcion1 != null) {
            habilidadElegida = opcion1;
        } 
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && opcion2 != null) {
            habilidadElegida = opcion2;
        } 
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && opcion3 != null) {
            habilidadElegida = opcion3;
        }

        if (habilidadElegida != null) {
            // ¡Magia! Aplica la habilidad sin saber cuál es
            habilidadElegida.aplicar(this);
            
            // Continúa al siguiente nivel
            iniciarSiguienteNivel();
        }
    }

    /**
     * Se llama después de elegir una habilidad para reanudar el juego.
     */
    private void iniciarSiguienteNivel() {
        estadoActual = EstadoJuego.JUGANDO;
        ball.setEstaQuieto(true); // La bola empieza quieta sobre el paddle
    }
    
    private void reiniciarJuego() {
        // Resetea todo al estado inicial
        vidas = 3;
        puntaje = 0;
        nivel = 1;
        
        // Limpia los bloques viejos y crea el nivel 1
        blocks.clear();
        crearBloques(nivel);
        
        // Resetea el tamaño y velocidad del paddle (si los cambiaste)
        pad.reset(); // (Necesitarías añadir este método a Paddle)
        
        // Resetea la bola
        ball.setEstaQuieto(true);
        
        // Vuelve al estado de juego
        estadoActual = EstadoJuego.JUGANDO;
    }

    /**
     * Método DISPOSE. Se llama UNA VEZ al cerrar el juego.
     * Libera la memoria de los objetos de LibGDX.
     */
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shape.dispose();
    }
    
    // --- ============================================ ---
    // --- "ENCHUFES" (Getters/Ayudantes) PARA HABILIDADES ---
    // --- ============================================ ---
    
    public void anadirVida() {
        this.vidas++;
    }
    
    public Paddle getPaddle() {
        return this.pad;
    }
    
    public PingBall getBall() {
        return this.ball;
    }
}