package io.github.some.BlockBreaker;

// --- Imports de LibGDX (de ambas ramas) ---
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

//imports de Habilidades
import Habilidades.AgrandarPaddle;
import Habilidades.Escudo;
import Habilidades.Habilidad;
import Habilidades.PaddlePegajoso;
import Habilidades.VelocidadPaddle;
import Habilidades.VidaExtra;


import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class BlockBreakerGame extends ApplicationAdapter {

    // --- 1. VARIABLES DE RENDERIZADO (de 'main') ---
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private ShapeRenderer shape;

    // --- 2. VARIABLES DE OBJETOS DEL JUEGO (de 'main') ---
    private PingBall ball;
    private Paddle pad;
    private ArrayList<Block> blocks; 

    // --- 3. VARIABLES DE ESTADO DEL JUEGO (de 'main') ---
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
    private Habilidad opcion1;
    private Habilidad opcion2;
    private Habilidad opcion3;

    // Variables para habilidades
    private boolean escudoActivo = false;
    private int yEscudo = 5; 

    // Referencias para el filtro de habilidades
    private Habilidad habilidadEscudo;
    private Habilidad habilidadPaddlePegajoso;
    // (Añade aquí otras referencias si las necesitas)

    @Override
    public void create() {
        // --- Inicializar LibGDX (de 'main') ---
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480); // Usamos el tamaño de 800x480 de 'main'
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.5f); 
        shape = new ShapeRenderer();

        // --- Inicializar Objetos del Juego (de 'main') ---
        blocks = new ArrayList<>();
        pad = new Paddle(Gdx.graphics.getWidth() / 2 - 50, 20, 100, 10);
        ball = new PingBall(pad.getX() + pad.getWidth() / 2, pad.getY() + 20, 5, 5, 5, true); // Usando 'size' 5

        // --- Inicializar Estado del Juego (de 'main') ---
        vidas = 3;
        puntaje = 0;
        nivel = 1;

        // --- Inicializar Sistema de Habilidades (de 'main', con filtro) ---
        poolDeHabilidades = new ArrayList<>();
        poolDeHabilidades.add(new VidaExtra());
        poolDeHabilidades.add(new AgrandarPaddle());
        poolDeHabilidades.add(new VelocidadPaddle());
        
        // Guarda referencias para el filtro
        habilidadPaddlePegajoso = new PaddlePegajoso();
        poolDeHabilidades.add(habilidadPaddlePegajoso);
        
        habilidadEscudo = new Escudo();
        poolDeHabilidades.add(habilidadEscudo);
        // (Añade aquí el resto de habilidades)

        // --- Estado Inicial (de 'main') ---
        estadoActual = EstadoJuego.JUGANDO;
        
        // Carga los bloques del primer nivel (usando la lógica de 'feature')
        crearBloques(2 + nivel); // Llama al método de 'feature/blocks-antonia'
    }

    @Override
    public void render() {
        // 1. Limpiar la pantalla
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        // 2. Lógica de Update (de 'main')
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
        
        ball.draw(shape); 
        pad.draw(shape);  
        
        for (Block block : blocks) {
            block.draw(shape);
        }
        
        // Dibuja el escudo (de 'main')
        if(this.escudoActivo) {
        	shape.setColor(Color.CYAN);
        	shape.rect(0, yEscudo, Gdx.graphics.getWidth(), 5);
        }
        
        shape.end();

        // Dibuja la UI (de 'main')
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        font.draw(batch, "Vidas: " + vidas, 10, 470);
        font.draw(batch, "Puntaje: " + puntaje, Gdx.graphics.getWidth() - 150, 470);

        if (estadoActual == EstadoJuego.SELECCIONANDO_HABILIDAD) {
            dibujarMenuHabilidades();
        }
        
        batch.end();
    }

    /**
     * Lógica principal del juego (MERGEADA)
     */
    private void updateJuego() {
        
        // --- Input del Paddle (de 'main') ---
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pad.moveLeft(); 
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pad.moveRight(); 
        }

        // --- Update del Paddle (para efecto hielo) ---
        pad.update(Gdx.graphics.getDeltaTime());

        // --- Iniciar la bola (de 'main') ---
        if (ball.estaQuieto() && Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            ball.setEstaQuieto(false);
            ball.forzarVelocidadArriba();
        }
        
        if(ball.estaQuieto()) {
            // Usa getSize() para el posicionamiento correcto
            ball.setXY(pad.getX() + pad.getWidth() / 2, pad.getY() + pad.getHeight() + ball.getSize() + 1);
        } else {
            ball.update();
            ball.checkCollision(pad); // Revisa colisión con paddle
        }
        
        
        // 1. La bola "avisa" a los bloques que chocó
        for (Block b : blocks) {       
            ball.checkCollision(b); // La bola solo "revisa", no destruye
        }
        
        // 2. Los bloques se auto-gestionan (se destruyen, dan puntos, etc.)
        for (int i = 0; i < blocks.size(); i++) {
            Block b = blocks.get(i);
            if (b.isDestroyed()) { // (Tu Block.java debe tener este método)
                
                // Aplicar efecto si el bloque era de hielo
                if (b.getType() == Block.BlockType.HIELO) { // (Tu Block.java debe tener getType)
                    pad.applySlow(2f); // (Tu Paddle.java ya tiene applySlow)
                }
                
                puntaje += b.getPoints(); // (Tu Block.java debe tener getPoints)
                blocks.remove(b);
                i--; 
            }
        }
        // --- Fin de la lógica transplantada ---

        // --- Comprobar Fin de Nivel (de 'main') ---
        if (blocks.isEmpty()) {
            prepararSeleccionHabilidad();
        }
        
        // --- Comprobar Muerte (de 'main', con lógica de escudo) ---
        if (ball.getY() - ball.getSize() < 0) { // Usa getSize()
        	
        	if(this.escudoActivo) {
        		ball.setY(yEscudo + ball.getSize() + 1);
        		ball.forzarVelocidadArriba();
        		escudoActivo = false;
                // (Opcional: añadir el escudo de vuelta al pool disponible)
        	}
        	else {
        		vidas--;
                if (vidas <= 0) {
                    reiniciarJuego(); 
                } else {
                    ball.setEstaQuieto(true);
                }
        	}
        }
    }

    // --- ======================================================= ---
    // --- MÉTODOS DE LÓGICA DE BLOQUES (de 'feature/blocks-antonia') ---
    // --- ======================================================= ---
    
    /**
     * Crea los bloques (de 'feature/blocks-antonia')
     */
    public void crearBloques(int filas) {
		blocks.clear();
		int blockWidth = 70;
	    int blockHeight = 26;
	    int y = Gdx.graphics.getHeight(); // 480
	    
        for (int cont = 0; cont < filas; cont++ ) {
	    	y -= blockHeight + 10; // La primera fila empieza en y=444
	    	for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
	    		Block.BlockType tipo = getRandomBlockType();
	    		blocks.add(new Block(x, y, blockWidth, blockHeight, tipo));
	    	}
	    }
	}

    /**
     * Selecciona tipo aleatorio con probabilidades ponderadas
     * (de 'feature/blocks-antonia')
     */
	private Block.BlockType getRandomBlockType() {
		// Probabilidades: NORMAL 0.45, FUERTE 0.20, DURO 0.15, DURISIMO 0.10, HIELO 0.10
		double r = ThreadLocalRandom.current().nextDouble();
		if (r < 0.45) return Block.BlockType.NORMAL;
		if (r < 0.65) return Block.BlockType.FUERTE;
		if (r < 0.80) return Block.BlockType.DURO;
		if (r < 0.90) return Block.BlockType.DURISIMO;
		return Block.BlockType.HIELO;
	}

    // --- ============================================ ---
    // --- MÉTODOS DEL SISTEMA DE HABILIDADES  ---
    // --- ============================================ ---

    private void prepararSeleccionHabilidad() {
        estadoActual = EstadoJuego.SELECCIONANDO_HABILIDAD;
        ball.setEstaQuieto(true); 

        // --- Lógica de Filtro (de 'main') ---
        ArrayList<Habilidad> poolFiltrado = new ArrayList<>();
        for (Habilidad h : poolDeHabilidades) { // Usa el pool maestro
            
            // Regla 1: Paddle Pegajoso
            if (h == habilidadPaddlePegajoso && pad.esPegajoso()) {
                continue; // Sáltatelo, ya está activo
            }
            
            // Regla 2: Escudo
            if (h == habilidadEscudo && escudoActivo) {
                continue; // Sáltatelo, ya está activo
            }
            
            poolFiltrado.add(h);
        }
        
        Collections.shuffle(poolFiltrado); // Baraja la lista filtrada
        
        opcion1 = (poolFiltrado.size() > 0) ? poolFiltrado.get(0) : null;
        opcion2 = (poolFiltrado.size() > 1) ? poolFiltrado.get(1) : null;
        opcion3 = (poolFiltrado.size() > 2) ? poolFiltrado.get(2) : null;

        this.nivel++;
        crearBloques(2 + nivel); // Usa la lógica de 'feature'
    }

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
            habilidadElegida.aplicar(this);
            iniciarSiguienteNivel();
        }
    }

    private void iniciarSiguienteNivel() {
        estadoActual = EstadoJuego.JUGANDO;
        ball.setEstaQuieto(true); 
    }
    
    private void reiniciarJuego() {
        vidas = 3;
        puntaje = 0;
        nivel = 1;
        
        blocks.clear();
        crearBloques(2 + nivel); // Usa la lógica de 'feature'
        
        pad.reset(); // Resetea paddle (tamaño, velocidad, pegajoso, hielo)
        
        escudoActivo = false;
        // (Opcional: resetear multiplicador de puntos, etc.)
        
        ball.setEstaQuieto(true);
        estadoActual = EstadoJuego.JUGANDO;
    }

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
    
    public void activarEscudo() {
    	this.escudoActivo = true;
    }

    // Opcional: Getter para el filtro de habilidades si lo mueves
    public boolean isEscudoActivo() {
        return this.escudoActivo;
    }
}