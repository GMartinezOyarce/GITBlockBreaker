package io.github.some.BlockBreaker;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public abstract class Item {
	private float x;
	private float y;
	private float ancho;
	private float alto;
	private Color color;
	private boolean activo;
	
	public Item(float x, float y, float ancho, float alto, Color color, boolean activo) {
		this.x=x;
		this.y=y;
		this.ancho=ancho;
		this.alto=alto;
		this.color=color;
		this.activo=activo;
	}
    public void bajar(float delta) {
        y -= 100 * delta; 
    }

    public void draw(ShapeRenderer shape) {
        shape.setColor(color);
        shape.rect(x, y, ancho, alto);
    }

    public abstract void applyEffect(BlockBreakerGame game);
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getAncho() { return ancho; }
    public float getAlto() { return alto; }
    public boolean isActivo() { return activo; }
    public void desactivar() { activo = false; }
}
