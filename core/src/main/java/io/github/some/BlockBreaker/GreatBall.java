package io.github.some.BlockBreaker;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;

public class GreatBall extends Item implements EstrategiaItem{
	public GreatBall(float x, float y) {
        super(x, y, 20, 20, Color.PURPLE, true, null);
        this.estrategia = this;
    }
	@Override
	public void aplicar(BlockBreakerGame game) {
		ArrayList<PingBall> balls=game.getBalls();
		
	    for(PingBall ball : balls) {
	    	if(ball.getSize() >= 15) {
		    	continue;
		    }
		    final int tamañoOriginal = ball.getSize();
		    int nuevoTamaño = ball.getSize() * 2;
		    
		    int centroX= ball.getX();
		    int centroY= ball.getY();
		    
		    ball.setSize(nuevoTamaño);
		    ball.setXY(centroX, centroY);
		    
		    Timer.schedule(new Timer.Task(){
		    	public void run () {
		    		ball.setSize(tamañoOriginal);
		    	}
		    }, 20);
		    
		   
	    }
	}
}