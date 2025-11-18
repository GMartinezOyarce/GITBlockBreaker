package io.github.some.BlockBreaker;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import io.github.some.BlockBreaker.GameSession;

public class Block {
    private int x,y,width,height;
    private Color cc;
    private boolean destroyed;
    private BlockType type;// tipo de bloque (NORMAL, FUERTE, DURO, DURISIMO, HIELO)
    private int hp; //vidas o golpes restantes
    private int maxHp; //guarda cuantas vidas tenía al principio 
    private int points; //puntos que da cuando se destruye

    //constructor con tipo
    public Block(int x, int y, int width, int height, BlockType type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        destroyed = false;
        this.type = (type == null ? BlockType.NORMAL : type); //evita error si no se pasa tipo

        //hp y puntos según el tipo
        switch (this.type) {
            case NORMAL:
                maxHp = hp = 1;
                points = 50;
                break;
            case FUERTE:
                maxHp = hp = 2;
                points = 100;
                break;
            case DURO:
                maxHp = hp = 3;
                points = 150;
                break;
            case DURISIMO:
                maxHp = hp = 4;
                points = 200;
                break;
            case HIELO:
                maxHp = hp = 2;
                points = 120;
                break;
        }

        //color según el tipo
        switch (this.type) {
            case NORMAL:
                cc = new Color(0.3f, 0.7f, 1f, 1f);// azul
                break;
            case FUERTE:
                cc = new Color(0.2f, 1f, 0.4f, 1f);// verde
                break;
            case DURO:
                cc = new Color(1f, 0.7f, 0.2f, 1f); // naranjo
                break;
            case DURISIMO:
                cc = new Color(1f, 0.2f, 0.2f, 1f);// rojo
                break;
            case HIELO:
                cc = new Color(0.75f, 0.9f, 1.0f, 1f);// celeste hielo
                break;
        }
    }

    //constructor sin tipo (por defecto NORMAL)
    public Block(int x, int y, int width, int height) {
        this(x, y, width, height, BlockType.NORMAL);
    }

    //getters
    public int getX(){ 
    	return x; 
    }
    public int getY(){ 
    	return y; 
    }
    public int getWidth(){ 
    	return width; 
    }
    public int getHeight(){ 
    	return height; 
    }
    public boolean isDestroyed(){ 
    	return destroyed; 
    }
    public int getPoints() {
        return points;
    }
    public BlockType getType(){
        return type;
    }

    //dibujar rectangulo (bloque)
    public void draw(ShapeRenderer shape){
    	if (destroyed) {
    		return; //no se dibuja, ya que esta destruido
    	}
    	//si el bloque tiene menos hp, se oscurece.
    	float factor = Math.max(0.3f, (float)hp / (float)maxHp);
        shape.setColor(cc.r * factor, cc.g * factor, cc.b * factor, 1f);
        shape.rect(x, y, width, height);
    }

    //romper el bloque
    public boolean hit(){
    	if (destroyed) {
    		return false;
    	}
        
        hp--;//le quito un golpe

        if (hp <= 0) {
            destroyed = true;
            GameSession.getInstance().addScore(points);// sumamos los puntos de este bloque al puntaje total de la partida
            return true; //se rompió
        }
        return false; //sigue vivo (solo se dañó)
    }
    
    public Rectangle getBounds(){
        return new Rectangle(x, y, width, height); //crea un bloque (rectangulo)
    }

    //para saber si la pelota toco el bloque
    public boolean intersectsCircle(float cx, float cy, float r){
        float nearestX = Math.max(x, Math.min(cx, x + width));
        float nearestY = Math.max(y, Math.min(cy, y + height));
        float dx = cx - nearestX;
        float dy = cy - nearestY;
        return dx*dx + dy*dy <= r*r;
    }

    //etiquetas para los bloques
    public enum BlockType {
        NORMAL,//1 golpe
        FUERTE,//2 golpes
        DURO, //3 golpes
        DURISIMO, //4 golpes
        HIELO //2 golpes y aplica lentitud
    }
}
