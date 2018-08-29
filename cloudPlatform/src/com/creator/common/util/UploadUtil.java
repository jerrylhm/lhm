package com.creator.common.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


public class UploadUtil {
	private static Logger logger = Logger.getLogger(UploadUtil.class);
	
	/**
	 * 上传文件方法
	 * @param request request请求
	 * @param preffix 文件名前缀,如 user_
	 * @param suffix  文件名后缀， 如  _用户名， 文件名组成是 [preffix + 时间戳  + suffix + 文件格式名]
	 * @param dir  文件名称，upload文件夹的下一级文件夹名称
	 * @param name 表单中文件的name
	 * @return 如果没有上传文件，返回null;上传则返回upload/....
	 * @throws Exception
	 */
	public static String uploadFile(HttpServletRequest request,String preffix,String suffix,String dir) throws Exception{
		String errorResult = null;
		if(request == null) {
			return errorResult;
		}
		
		if(preffix == null) {
			preffix = "";
		}
		if(suffix == null) {
			suffix = "";
		}
		
		if(dir == null) {
			dir = "";
		}
		CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
		logger.debug("multipart:" + multipartResolver);
		//如果没有文件，则返回null
		if(!multipartResolver.isMultipart(request)) {
			return errorResult;
		}
		MultipartRequest fileRequest = (MultipartRequest) request;
		//没有上传文件
		Iterator<String> nameIter = fileRequest.getFileNames();
		if(!nameIter.hasNext()) {
			return errorResult;
		}
		String name = nameIter.next();
		
		MultipartFile file = fileRequest.getFile(name);
		String originalFilename = file.getOriginalFilename();       //原来的文件名
		//获取完整文件名
		String fullname = preffix + System.currentTimeMillis() + suffix + getSuffix(originalFilename);
		logger.debug("完整文件名：" + fullname);
		
		//获取文件实际目录
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String basePath = request.getServletContext().getRealPath("");
		String dirPath = basePath + "/WEB-INF/views/public/upload/" + dir + "/" + format.format(new Date());
		
		//判断该文件夹是否存在,不存在则创建文件夹
		File dirFile = new File(dirPath);
		if(!dirFile.exists() || !dirFile.isDirectory()) {
			dirFile.mkdir();
		}
		//完整文件路径
		String fullFilePath = dirPath + "/" + fullname;
		logger.debug("完整文件路径：" + fullFilePath);
		file.transferTo(new File(fullFilePath));
		
		//分割路径
		String result = fullFilePath.substring(fullFilePath.lastIndexOf("upload"),fullFilePath.length());
		return result;
	}
	
	/**
	 * 上传文件，不加后缀
	 * @param request
	 * @param preffix
	 * @param suffix
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(HttpServletRequest request,String preffix,String dir) throws Exception {
		return uploadFile(request, preffix,"", dir);
	}
	
	/**
	 * 上传头像
	 * @return
	 * @throws Exception 
	 */
	public static String uploadHeadImage(HttpServletRequest request,String username) throws Exception {
		String filename = UploadUtil.uploadFile(request, "user_", username, "head-image");
		return filename;
	}
	
	public static String deleteHeadImage(HttpServletRequest request, String imageUrl) throws Exception {
		String basePath = request.getServletContext().getRealPath("");
		String fileName = basePath + "/WEB-INF/views/public/" + imageUrl;
		File file = new File(fileName);
	    if (!file.exists()) {
	        logger.debug("删除文件失败:" + fileName + "不存在！");
		    return null;
		} else {
		    if (file.isFile()) {
		        file.delete();
		    }else {
		    	logger.debug("删除文件失败:" + fileName + "不是文件！");
		    	return null;
		    }
		}
		return fileName;
	}
	
	/*
	 * 获取文件类型名
	 */
	public static String getSuffix(String filename) {
		if(filename == null) {
			return "";
		}
		//不包含.
		if(!filename.contains(".")) {
			return "";
		}
		String suffix = filename.substring(filename.lastIndexOf("."),filename.length());
		return suffix;
	}
}
