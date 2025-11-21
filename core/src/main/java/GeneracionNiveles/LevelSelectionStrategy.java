package GeneracionNiveles;

public class LevelSelectionStrategy {
	public LevelGenerator selectGenerator(int nivel) {
		
		if(nivel <= 2) {
			return new ArgentinaGenerador();
		}
		double randomValue = Math.random();
		
		if (randomValue <= 0.4) {
			return new CirculosConcentricosGenerador();
		}else if(randomValue <= 0.7) {
			return new CirculosConcentricosGenerador();
		}else {
			return new CirculosConcentricosGenerador();
		}
	}
}
