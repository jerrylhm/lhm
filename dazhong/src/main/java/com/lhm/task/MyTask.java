package com.lhm.task;

import org.springframework.stereotype.Component;

@Component("myTask")
public class MyTask {

	public void run() {
		System.out.println("定时任务执行了");
	}
}
