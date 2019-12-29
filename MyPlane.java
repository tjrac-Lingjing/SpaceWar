package spacewar.detail;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import spacewar.utils.ImageUtil;


public class MyPlane extends GameObject {

	public static final int PLANE_WIDTH = 120;
	public static final int PLANE_HEIGHT = 150;
	public static final int PLANE1_WIDTH = 120;
	public static final int PLANE1_HEIGHT = 90;
	public static final int PLANE_PRO_WIDTH = 165;
	public static final int PLANE_PRO_HEIGHT = 166;
	public static final int PLANE_X = 400;
	public static final int PLANE_Y = 300;

	public static List<BufferedImage> images = new ArrayList<BufferedImage>();// �洢����ս��ͼƬ
	public static List<BufferedImage> images1 = new ArrayList<BufferedImage>();// �洢δ����ս��ͼƬ
	public static List<BufferedImage> imagespro = new ArrayList<BufferedImage>();// �洢������ͼƬ
	public boolean isUpdate;// ���Ҫ��ʾ��ս��ͼƬ�±���������ս���Ƿ��������
	public int progress;

	public MyPlane(boolean isUpdate) {
		super(0, 0);
		this.isUpdate = isUpdate;
		// ս���ĳ�ʼλ��
		point.x = PLANE_X;
		point.y = PLANE_Y;
		progress = 0;
	}
 
	@Override
	public boolean draw(Graphics g, JPanel panel, boolean pause) {
		if (!pause) {
			if (isUpdate) {
				if (progress < 14) {
					g.drawImage(images.get(progress), point.x, point.y, panel);
					progress++;
				} else {
					progress = 0;
					g.drawImage(images.get(progress), point.x, point.y, panel);
				}
			} else {
				g.drawImage(images1.get(3), point.x, point.y, panel);
			}
			return true;
		}
		return false;
	}

	public boolean draw(Graphics g, JPanel panel, boolean pause,boolean isProtect) {
		if (!pause) {
			if (isProtect) {
				Point point = new Point(this.point.x - 25, this.point.y - 30);
				g.drawImage(imagespro.get(0), point.x, point.y, panel);
			}
			if (isUpdate) {
				if (progress < 14) {//ʮ�ĸ�����ͼƬ���ػ��γ������Ķ���
					g.drawImage(images.get(progress), point.x, point.y, panel);
					progress++;
				} else {
					progress = 0;
					g.drawImage(images.get(progress), point.x, point.y, panel);
				}
			} else {
				g.drawImage(images1.get(3), point.x, point.y, panel);//������ͼƬΪ��ʼ�ɻ�
			}
			return true;
		} 
		return false;
	}

	public static boolean loadImage() {
		try {
			BufferedImage temp = ImageIO.read(new File("images/me.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 14; i++) {
				BufferedImage image = temp.getSubimage(i * PLANE_WIDTH, 0,
						PLANE_WIDTH, PLANE_HEIGHT);
				images.add(image);
			}
			temp = ImageIO.read(new File("images/me1.bmp"));
			temp = ImageUtil.createImageByMaskColorEx(temp, new Color(0, 0, 0));
			for (int i = 0; i < 4; i++) {
				BufferedImage image = temp.getSubimage(i * PLANE1_WIDTH, 0,
						PLANE1_WIDTH, PLANE1_HEIGHT);
				images1.add(image);
			}
			BufferedImage image = ImageIO.read(new File("images/protect.bmp"));
			image = ImageUtil.createImageByMaskColorEx(image, new Color(255,
					255, 255));
			imagespro.add(image);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(point.x, point.y, PLANE_WIDTH, PLANE_HEIGHT);
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
