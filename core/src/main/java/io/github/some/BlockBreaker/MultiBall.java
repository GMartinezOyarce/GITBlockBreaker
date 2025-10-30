package io.github.some.BlockBreaker;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;

public class MultiBall extends Item{
	public MultiBall(float x, float y) {
		super(x,y,20,20,Color.YELLOW, true);
	}
	public void applyEffect(BlockBreakerGame game) {
		ArrayList<PingBall> balls=game.getBalls();
		Paddle paddle=game.getPaddle();
		
		int baseX = paddle.getX() +paddle.getWidth() / 2;
		int baseY = paddle.getY() + paddle.getHeight() + 11;
		int cant = balls.size();
		int xSpeed;
		
		for(int i=0; i<cant; i++) {
			xSpeed = (i % 2 == 0) ? 5 : -5;
			PingBall nueva = new PingBall(baseX,baseY,10,xSpeed,5,false);  
			balls.add(nueva);
		}
		desactivar();
	}
}

