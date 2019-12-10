package spacewar;



import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import spacewar.detail.*;
import spacewar.task.EnemyTask;
import spacewar.task.RefreshTask;
//import spacewar.task.*;
import spacewar.utils.*;


public class MyPanel extends JPanel {

	public static final int DEFAULT_SPEED = 30;
	public static final int DEFAULT_LIFE = 10;
	public static final int DEFAULT_LIFE_COUNT = 3;
	public static final int DEFAULT_PASS = 1;
	public static final int BOMB_DISTANCE = 35;
	public static final int STEP = 30;
	public static final int PASS_SCORE = 20;
	public static final int FLAG_RESTART = 2;
	public static final int FLAG_STOP = 3;


	// ��������Ϸ����
	public static MyPlane myplane = null;
	Enemy enemy = null;
	Bomb bomb = null;

	Explosion explosion = null;

	public static List<Enemy> enemyList = new ArrayList<Enemy>();
	public static List<MyPlane> meList = new ArrayList<MyPlane>();
	public static List<Bomb> bombList = new ArrayList<Bomb>();

	public static List<Explosion> explosionList = new ArrayList<Explosion>();

	int speed;// ս�����ٶȣ����������
	public static int myLife;// Ϊս����������ֵ
	public static int lifeNum;// ս��������
	public static int myScore;// ս���ĵ÷�
	public static int passScore;// ��ǰ�ؿ��÷���
	
	public static int passNum;// ��¼��ǰ�ؿ�
	public static boolean isPass;// �Ƿ�ͨ�صı�־

	public static int isStop;// �����Ϸֹͣ
	public static boolean isStarted;// ��ǻ�ӭ�����Ƿ�������

	public static Timer enemyTimer;
	public static Timer painTimer;

	private static BufferedImage titleImage;
	public static Scene scene;

	static {
		// ���ر���ͼƬ
		try {
			titleImage = ImageUtil.createImageByMaskColorEx(ImageIO
					.read(new File("images/title.png")), new Color(0, 0, 0));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public MyPanel() {
		// -----------��ʼ������------------
		// ������Ϸ����ͼƬ
		MyPlane.loadImage();
		Enemy.loadImage();
		Bomb.loadImage();
		Explosion.loadImage();

		// ��������
		scene = new Scene();
		// ������ʼ��ʧ��
		if (!scene.initScene()) {
			JDialog dialog = new JDialog();
			dialog.setTitle("ͼƬ����ʧ�ܣ�����");
			dialog.setLocation(300, 150);
			dialog.setSize(200, 100);
			JButton button = new JButton("ȷ��");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			dialog.add(button);
			dialog.setVisible(true);
		}

		// ������ʼ��
		myplane = new MyPlane(false);

		speed = DEFAULT_SPEED;
		myLife = DEFAULT_LIFE;
		lifeNum = DEFAULT_LIFE_COUNT;
		passScore = 0;
		myScore = 0;
		passNum = DEFAULT_PASS;
		isPass = false;
		isStop = 0;
		isStarted = false;
		// ����ˢ�¼�ʱ��
		painTimer = new Timer();
		painTimer.schedule(new RefreshTask(this), 0, 15);// ˢ�½��涨ʱ��
		// ������ʱ��
		initTimer();
	}

	private static void initTimer() {
		enemyTimer = new Timer();
		enemyTimer.schedule(new EnemyTask(enemyList), 0, 400 - passNum * 30);// �����л���ʱ��

	}

	public static void killTimer() {
		enemyTimer.cancel();
		enemyTimer = null;

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	
		if (!isStarted) {
			// ��������
			scene.stickScene(g, -1, this);
			scene.moveBg();
			// �ɻ���ս����ͼƬ
			g.drawImage(titleImage, SpaceWar.WINDOWS_WIDTH / 2 - 173, 100,
					titleImage.getWidth(), titleImage.getHeight(), this);
			// ����˵��
			Font font = new Font("����", Font.PLAIN, 12);
			g.setColor(new Color(128, 128, 0));
			g.setFont(font);
			g.drawString("������ƣ��������ASDW�����", SpaceWar.WINDOWS_WIDTH / 2 - 150,
					210);
			g.drawString("������ո����������", SpaceWar.WINDOWS_WIDTH / 2 - 150, 225);

			g.drawString("��ʼ����ֵ��10", SpaceWar.WINDOWS_WIDTH / 2 - 150, 315);
			g.drawString("�л�����ֵ��2", SpaceWar.WINDOWS_WIDTH / 2 - 150, 345);
			g.drawString("����һ���л���1�֣���������ﵽҪ�󼴿ɽ�����һ�ء�",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 360);

			g.drawString("���Źؿ����࣬�л����ڵ��ٶȺ����������ӣ�ͨ��7�ؼ���ͨ�أ�",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 405);
			font = new Font("����", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("�����������ո����ʼ��Ϸ", SpaceWar.WINDOWS_WIDTH / 2 - 150,
					450);
			return;
		} else {
			scene.stickScene(g, passNum, this);
			scene.moveBg();
		}


		// ��Ϸ�����������Ϸ��ǰ��Ϣ
		if (myplane != null) {
			Font textFont = new Font("����", Font.BOLD, 15);
			g.setFont(textFont);
			g.setColor(Color.red);

			g.drawString("��ǰ�ؿ�:" + passNum, 10, 20);
			g.drawString("��ǰ����:" + lifeNum, 110, 20);
			g.drawString("��ǰ�÷�:" + passScore, 10, 35);
	
			textFont = new Font("����", Font.BOLD, 12);
			g.setFont(textFont);
			int goal=0;
			switch(passNum)
			{
			case 1:goal=20;break;
			case 2:goal=40;break;
			case 3:goal=50;break;
			case 4:goal=70;break;
			case 5:goal=80;break;
			case 6:goal=90;break;
			default:goal=150;break;
			}
			
			g.drawString("Ŀ��÷֣�"+goal,
					SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE - 45, 20);




		}// if  

		// ��Ϸֹͣ���ؿ�״̬��Ϣ
		if (isStop == FLAG_RESTART) {
			Font textFont = new Font("����", Font.BOLD, 20);
			g.setFont(textFont);
		
			g.setColor(Color.red);
			g.drawString("�ۣ���ϲ����ͨ�أ�", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("���ĵ÷�Ϊ��" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("COME ON �����¿�ʼ��Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		} else if (isStop == FLAG_STOP) {
			Font textFont = new Font("����", Font.BOLD, 20);
			g.setFont(textFont);
			g.setColor(Color.red);
			// ��ʾ�����
			g.drawString("GAME OVER��", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("���ĵ÷�Ϊ��" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("COME ON �����¿�ʼ��Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		}


		// ˢ����ʾս��
		if (myplane != null) {
			myplane.draw(g, this);
		}
		// ��ʾ�л�
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			if (enemy == null)
				continue;
			enemy.setCurrentIndex(i);
			if (!enemy.draw(g, this, passNum))
				i--;
		}
	 
		for (int i = 0; i < bombList.size(); i++) {
			bomb = bombList.get(i);
			if (bomb == null)
				continue;
			bomb.setCurrentIndex(i);
		//	bomb.isUpdate = isUpdate;
			if (!bomb.draw(g, this))
				i--;
		}
		// ��ʾ��ըЧ��
		for (int i = 0; i < explosionList.size(); i++) {
			explosion = explosionList.get(i);
			if (explosion == null)
				continue;
			boolean b = explosion.draw(g, this);
			if (!b) {
				explosionList.remove(i);
				i--;
			}
		}
	
	}
	

	// ����ֵ���㣬��Ϸ����
	public static void gameOver() {

		killTimer();
		// �������÷�
		myScore += passScore;
	
		AudioUtil.play(AudioUtil.AUDIO_GAMEOVER);
		isStop = FLAG_STOP;
		// TODO
		System.out.println("-----------gameOver");
	}

	// ��Ϸ���¿�ʼ
	public static void Restart() {
		// TODO: �ڴ˴������Ϸ���¿�ʼ��ʼ������
		// ս�����¼���
		MyPanel.myplane = new MyPlane(false);

		scene.setBeginY(0);
		// ��յл�����
		if (MyPanel.enemyList.size() > 0)
			MyPanel.enemyList.removeAll(MyPanel.enemyList);
		// ���ս������
		if (MyPanel.meList.size() > 0)
			MyPanel.meList.removeAll(MyPanel.meList);
		// ���ս���ӵ�����
		if (MyPanel.bombList.size() > 0)
			MyPanel.bombList.removeAll(MyPanel.bombList);

		if (MyPanel.explosionList.size() > 0)
			MyPanel.explosionList.removeAll(MyPanel.explosionList);
	

		// �������³�ʼ��
		MyPanel.myLife = DEFAULT_LIFE;
		MyPanel.lifeNum = DEFAULT_LIFE_COUNT;
		MyPanel.myScore = 0;
		MyPanel.passScore = 0;
		MyPanel.passNum = DEFAULT_PASS;
		MyPanel.isPass = false;

		initTimer(); 
	}
}
