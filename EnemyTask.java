package spacewar.task;


import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import spacewar.MyPanel;
import spacewar.detail.*;
import spacewar.utils.*;

public class EnemyTask extends TimerTask {

	private List<Enemy> enemyList;

	public EnemyTask(List<Enemy> enemyList) {
		this.enemyList = enemyList;
	}

	@Override
	public void run() {
		if ( MyPanel.myplane == null || !MyPanel.isStarted) {
			return;
		}
		// 根据关卡数产生敌机
		if (MyPanel.passNum <= 5) {
			// 前五关只有一个方向的敌机
			Enemy enemy = new Enemy(Enemy.ENEMY_SPEED, 1);// 设置敌机的方向，从上方飞出
			enemyList.add(enemy);// 随机产生敌机
 
		} else if (MyPanel.passNum > 5) {// 第五关之后，两个方向的敌机
			Enemy enemy1 = new Enemy(Enemy.ENEMY_SPEED, 1);// 设置敌机的方向，从上方飞出
			enemy1.setSpeed(Enemy.ENEMY_SPEED
					+ (new Random().nextInt(2) + MyPanel.passNum - 1));
			enemyList.add(enemy1);

			Enemy enemy2 = new Enemy(Enemy.ENEMY_SPEED, -1);// 设置敌机的方向，从下方飞出
			enemy2.setSpeed(Enemy.ENEMY_SPEED
					+ (new Random().nextInt(2) + MyPanel.passNum - 1));
			enemyList.add(enemy2);
		}
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}