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

import spacewar.SpaceWar;

import spacewar.detail.Blood;

import spacewar.MyPanel;

import spacewar.detail.Ball;

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
    private boolean flagPause= false;

	// 创建各游戏对象
	public static MyPlane myplane = null;
	Enemy enemy = null;
	Bomb bomb = null;
	Ball ball = null;
	Explosion explosion = null;
	Blood blood = null;
	
	public static List<Enemy> enemyList = new ArrayList<Enemy>();
	public static List<MyPlane> meList = new ArrayList<MyPlane>();
	public static List<Bomb> bombList = new ArrayList<Bomb>();
	public static List<Ball> ballList = new ArrayList<Ball>();
	public static List<Explosion> explosionList = new ArrayList<Explosion>();
	public static List<Blood> bloodList = new ArrayList<Blood>();

	int speed;// 战机的速度，方向键控制
	public static int myLife;// 为战机设置生命值
	public static int lifeNum;// 战机命条数
	public static int myScore;// 战机的得分
	public static int passScore;// 当前关卡得分数
	public static boolean bloodExist;// 标记屏幕中是否存在血包
	public static int passNum;// 记录当前关卡
	public static boolean isPass;// 是否通关的标志
	public static int lifeCount;// 血包产生控制参数
	public static int isStop;// 标记游戏停止
	public static boolean isStarted;// 标记欢迎界面是否加载完成

	public static Timer enemyTimer;
	public static Timer painTimer;

	private static BufferedImage titleImage;
	public static Scene scene;

	static {
		// 加载标题图片
		try {
			titleImage = ImageUtil.createImageByMaskColorEx(ImageIO
					.read(new File("images/title.png")), new Color(0, 0, 0));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public MyPanel() {
		// -----------初始化工作------------
		// 加载游戏对象图片
		MyPlane.loadImage();
		Enemy.loadImage();
		Bomb.loadImage();
		Explosion.loadImage();
		Ball.loadImage();
		Blood.loadImage();
		// 滚动背景
		scene = new Scene();
		// 场景初始化失败
		if (!scene.initScene()) {
			JDialog dialog = new JDialog();
			dialog.setTitle("图片加载失败！！！");
			dialog.setLocation(300, 150);
			dialog.setSize(200, 100);
			JButton button = new JButton("确定");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			dialog.add(button);
			dialog.setVisible(true);
		}

		// 参数初始化
		myplane = new MyPlane(false);

		speed = DEFAULT_SPEED;
		myLife = DEFAULT_LIFE;
		lifeCount = 1;
		lifeNum = DEFAULT_LIFE_COUNT;
		passScore = 0;
		myScore = 0;
		passNum = DEFAULT_PASS;
		bloodExist = false;
		isPass = false;
		isStop = 0;
		isStarted = false;
		// 界面刷新计时器
		painTimer = new Timer();
		painTimer.schedule(new RefreshTask(this), 0, 15);// 刷新界面定时器
		// 其他计时器
		initTimer();
	}

	private static void initTimer() {
		enemyTimer = new Timer();
		enemyTimer.schedule(new EnemyTask(enemyList), 0, 400 - passNum * 30);// 产生敌机定时器

	}

	public static void killTimer() {
		enemyTimer.cancel();
		enemyTimer = null;

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	
		if (!isStarted) {
			if(flagPause) {
				Font textFont = new Font("宋体", Font.BOLD, 15);
				g.setFont(textFont);
				g.setColor(Color.RED);
				g.drawString("游戏暂停！",10,35 );
			}

			// 滚动背景
			scene.stickScene(g, -1, this);
			scene.moveBg();
			// 飞机大战标题图片
			g.drawImage(titleImage, SpaceWar.WINDOWS_WIDTH / 2 - 173, 100,
					titleImage.getWidth(), titleImage.getHeight(), this);
			// 文字说明
			Font font = new Font("宋体", Font.PLAIN, 12);
			g.setColor(new Color(128, 128, 0));
			g.setFont(font);
			g.drawString("方向控制：方向键、ASDW、鼠标", SpaceWar.WINDOWS_WIDTH / 2 - 150,
					210);
			g.drawString("射击：空格键、鼠标左键", SpaceWar.WINDOWS_WIDTH / 2 - 150, 225);

			g.drawString("初始生命值：10", SpaceWar.WINDOWS_WIDTH / 2 - 150, 315);
			g.drawString("敌机生命值：2", SpaceWar.WINDOWS_WIDTH / 2 - 150, 345);
			g.drawString("消灭一个敌机加1分，如果分数达到要求即可进入下一关。",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 360);

			g.drawString("随着关卡增多，敌机、炮弹速度和数量均增加，通过7关即可通关！",
					SpaceWar.WINDOWS_WIDTH / 2 - 150, 405);
			font = new Font("黑体", Font.BOLD, 24);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("点击鼠标左键或空格键开始游戏", SpaceWar.WINDOWS_WIDTH / 2 - 150,
					450);
			return;
		} else {
			scene.stickScene(g, passNum, this);
			scene.moveBg();
		}
		if(flagPause) {
			Font textFont = new Font("宋体", Font.BOLD, 15);
			g.setFont(textFont);
			g.setColor(Color.RED);
			g.drawString("游戏暂停！",10,35 );
		}


		// 游戏界面输出该游戏当前信息
		if (myplane != null) {
			Font textFont = new Font("宋体", Font.BOLD, 15);
			g.setFont(textFont);
			g.setColor(Color.white);

			g.drawString("当前关卡:" + passNum, 10, 20);
			g.drawString("当前命数:" + lifeNum, 110, 20);
			g.drawString("当前得分:" + passScore, 10, 35);
	
			textFont = new Font("宋体", Font.BOLD, 12);
			g.setFont(textFont);
			g.drawString("血量：",
					SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE - 45, 20);
			// 输出血条
			g.setColor(Color.red);
			int leftPos, topPos = 10, width, height = 10;
			leftPos = SpaceWar.WINDOWS_WIDTH - 12 * DEFAULT_LIFE;
			width = 12 * myLife;
			g.fillRect(leftPos, topPos, width, height);
			

			textFont = new Font("宋体", Font.BOLD, 12);
			g.setFont(textFont);
			g.setColor(Color.white);

			
			
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
			
			g.drawString("目标得分："+goal,
					SpaceWar.WINDOWS_WIDTH - 10 * DEFAULT_LIFE - 20, 40);




		}// if  

		// 游戏停止和重开状态信息
		if (isStop == FLAG_RESTART) {
			Font textFont = new Font("宋体", Font.BOLD, 20);
			g.setFont(textFont);
		
			g.setColor(Color.red);
			g.drawString("哇，恭喜你已通关！", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("您的得分为：" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("COME ON ！重新开始？Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		} else if (isStop == FLAG_STOP) {
			Font textFont = new Font("宋体", Font.BOLD, 20);
			g.setFont(textFont);
			g.setColor(Color.red);
			// 显示最后结果
			g.drawString("GAME OVER！", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 30);
			g.drawString("您的得分为：" + myScore, SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 - 10);
			g.drawString("COME ON ！重新开始？Y/N", SpaceWar.WINDOWS_WIDTH / 2 - 100,
					SpaceWar.WINDOWS_HEIGHT / 2 + 10);
			return;
		}


		// 刷新显示战机
		if (myplane != null) {
			myplane.draw(g, this);
		}
		// 显示敌机
		for (int i = 0; i < enemyList.size(); i++) {
			enemy = enemyList.get(i);
			if (enemy == null)
				continue;
			enemy.setCurrentIndex(i);
			if (!enemy.draw(g, this, passNum))
				i--;
		}
		// 显示敌机子弹
				for (int i = 0; i < ballList.size(); i++) {
					ball = ballList.get(i);
					if (ball == null)
						continue;
					ball.setCurrentIndex(i);
					if (!ball.draw(g, this, passNum))
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
		// 显示爆炸效果
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
	
	// 显示血包
	if (myplane != null ) {
		int i = 0;
		while (i < bloodList.size()) {
			blood = bloodList.get(i);
			if (blood == null)
				continue;
			blood.draw(g, this);
			i++;
		}// while
	}
}

	// 生命值归零，游戏结束
	public static void gameOver() {

		killTimer();
		// 计算最后得分
		myScore += passScore;
	
		AudioUtil.play(AudioUtil.AUDIO_GAMEOVER);
		isStop = FLAG_STOP;
		// TODO
		System.out.println("-----------gameOver");
	}

	// 游戏重新开始
	public static void Restart() {
		// TODO: 在此处添加游戏重新开始初始化参数
		// 战机重新加载
		MyPanel.myplane = new MyPlane(false);

		scene.setBeginY(0);
		// 清空敌机链表
		if (MyPanel.enemyList.size() > 0)
			MyPanel.enemyList.removeAll(MyPanel.enemyList);
		// 清空战机链表
		if (MyPanel.meList.size() > 0)
			MyPanel.meList.removeAll(MyPanel.meList);
		// 清空战机子弹链表
		if (MyPanel.bombList.size() > 0)
			MyPanel.bombList.removeAll(MyPanel.bombList);
		// 清空敌机炸弹链表
				if (MyPanel.ballList.size() > 0)
					MyPanel.ballList.removeAll(MyPanel.ballList);
		// 清空爆炸链表
		if (MyPanel.explosionList.size() > 0)
			MyPanel.explosionList.removeAll(MyPanel.explosionList);
		// 清空血包列表
				if (MyPanel.bloodList.size() > 0)
					MyPanel.bloodList.removeAll(MyPanel.bloodList);


		// 参数重新初始化
		MyPanel.myLife = DEFAULT_LIFE;
		MyPanel.lifeNum = DEFAULT_LIFE_COUNT;
		MyPanel.myScore = 0;
		MyPanel.passScore = 0;
		MyPanel.passNum = DEFAULT_PASS;
		MyPanel.isPass = false;
		MyPanel.lifeCount = 1;
		MyPanel.bloodExist = false;
		initTimer(); 
	}
}
