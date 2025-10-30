package io.github.some.BlockBreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils; // Import necesario de la rama feature

public class Paddle {
    private int x = 20;
    private int y = 20;
    private int width = 100;
    private int height = 10;
    

    private float velocidad;
    private int originalWidth; // Corregí el error tipográfico "Witdh"
    private float originalVelocidad;
    private boolean esPegajoso = false; // Inicializado
    
    
    private long slowUntilMs = 0L; // Para el efecto de hielo
    
    public Paddle(int x, int y, int ancho, int alto) {
    	this.x = x;
    	this.y= y;
    	this.width = ancho;  // Usa la variable de clase
    	this.height = alto; // Usa la variable de clase
    	
    	this.velocidad = 10.0f;
    	this.originalWidth = ancho; // Corregido
    	this.originalVelocidad = this.velocidad;
    }
    

    public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public boolean esPegajoso() {return this.esPegajoso;}

    // --- Lógica de Hielo 
    // Reduce la velocidad del paddle temporalmente.
    public void applySlow(float seconds){
    	// Reduce la velocidad a la mitad (o un mínimo de 2)
    	velocidad = Math.max(2, originalVelocidad / 2);
    	slowUntilMs = TimeUtils.millis() + (long)(seconds * 1000);
    }
    
  
    public void update(float delta) {
        // Restaurar velocidad si ya pasó el efecto de lentitud
        if (slowUntilMs != 0 && TimeUtils.millis() > slowUntilMs) {
            velocidad = originalVelocidad; // Restaura a la velocidad base
            slowUntilMs = 0L;
        }
    }

    /**
     * Método de dibujado (MERGEADO)
     */
	public void draw(ShapeRenderer shape){
        // Lógica de color de 'feature': cambia si está ralentizado
        shape.setColor(velocidad < originalVelocidad ? new Color(0.6f, 0.8f, 1f, 1f) : Color.BLUE);
        
        // Lógica de dibujado de 'main'
        shape.rect(x, y, width, height);
		}
    // --- Lógica de Movimiento (de tu rama 'main') ---
    // (Esta es llamada por BlockBreakerGame)
	public void moveLeft() {
		x -= velocidad; // Usa la variable 'velocidad' que puede ser alterada
		if(x < 0) x = 0;
	}
	public void moveRight() {
		x += velocidad;
		// Asumiendo que el ancho de tu mundo es 800
		if(x + this.width > 800) { 
			x = 800 - width;
		}
	}
    
    // --- Lógica de Habilidades (de tu rama 'main') ---
	public void reset() {
		this.velocidad = this.originalVelocidad;
		this.width = this.originalWidth;
		this.desactivarPegajoso();
        this.slowUntilMs = 0L; // Resetea también el hielo
	}
	
    public void agrandar(float factor) {
    	this.width = (int)(this.width * factor);
    }
    
    public void aumentarVelocidad(float factor) {
    	this.velocidad *= factor;
        this.originalVelocidad = this.velocidad; // Actualiza la base
    }
    
    public void activarPegajoso() {
    	this.esPegajoso = true;
    }
    
    public void desactivarPegajoso(){
    	this.esPegajoso = false;
    }
}