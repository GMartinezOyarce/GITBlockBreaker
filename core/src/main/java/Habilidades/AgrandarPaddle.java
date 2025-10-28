package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;
import io.github.some.BlockBreaker.Paddle;

public class AgrandarPaddle implements Habilidad{
	private float factorCrecimiento = 1.2f;
	
	@Override
	public void aplicar(BlockBreakerGame game) {
		Paddle paddle = game.getPaddle();
		
		paddle.agrandar(factorCrecimiento);
		
	}

	@Override
	public String getNombre() {
		return "Paddle gigante";
	}
}
