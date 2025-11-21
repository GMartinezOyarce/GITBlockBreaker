package io.github.some.BlockBreaker;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class ExtraLife extends  Item implements EstrategiaItem{
	public ExtraLife(float x, float y) {
		super(x,y,20,20,Color.GREEN, true, null);
		this.estrategia=this;
	}
	public void aplicar (BlockBreakerGame game) {
		game.upLife();
	}
}