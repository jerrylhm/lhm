package com.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

import com.shiro.realm.MyRealm;

public class AuthenticatedTest {

	@Test
	public void testAuthenticated() {
//		SimpleAccountRealm sar = new SimpleAccountRealm();
//		sar.addAccount("admin", "123456");
		
//		Factory<org.apache.shiro.mgt.SecurityManager> factory = 
//				new IniSecurityManagerFactory("classpath:shiro.ini");
//				new IniSecurityManagerFactory("classpath:shiro-roles.ini");
//		org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
		
		//Shiro提供的CredentialsMatcher加密验证工具

		HashedCredentialsMatcher hcm = new HashedCredentialsMatcher();
		hcm.setHashAlgorithmName("md5");
		hcm.setHashIterations(2);

		
		MyRealm myRealm = new MyRealm();
		//设置realm的验证工具
		myRealm.setCredentialsMatcher(hcm);
		
		DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm);
		Md5Hash md5 = new Md5Hash("admin","aa");
		System.out.println(md5.toString());
		SecurityUtils.setSecurityManager(securityManager);
		
		Subject subject = SecurityUtils.getSubject();
		AuthenticationToken at = new UsernamePasswordToken("admin", "123456");
        try {
            //4、登录，即身份验证
            subject.login(at);
        } catch (AuthenticationException e) {
            //5、身份验证失败
        	e.printStackTrace();
        	System.out.println("身份验证失败");
        }
        Assert.assertEquals(true, subject.isAuthenticated());
        
//		System.out.println("是否拥有角色role1:" + subject.hasRole("role1"));
//		System.out.println("是否拥有role1的权限create:" + subject.isPermitted("user:insert"));
//		System.out.println("是否拥有role1的权限create和update:" + subject.isPermittedAll("user:insert","user:update"));
		subject.logout();
	}
}
