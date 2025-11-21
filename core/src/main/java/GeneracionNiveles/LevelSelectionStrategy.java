package GeneracionNiveles;

public class LevelSelectionStrategy {
	public LevelGenerator selectGenerator(int nivel) {
		
		if(nivel <= 1) {
			return new CorazonGenerador();
		}
		
		double randomValue = Math.random();
		
		if (randomValue <= 0.4) {
			return new ArgentinaGenerador();
		}else if(randomValue <= 0.7) {
			return new CirculosConcentricosGenerador();
		}else {
			return new CorazonGenerador();
		}
	}
}
