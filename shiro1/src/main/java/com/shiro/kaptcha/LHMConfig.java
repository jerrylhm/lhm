package com.shiro.kaptcha;

import java.util.Properties;

import com.google.code.kaptcha.text.TextProducer;
import com.google.code.kaptcha.util.Config;

public class LHMConfig extends Config{

	public LHMConfig(Properties properties) {
		super(properties);
	}

	@Override
	public TextProducer getTextProducerImpl() {
		return new LHMText();
	}

	
}
