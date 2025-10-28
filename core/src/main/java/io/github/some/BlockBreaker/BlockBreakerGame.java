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

//hola 
public class BlockBreakerGame extends ApplicationAdapter {
    private OrthographicCamera camera;
	private SpriteBatch batch;	   
	private BitmapFont font;
	private ShapeRenderer shape;
	private ArrayList<PingBall> balls = new ArrayList<>();
	private Paddle pad;
	private ArrayList<Block> blocks = new ArrayList<>();
	private ArrayList<Item> items = new ArrayList<>();
	private ArrayList<Bala> balas = new ArrayList<>();
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
		    balls.add(new PingBall(Gdx.graphics.getWidth()/2-10, 41, 10, 5, 5, true));
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
		            blocks.add(new Block(x, y, blockWidth, blockHeight));
		        }
		    }
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
	        for (PingBall ball : balls) {
	        	if (ball.estaQuieto()) {
	        		ball.setXY(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11);
	        		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) ball.setEstaQuieto(false);
	        	}else ball.update();
	        }
	        // verificar si se fue la bola x abajo
	        for(int i=0; i<balls.size();i++) {
	        	PingBall ball = balls.get(i);
	        	if (ball.getY()<0) {
	        		balls.remove(i);
	        		i--;
	        	}
	        }
	        // verificar si se perdio una vida
	        if(balls.isEmpty()) {
	        	items.clear();
	        	com.badlogic.gdx.utils.Timer.instance().clear();
	        	vidas--;
	        	
	        	// verificar game over
	        	if (vidas<=0) {
	        		vidas = 3;
	        		nivel = 1;
	        		puntaje = 0;
	        		crearBloques(2+nivel);
	        		       	
	        	}
	        	
	        	balls.add(new PingBall(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11, 10, 5, 5, true));
	        }
	        // verificar si el nivel se terminó
	        if (blocks.size()==0) {
	        	items.clear();
	        	com.badlogic.gdx.utils.Timer.instance().clear();
	        	nivel++;
	        	crearBloques(2+nivel);
	        	balls.clear();
	        	balls.add(new PingBall(pad.getX()+pad.getWidth()/2-5, pad.getY()+pad.getHeight()+11, 10, 5, 5, true));
	        }
	        // dibujar balas
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
	        	
	        	b.draw(shape);
	        }
	        //dibujar bloques
	        for (Block b : blocks) {        	
	            b.draw(shape);
	            for(PingBall ball : balls) {
	            	ball.checkCollision(b);
	            }
	        }
	        // actualizar estado de los bloques 
	        for (int i = 0; i < blocks.size(); i++) {
	            Block b = blocks.get(i);
	            
	            if (b.destroyed) {
	            	puntaje++;
	            	
	            	// Lanzar Items
		            if(Math.random() < 0.20) {
		            	double tipo = Math.random();
		            	if (tipo < 0.30) {
		                    items.add(new GreatBall(b.x, b.y));
		                } else if (tipo < 0.60) {
		                    items.add(new MultiBall(b.x, b.y));
		                } else if (tipo < 0.90) {
		                    items.add(new Shooter(b.x, b.y));
		                } else {
		                    items.add(new ExtraLife(b.x, b.y));
		                }
		            	
		            }
	            	
	                blocks.remove(b);
	                i--; //para no saltarse 1 tras eliminar del arraylist
	            }
	        }
	        for (PingBall ball : balls) {  
	        	ball.checkCollision(pad);
	        	ball.draw(shape);
	        }
	        // mover, dibujar y activar items
	        for(int i=0;i<items.size(); i++) {
	        	Item it = items.get(i);
	        	
	        	if(it.isActivo()) {
	        		it.bajar(Gdx.graphics.getDeltaTime());
	        		it.draw(shape);
	        		
	        		//si paleta recoge

	        		if (colisionaConPaddle(it, pad)) {
	        		    it.applyEffect(this);
	        		}
	        		
	        	}
	        	else {
	        		items.remove(i);
	        		i--;
	        	}
	        }
	        
	        shape.end();
	        dibujaTextos();
		}
		
		// Deteccion de colision rectangulo vs paleta
		private boolean colisionaConPaddle(Item it, Paddle pad) {
		    boolean intersectaX = (pad.getX() + pad.getWidth() >= it.getX()) &&
		                          (pad.getX() <= it.getX() + it.getAncho());
		    boolean intersectaY = (pad.getY() + pad.getHeight() >= it.getY()) &&
		                          (pad.getY() <= it.getY() + it.getAlto());
		    return intersectaX && intersectaY;
		}
		
		@Override
		public void dispose () {

		}
		
		//ENCHUFES
		
		public ArrayList<PingBall> getBalls(){
			return this.balls;
		}
		public Paddle getPaddle(){
			return this.pad;
		}
		public ArrayList<Bala> getBalas(){
			return this.balas;
		}
		public void upLife(){
			this.vidas++;
		}
	}
