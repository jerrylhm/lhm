package com.lhm.transform.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputHandle extends Thread{

	private volatile boolean status = true;
	
	private BufferedReader br;
	
	private String type = null;
	
	private String id = null;
	
	public OutputHandle(InputStream is, String type, String id) {
		br = new BufferedReader(new InputStreamReader(is));
		this.type = type;
		this.id = id;
	}
	
	@Override
	public void run() {
		String msgLine = null;
		while(status) {
			try {
				if((msgLine = br.readLine()) != null) {
					System.out.println(type + "输出:" + msgLine);
					for(String error:TestController.getErrorList()) {
						if(msgLine.contains(error)) {
							TestController.destroy(id);
						}
					}

				}else {
					destroy();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		status = false;
	}

}
