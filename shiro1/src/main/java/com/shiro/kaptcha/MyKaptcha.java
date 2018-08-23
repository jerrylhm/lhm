package com.shiro.kaptcha;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

public class MyKaptcha extends DefaultKaptcha{

//	@Override
//	public String createText() {
//		int length = 4;
//		char[] text = new char[length];
//		for(int i=0;i<length;i++) {
//			text[i] = (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
//		}
//		String str = new String(text);
//		System.out.println("生成的文字：" + str);
//		return str;
//	}

	
	/*
	@Override
	public BufferedImage createImage(String text) {
		BufferedImage image = super.createImage(text);
		Graphics g = image.getGraphics();
		g.setColor(Color.RED);
		return image;
	}
	*/
	
	
}
