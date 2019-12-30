package spacewar.task;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import spacewar.*;
import spacewar.detail.*;

import spacewar.utils.*;

public class RefreshTask extends TimerTask {

	private JPanel panel;


	public RefreshTask(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void run() {
		// 碰撞检测
		bombAndEnemy();
		enemyAndMe();
		checkPass();
		panel.repaint();
	}

 
	private void checkPass() {
		if (MyPanel.isPass) {
			MyPanel.isPass = false;
			if (MyPanel.passNum == 7)// 7关
			{
				// 重新初始化数据
				MyPanel.killTimer();
				MyPanel.myplane = new MyPlane(false);

				MyPanel.isStop = MyPanel.FLAG_RESTART;
				// 清屏
			}// if
			else {
				MyPanel.killTimer();

				int tScore = MyPanel.myScore + MyPanel.passScore;
				int tPassNum = MyPanel.passNum + 1;

				MyPanel.Restart();
				MyPanel.myplane = new MyPlane(false);
				MyPanel.myScore = tScore;
				MyPanel.passNum = tPassNum;
			}// else
		}// if
	}



	private void enemyAndMe() {
		if (MyPanel.myplane != null ) {
			// 敌机战机碰撞
			for (int i = 0; i < MyPanel.enemyList.size(); i++) {
				Enemy enemy = MyPanel.enemyList.get(i);
				if (enemy == null)
					continue;
				Rectangle enemyRectangle = enemy.getRect();
				Rectangle meRectangle = MyPanel.myplane.getRect();
				if (meRectangle.intersects(enemyRectangle)) {
					Explosion explosion = new Explosion(
							MyPanel.myplane.getPoint().x + MyPlane.PLANE_WIDTH
									/ 2 - Explosion.EXPLOSION_WIDTH / 2,
							MyPanel.myplane.getPoint().y + MyPlane.PLANE_HEIGHT
									/ 2 - Explosion.EXPLOSION_WIDTH / 2);
					MyPanel.explosionList.add(explosion);
					// 音效
					AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// 战机生命值减1
						MyPanel.myLife--;
					// 敌机生命值减少
					enemy.life--;
					if (enemy.life <= 0) {
						// 得分
						MyPanel.passScore++;
						// 删除敌机
						MyPanel.enemyList.remove(i);
						i--;
					}
					// 游戏结束
					if (MyPanel.myLife == 0) {
						MyPanel.lifeNum--;
						if (MyPanel.lifeNum <= 0) {
							// 删除战机对象
							MyPanel.myplane = null;
							MyPanel.gameOver();
							break;
						} else {
							MyPanel.myLife = MyPanel.DEFAULT_LIFE;
						}
					}// if
				}
			}
	
		}
	}

	
 
	private void bombAndEnemy() {
		if (MyPanel.myplane != null ) {
			// 子弹打中敌机
			boolean flag = false;
			for (int i = 0; i < MyPanel.bombList.size(); i++) {
				Bomb bomb = MyPanel.bombList.get(i);
				if (bomb == null)
					continue;
				Rectangle bombRectangle = bomb.getRect();
				for (int j = 0; j < MyPanel.enemyList.size(); j++) {
					Enemy enemy = MyPanel.enemyList.get(j);
					if (enemy == null)
						continue;
					Rectangle enemyRectangle = enemy.getRect();
					if (enemyRectangle.intersects(bombRectangle)) {
						Explosion explosion = new Explosion(
								(bomb.getPoint().x + Bomb.BOMB_WIDTH / 2 - Explosion.EXPLOSION_WIDTH / 2),
								(bomb.getPoint().y + Bomb.BOMB_HEIGHT / 2 - Explosion.EXPLOSION_WIDTH / 2));
						MyPanel.explosionList.add(explosion);
						// 音效
						AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// 爆炸后删除子弹
						MyPanel.bombList.remove(i);
						i--;
						enemy.life -=1;
						if (enemy.life <= 0) {
							// 增加得分
							MyPanel.passScore++;
							// 删除敌机
							MyPanel.enemyList.remove(j);
							j--;
						}
						// 炮弹已删除，直接跳出本循环
						flag = true;
						break;
					}
				}
				if (flag)
					continue;
				
				if(MyPanel.passScore>=20 && MyPanel.passNum==1)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=40 && MyPanel.passNum==2)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=50 && MyPanel.passNum==3)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=70 && MyPanel.passNum==4)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=80 && MyPanel.passNum==5)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=90 && MyPanel.passNum==6)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				else if(MyPanel.passScore>=150 && MyPanel.passNum==7)
				{
					MyPanel.myplane = null;
					MyPanel.isPass = true;
				}
				
				
			}
		}
	}
}