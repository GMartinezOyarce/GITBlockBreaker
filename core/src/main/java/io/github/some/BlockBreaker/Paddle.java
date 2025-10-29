package io.github.some.BlockBreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Paddle {
    private int x = 20;
    private int y = 20;
    private int width = 100;
    private int height = 10;
    private int baseSpeed = 8;
    private int currentSpeed = 8;
    private long slowUntilMs = 0L;
    
    public Paddle(int x, int y, int ancho, int alto) {
    	this.x = x;
    	this.y= y;
    	width = ancho;
    	height = alto;
    }
     
    public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
    
    public void applySlow(float seconds){
    	// reduce speed a la mitad temporalmente
    	currentSpeed = Math.max(2, baseSpeed / 2);
    	slowUntilMs = TimeUtils.millis() + (long)(seconds * 1000);
    }

	public void draw(ShapeRenderer shape){
        // restaurar velocidad si ya pasó el efecto de lentitud
        if (slowUntilMs != 0 && TimeUtils.millis() > slowUntilMs) {
        	currentSpeed = baseSpeed;
        	slowUntilMs = 0L;
        }

        // color cambia levemente si está ralentizado
        shape.setColor(currentSpeed < baseSpeed ? new Color(0.6f, 0.8f, 1f, 1f) : Color.BLUE);
        int x2 = x; //= Gdx.input.getX();
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x2 = x - currentSpeed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x2 = x + currentSpeed; 
       // y = Gdx.graphics.getHeight() - Gdx.input.getY(); 
        if (x2 > 0 && x2+width < Gdx.graphics.getWidth()) {
            x = x2;
        }
        shape.rect(x, y, width, height);
    }
    
    
}
