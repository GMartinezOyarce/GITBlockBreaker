package Habilidades;

import io.github.some.BlockBreaker.BlockBreakerGame;

public interface Habilidad {
	void aplicar(BlockBreakerGame game);
	
	String getNombre();

}
