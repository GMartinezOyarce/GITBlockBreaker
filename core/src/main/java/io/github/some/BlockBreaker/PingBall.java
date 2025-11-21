package io.github.some.BlockBreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PingBall {
	private int x;
	private int y;
	private int size; // Este es tu "radio"
	private int xSpeed;
	private int ySpeed;
	private Color color = Color.WHITE;
	private boolean estaQuieto;
	
	public PingBall(int x, int y, int size, int xSpeed, int ySpeed, boolean iniciaQuieto) {
	    this.x = x;
	    this.y = y;
	    this.size = size;
	    this.xSpeed = xSpeed;
	    this.ySpeed = ySpeed;
	    estaQuieto = iniciaQuieto;
	}
	
    // --- Métodos Get/Set (de tu rama 'main') ---
	public int getSize() {
		return this.size;
	}
	public boolean estaQuieto() {
		return estaQuieto;
	}
	public void setEstaQuieto(boolean bb) {
		estaQuieto=bb;
	}
	public void setXY(int x, int y) {
		this.x = x;
	    this.y = y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {return y;}
	public int getX() {return x;}
	
	public void draw(ShapeRenderer shape){
	    shape.setColor(color);
	    shape.circle(x, y, size);
	}
	
	public void setSize(int tamano) {
		this.size = tamano;
	}
	
    /**
     * Método de actualización (MERGEADO)
     * Usa los límites del mundo (800x465) de 'main'
     */
	public void update() {
		if (estaQuieto) return;
	    x += xSpeed;
	    y += ySpeed;
        
	    // Límites del mundo (de 'main')
	    int anchoMundo = 1200;
	    int techoMundo = 800; // Ajustado para estar sobre los bloques y bajo la UI

	    if (x-size < 0 || x+size > anchoMundo) { // Usa anchoMundo
	        xSpeed = -xSpeed;
            x = (x-size < 0) ? size : (anchoMundo - size); // Reposiciona
	    }
	    if (y+size > techoMundo) { // Usa techoMundo
	        ySpeed = -ySpeed;
            y = techoMundo - size; // Reposiciona
	    }
	}
	
    /**
     * Colisión con Paddle (de tu rama 'main')
     * Esta es la lógica de Círculo-vs-Rectángulo que ya funciona.
     */
	public void checkCollision(Paddle paddle) {
		int cercanoX = Math.max(paddle.getX(),Math.min(this.x, paddle.getX() + paddle.getWidth()));
		int cercanoY = Math.max(paddle.getY(), Math.min(this.y, paddle.getY() + paddle.getHeight()));
	
		int distanciaX = this.x - cercanoX;
		int distanciaY = this.y - cercanoY;
    
        // ¡Corregido con '*' en lugar de '+'!
		int distancia = (distanciaX * distanciaX) + (distanciaY * distanciaY);	
	
		if(distancia < this.size * this.size) {
			y = paddle.getY() + paddle.getHeight() + size + 1;
			if(paddle.esPegajoso()) {
				setEstaQuieto(true);
			}
			else {
				this.ySpeed = Math.abs(ySpeed); // Forza hacia arriba
			}
		}
	}
    
	// --- Lógica de Bloques (MERGEADA) ---
    
	/**
	 * Colisión con Bloque (de 'main', que llama a block.hit())
	 */
	public boolean checkCollision(Block block) { // Devuelve boolean como en tu rama
	    if(collidesWith(block)){ // Llama al 'collidesWith' de abajo
	        ySpeed = - ySpeed;
	        block.hit(); // ¡Esta es la nueva lógica!
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Detección de Colisión AABB (de 'main'/'feature')
	 */
	private boolean collidesWith(Block bb) {
	    // (Usa los getters del bloque)
	    boolean intersectaX = (bb.getX() + bb.getWidth() >= x-size) && (bb.getX() <= x+size);
	    boolean intersectaY = (bb.getY() + bb.getHeight() >= y-size) && (bb.getY() <= y+size);		
	    return intersectaX && intersectaY;
	}

	/**
	 * Forzar velocidad (de tu rama 'main')
	 */
	public void forzarVelocidadArriba() {
		this.ySpeed = Math.abs(ySpeed);
        if (this.ySpeed == 0) {
            this.ySpeed = 5; // Asegura que no sea 0
        }
	}
}