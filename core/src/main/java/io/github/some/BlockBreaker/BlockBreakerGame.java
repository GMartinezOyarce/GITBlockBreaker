package io.github.some.BlockBreaker;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.concurrent.ThreadLocalRandom;


public class BlockBreakerGame extends ApplicationAdapter {
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private ShapeRenderer shape;
	private PingBall ball;
	private Paddle pad;
	private ArrayList<Block> blocks = new ArrayList<>();
	private int vidas;
	private int puntaje;
	private int nivel;
    
		@Override
		public void create () {	
			camera = new OrthographicCamera();
		    camera.setToOrtho(false, 800, 400);
		    batch = new SpriteBatch();
		    font = new BitmapFont();
		    font.getData().setScale(3, 2);
		    nivel = 1;
		    crearBloques(2+nivel);
			
		    shape = new ShapeRenderer();
		    ball = new PingBall(Gdx.graphics.getWidth()/2-10, 41, 10, 5, 5, true);
		    pad = new Paddle(Gdx.graphics.getWidth()/2-50,40,100,10);
		    vidas = 3;
		    puntaje = 0;    
		}
		public void crearBloques(int filas) {
			blocks.clear();
			int blockWidth = 70;
		    int blockHeight = 26;
		    int y = Gdx.graphics.getHeight();
		    for (int cont = 0; cont<filas; cont++ ) {
		    	y -= blockHeight+10;
		    	for (int x = 5; x < Gdx.graphics.getWidth(); x += blockWidth + 10) {
		    		Block.BlockType tipo = getRandomBlockType();
		            blocks.add(new Block(x, y, blockWidth, blockHeight, tipo));
		        }
		    }
		}

		// selecciona tipo aleatorio con probabilidades ponderadas
		private Block.BlockType getRandomBlockType() {
			// Probabilidades: NORMAL 0.45, FUERTE 0.20, DURO 0.15, DURISIMO 0.10, HIELO 0.10
			double r = ThreadLocalRandom.current().nextDouble();
			if (r < 0.45) return Block.BlockType.NORMAL;
			if (r < 0.65) return Block.BlockType.FUERTE;
			if (r < 0.80) return Block.BlockType.DURO;
			if (r < 0.90) return Block.BlockType.DURISIMO;
			return Block.BlockType.HIELO;
		}
		public void dibujaTextos() {
			//actualizar matrices de la cámara
			camera.update();
			//actualizar 
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			//dibujar textos
			font.draw(batch, "Puntos: " + puntaje, 10, 25);
			font.draw(batch, "Vidas : " + vidas, Gdx.graphics.getWidth()-20, 25);
			batch.end();
		}	
		// doaisndasoidnasdoinas
		int pedro = 2;
		
		@Override
		public void render () {
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 		
	        shape.begin(ShapeRenderer.ShapeType.Filled);
	        pad.draw(shape);
	        // monitorear inicio del juego
	        if (ball.estaQuieto()) {
	        	ball.setXY(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11);
	        	if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) ball.setEstaQuieto(false);
	        }else ball.update();
	        //verificar si se fue la bola x abajo
	        if (ball.getY()<0) {
	        	vidas--;
	        	//nivel = 1;
	        	ball = new PingBall(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11, 10, 5, 5, true);
	        }
	        // verificar game over
	        if (vidas<=0) {
	        	vidas = 3;
	        	nivel = 1;
	        	puntaje = 0;
	        	crearBloques(2+nivel);
	        	//ball = new PingBall(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11, 10, 5, 7, true);	        	
	        }
	        // verificar si el nivel se terminó
	        if (blocks.size()==0) {
	        	nivel++;
	        	crearBloques(2+nivel);
	        	ball = new PingBall(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11, 10, 5, 5, true);
	        }    	
	        //dibujar bloques
	        for (Block b : blocks) {        	
	            b.draw(shape);
	            ball.checkCollision(b);
	        }
	        // actualizar estado de los bloques 
	        for (int i = 0; i < blocks.size(); i++) {
	            Block b = blocks.get(i);
            if (b.isDestroyed()) {
	            	// aplicar efecto si el bloque era de hielo
	            	if (b.getType() == Block.BlockType.HIELO) {
	            		pad.applySlow(2f);
	            	}
	            	puntaje += b.getPoints();
	                blocks.remove(b);
	                i--; //para no saltarse 1 tras eliminar del arraylist
	            }
	        }
	        
	        ball.checkCollision(pad);
	        ball.draw(shape);
	        
	        shape.end();
	        dibujaTextos();
		}
		
		@Override
		public void dispose () {

		}
	}
