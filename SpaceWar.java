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

import  spacewar.detail.*;
import spacewar.utils.*;

public class SpaceWar {

	// 全局窗口大小
	public static int WINDOWS_HEIGHT = 600;
	public static int WINDOWS_WIDTH = 900;
	private JFrame frame;
	// 单例模式
	static {
		new SpaceWar().start();
	}

	// 构造函数
	private SpaceWar() {
		gameInit();
	}

	private void start() {
		// 显示窗口
		frame.setVisible(true);
	}

	private void gameInit() {
		// 设置全局窗口大小
		WINDOWS_HEIGHT = 600;
		WINDOWS_WIDTH = 900;
		// 设置窗口初始化参数
		frame = new JFrame();
		frame.setSize(WINDOWS_WIDTH, WINDOWS_HEIGHT);
		frame.setLocation(200, 50);
		frame.setTitle("干翻对面小敌机      ———— 万物皆可制造");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 窗口添加显示面板
		MyPanel panel = new MyPanel();
		frame.add(panel);
		// 添加窗口事件监听
		addListener(frame);
	}

	// 添加事件监听
	private void addListener(JFrame frame) {
		// 按键监听
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
				if (MyPanel.myplane != null ) {
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
						int x1 = MyPanel.myplane.getPoint().x
								+ MyPanel.DEFAULT_SPEED;
						if (x1 > SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH)
							x1 = SpaceWar.WINDOWS_WIDTH - Bomb.BOMB_WIDTH;
						MyPanel.myplane.setPoint(new Point(x1, MyPanel.myplane
								.getPoint().y));
						break;
					case KeyEvent.VK_UP:
						int y = MyPanel.myplane.getPoint().y
								- MyPanel.DEFAULT_SPEED;
						if (y < 0)
							y = 0;
						MyPanel.myplane.setPoint(new Point(MyPanel.myplane
								.getPoint().x, y));
						break;
					case KeyEvent.VK_DOWN:
						int y1 = MyPanel.myplane.getPoint().y
								+ MyPanel.DEFAULT_SPEED;
						if (y1 > SpaceWar.WINDOWS_HEIGHT)
							y1 = SpaceWar.WINDOWS_HEIGHT;
						MyPanel.myplane.setPoint(new Point(MyPanel.myplane
								.getPoint().x, y1));
						break;
					case KeyEvent.VK_SPACE:
						Bomb bomb1 = new Bomb(
								MyPanel.myplane.getPoint().x + 10,
								MyPanel.myplane.getPoint().y, 1);
						MyPanel.bombList.add(bomb1);
						Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x
								+ MyPlane.PLANE_WIDTH - 40, MyPanel.myplane
								.getPoint().y, 1);
						MyPanel.bombList.add(bomb2);
						// 音效
						AudioUtil.play(AudioUtil.AUDIO_BOMB);
						break;
	 
					default:
						break;
					}
				}
		 
				else if (event.getKeyCode() == KeyEvent.VK_N) {
					if (MyPanel.isStop != 0) {
						JDialog dialog = new JDialog(frame);
						dialog.setTitle("关于");
						dialog.setSize(400, 200);
						dialog.setLocation(450, 200);
						JButton button = new JButton();
						button.setText("确定");
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
				// 确认键Y
				if (MyPanel.isStop != 0 && event.getKeyCode() == KeyEvent.VK_Y) {
					MyPanel.isStop = 0;
					MyPanel.Restart();
				}
				// 按空格进入游戏
				if (!MyPanel.isStarted
						&& event.getKeyCode() == KeyEvent.VK_SPACE) {
					MyPanel.isStarted = true;
					MyPanel.scene.setBeginY(0);
				}
			}
		});
		// 鼠标点击
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {

			}

			@Override
			public void mousePressed(MouseEvent event) {
				if (MyPanel.myplane != null ) {
					if (event.getButton() == MouseEvent.BUTTON1) {
						// 左键
						if (MyPanel.myplane != null ) {
							Bomb bomb1 = new Bomb(
									MyPanel.myplane.getPoint().x + 10,
									MyPanel.myplane.getPoint().y, 1);
							MyPanel.bombList.add(bomb1);
							Bomb bomb2 = new Bomb(MyPanel.myplane.getPoint().x
									+ MyPlane.PLANE_WIDTH - 40, MyPanel.myplane
									.getPoint().y, 1);
							MyPanel.bombList.add(bomb2);
							// 音效
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

		// 鼠标移动
		frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent arg0) {
				if (MyPanel.myplane != null )
					MyPanel.myplane.setPoint(arg0.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
			}
		});
	}
}
