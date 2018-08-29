package com.creator.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;


/**
 * 此工具类用于模拟客户端或者设备发送get或post请求到指定的网址 
 *
 */
public class HttpUtil {
	
	/**
	 * 发送get请求到指定网址
	 * @param url 要get的完整网址
	 * @param chaset 字符集字符串
	 * @return String 返回get请求结果
	 * @throws Exception 
	 */
	public static String httpGet(String url,String charset) throws Exception {
		URL connectionUrl = new URL(url);
		URLConnection connection = connectionUrl.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		httpURLConnection.setRequestProperty("Accept-Charst", charset);
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
		if(httpURLConnection.getResponseCode() >= 300) {
			throw new Exception("Http连接超时！应答码：" + httpURLConnection.getResponseCode());
		}
		
		return getReturn(httpURLConnection,charset);
	}
	
	/**
	 * 发送httpPost请求到指定网址
	 * @param url 要post的完整网址
	 * @param chaset 字符集字符串
	 * @param paramMap 参数集合
	 * @return String 返回post请求结果
	 * @throws Exception 
	 */
	public static String httpPost(String url,String charset,Map<String,String> paramMap) throws Exception {
		URL connectionUrl = new URL(url);
		URLConnection connection = connectionUrl.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
		//设置参数
		StringBuffer paramBuffer = new StringBuffer();
		if(paramMap != null) {
			Iterator<String> iterator = paramMap.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				String value = "";
				if(paramMap.get(key) != null) {
					value = paramMap.get(key);
				}
				paramBuffer.append(key).append("=").append(value);
				if(iterator.hasNext()) {
					paramBuffer.append("&");
				}
			}
		}
		System.out.println("参数：" + paramBuffer.toString());
		
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setRequestProperty("Accept-Charst", charset);
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpURLConnection.setRequestProperty("Content-Length", String.valueOf(paramBuffer.length()));
		
		OutputStream outputStream = null;
		OutputStreamWriter writer = null;
		String result = "";
		try {
			outputStream = httpURLConnection.getOutputStream();
			writer = new OutputStreamWriter(outputStream, charset);
			
			writer.write(paramBuffer.toString());
			writer.flush();
			
			//判断是否post上
			if(httpURLConnection.getResponseCode() >= 300) {
				throw new Exception("Http连接超时！应答码：" + httpURLConnection.getResponseCode());
			}
			result = getReturn(httpURLConnection, charset);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(writer != null) {
				try {
					writer.close();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		return result;
	}
	
	/*
	 * 获得返回的字符串
	 */
	private static String getReturn(URLConnection connection,String charset) {
		InputStream inputStream = null;
		BufferedReader reader = null;
		
		StringBuffer result = new StringBuffer();
		String line = null;
		
		try {
			inputStream = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inputStream,charset));
			while((line = reader.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (Exception e2) {
				}
			}
			
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e2) {
				}
			}
		}
		return result.toString();
	}

}
