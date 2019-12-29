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

import  spacewar.detail.*;

import spacewar.task.MagicTask;
import spacewar.utils.AudioUtil;
import spacewar.utils.ImageUtil;

import spacewar.SpaceWar;

import spacewar.detail.Blood;

import spacewar.MyPanel;

import spacewar.detail.Ball;

import spacewar.detail.*;
import spacewar.task.EnemyTask;
import spacewar.task.RefreshTask;
import spacewar.task.*;
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

	// Scene scene;//����

	// ��������Ϸ����
	public static MyPlane myplane = null;
	Enemy enemy = null;
	public static Boss boss = null;
	Bomb bomb = null;
	Ball ball = null;
	Explosion explosion = null;
	Blood blood = null; 
	Magic magic = null;

	// �����洢��Ϸ����Ķ����б�
	public static List<Enemy> enemyList = new ArrayList<Enemy>();
	public static List<MyPlane> meList = new ArrayList<MyPlane>();
	public static List<Bomb> bombList = new ArrayList<Bomb>();
	public static List<Ball> ballList = new ArrayList<Ball>();
	public static List<Explosion> explosionList = new ArrayList<Explosion>();
	public static List<Blood> bloodList = new ArrayList<Blood>();
	public static List<Magic> magicList = new ArrayList<Magic>();

	int speed;// ս�����ٶȣ����������
	public static int myLife;// Ϊս����������ֵ
	public static int lifeNum;// ս��������
	public static int myScore;// ս���ĵ÷�
	public static int passScore;// ��ǰ�ؿ��÷���
	public static int lifeCount;// Ѫ���������Ʋ���
	public static boolean bloodExist;// �����Ļ���Ƿ����Ѫ��
	public static boolean magicExist;// �����Ļ���Ƿ����������
	public static int magicCount;// ħ��ֵ�������ܷ�����
	public static int bossBlood;// BossѪ��

	public static int passNum;// ��¼��ǰ�ؿ�
	public static boolean isPass;// �Ƿ�ͨ�صı�־
	public static boolean isPause;// �Ƿ���ͣ
	public static boolean isBoss;// ����Ƿ����Boss
	public static boolean bossLoaded;// ���Boss�������
	public static boolean isProtect;// ����Ƿ���������
	public static boolean isUpdate;// ���ս���Ƿ�����
	public static boolean test;// �޵�ģʽ����
	public static int isStop;// �����Ϸֹͣ
	public static boolean isStarted;// ��ǻ�ӭ�����Ƿ�������

	public static Timer enemyTimer;
	public static Timer painTimer;
	public static Timer magicTimer;

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

	// List<BufferedImage> startIMG;
	public MyPanel() {
		// -----------��ʼ������------------
		// ������Ϸ����ͼƬ
		MyPlane.loadImage();
		Enemy.loadImage();
		Boss.loadImageBoss();
		Ball.loadImage();
		Bomb.loadImage();
		Explosion.loadImage();
		Blood.loadImage();
		Magic.loadImage();

		// ��������
		scene = new Scene();
		// ������ʼ��ʧ��
		if (!scene.initScene()) {
			JDialog dialog = new JDialog();//�Ի���
			dialog.setTitle("ͼƬ����ʧ�ܣ�����");
			dialog.setLocation(300, 150);
			dialog.setSize(200, 100);
			JButton button = new JButton("ȷ��");
			button.addActionListener(new ActionListener() {
				//��Ӧ�¼�
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
		isBoss = false;
		speed = DEFAULT_SPEED;
		myLife = DEFAULT_LIFE;
		lifeNum = DEFAULT_LIFE_COUNT;
		lifeCount = 1;
		passScore = 0;
		myScore = 0;
		bossLoaded = true;
		passNum = DEFAULT_PASS;
		isPass = false;
		isPause = false;
		magicCount = 0;
		bloodExist = false;
		magicExist=false;
		bossBlood = Boss.BOSS_LIFE;
		isProtect = false;
		isUpdate = false;
		test = false;
		isStop = 0;
		boss = null;
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
		magicTimer = new Timer();
		magicTimer.schedule(new MagicTask(), 0, 2000);// ����ħ��ֵ�仯Ƶ��
	}

	public static void killTimer() {
		enemyTimer.cancel();
		// painTimer.cancel();
		magicTimer.cancel();
		enemyTimer = null;
		// painTimer = null;
		magicTimer = null;
	}
 
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// ����ͼƬ
		// g.drawImage(bkImage, 0, 0, App.WINDOWS_WIDTH, App.WINDOWS_HEIGHT,
		// this);
		// ��ӭ����
		if (!isStarted) {
			// ��������
			scene.stickScene(g, -1, this);
			scene.moveBg();
			// �ɻ���ս����ͼƬ
			g.drawImage(titleImage, SpaceWar.WINDOWS_WIDTH / 2 - 173, 100,
					titleImage.getWidth(), titleImage.getHeight(), this);
			// ����˵��
			Font font = new Font("����", Font.PLAIN, 14);
			g.setColor(new Color(128, 128, 0));
			g.setFont(font);
			g.drawString("������ƣ��������ASDW�����", SpaceWar.WINDOWS_WIDTH / 2 - 150,
					210);
			g.drawString("������ո����������", SpaceWar.WINDOWS_WIDTH / 2 - 150, 225);
			g.drawString("��ͣ��Z��", SpaceWar.WINDOWS_WIDTH / 2 - 150, 240);
			g.drawString("���У�X��", SpaceWar.WINDOWS_WIDTH / 2 - 150, 255);
			g.drawString("�����֣�C��", SpaceWar.WINDOWS_WIDTH / 2 - 150, 270);
			g.drawString("ս��������V��", SpaceWar.WINDOWS_WIDTH / 2 - 150, 285);
			g.drawString("�޵�ģʽ��Y��", SpaceWar.WINDOWS_WIDTH / 2 - 150, 300);
			g.drawString("��ʼ����ֵ��10", SpaceWar.WINDOWS_WIDTH / 2 - 150, 315);
			g.drawString("��ʼħ��ֵ��0", SpaceWar.WINDOWS_WIDTH / 2 - 150, 330);
			g.drawString("�л�����ֵ��2", SpaceWar.WINDOWS_WIDTH / 2 - 150, 345);
			g.drawString("����һ���л���1�֣���������ﵽҪ�󼴿ɽ���Bossģʽ����ӮBoss���ɽ�����һ�ء�",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 360);
			g.drawString("ħ��ֵ������Ϸ�������ӣ���ͨ��ʹ��ħ��ֵʹ�÷����֡�ս��������ս�����е�ʹ�á�",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 375);
			g.drawString("��Ϸ�����л���һ��������Ѫ����������������֧�ָ��õ���Ϸ��",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 390);
			g.drawString("���Źؿ����࣬�л����ڵ��ٶȺ����������ӣ�ͨ��10�ؼ���ͨ�أ�",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 405);
			font = new Font("����", Font.BOLD, 20);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("�����������ո����ʼ��Ϸ", SpaceWar.WINDOWS_WIDTH / 2 - 150,450);
			return;
		} else {
			scene.stickScene(g, passNum, this);
			scene.moveBg();
		}

		// ��ʾ��ͣ��Ϣ
		if (myplane != null && isPause && isStop != 0 && isStop == 0) {
			Font textFont = new Font("����", Font.BOLD, 20);
			g.setFont(textFont);
			g.setColor(Color.red);
			// ����͸������
			// cdc.SetBkMode(TRANSPARENT);
			g.drawString("��ͣ", SpaceWar.WINDOWS_WIDTH / 2 - 10, 150);
			return;
		}
 
		// ��Ϸ�����������Ϸ��ǰ��Ϣ
		if (myplane != null) {
			Font textFont = new Font("����", Font.BOLD, 18);
			g.setFont(textFont);
			g.setColor(Color.red);
			// ����͸������
			// cdc.SetBkMode(TRANSPARENT);
			g.drawString("��ǰ�ؿ�:" + passNum, 10, 20);
			g.drawString("��ǰ����:" + lifeNum, 110, 20);
			g.drawString("��ǰ�÷�:" + passScore, 10, 35);
			if (test) {
				g.drawString("�޵�ģʽ������", 10, 220);
			}

			textFont = new Font("����", Font.BOLD, 12);
			g.setFont(textFont);
			g.drawString("Ѫ����",
					SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE - 45, 20);

			// ���Ѫ��
			g.setColor(Color.red);
			int leftPos, topPos = 10, width, height = 12;
			leftPos = SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE;
			width = 12 * myLife;
			g.fillRect(leftPos, topPos, width, height);

			textFont = new Font("����", Font.BOLD, 12);
			g.setFont(textFont);
			g.setColor(Color.blue);
			g.drawString("ħ����",
					SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE - 45, 35);

			// ���ħ��ֵ
			g.setColor(Color.blue);
			topPos = 25;
			height = 12;
			leftPos = SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE;
			width = 12 * magicCount;
			g.fillRect(leftPos, topPos, width, height);

			// ���BossѪ��
			if (isBoss) {
				g.setColor(new Color(128, 0, 128));
				topPos = 40;
				height = 12;
				leftPos = SpaceWar.WINDOWS_WIDTH -12 * 10;
				width = bossBlood / (boss.life / 10) * 20;
				g.fillRect(leftPos, topPos, width, height);
			}
 
			// ���Ѫ���е���ϸѪֵ
			textFont = new Font("����", Font.BOLD, 12);
			g.setFont(textFont);
			g.setColor(Color.white);
			g.drawString(DEFAULT_LIFE + "/" + myLife, SpaceWar.WINDOWS_WIDTH- 12 * DEFAULT_LIFE + 48, 20);
			g.drawString(DEFAULT_LIFE + "/" + magicCount,SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE + 48, 35);
			if (isBoss) {
				g.drawString(boss.life + "/" + bossBlood,SpaceWar.WINDOWS_WIDTH -12 * 10 + 48, 50);
			}

			// ��ʾ��ǰ�ܷ����ĵ���
			textFont = new Font("����", Font.BOLD, 24);
			g.setFont(textFont);
			g.setColor(Color.white);
			if (magicCount > 0) {
				g.drawString("��C�ɴ򿪷�����", 0, SpaceWar.WINDOWS_HEIGHT - 100);
				g.drawString("��V������ս��", 0, SpaceWar.WINDOWS_HEIGHT - 75);
			}
			if (magicCount >= 10) {
				g.drawString("��X��ʹ��ս������", 0, SpaceWar.WINDOWS_HEIGHT - 50);
			}
		}// if

		// ��Ϸֹͣ���ؿ�״̬��Ϣ
		if (isStop == FLAG_RESTART) {
			Font textFont = new Font("����", Font.BOLD, 20);
			g.setFont(textFont);
			// ����͸������
			// cdc.SetBkMode(TRANSPARENT);
			g.setColor(Color.red);
			g.drawString("�ۣ�����С�л������ɷ��ˣ�", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("���ĵ÷�Ϊ��" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("�ٽ��������Ƿ����¿�ս��Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		} else if (isStop == FLAG_STOP) {
			Font textFont = new Font("����", Font.BOLD, 20);
			g.setFont(textFont); 
			// ����͸������ 
			// cdc.SetBkMode(TRANSPARENT);
			g.setColor(Color.red);
			// ��ʾ�����
			g.drawString("��ѽ��������С�л��ɷ����أ�", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("���յ÷�Ϊ��" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("����ģ��Ƿ����¿�ս��Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		}

		// ��ʾBoss
		if (myplane != null && boss != null && !isPause && isBoss) {
			boolean status = boss.draw(g, this, passNum, isPause);
			if (status)
				bossLoaded = true;
		}

		// ˢ����ʾս��
		if (myplane != null) {
			myplane.draw(g, this, isPause, isProtect);
		}
		// ��ʾ�л�
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			if (enemy == null)
				continue;
			enemy.setCurrentIndex(i);
			if (!enemy.draw(g, this, passNum, isPause))
				i--;
		}
		// ��ʾ�л��ӵ�
		for (int i = 0; i < ballList.size(); i++) {
			ball = ballList.get(i);
			if (ball == null)
				continue; 
			ball.setCurrentIndex(i);
			if (!ball.draw(g, this, passNum, isPause))
				i--;
		}
		// ��ʾ�ҷ��ӵ�-----------------------------------------//
		for (int i = 0; i < bombList.size(); i++) {
			bomb = bombList.get(i);
			if (bomb == null)
				continue;
			bomb.setCurrentIndex(i);
			bomb.isUpdate = isUpdate;
			if (!bomb.draw(g, this, isPause))
				i--;
		}
		// ��ʾ��ըЧ��
		for (int i = 0; i < explosionList.size(); i++) {
			explosion = explosionList.get(i);
			if (explosion == null)
				continue;
			boolean b = explosion.draw(g, this, isPause);
			if (!b) {
				explosionList.remove(i);
				i--;
			}
		}
		// ��ʾѪ��
		if (myplane != null && !isPause) {
			// ����Ѫ�������ǿ�ʱ������λ����ʾ
			int i = 0;
			while (i < bloodList.size()) {
				blood = bloodList.get(i);
				if (blood == null)
					continue;
				blood.draw(g, this, false);
				i++;
			}// while
		}
		
		if (myplane != null && !isPause) {
			// �������������ǿ�ʱ������λ����ʾ
			int i = 0;
			while (i < magicList.size()) {
				magic = magicList.get(i);
				if (magic == null)
					continue;
				magic.draw(g, this, false);
				i++;
			}// while
		}
	}
 
	// ����ֵ���㣬��Ϸ����
	public static void gameOver() {
		// ������Ϸ����
		// �ͷż�ʱ��
		killTimer();
		// �������÷�
		myScore += passScore;
		// ������Ϸ��������
		// ����
		// ��Ч
		AudioUtil.play(AudioUtil.AUDIO_GAMEOVER);
		isStop = FLAG_STOP;
		// TODO
		System.out.println("-----------��Ϸ����");
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
		// ��յл�ը������
		if (MyPanel.ballList.size() > 0)
			MyPanel.ballList.removeAll(MyPanel.ballList);
		// ��ձ�ը����
		if (MyPanel.explosionList.size() > 0)
			MyPanel.explosionList.removeAll(MyPanel.explosionList);
		// ���Ѫ���б�
		if (MyPanel.bloodList.size() > 0)
			MyPanel.bloodList.removeAll(MyPanel.bloodList);
		if (MyPanel.magicList.size() > 0)
			MyPanel.magicList.removeAll(MyPanel.magicList);
		

		// �������³�ʼ��
		MyPanel.myLife = DEFAULT_LIFE;
		MyPanel.lifeNum = DEFAULT_LIFE_COUNT;
		MyPanel.myScore = 0;
		MyPanel.passScore = 0;
		MyPanel.passNum = DEFAULT_PASS;
		MyPanel.isPass = false;
		MyPanel.isPause = false;
		MyPanel.lifeCount = 1;
		MyPanel.magicCount = 0;
		MyPanel.bloodExist = false;
		MyPanel.magicExist = false;
		MyPanel.bossBlood = Boss.BOSS_LIFE;
		MyPanel.isBoss = false;
		MyPanel.bossLoaded = true;
		MyPanel.isProtect = false;
		MyPanel.isUpdate = false;
		MyPanel.test = false;
		MyPanel.boss = null;
		// isStarted = FALSE;
		initTimer();
	}
}
