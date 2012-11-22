package com.fs.game.gobang.player.concrete;

import java.util.List;

import com.fs.game.gobang.main.Point;
import com.fs.game.gobang.player.base.BasePlayer;
import com.fs.game.gobang.player.interfaces.IPlayer;


public class HumanPlayer extends BasePlayer implements IPlayer{

	public void run(List<Point> enemyPoints,Point p) {
		getMyPoints().add(p);
		allFreePoints.remove(p);
	}
}
