package io.github.some.BlockBreaker;

// --- Imports de LibGDX (Combinados) ---
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20; // Importado de CP
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer; // Importado de CP

import GeneracionNiveles.LevelGenerator;
import GeneracionNiveles.LevelSelectionStrategy;
// --- Imports de Habilidades (de 'main') ---
import Habilidades.AgrandarPaddle;
import Habilidades.Escudo;
import Habilidades.Habilidad;
import Habilidades.PaddlePegajoso;
import Habilidades.VelocidadPaddle;
import Habilidades.VidaExtra;

// --- Imports de Java (Combinados) ---
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Clase principal del juego (MERGEADA).
 * Contiene la máquina de estados de 'main' y las features de 'CP'.
 */
public class BlockBreakerGame extends ApplicationAdapter {

    // --- 1. VARIABLES DE RENDERIZADO ---
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;

    // --- 2. VARIABLES DE OBJETOS DEL JUEGO (MERGEADAS) ---
    private ArrayList<PingBall> balls = new ArrayList<>(); // de CP
    private Paddle pad;
    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>(); // de CP
    private ArrayList<Bala> balas = new ArrayList<>(); // de CP

    // --- 3. VARIABLES DE ESTADO DEL JUEGO ---
    private int vidas;
    private int puntaje;
    private int nivel;

    // --- 4. MÁQUINA DE ESTADOS (de 'main') ---
    private enum EstadoJuego {
        JUGANDO,
        SELECCIONANDO_HABILIDAD
    }
    private EstadoJuego estadoActual;

    // --- 5. SISTEMA DE HABILIDADES (de 'main') ---
    private ArrayList<Habilidad> poolDeHabilidades;
    private Habilidad opcion1, opcion2, opcion3;
    private boolean escudoActivo = false;
    private int yEscudo = 5;
    private Habilidad habilidadEscudo;
    private Habilidad habilidadPaddlePegajoso;
    
    private LevelSelectionStrategy generationStrategy = new LevelSelectionStrategy();

    @Override
    public void create() {
        // --- Inicializar LibGDX (de 'main') ---
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1200, 800); // Usamos 800x480 de 'main'
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.5f); // Usamos la escala de 'main'
        shape = new ShapeRenderer();

        // --- Inicializar Objetos del Juego (Mergeado) ---
        blocks = new ArrayList<>();
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 20, 100, 10); // Posición de 'main'
        // Añade la primera bola a la lista de 'CP'
        balls.add(new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + 20, 5, 5, 5, true));

        // --- Inicializar Estado del Juego (de 'main') ---
        vidas = 3;
        puntaje = 0;
        nivel = 1;

        // --- Inicializar Sistema de Habilidades (de 'main') ---
        poolDeHabilidades = new ArrayList<>();
        poolDeHabilidades.add(new VidaExtra());
        poolDeHabilidades.add(new AgrandarPaddle());
        poolDeHabilidades.add(new VelocidadPaddle());
        habilidadPaddlePegajoso = new PaddlePegajoso();
        poolDeHabilidades.add(habilidadPaddlePegajoso);
        habilidadEscudo = new Escudo();
        poolDeHabilidades.add(habilidadEscudo);

        // --- Estado Inicial (de 'main') ---
        estadoActual = EstadoJuego.JUGANDO;
        
        // Carga los bloques (usando la lógica de 'main' que era más avanzada)
        crearBloques(nivel);
    }

    @Override
    public void render() {
        // 1. Limpiar la pantalla
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        // 2. Lógica de Update (basada en la máquina de estados de 'main')
        switch (estadoActual) {
            case JUGANDO:
                updateJuego(); 
                break;
            case SELECCIONANDO_HABILIDAD:
                manejarInputSeleccion(); 
                break;
        }

        // 3. Lógica de Dibujado (MERGEADA)
        shape.setProjectionMatrix(camera.combined);
        shape.begin(ShapeType.Filled);
        
        pad.draw(shape);  
        
        // Dibuja todas las bolas (de 'CP')
        for (PingBall ball : balls) {
            ball.draw(shape);
        }
        
        // Dibuja todos los bloques
        for (Block block : blocks) {
            block.draw(shape);
        }
        
        // Dibuja las balas (de 'CP')
        for (Bala b : balas) {
            b.draw(shape);
        }

        // Dibuja los items (de 'CP')
        for (Item it : items) {
            it.draw(shape);
        }

        // Dibuja el escudo (de 'main')
        if(this.escudoActivo) {
        	shape.setColor(Color.CYAN);
        	shape.rect(0, yEscudo, Gdx.graphics.getWidth(), 5);
        }
        
        shape.end();

        // Dibuja la UI (usando la UI de 'main', arriba)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        font.draw(batch, "Vidas: " + vidas, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Puntaje: " + puntaje, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 30);

        // Dibuja el menú de habilidades si es el estado
        if (estadoActual == EstadoJuego.SELECCIONANDO_HABILIDAD) {
            dibujarMenuHabilidades();
        }
        
        batch.end();
    }

    /**
     * Lógica principal del juego (MERGEADA)
     * Contiene la lógica de 'CP' pero controlada por la máquina de estados.
     */
    private void updateJuego() {
        
        // --- Input y Update del Paddle (de 'main' + 'feature') ---
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pad.moveLeft(); 
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pad.moveRight(); 
        }
        pad.update(Gdx.graphics.getDeltaTime()); // Para el efecto hielo

        // --- Lógica de Bolas (de 'CP', adaptada por 'main') ---
        for (PingBall ball : balls) {
            if (ball.estaQuieto()) {
                ball.setXY(pad.getX() + pad.getWidth() / 2, pad.getY() + pad.getHeight() + ball.getSize() + 1);
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    ball.setEstaQuieto(false);
                    ball.forzarVelocidadArriba();
                }
            } else {
                ball.update();
                ball.checkCollision(pad); // Revisa colisión con paddle
            }
        }

        // --- Lógica de Muerte de Bolas (de 'CP', con escudo de 'main') ---
        for(int i=0; i<balls.size();i++) {
            PingBall ball = balls.get(i);
            if (ball.getY() - ball.getSize() < 0) { // Lógica de 'main'
                
                if (escudoActivo) { // Lógica de 'main'
                    ball.setY(yEscudo + ball.getSize() + 1);
                    ball.forzarVelocidadArriba();
                    escudoActivo = false;
                } else {
                    balls.remove(i); // Lógica de 'CP'
                    i--;
                }
            }
        }

        // --- Lógica de Muerte/Game Over (de 'CP' y 'main' combinadas) ---
        if(balls.isEmpty()) {
            items.clear(); // de CP
            balas.clear(); // de CP
            Timer.instance().clear(); // de CP
            vidas--; // de 'main'
            
            if (vidas <= 0) {
                reiniciarJuego(); // de 'main'
            } else {
                // Pierde una vida, resetea la bola
                balls.add(new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + pad.getHeight() + 5, 5, 5, 5, true));
                balls.get(0).setEstaQuieto(true); // de 'main'
            }
        }

        // --- Lógica de Balas (de 'CP') ---
        for (int i = 0; i < balas.size(); i++) {
            Bala b = balas.get(i);
            b.update();
            
            if(!b.getActivo()) {
                balas.remove(i);
                i--;
                continue;
            }
            
            for(Block block : blocks) { 
                b.checkCollision(block);
            }
        }

        // --- Lógica de Colisión de Bloques (MERGEADA) ---
        // 1. Bolas y Balas "avisan" a los bloques
        for (Block b : blocks) {
            for(PingBall ball : balls) { // Bucle de 'CP'
                ball.checkCollision(b); 
            }
        }
        
        // 2. Los bloques se auto-gestionan (lógica de 'feature/blocks-antonia' que está en 'main')
        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            if (b.isDestroyed()) { 
                
                // Aplicar efecto si el bloque era de hielo
                if (b.getType() == Block.BlockType.HIELO) { 
                    pad.applySlow(2f); 
                }
                
                // Lógica de Puntos (de 'main' y 'feature')
                puntaje += b.getPoints(); 
                
                // --- Lógica de Items (de 'CP') ---
                if(Math.random() < 0.20) {
                    double tipo = Math.random();
                    if (tipo < 0.30) {
                        items.add(new GreatBall(b.getX(), b.getY()));
                    } else if (tipo < 0.60) {
                        items.add(new MultiBall(b.getX(), b.getY()));
                    } else if (tipo < 0.90) {
                        items.add(new Shooter(b.getX(), b.getY()));
                    } else {
                        items.add(new ExtraLife(b.getX(), b.getY()));
                    }
                }
                
                blocks.remove(b);
                i--; 
            }
        }

        // --- Lógica de Items (de 'CP') ---
        for(int i=0;i<items.size(); i++) {
            Item it = items.get(i);
            
            if(it.isActivo()) {
                it.bajar(Gdx.graphics.getDeltaTime());
                
                if (colisionaConPaddle(it, pad)) {
                    it.applyEffect(this); // Llama a los "enchufes"
                }
            }
            else {
                items.remove(i);
                i--;
            }
        }

        // --- Comprobar Fin de Nivel (de 'main') ---
        if (blocks.isEmpty()) {
            prepararSeleccionHabilidad(); // ¡Llama al menú de habilidades!
        }
    }

    // --- ======================================================= ---
    // --- MÉTODOS DE LÓGICA DE BLOQUES (de 'main'/'feature') ---
    // --- ======================================================= ---
    
    /*public void crearBloques(int filas) {
		blocks.clear();
		int blockWidth = 70;
	    int blockHeight = 26;
	    int y = Gdx.graphics.getHeight(); // 480
	    
        for (int cont = 0; cont < filas; cont++ ) {
	    	y -= blockHeight + 10; 
	    	for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
	    		Block.BlockType tipo = getRandomBlockType();
	    		blocks.add(new Block(x, y, blockWidth, blockHeight, tipo));
	    	}
	    }
	}*/
    public void crearBloques(int filas) {
    	LevelGenerator  currentGenerator = generationStrategy.selectGenerator(nivel);
    	currentGenerator.generadorNivel(nivel, this.blocks, 1200);
    }

    
	private Block.BlockType getRandomBlockType() {
		double r = ThreadLocalRandom.current().nextDouble();
		if (r < 0.45) return Block.BlockType.NORMAL;
		if (r < 0.65) return Block.BlockType.FUERTE;
		if (r < 0.80) return Block.BlockType.DURO;
		if (r < 0.90) return Block.BlockType.DURISIMO;
		return Block.BlockType.HIELO;
	}

    // --- ======================================================= ---
    // --- MÉTODOS DE HABILIDADES E ITEMS (de 'main' y 'CP') ---
    // --- ======================================================= ---

    private void prepararSeleccionHabilidad() {
        estadoActual = EstadoJuego.SELECCIONANDO_HABILIDAD;
        // Limpia balas, items y para el tiempo
        balas.clear();
        items.clear();
        Timer.instance().clear(); 
        
        // Para todas las bolas
        for (PingBall ball : balls) {
            ball.setEstaQuieto(true); 
        }

        // Filtra habilidades
        ArrayList<Habilidad> poolFiltrado = new ArrayList<>();
        for (Habilidad h : poolDeHabilidades) {
            if (h == habilidadPaddlePegajoso && pad.esPegajoso()) continue;
            if (h == habilidadEscudo && escudoActivo) continue;
            poolFiltrado.add(h);
        }
        
        Collections.shuffle(poolFiltrado);
        opcion1 = (poolFiltrado.size() > 0) ? poolFiltrado.get(0) : null;
        opcion2 = (poolFiltrado.size() > 1) ? poolFiltrado.get(1) : null;
        opcion3 = (poolFiltrado.size() > 2) ? poolFiltrado.get(2) : null;

        this.nivel++;
        crearBloques(nivel);
    }

    private void dibujarMenuHabilidades() {
        // (Tu código de dibujar menú de 'main' está perfecto)
        float centroX = Gdx.graphics.getWidth() / 2;
        float centroY = Gdx.graphics.getHeight() / 2;
        font.draw(batch, "¡NIVEL " + (nivel-1) + " COMPLETADO!", centroX - 130, centroY + 100);
        font.draw(batch, "Elige una mejora:", centroX - 100, centroY + 50);
        if (opcion1 != null) font.draw(batch, "1. " + opcion1.getNombre(), centroX - 80, centroY);
        if (opcion2 != null) font.draw(batch, "2. " + opcion2.getNombre(), centroX - 80, centroY - 40);
        if (opcion3 != null) font.draw(batch, "3. " + opcion3.getNombre(), centroX - 80, centroY - 80);
    }

    private void manejarInputSeleccion() {
        // (Tu código de 'main' está perfecto)
        Habilidad habilidadElegida = null;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) && opcion1 != null) habilidadElegida = opcion1;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2) && opcion2 != null) habilidadElegida = opcion2;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3) && opcion3 != null) habilidadElegida = opcion3;

        if (habilidadElegida != null) {
            habilidadElegida.aplicar(this);
            iniciarSiguienteNivel();
        }
    }

    private void iniciarSiguienteNivel() {
        estadoActual = EstadoJuego.JUGANDO;
        // Asegúrate de que todas las bolas estén quietas
        for(PingBall ball : balls) {
            ball.setEstaQuieto(true);
        }
        // Si no quedan bolas (por si acaso), añade una
        if (balls.isEmpty()) {
            balls.add(new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + 20, 5, 5, 5, true));
        }
    }
    
    private void reiniciarJuego() {
        vidas = 3;
        puntaje = 0;
        nivel = 1;
        
        blocks.clear();
        balls.clear();
        items.clear();
        balas.clear();
        Timer.instance().clear();
        
        crearBloques(nivel); 
        pad.reset(); 
        escudoActivo = false;
        
        balls.add(new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + 20, 5, 5, 5, true));
        estadoActual = EstadoJuego.JUGANDO;
    }

    // Deteccion de colision rectangulo vs paleta (de 'CP')
    private boolean colisionaConPaddle(Item it, Paddle pad) {
        boolean intersectaX = (pad.getX() + pad.getWidth() >= it.getX()) &&
                              (pad.getX() <= it.getX() + it.getAncho());
        boolean intersectaY = (pad.getY() + pad.getHeight() >= it.getY()) &&
                              (pad.getY() <= it.getY() + it.getAlto());
        return intersectaX && intersectaY;
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        shape.dispose();
    }
    
    // --- ============================================ ---
    // --- "ENCHUFES" (Getters/Ayudantes) - COMBINADOS ---
    // --- ============================================ ---
    
    // de 'main'
    public void anadirVida() {
        this.vidas++;
    }
    
    // de 'main'
    public Paddle getPaddle() {
        return this.pad;
    }
    
    /**
     * ¡MODIFICADO! 'getBall' de 'main' ahora devuelve la primera bola.
     * Las habilidades de 'main' (como PaddlePegajoso) solo afectarán a la primera bola.
     * Para que funcione con todas, esas habilidades deben ser refactorizadas.
     */
    public PingBall getBall() {
        if (balls.isEmpty()) {
            return null; // No hay bolas
        }
        return balls.get(0); // Devuelve la primera bola de la lista
    }
    
    // de 'main'
    public void activarEscudo() {
    	this.escudoActivo = true;
    }

    // de 'main'
    public boolean isEscudoActivo() {
        return this.escudoActivo;
    }

    // --- "Enchufes" de 'CP' para los Items ---
    
    public ArrayList<PingBall> getBalls(){
        return this.balls;
    }
    public ArrayList<Bala> getBalas(){
        return this.balas;
    }
    public void upLife(){ // 'CP' la llama 'upLife', 'main' la llama 'anadirVida'. Ambas funcionan.
        this.vidas++;
    }
}