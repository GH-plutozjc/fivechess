package com.fs.game.gobang.player.interfaces;

import java.util.List;

import com.fs.game.gobang.main.IChessboard;
import com.fs.game.gobang.main.Point;

public interface IPlayer {
	//��һ�����ӣ���������Ѿ��µ����Ӽ���
	public void run(List<Point> enemyPoints, Point point);

	public boolean hasWin();
	
	public void setChessboard(IChessboard chessboard);
	
	public List<Point> getMyPoints();
}
