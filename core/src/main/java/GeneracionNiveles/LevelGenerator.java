package GeneracionNiveles;

import java.util.ArrayList;

import io.github.some.BlockBreaker.Block;

public abstract class LevelGenerator {
	//metodos abtractos
	protected abstract int getNumeroFilas(int nivelActual);
	
	protected abstract void posicionarBloquesAleatoriamente(int nivel, int filas, int blockWidth, int blockHeight, ArrayList<Block> bloques);
	
	//metodo plantilla
	public final void generadorNivel(int nivel, ArrayList<Block> blocks, int worldWidth) {
		//me aseguro de que no haya ningun bloque
		blocks.clear();
		
	
		
		//tamaño de bloques
		int blockWidth = worldWidth / 40;
		int blockHeight = 20;
		
		int filas = getNumeroFilas(nivel);

		posicionarBloquesAleatoriamente(nivel, filas, blockWidth, blockHeight, blocks);
		
		añadirBloquesEspeciales(nivel, blocks);		
	}
	
	// esto es solo por si se quiere añadir bloques especiales
	protected void añadirBloquesEspeciales(int nivel, ArrayList<Block> bloques) {
		// de por si no hace nada
	}
}
