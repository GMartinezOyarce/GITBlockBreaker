package GeneracionNiveles;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;

import io.github.some.BlockBreaker.Block;

public abstract class GeneradorFormaProcedimental extends LevelGenerator{
	// pasos que pueden variar al crear un nivel
	
	protected abstract String getTipoForma(int nivel);
	
	protected abstract float getDensityFactor(int nivel);
	
	protected abstract Block.BlockType getBlockTypeForPosition(int x, int y, float distanceToCenter);
	
	//implementacion template
	
	@Override
	protected int getNumeroFilas(int nivelActual) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	protected void posicionarBloquesAleatoriamente(int nivel, int filas, int blockWidth, int blockHeight, ArrayList<Block> bloques) {
		
		int worldWidth = Gdx.graphics.getWidth();
		
		int centroX = worldWidth/2;
		int centroY = 400;
		
		String tipoForma = getTipoForma(nivel);
		float densityFactor = getDensityFactor(nivel);
		
		
		for(int x = 0 ; x < worldWidth; x += blockWidth) {
			for(int y = 100; y < 700; y += blockHeight){
				
				float distancia = calculateDistance(x,y,centroX, centroY);
				
				if (deberiaPonerBloque(x , y ,distancia ,tipoForma, densityFactor)) {
					
					Block.BlockType type = getBlockTypeForPosition(x, y, distancia); 
                    Block newBlock = new Block(x, y, blockWidth, blockHeight, type);
                    bloques.add(newBlock);
				}
				
			}
			
		}
	}
	
	

	//logica fija de la clase
	
	//evalua si un bloque deberia colocarse en base a la forma
	
	private boolean deberiaPonerBloque(int x, int y, float distancia, String tipoForma, float density) {
		// formula matematica dependiente de la forma que quiero agregar
		if( tipoForma.equals("CIRCULO")) {
			return distancia < (density * 350);
		} else if (tipoForma.equals("ARGENTINA")){
			
			float manhattan = Math.abs(x - 500) + Math.abs(y - 400);
			return manhattan < (density * 300);
		} else if(tipoForma.equals("CORAZON")) {
			// 1. Normalizar coordenadas (centrar y escalar)
	        float cx = 600; 
	        float cy = 450; 
	        
	        float px = (x - cx) / density;
	        float py = (y - cy) / density;
	        
	        // 2. La Fórmula del Corazón
	        // (x^2 + y^2 - 1)^3 - x^2 * y^3 <= 0
	        double parte1 = Math.pow(px*px + py*py - 1, 3);
	        double parte2 = px*px * Math.pow(py, 3);
	        
	        return (parte1 - parte2) <= 0;
			
		}
		
		
		
		return false;
	}
	
	private float calculateDistance(int x, int y, int centroX, int centroY) {
		return (float) Math.sqrt(Math.pow(x - centroX, 2) + Math.pow(y - centroY, 2));
	}
	
}