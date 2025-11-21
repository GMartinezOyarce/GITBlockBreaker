package GeneracionNiveles; // Tu paquete

import io.github.some.BlockBreaker.Block;
import java.util.ArrayList;

public class CorazonGenerador extends GeneradorFormaProcedimental {

    @Override
    protected int getNumeroFilas(int nivelActual) {
        return 1; // No usado
    }

    @Override
    protected String getTipoForma(int nivel) {
        return "CORAZON"; // Esta es la clave que usaremos en la fórmula
    }

    @Override
    protected float getDensityFactor(int nivel) {
        // El corazón necesita un factor de escala. 120 es un buen tamaño base.
        return 120 + (nivel * 10); 
    }

    @Override
    protected Block.BlockType getBlockTypeForPosition(int x, int y, float distanceToCenter) {
        // Dificultad basada en la altura (Y) para variar
        if (y > 500) {
            return Block.BlockType.NORMAL; // Parte superior (lóbulos) suave
        } else if (y > 350) {
            return Block.BlockType.FUERTE; // Centro medio
        } else {
            return Block.BlockType.DURISIMO; // Punta inferior dura
        }
    }
}
