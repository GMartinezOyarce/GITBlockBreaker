package io.github.some.BlockBreaker;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class PingBall {
<<<<<<< Updated upstream
	    private int x;
	    private int y;
	    private int size;
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
	    
	    public void draw(ShapeRenderer shape){
	        shape.setColor(color);
	        shape.circle(x, y, size);
	    }
	    
	    public void update() {
	    	if (estaQuieto) return;
	        x += xSpeed;
	        y += ySpeed;
	        if (x-size < 0 || x+size > Gdx.graphics.getWidth()) {
	            xSpeed = -xSpeed;
	        }
	        if (y+size > Gdx.graphics.getHeight()) {
	            ySpeed = -ySpeed;
	        }
	    }
	    
	    
	    public void checkCollision(Paddle paddle) {
	    	int cercanoX = Math.max(paddle.getX(),Math.min(this.x, paddle.getX() + paddle.getWidth()));
	    	int cercanoY = Math.max(paddle.getY(), Math.min(this.y, paddle.getY() + paddle.getHeight()));
	    
	    	int distanciaX = this.x - cercanoX;
	    	int distanciaY = this.y - cercanoY;
	    
	    	int distancia = (distanciaX * distanciaX) + (distanciaY * distanciaY);
	    
	    	if(distancia < this.size * this.size) {
	    		y = paddle.getY() + paddle.getHeight() + size + 1;
	    		if(paddle.esPegajoso()) {
	    			setEstaQuieto(true);
	    		}
	    		else {
	    			this.ySpeed = Math.abs(ySpeed);
	        
	    		}
	    	}
	    }
	    public boolean checkCollision(Block block) {
	        if(collidesWith(block)){
	            ySpeed = - ySpeed;
	            block.destroyed = true;
	            return true;
	        }
	        return false;
	    }
	    private boolean collidesWith(Block bb) {
=======
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
>>>>>>> Stashed changes

	    	boolean intersectaX = (bb.x + bb.width >= x-size) && (bb.x <= x+size);
	        boolean intersectaY = (bb.y + bb.height >= y-size) && (bb.y <= y+size);		
	    	return intersectaX && intersectaY;
	    }
	    public void forzarVelocidadArriba() {
	    	this.ySpeed = Math.abs(ySpeed);
	    			
	    }

	    
	}
