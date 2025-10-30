package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;
import io.github.some.BlockBreaker.Paddle;

public class PaddlePegajoso implements Habilidad{

	@Override
	public void aplicar(BlockBreakerGame game) {
		Paddle paddle = game.getPaddle();
		paddle.activarPegajoso();
		
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Paddle Pegajoso";
	}

}
