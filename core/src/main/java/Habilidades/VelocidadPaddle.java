package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;
import io.github.some.BlockBreaker.Paddle;

public class VelocidadPaddle implements Habilidad {
	private float factorVelocidad = 1.15f;
	@Override
	public void aplicar(BlockBreakerGame game) {
		Paddle paddle = game.getPaddle();
		
		paddle.aumentarVelocidad(factorVelocidad);
	}

	@Override
	public String getNombre() {
		return "Padd Rapido";
	}
}
