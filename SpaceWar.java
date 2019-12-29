package spacewar;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import spacewar.MyPanel;
import spacewar.SpaceWar;
import spacewar.detail.*;

import  spacewar.utils.AudioUtil;

import  spacewar.detail.*;
import spacewar.utils.*;

public class SpaceWar {
  
	// ȫ�ִ��ڴ�С
	public static int WINDOWS_HEIGHT = 650;
	public static int WINDOWS_WIDTH = 1100;
	private JFrame frame;
	// ����ģʽ
	static {
		new SpaceWar().start();
	}

	// ���캯��
	private SpaceWar() {
		gameInit();
	}

	private void start() {
		// ��ʾ����
		frame.setVisible(true);
	}

	private void gameInit() {
		// ����ȫ�ִ��ڴ�С
		WINDOWS_HEIGHT = 650;
		WINDOWS_WIDTH = 1100;
		// ���ô��ڳ�ʼ������
		frame = new JFrame();
		frame.setSize(WINDOWS_WIDTH, WINDOWS_HEIGHT);
		frame.setLocation(200, 50);
		frame.setTitle("�ɷ�����С�л�                                    ������������Կ������Ŷ�");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ���������ʾ���
		MyPanel panel = new MyPanel();
		frame.add(panel);
		// ��Ӵ����¼�����
		addListener(frame);
	}

	// ����¼�����
	private void addListener(JFrame frame) {
		// ��������
		frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent event) {
				if (MyPanel.myplane != null && !MyPanel.isPause) {
					switch (event.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						int x = MyPanel.myplane.getPoint().x
								- MyPanel.DEFAULT_SPEED;
						if (x < 0)
							x = 0;
						MyPanel.myplane.setPoint(new Point(x, MyPanel.myplane
								.getPoint().y));
						break;
					case KeyEvent.VK_RIGHT:
						int x1 = MyPanel.myplane.getPoint().x+ MyPanel.DEFAULT_SPEED;
						if (x1 > SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH)
							x1 = SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH;
						MyPanel.myplane.setPoint(new Point(x1, MyPanel.myplane.getPoint().y));
						break;
					case KeyEvent.VK_UP:
						int y = MyPanel.myplane.getPoint().y- MyPanel.DEFAULT_SPEED;
						if (y < 0)
							y = 0;
						MyPanel.myplane.setPoint(new Point(MyPanel.myplane.getPoint().x, y));
						break;
					case KeyEvent.VK_DOWN:
						int y1 = MyPanel.myplane.getPoint().y+ MyPanel.DEFAULT_SPEED;
						if (y1 > SpaceWar.WINDOWS_HEIGHT)
							y1 = SpaceWar.WINDOWS_HEIGHT;
						MyPanel.myplane.setPoint(new Point(MyPanel.myplane.getPoint().x, y1));
						break;
					case KeyEvent.VK_SPACE:
						if(!MyPanel.isUpdate )
						{
							Bomb bomb1 = new Bomb(
									MyPanel.myplane.getPoint().x + 10,
									MyPanel.myplane.getPoint().y, 1,
									MyPanel.isUpdate);
							MyPanel.bombList.add(bomb1);
							Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 40, MyPanel.myplane
									.getPoint().y, 1, MyPanel.isUpdate);
							MyPanel.bombList.add(bomb2);
						}else
						{
							Bomb bomb1 = new Bomb(
									MyPanel.myplane.getPoint().x + 10,
									MyPanel.myplane.getPoint().y, 1,
									MyPanel.isUpdate);
							MyPanel.bombList.add(bomb1);
							Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 70, MyPanel.myplane
									.getPoint().y, 1, MyPanel.isUpdate);
							MyPanel.bombList.add(bomb2);
							Bomb bomb3 = new Bomb(
									MyPanel.myplane.getPoint().x +  MyPlane.PLANE_WIDTH - 40,
									MyPanel.myplane.getPoint().y, 1,
									MyPanel.isUpdate);
							MyPanel.bombList.add(bomb3);
							Bomb bomb4 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 10, MyPanel.myplane
									.getPoint().y, 1, MyPanel.isUpdate);
							MyPanel.bombList.add(bomb4);
						}
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_BOMB);
						break;
					case KeyEvent.VK_C:
						// ����������
						MyPanel.isProtect = true;
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_PROTECT);
						break;
					case KeyEvent.VK_V:
						// ս������
						MyPanel.isUpdate = true;
						MyPanel.myplane.isUpdate = true;
						// ��Ч
						AudioUtil.play(AudioUtil.AUDIO_UPDATE);
						break;
					case KeyEvent.VK_Y:
						if (MyPanel.isStop == 0) {
							// �޵�ģʽ����
							if (MyPanel.test == false)
								MyPanel.test = true;
							else
								MyPanel.test = false;
						}
						break;
					case KeyEvent.VK_X:// ����
						if (MyPanel.bossLoaded) {
							// ս��������
							if (MyPanel.magicCount >= 10) {
								MyPanel.magicCount -= 10;
								// ��յл�
								for (int i = 0; i < MyPanel.enemyList.size(); i++) {
									Enemy enemy = MyPanel.enemyList.get(i);
									// ����ը������ӵ���ը������
									Explosion explosion = new Explosion(
											(enemy.getPoint().x + Enemy.ENEMY_WIDTH / 2),
											(enemy.getPoint().y + Enemy.ENEMY_HEIGHT / 2));
									MyPanel.explosionList.add(explosion);

									// ɾ���л�
									MyPanel.enemyList.remove(i);
									// ���ӵ÷�
									MyPanel.passScore++;
								}// for
								if (MyPanel.isBoss) {
									// ����ը������ӵ���ը������
									Explosion explosion = new Explosion(
											MyPanel.boss.getPoint().x
													+ Boss.BOSS_WIDTH / 2,
											MyPanel.boss.getPoint().y
													+ Boss.BOSS_HEIGHT / 2);
									MyPanel.explosionList.add(explosion);
									MyPanel.bossBlood -= 50;
									if (MyPanel.bossBlood <= 0) {
										// boss��������
										// ���صı�־���� 
										MyPanel.boss = null;
										// ���صı�־����
										MyPanel.isPause = true;
										MyPanel.myplane = new MyPlane(false);
										MyPanel.isPass = true;
										MyPanel.isBoss = false;
									}
								}
								// ��յл��ڵ�
								for (int i = 0; i < MyPanel.ballList.size(); i++) {
									MyPanel.ballList.remove(i);
								}
								// ��Ч
								AudioUtil.play(AudioUtil.AUDIO_DAZHAO);
							}
						}
						break;
					default:
						break;
					}
				}
				// ��ͣ  
				if (event.getKeyCode() == KeyEvent.VK_Z) {
					if (MyPanel.isPause)
						MyPanel.isPause = false;
					else
						MyPanel.isPause = true;
				}
				// ȡ����N
				else if (event.getKeyCode() == KeyEvent.VK_N) {
					if (MyPanel.isStop != 0) {
						JDialog dialog = new JDialog(frame);
						dialog.setTitle("�˳�����");
						dialog.setSize(400, 200);
						dialog.setLocation(450, 200);
						JButton button = new JButton();
						button.setText("�������˳���Ϸ��");
						button.setSize(100, 50);
						button.setLocation(200, 120);

						button.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								System.exit(0);
							}
						});
						dialog.add(button);
						dialog.setVisible(true);
					}
				}
				// ȷ�ϼ�Y
				if (MyPanel.isStop != 0 && event.getKeyCode() == KeyEvent.VK_Y) {
					MyPanel.isStop = 0;
					MyPanel.Restart();
				}
				// ���ո������Ϸ
				if (!MyPanel.isStarted && event.getKeyCode() == KeyEvent.VK_SPACE) {
					MyPanel.isStarted = true;
					MyPanel.scene.setBeginY(0);
				}
			}
		});
		// �����
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent event) {
				if (MyPanel.myplane != null && !MyPanel.isPause) {
					if (event.getButton() == MouseEvent.BUTTON1) {
						// ���
						if (MyPanel.myplane != null && !MyPanel.isPause) {
							if(!MyPanel.isUpdate )
							{
								Bomb bomb1 = new Bomb(
										MyPanel.myplane.getPoint().x + 10,
										MyPanel.myplane.getPoint().y, 1,
										MyPanel.isUpdate);
								MyPanel.bombList.add(bomb1);
								Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 40, MyPanel.myplane
										.getPoint().y, 1, MyPanel.isUpdate);
								MyPanel.bombList.add(bomb2);
							}else
							{
								Bomb bomb1 = new Bomb(
										MyPanel.myplane.getPoint().x + 10,
										MyPanel.myplane.getPoint().y, 1,
										MyPanel.isUpdate);
								MyPanel.bombList.add(bomb1);
								Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 70, MyPanel.myplane
										.getPoint().y, 1, MyPanel.isUpdate);
								MyPanel.bombList.add(bomb2);
								Bomb bomb3 = new Bomb(
										MyPanel.myplane.getPoint().x +  MyPlane.PLANE_WIDTH - 40,
										MyPanel.myplane.getPoint().y, 1,
										MyPanel.isUpdate); 
								MyPanel.bombList.add(bomb3);
								Bomb bomb4 = new Bomb(MyPanel.myplane.getPoint().x+ MyPlane.PLANE_WIDTH - 10, MyPanel.myplane
										.getPoint().y, 1, MyPanel.isUpdate);
								MyPanel.bombList.add(bomb4);
							}
							// ��Ч
							AudioUtil.play(AudioUtil.AUDIO_BOMB);
						}

						if (!MyPanel.isStarted) {
							MyPanel.isStarted = true;
							MyPanel.scene.setBeginY(0);
						}
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});

		// ����ƶ�
		frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (MyPanel.myplane != null && !MyPanel.isPause)
					MyPanel.myplane.setPoint(arg0.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {

			}
		});
	}
}
