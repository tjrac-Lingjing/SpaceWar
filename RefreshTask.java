package spacewar.task;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import spacewar.detail.*;

import spacewar.task.EnemyTask;

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
	private Timer bloodTimer;
	private Timer magicTimer;

	public RefreshTask(JPanel panel) {
		this.panel = panel;
	}
 
	@Override
	public void run() {
		// ��ײ���
		bombAndEnemy();
		ballAndMe();
		enemyAndMe();
		ballAndBomb();
		bloodAndMe();
		magicAndMe();
		// ����Ƿ�÷ֹ�����Boss
		gotoBoss();
		// ����Ƿ����
		checkPass();
		// ����Ƿ��Ѫ��
		openMagic();
		openBlood();
		// ˢ�½���
		panel.repaint();
	}

	private void gotoBoss() {
		// ������һ�ؽ���
		int pScore = MyPanel.PASS_SCORE + MyPanel.passNum * 5;
		// TODO��������
		// if (MyPanel.myplane != null && MyPanel.passScore >= 3 &&
		// !MyPanel.isPause&&!MyPanel.isBoss)
		if (MyPanel.myplane != null && MyPanel.passScore >= pScore && !MyPanel.isPause && !MyPanel.isBoss) {
			// ����Boss
			MyPanel.isBoss = true;
			MyPanel.boss = new Boss(1);
			MyPanel.boss.setSpeed(Boss.BOSS_SPEED + MyPanel.passNum - 1);
			MyPanel.boss.life = Boss.BOSS_LIFE + MyPanel.passNum * 50;// Boss��Ѫ��
			MyPanel.bossBlood = Boss.BOSS_LIFE + MyPanel.passNum * 50;// ��ǰBossѪ��
			// Boss��������ͣ��Ϸ
			MyPanel.bossLoaded = false;
			

			// ��������Boss���ӵ�����Ƶ�ʣ���ǿBoss�ӵ�����Ƶ��
			MyPanel.enemyTimer.cancel();
			MyPanel.enemyTimer = null;
			MyPanel.enemyTimer = new Timer();
			MyPanel.enemyTimer.schedule(new EnemyTask(MyPanel.enemyList), 0, 2000 - MyPanel.passNum * 120);
		}
	} 

	private void openBlood() {
		// ����Ѫ��
		if (MyPanel.myplane != null && MyPanel.myLife > 0 && !MyPanel.isPause) {
			// �ؿ���������֮һ����֮��������Ѫ��
			if (MyPanel.passScore > (MyPanel.PASS_SCORE + MyPanel.passNum * 5)
					* MyPanel.lifeCount / 3) {
				// ����Ļ����δ�Ե���Ѫ������β�����Ѫ��
				if (!MyPanel.bloodExist) {
					MyPanel.lifeCount++;
					// ����Ѫ��
					Blood blood = new Blood();
					MyPanel.bloodList.add(blood);
					MyPanel.bloodExist = true;
					bloodTimer = new Timer();
					bloodTimer.schedule(new TimerTask() {

						@Override
						public void run() {
							bloodTimer.cancel();
							bloodTimer = null;
							MyPanel.bloodExist = false;
							// ����Ѫ��λ��
							for (int i = 0; i < MyPanel.bloodList.size(); i++) {
								MyPanel.bloodList.remove(i);
								i--;
							}
						}
					}, 10000);
				} else
					MyPanel.lifeCount++;
			}
		}
	}
	private void openMagic() {
		// ����Ѫ��
		if (MyPanel.myplane != null && MyPanel.myLife > 0 && !MyPanel.isPause) {
			// �ؿ���������֮һ����֮��������Ѫ��
			if (MyPanel.passScore > (MyPanel.PASS_SCORE + MyPanel.passNum * 5)* MyPanel.lifeCount / 4) {
				// ����Ļ����δ�Ե���Ѫ������β�����Ѫ��
				if (!MyPanel.magicExist) {
					//MyPanel.magicCount++; 
					// ����Ѫ��
					Magic magic = new Magic();
					MyPanel.magicList.add(magic);
					MyPanel.magicExist = true;
					magicTimer = new Timer();
					magicTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							magicTimer.cancel(); 
							magicTimer = null;
							MyPanel.magicExist = false;
							// ����Ѫ��λ�� 
							for (int i = 0; i < MyPanel.magicList.size(); i++) {
								MyPanel.magicList.remove(i);
								i--;
							}
						}
					}, 10000);
				} else
					;//MyPanel.magicCount++;
			}
		}
	}

	private void checkPass() {
		if (MyPanel.isPass) {
			MyPanel.isPass = false;
			if (MyPanel.passNum == 10)// 10��
			{
				// ���³�ʼ������
				MyPanel.killTimer();
				MyPanel.myplane = new MyPlane(false);
				MyPanel.isPause = true;

				MyPanel.isStop = MyPanel.FLAG_RESTART;
				// ����
			}// if
			else {
				MyPanel.killTimer();
				MyPanel.isPause = true;
				// ������������
				int tScore = MyPanel.myScore + MyPanel.passScore;
				int tPassNum = MyPanel.passNum + 1;
				boolean tTest = MyPanel.test;
				int magic = MyPanel.magicCount;
				// ���¿�ʼ��Ϸ
				MyPanel.Restart();
				MyPanel.myplane = new MyPlane(false);
				MyPanel.myScore = tScore;
				MyPanel.passNum = tPassNum;
				MyPanel.magicCount = magic;
				MyPanel.test = tTest;
			}// else
		}// if
	}

	private void bloodAndMe() {
		if (MyPanel.myplane != null && !MyPanel.isPause) {
			// �Ե�Ѫ��
			// ����Ѫ��λ��
			for (int i = 0; i < MyPanel.bloodList.size(); i++) {
				Blood blood = MyPanel.bloodList.get(i);
				// ���Ѫ������
				Rectangle bloodbRect = blood.getRect();
				// ���ս������
				Rectangle planeRect = MyPanel.myplane.getRect();
				// �ж��������������Ƿ��н���
				if (bloodbRect.intersects(planeRect)) {// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_BLOOD);
					// ��ѪЧ��
					MyPanel.myLife += 5;
					if (MyPanel.myLife > MyPanel.DEFAULT_LIFE)
						MyPanel.myLife = MyPanel.DEFAULT_LIFE;
					// TODO ����
					// ��Ѫ��Ѫ��ɾ��
					MyPanel.bloodList.remove(i);
					i--;
					break;
				}// if
			}// for
		}
	}

	private void magicAndMe() {
		if (MyPanel.myplane != null && !MyPanel.isPause) {
			// �Ե�Ѫ��
			// ����Ѫ��λ��
			for (int i = 0; i < MyPanel.magicList.size(); i++) {
				Magic magic = MyPanel.magicList.get(i);
				// ���Ѫ������
				Rectangle magicbRect = magic.getRect();
				// ���ս������
				Rectangle planeRect = MyPanel.myplane.getRect();
				// �ж��������������Ƿ��н���
				if (magicbRect.intersects(planeRect)) {// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_BLOOD);
					// ��ѪЧ��
					if(MyPanel.magicCount>7)
					MyPanel.magicCount=10;
					else
						 MyPanel.magicCount =MyPanel.magicCount+ 3;
					
					// TODO ����
					// ��Ѫ��Ѫ��ɾ��
					MyPanel.magicList.remove(i);
					i--;
					break;
				}// if
			}// for
		}
	}
	
	private void ballAndBomb() {
		if (MyPanel.myplane != null && !MyPanel.isPause) {
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

	private void enemyAndMe() {
		if (MyPanel.myplane != null && !MyPanel.isPause) {
			// �л�ս����ײ
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
					// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
					if (!MyPanel.isProtect && !MyPanel.test)
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
					}// if
				}
			}
			// Boss��ս����ײ
			if (MyPanel.myplane != null && !MyPanel.isPause && MyPanel.isBoss) {
				// ���ս���ľ�������
				Rectangle myPlaneRect = MyPanel.myplane.getRect();
				// Boss��ս����ײ
				// ���Boss�ľ�������
				Rectangle bossRect = MyPanel.boss.getRect();
				// �ж��������������Ƿ��н���
				if (myPlaneRect.intersects(bossRect)) {
					// ����ը������ӵ���ը������
					Explosion explosion = new Explosion(
							MyPanel.myplane.getPoint().x + MyPlane.PLANE_WIDTH
									/ 2 - Explosion.EXPLOSION_WIDTH / 2,
							MyPanel.myplane.getPoint().y + MyPlane.PLANE_HEIGHT
									/ 2 - Explosion.EXPLOSION_WIDTH / 2);
					MyPanel.explosionList.add(explosion);
					// ��Ч
					AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
					if (!MyPanel.isProtect && !MyPanel.test)
						// ս������ֵ��1
						MyPanel.myLife--;
					// ��Boss����ɾ���л���ֻ��Ѫ
					MyPanel.bossBlood--;
					// MyPanel.myplane.setPoint(new Point(MyPlane.PLANE_X,
					// MyPlane.PLANE_Y));
					if (MyPanel.bossBlood <= 0) {
						Explosion explosion1 = new Explosion(
								MyPanel.boss.getPoint().x,
								MyPanel.boss.getPoint().y);
						MyPanel.explosionList.add(explosion1);
						Explosion explosion2 = new Explosion(
								(MyPanel.boss.getPoint().x + Boss.BOSS_WIDTH),
								(MyPanel.boss.getPoint().y + Boss.BOSS_HEIGHT));
						MyPanel.explosionList.add(explosion2);
						Explosion explosion3 = new Explosion(
								(MyPanel.boss.getPoint().x + Boss.BOSS_WIDTH),
								(MyPanel.boss.getPoint().y));
						MyPanel.explosionList.add(explosion3);
						Explosion explosion4 = new Explosion(
								(MyPanel.boss.getPoint().x),
								(MyPanel.boss.getPoint().y + Boss.BOSS_HEIGHT));
						MyPanel.explosionList.add(explosion4);
						Explosion explosion5 = new Explosion(
								(MyPanel.boss.getPoint().x + Boss.BOSS_WIDTH
										/ 2 - Explosion.EXPLOSION_WIDTH / 2),
								(MyPanel.boss.getPoint().y + Boss.BOSS_HEIGHT
										/ 2 - Explosion.EXPLOSION_WIDTH / 2));
						explosion5.setBossDie(true);// ������һ��ը����ը��֮��������һ��
						MyPanel.explosionList.add(explosion5);
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						MyPanel.boss = null;
						// ���صı�־����
						// isPause = TRUE;
						// CMyPlane* temp = myplane;
						// myplane = new CMyPlane(FALSE);
						MyPanel.myplane = null;
						MyPanel.isPass = true;
						MyPanel.isBoss = false;
					}
					// ��Ϸ����
					if (MyPanel.myLife == 0) {
						MyPanel.lifeNum--;
						if (MyPanel.lifeNum <= 0) {
							// isPause = TRUE;
							// ɾ��ս������
							MyPanel.myplane = null;

							MyPanel.gameOver();
						} else {
							MyPanel.myLife = MyPanel.DEFAULT_LIFE;
							// ɾ��ԭս������
						}
					}// if
				}// if
			}
		}
	}

	private void ballAndMe() {
		if (MyPanel.myplane != null && !MyPanel.isPause) {
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
					if (!MyPanel.isProtect && !MyPanel.test)
						// ս������ֵ��1
						MyPanel.myLife--;
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
		if (MyPanel.myplane != null && !MyPanel.isPause) {
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
						// �л�����ֵ����
						enemy.life -= MyPanel.isUpdate ? 2 : 1;
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
				if (MyPanel.isBoss && bomb != null) {
					// ���ս���ӵ��ľ�������
					Rectangle bombRect = bomb.getRect();
					// ���Boss�ľ�������
					Rectangle bossRect = MyPanel.boss.getRect();
					// �ж��������������Ƿ��н���
					if (bombRect.intersects(bossRect)) {
						// ����ը������ӵ���ը������
						Explosion explosion = new Explosion(
								(bomb.getPoint().x + Bomb.BOMB_WIDTH / 2 - Explosion.EXPLOSION_WIDTH / 2),
								(bomb.getPoint().y + Bomb.BOMB_HEIGHT / 2 - Explosion.EXPLOSION_WIDTH / 2));
						MyPanel.explosionList.add(explosion);
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_EXPLOSION);
						// ��ը��ɾ���ӵ�
						MyPanel.bombList.remove(i);
						i--;
						bomb = null;
						// ��Boss����ɾ���л���ֻ��Ѫ
						MyPanel.bossBlood -= MyPanel.isUpdate ? 2 : 1;
						if (MyPanel.bossBlood <= 0) {
							Explosion explosion1 = new Explosion(
									MyPanel.boss.getPoint().x,
									MyPanel.boss.getPoint().y);
							MyPanel.explosionList.add(explosion1);
							Explosion explosion2 = new Explosion(
									(MyPanel.boss.getPoint().x + Boss.BOSS_WIDTH),
									(MyPanel.boss.getPoint().y + Boss.BOSS_HEIGHT));
							MyPanel.explosionList.add(explosion2);
							Explosion explosion3 = new Explosion(
									(MyPanel.boss.getPoint().x + Boss.BOSS_WIDTH),
									(MyPanel.boss.getPoint().y));
							MyPanel.explosionList.add(explosion3);
							Explosion explosion4 = new Explosion(
									(MyPanel.boss.getPoint().x),
									(MyPanel.boss.getPoint().y + Boss.BOSS_HEIGHT));
							MyPanel.explosionList.add(explosion4);
							Explosion explosion5 = new Explosion(
									(MyPanel.boss.getPoint().x
											+ Boss.BOSS_WIDTH / 2 - Explosion.EXPLOSION_WIDTH / 2),
									(MyPanel.boss.getPoint().y
											+ Boss.BOSS_HEIGHT / 2 - Explosion.EXPLOSION_WIDTH / 2));
							explosion5.setBossDie(true);// ������һ��ը����ը��֮��������һ��
							MyPanel.explosionList.add(explosion5);

							MyPanel.boss = null;
							// ���صı�־����
							// isPause = TRUE;
							// CMyPlane* temp = myplane;
							// myplane = new CMyPlane(FALSE);
							MyPanel.myplane = null;
							MyPanel.isPass = true;
							MyPanel.isBoss = false;
						}
					}
				}
			}
		}
	}
}