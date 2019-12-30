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
		// ���ݹؿ��������л�
		if (MyPanel.passNum <= 5) {
			// ǰ���ֻ��һ������ĵл�
			Enemy enemy = new Enemy(Enemy.ENEMY_SPEED, 1);// ���õл��ķ��򣬴��Ϸ��ɳ�
			enemyList.add(enemy);// ��������л�
 
		} else if (MyPanel.passNum > 5) {// �����֮����������ĵл�
			Enemy enemy1 = new Enemy(Enemy.ENEMY_SPEED, 1);// ���õл��ķ��򣬴��Ϸ��ɳ�
			enemy1.setSpeed(Enemy.ENEMY_SPEED
					+ (new Random().nextInt(2) + MyPanel.passNum - 1));
			enemyList.add(enemy1);

			Enemy enemy2 = new Enemy(Enemy.ENEMY_SPEED, -1);// ���õл��ķ��򣬴��·��ɳ�
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