package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;

public class VidaExtra implements Habilidad {
	@Override
	public void aplicar(BlockBreakerGame game) {
		game.anadirVida();
	}
	@Override
	public String getNombre() {
		return "Vida Extra (+1)";
	}
	
}
