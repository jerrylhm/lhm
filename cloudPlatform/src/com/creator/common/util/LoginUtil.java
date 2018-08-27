package com.creator.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.creator.db.user.User;
import com.creator.db.user.UserDao;

@Component
public class LoginUtil {

	private static UserDao userDao;

	@Autowired
	public void setUserDao(UserDao userDao) {
		LoginUtil.userDao = userDao;
	}

	public static boolean checkPassword(String username,String password) {
		User user = userDao.findByUsername(username);
		if(user != null && user.getUr_status() != 0) {
			if(user.getUr_password().equals(MD5Util.md5(password))) {
				return true;
			}else {
				return false;
			}			
		}else {
			return false;
		}
	}
	
}
