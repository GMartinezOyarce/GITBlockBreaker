package GeneracionNiveles;

import io.github.some.BlockBreaker.Block;
import io.github.some.BlockBreaker.Block.BlockType;

public class ArgentinaGenerador extends GeneradorFormaProcedimental {
	
	
	@Override
	protected int getNumeroFilas(int nivelActual) {
	    return 1; 
	}

	@Override
	protected String getTipoForma(int nivel) {
		return "DIAMANTE";
	}

	@Override
	protected float getDensityFactor(int nivel) {
		// TODO Auto-generated method stub
		return 300 + (nivel * 20);
	}

	@Override
	protected BlockType getBlockTypeForPosition(int x, int y, float distanceManhattan) {

    	// 1. NÚCLEO CENTRAL (Radio 0 a 100) -> Bloques de Hielo/Azules
        if (distanceManhattan < 100) {
            return Block.BlockType.DURISIMO; // O DURISIMO, el color que prefieras para el centro
        }
        
        // 2. PRIMER ANILLO (Radio 100 a 200) -> Bloques Rojos
        if (distanceManhattan < 200) {
            if( Math.random() < 0.5) {
            	return Block.BlockType.DURO;
            }
            return Block.BlockType.FUERTE;
            
        }
        
        // 4. BORDE EXTERIOR (El resto) -> Bloques Naranjas
        return Block.BlockType.NORMAL; 
	}

}
