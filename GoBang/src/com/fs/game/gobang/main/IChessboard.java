package com.fs.game.gobang.main;

import java.util.List;


public interface IChessboard {
	//ȡ�������������
	public int getMaxX();
	//��������
	public int getMaxY();
	//ȡ�õ�ǰ���пհ׵㣬��Щ��ſ�������
	public List<Point> getFreePoints();
}
