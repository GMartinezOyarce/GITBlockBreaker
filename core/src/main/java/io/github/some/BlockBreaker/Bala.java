package io.github.some.BlockBreaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Bala extends Proyectil{
	private boolean activo = true;
	
	public Bala(int x, int y, int size, int xSpeed, int ySpeed, boolean iniciaQuieto) {
		super(x, y, size, xSpeed, ySpeed, iniciaQuieto);
		this.color = Color.RED;
	}
	public boolean getActivo() {
		return activo;
	}
	@Override
    public void draw(ShapeRenderer shape){
		shape.setColor(color);
	    float half = size / 2f;
	    shape.triangle(x, y + half,x - half, y,x + half, y);
	    shape.triangle(x, y - half,x - half, y,x + half, y);
    }
    @Override
    public void update() {
    	if (estaQuieto) return;
        y += ySpeed;
        if (y - size > Gdx.graphics.getHeight()) {
            activo=false;
        }
    }
    public void checkCollision(Block block) {
        if(collidesWith(block)){
            block.destroyed = true;
            activo=false;
        }
    }
    protected boolean collidesWith(Block bb) {
    	boolean intersectaX = (bb.x + bb.width >= x-size) && (bb.x <= x+size);
        boolean intersectaY = (bb.y + bb.height >= y-size) && (bb.y <= y+size);		
    	return intersectaX && intersectaY;
    }
}
