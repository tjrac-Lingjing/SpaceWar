package spacewar.detail;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import javax.imageio.ImageIO;

import spacewar.SpaceWar;
import spacewar.utils.AudioUtil;
import spacewar.utils.ImageUtil;

//������
public class Scene {

	private int beginY;// ������Y����
	private List<BufferedImage> images;

	public Scene() {
		this.images = new ArrayList<BufferedImage>();
	}

	// ��ʼ������
	public boolean initScene() {
		// ���ؿ�ʼͼƬ
		BufferedImage buffer;
		try {
			buffer = ImageUtil.copyImage(ImageIO.read(new File(
					"images/start.jpg")));
			this.images.add(buffer);
			// �������ʧ��, ����false
			for (int i = 1; i <= 6; i++) {
				buffer = ImageUtil.copyImage(ImageIO.read(new File(
						"images/background" + i + ".jpg")));
				this.images.add(buffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		// ������ʼ����Ϊ0 
		this.beginY = 0;

		// ���ű�������
		AudioUtil.playBackground();
		return true;
	}

	// ���Ƴ���
	public void stickScene(Graphics graphics, int index, ImageObserver observer) {
		if (index == -1)
			index = 0;
		else
			index = index % 6 + 1;
		BufferedImage image = images.get(index);
		// ���ڻ���ͼƬ�м�
		if (beginY >= 0
				&& beginY + SpaceWar.WINDOWS_HEIGHT <= image.getHeight()) {
			BufferedImage buffer = image.getSubimage(0, beginY,
					image.getWidth(), SpaceWar.WINDOWS_HEIGHT);
			graphics.drawImage(buffer, 0, 0, SpaceWar.WINDOWS_WIDTH,
					SpaceWar.WINDOWS_HEIGHT, observer);
		} else if (beginY < 0) {
			// ����ͼƬ�Ͻ�
			BufferedImage imageUp = image.getSubimage(0, image.getHeight()
					+ beginY, image.getWidth(), -beginY);
			graphics.drawImage(imageUp, 0, 0, SpaceWar.WINDOWS_WIDTH, -beginY,
					observer);
			graphics.drawImage(image, 0, -beginY, SpaceWar.WINDOWS_WIDTH,
					SpaceWar.WINDOWS_HEIGHT, observer);
			if (-beginY > SpaceWar.WINDOWS_HEIGHT) {
				beginY = image.getHeight() + beginY;
			}
		}
	}

	// �ƶ�����
	public void moveBg() {
		// �ƶ�����
		beginY -= 1;
	}

	// �ͷ��ڴ���Դ
	public void releaseScene() {
		for (int i = 0; i < 7; i++)
			if (images.get(i) != null)
				images.get(i).flush();
		// �رձ�������
		AudioUtil.stopBackground();
	}
 
	public int getBeginY() {
		return beginY;
	}

	public void setBeginY(int beginY) {
		this.beginY = beginY;
	}
}
