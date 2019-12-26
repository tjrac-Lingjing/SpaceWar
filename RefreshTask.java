package spacewar.task;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import spacewar.MyPanel;
import spacewar.detail.Ball;
import spacewar.detail.Bomb;
import spacewar.detail.Explosion;
import spacewar.utils.AudioUtil;

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
		// ��ײ���
		bombAndEnemy();//�ӵ��͵л�
		enemyAndMe();//�Һ͵л�
		ballAndBomb();
		ballAndMe();
		checkPass();//ͨ��
		panel.repaint();
	}

 
	private void checkPass() {
		if (MyPanel.isPass) {
			MyPanel.isPass = false;//ͨ�غ��ʼ��
			if (MyPanel.passNum == 7)// 7��
			{
				// ���³�ʼ������
				MyPanel.killTimer();
				MyPanel.myplane = new MyPlane(false);

				MyPanel.isStop = MyPanel.FLAG_RESTART;
				// ����
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
			// �л�ս����ײ
			for (int i = 0; i < MyPanel.enemyList.size(); i++) {
				Enemy enemy = MyPanel.enemyList.get(i);
				if (enemy == null)
					continue;
				Rectangle enemyRectangle = enemy.getRect();
				Rectangle meRectangle = MyPanel.myplane.getRect();
				if (meRectangle.intersects(enemyRectangle)) {//�߽���
					Explosion explosion = new Explosion(
							MyPanel.myplane.getPoint().x + MyPlane.PLANE_WIDTH
									/ 2 - Explosion.EXPLOSION_WIDTH / 2,
							MyPanel.myplane.getPoint().y + MyPlane.PLANE_HEIGHT
									/ 2 - Explosion.EXPLOSION_WIDTH / 2);
					MyPanel.explosionList.add(explosion);
					// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// ս������ֵ��1
						MyPanel.myLife--;
					// �л�����ֵ����
					enemy.life--;
					if (enemy.life <= 0) {
						// �÷�
						MyPanel.passScore++;
						// ɾ���л�
						MyPanel.enemyList.remove(i);
						i--;
					}
					// ��Ϸ����
					if (MyPanel.myLife == 0) {
						MyPanel.lifeNum--;
						if (MyPanel.lifeNum <= 0) {
							// ɾ��ս������
							MyPanel.myplane = null;
							MyPanel.gameOver();
							break;
						} else {
							MyPanel.myLife = MyPanel.DEFAULT_LIFE;
						}
					} 
				}
			}
	
		}
	}

	private void ballAndBomb() {
		if (MyPanel.myplane != null ) {
			// �л��ӵ����ҷ��ӵ���ײ
			for (int i = 0; i < MyPanel.bombList.size(); i++) {
				Bomb bomb = MyPanel.bombList.get(i);
				if (bomb == null)
					continue;
				Rectangle bombRectangle = bomb.getRect();
				for (int j = 0; j < MyPanel.ballList.size(); j++) {
					Ball ball = MyPanel.ballList.get(j);
					if (ball == null)
						continue;
					Rectangle ballRectangle = ball.getRect();
					if (bombRectangle.intersects(ballRectangle)) {
						Explosion explosion = new Explosion(
								(ball.getPoint().x + Ball.BALL_WIDTH / 2 - Explosion.EXPLOSION_WIDTH / 2),
								(ball.getPoint().y + Bomb.BOMB_HEIGHT / 2 - Explosion.EXPLOSION_WIDTH / 2));
						MyPanel.explosionList.add(explosion);
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// ��ը��ɾ��ս���ӵ�
						MyPanel.bombList.remove(i);
						// ɾ���л�ը��
						MyPanel.ballList.remove(j);
						i--;
						j--;
						// ����л��ڵ����ӷ�
						// myScore++;
						// ս���ڵ��ͷţ�ֱ��������ѭ��
						break;
					}
				}
			}
		}
	}
	private void ballAndMe() {
		if (MyPanel.myplane != null ) {
			// �л��ӵ�����ս��
			for (int i = 0; i < MyPanel.ballList.size(); i++) {
				Ball ball = MyPanel.ballList.get(i);
				if (ball == null)
					continue;
				Rectangle ballRectangle = ball.getRect();
				Rectangle meRectangle = MyPanel.myplane.getRect();
				if (meRectangle.intersects(ballRectangle)) {
					Explosion explosion = new Explosion(
							(ball.getPoint().x + Ball.BALL_WIDTH / 2 - Explosion.EXPLOSION_WIDTH / 2),
							(ball.getPoint().y + Ball.BALL_HEIGHT / 2 - Explosion.EXPLOSION_WIDTH / 2));
					MyPanel.explosionList.add(explosion);
					// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
					//if (!MyPanel.isProtect && !MyPanel.test)
						// ս������ֵ��1
					//	MyPanel.myLife--;
					// ɾ���л�ը��
					MyPanel.ballList.remove(i);
					i--;
					// ��Ϸ����
					if (MyPanel.myLife == 0) {
						MyPanel.lifeNum--;
						if (MyPanel.lifeNum <= 0) {
							// ɾ��ս������
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
			// �ӵ����ел�
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
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// ��ը��ɾ���ӵ�
						MyPanel.bombList.remove(i);
						i--;
						enemy.life -=1;
						if (enemy.life <= 0) {
							// ���ӵ÷�
							MyPanel.passScore++;
							// ɾ���л�
							MyPanel.enemyList.remove(j);
							j--;
						}
						// �ڵ���ɾ����ֱ��������ѭ��
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