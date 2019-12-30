package spacewar.utils;



import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioUtil {

	public static final int AUDIO_BALL = 0;
	public static final int AUDIO_BLOOD = 1;
	public static final int AUDIO_BOMB = 2;
	public static final int AUDIO_DAZHAO = 3;
	public static final int AUDIO_EXPLOSION = 4;
	public static final int AUDIO_GAMEOVER = 5;
	public static final int AUDIO_PROTECT = 6;
	public static final int AUDIO_UPDATE = 7;
	public static final int AUDIO_BACKGROUND = 8;
	// ��Ч�б�
	private static List<AudioClip> list = new ArrayList<AudioClip>();

	// ��ʼ�������������ļ�
	static {
		try {
			File wavFile = new File("sound/ball.wav");// ����ʹ���ļ�
			AudioClip ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/blood.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/bomb.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/dazhao.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/explosion.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/game_over.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/protect.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/update.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);

			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
			wavFile = new File("sound/background.wav");// ����ʹ���ļ�
			ais = Applet.newAudioClip(wavFile.toURL());
			list.add(ais);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	// ����ָ����Ч
	public static void play(int ais) {
		list.get(ais).play();
	}

	// ѭ�����ű�������
	public static void playBackground(int num) {
		list.get(AUDIO_BACKGROUND).loop();
	}

	// �رձ�������
	public static void stopBackground() {
		list.get(AUDIO_BACKGROUND).stop();
	}

}
