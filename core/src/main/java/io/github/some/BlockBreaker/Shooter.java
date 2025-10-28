package io.github.some.BlockBreaker;

import java.util.ArrayList;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.graphics.Color;

public class Shooter extends Item{
	public Shooter(float x, float y) {
		super(x,y,20,20,Color.RED, true);
	}
	
	public void applyEffect(BlockBreakerGame game) {
		final float DURACION = 20f; 
        final float INTERVALO = 1.0f;
        Paddle paddle=game.getPaddle();
        ArrayList<Bala> balas=game.getBalas();

        Timer.Task disparo = new Timer.Task() {
            float tiempoTranscurrido = 0;

            @Override
            public void run() {
                tiempoTranscurrido += INTERVALO;

                if (tiempoTranscurrido >= DURACION) {
                    this.cancel(); 
                    return;
                }

                int balaX = paddle.getX() + paddle.getWidth() / 2;
                int balaY = paddle.getY() + paddle.getHeight() + 5;
                Bala nueva = new Bala(balaX, balaY, 18, 0, 8, false);
                balas.add(nueva);
            }
        };
       
        Timer.schedule(disparo, 0, INTERVALO);

        desactivar();
	}
}
