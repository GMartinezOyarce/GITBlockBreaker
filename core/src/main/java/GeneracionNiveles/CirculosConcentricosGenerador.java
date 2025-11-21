package GeneracionNiveles; 


import io.github.some.BlockBreaker.Block;



public class CirculosConcentricosGenerador extends GeneradorFormaProcedimental {

    @Override
    protected int getNumeroFilas(int nivelActual) {
        // Implementación de ejemplo
        return 5; 
    }

    @Override
    protected String getTipoForma(int nivel) {
        return "CIRCULO"; 
    }

    @Override
    protected float getDensityFactor(int nivel) {
        // El círculo se hace más grande a medida que el nivel sube
        return 0.5f + (nivel * 0.15f); 
    }

    @Override
    protected Block.BlockType getBlockTypeForPosition(int x, int y, float distanceToCenter) {
        
    	// 1. NÚCLEO CENTRAL (Radio 0 a 100) -> Bloques de Hielo/Azules
        if (distanceToCenter < 50) {
            return Block.BlockType.HIELO; // O DURISIMO, el color que prefieras para el centro
        }
        
        // 2. PRIMER ANILLO (Radio 100 a 200) -> Bloques Rojos
        if (distanceToCenter < 150) {
            return Block.BlockType.FUERTE;
        }
        
        // 3. SEGUNDO ANILLO (Radio 200 a 300) -> Bloques Verdes
        if (distanceToCenter < 300) {
            return Block.BlockType.DURO; 
        }
        
        // 4. BORDE EXTERIOR (El resto) -> Bloques Naranjas
        return Block.BlockType.NORMAL; 
    }
}