package  spacewar.detail;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

//��Ϸ����ĸ��࣬������Ϸ���󶼼̳�����
abstract public class GameObject {

	protected Point point;// �����ڴ��ڵ�����

	public GameObject(int x, int y) {
		point = new Point(x, y);
	}

	// ������Ϸ����
	abstract public boolean draw(Graphics g, JPanel panel, boolean pause);

	// �õ���Ϸ����ľ��Σ���ײ���ʹ��
	abstract public Rectangle getRect();

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
 
	// ������Ϸ����ͼƬ
	public static boolean loadImage(BufferedImage image, String source) {
		try {
			image = ImageIO.read(new File(source));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
