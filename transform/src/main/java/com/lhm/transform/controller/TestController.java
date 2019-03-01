package com.lhm.transform.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("")
public class TestController {
	
	private static ConcurrentHashMap<String, Process> processMap = new ConcurrentHashMap<String, Process>();
	private static ConcurrentHashMap<String, OutputHandle> infoMap = new ConcurrentHashMap<String, OutputHandle>();
	private static ConcurrentHashMap<String, OutputHandle> errorMap = new ConcurrentHashMap<String, OutputHandle>();
	private static List<String> errorList = new ArrayList<>();
	
	public static ConcurrentHashMap<String, OutputHandle> getErrorMap() {
		return errorMap;
	}

	public static ConcurrentHashMap<String, Process> getProcessMap() {
		return processMap;
	}

	public static void setProcessMap(ConcurrentHashMap<String, Process> processMap) {
		TestController.processMap = processMap;
	}

	public static ConcurrentHashMap<String, OutputHandle> getInfoMap() {
		return infoMap;
	}

	public static void setInfoMap(ConcurrentHashMap<String, OutputHandle> infoMap) {
		TestController.infoMap = infoMap;
	}

	public static List<String> getErrorList() {
		return errorList;
	}

	public static void setErrorList(List<String> errorList) {
		TestController.errorList = errorList;
	}

	public TestController() {
		errorList.add("does not contain any stream");
		errorList.add("Protocol not found");
		errorList.add("Unknown error occurred");
		errorList.add("muxing overhead");
	}
	
	@RequestMapping("index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("transform")
	@ResponseBody
	public String aa(String rtsp, String rtmp, String id) {
		try {
			startTransform(rtsp, rtmp, id);
		} catch (Exception e) {
			destroy(id);
			e.printStackTrace();
		}
		return "ok";
	}
	
	@RequestMapping("destroy")
	@ResponseBody
	public String bb(String id) {
		destroy(id);
		return "ok";
	}
	
	public List<String> getTransformCommand(String rtspUrl, String rtmpUrl) {
		List<String> command = new ArrayList<String>();
		command.add("D:\\ffmpeg\\ffmpeg.exe");
		command.add("-i");
		command.add(rtspUrl);
		command.add("-f");
		command.add("flv");
		command.add("-r");
		command.add("60");//帧数
		command.add("-s");
		command.add("1280x720");//分辨率
		command.add("-an");
		command.add(rtmpUrl);
		System.out.println(command);
		
		return command;
	}
	
	public void startTransform(String rtspUrl, String rtmpUrl, String id) throws IOException {
		List<String> command = getTransformCommand(rtspUrl, rtmpUrl);
		ProcessBuilder pb = new ProcessBuilder(command);
		Process process = pb.start();
		processMap.put(id, process);
		System.out.println("开始转换");
		OutputHandle outputHandle1 = new OutputHandle(process.getInputStream(),"info", id);
		outputHandle1.start();
		infoMap.put(id, outputHandle1);
		OutputHandle outputHandle2 = new OutputHandle(process.getErrorStream(),"error", id);
		outputHandle2.start();
		errorMap.put(id, outputHandle2);
	}
	
	public static void destroy(String id) {	
		System.out.println("销毁线程");
		try {
			processMap.remove(id).destroy();
			infoMap.remove(id).destroy();
			errorMap.remove(id).destroy();
		} catch (NullPointerException e) {
			System.out.println("未创建该线程");
		}
		
	}
}
