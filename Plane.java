package details;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.io.IOException;
import java.awt.image.BufferedImage;

public class Plane{
	/*
	 创建飞机的类，类里包括 
	 飞机的属性（width,height）
	 */
	public static final int PLANE_WIDTH = 120;
	public static final int PLANE_HEIGHT = 150;
	public static final int PLANE_X = 400;
	public static final int PLANE_Y = 300;
	public Point point;
	public static boolean loadImage(BufferedImage image, String source) {
		try {
			image = ImageIO.read(new File(source));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean draw(Graphics g, JPanel panel, boolean pause) {
		if (!pause) {
					g.drawImage(null, point.y, point.x, panel);
			return true;
		}
		return false;
	}


}
