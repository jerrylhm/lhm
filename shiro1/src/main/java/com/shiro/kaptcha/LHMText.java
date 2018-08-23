package com.shiro.kaptcha;

import java.util.Random;

import com.google.code.kaptcha.text.TextProducer;

public class LHMText implements TextProducer{

	private String[] simplifiedChineseTexts = new String[]{
			"公公吃屎"
	};
	
	public String getText() {
		return simplifiedChineseTexts[new Random().nextInt(simplifiedChineseTexts.length)];
	}

}
