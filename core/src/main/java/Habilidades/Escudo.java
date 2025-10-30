package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;

public class Escudo implements Habilidad{

	@Override
	public void aplicar(BlockBreakerGame game) {
		game.activarEscudo();
	}

	@Override
	public String getNombre() {
		return "Escudo Protector";
	}

}
