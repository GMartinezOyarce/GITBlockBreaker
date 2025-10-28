package io.github.some.BlockBreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {
    private int x = 20;
    private int y = 20;
    private int width = 100;
    private int height = 10;
    private float velocidad;
    
    private int originalWitdh;
    private float originalVelocidad;
    
    private boolean esPegajoso;
    
    public Paddle(int x, int y, int ancho, int alto) {
    	this.x = x;
    	this.y= y;
    	width = ancho;
    	height = alto;
    	
    	this.velocidad = 10.0f;
    	this.originalWitdh = ancho;
    	this.originalVelocidad = this.velocidad;
    }
     
    public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public boolean esPegajoso() {return this.esPegajoso;}
	

	public void draw(ShapeRenderer shape){    	
        shape.setColor(Color.BLUE);
        shape.rect(x, y, width, height);
    }
	
	public void moveLeft() {
		x -=velocidad;
		if(x < 0) x = 0;
	}
	public void moveRight() {
		x+= velocidad;
		if(x + this.width >Gdx.graphics.getWidth()) {
			x = Gdx.graphics.getWidth() - width;
		}
	}
    
	public void reset() {
		this.velocidad = this.originalVelocidad;
		this.width = this.originalWitdh;
		this.desactivarPegajoso();
	}
	
    public void agrandar(float factor) {
    	this.width = (int)(this.width * factor);
    }
    
    public void aumentarVelocidad(float factor) {
    	this.velocidad *= factor; 
    }
    public void activarPegajoso() {
    	this.esPegajoso = true;
    }
    public void desactivarPegajoso(){
    	this.esPegajoso= false;
    }
    
    
}
