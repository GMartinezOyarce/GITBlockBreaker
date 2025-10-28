package io.github.some.BlockBreaker;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class ExtraLife extends  Item{
	public ExtraLife(float x, float y) {
		super(x,y,20,20,Color.GREEN, true);
	}
	public void applyEffect(BlockBreakerGame game) {
		game.upLife();
		desactivar();
	}
}
