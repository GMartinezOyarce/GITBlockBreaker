package io.github.some.BlockBreaker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Proyectil {
		protected int x;
		protected int y;
		protected int size;
		protected int xSpeed;
		protected int ySpeed;
		protected Color color;
	    protected boolean estaQuieto;
	    
	    public Proyectil(int x, int y, int size, int xSpeed, int ySpeed, boolean iniciaQuieto) {
	        this.x = x;
	        this.y = y;
	        this.size = size;
	        this.xSpeed = xSpeed;
	        this.ySpeed = ySpeed;
	        estaQuieto = iniciaQuieto;
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
	    public int getY() {return y;}
	    
	    public int getSize() {
	        return size;
	    }

	    public void setSize(int nuevoTamaño) {
	        this.size = nuevoTamaño;
	    }

	    public int getX() {return x;}
	    
	    
	    public abstract void draw(ShapeRenderer shape);
	    
	    public abstract void update();
	    
	}
