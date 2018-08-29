package com.shiro.controller;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.shiro.realm.MyRealm;
import com.shiro.service.EhcacheService;
import com.shiro.util.JedisUtils;

@Controller
@RequestMapping("")
public class ShiroAction {
	
	@Resource 
	private JedisUtils jedisUtils;
	@Autowired
	private MyRealm myRealm;
	@Autowired
	private EhCacheManager shiroCacheManager;
	
//	@Autowired
//	private CacheManager shiroCacheManager;
	
	@Autowired
	private EhcacheService ehcacheService;
	
    @Autowired  
    private Producer captchaProducer = null;  
	
	@RequestMapping("/login")
	public String login() {
		
		return "/login";
	}
	
	@RequestMapping(value="/dologin", method=RequestMethod.POST)
	@ResponseBody
	public String dologin(HttpServletRequest request, boolean rm) {
		
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken at = new UsernamePasswordToken("admin", "123456");
		at.setRememberMe(rm);
		subject.login(at);	
		System.out.println(request.getSession().getId());
		Cache<Object, Object> cache = shiroCacheManager.getCache("redisCache");
		cache.remove(subject.getPrincipal().toString());
		return "suc";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.POST)
	@ResponseBody
	public String logout(HttpServletRequest request) {
		
		Subject subject = SecurityUtils.getSubject();
		subject.logout();	
		return "suc";
	}
	
//	@RequiresRoles("role1")
	@RequestMapping(value="/role", method=RequestMethod.GET)
	@ResponseBody
//	@RequiresPermissions("user:insert")
	public String role() throws InterruptedException {
			Subject subject = SecurityUtils.getSubject();
			System.out.println("进来啦！！");
			myRealm.clearCache();
//	        System.out.println("第一次调用：" + ehcacheService.getTimestamp("param"));
//	        Thread.sleep(2000);
//	        System.out.println("2秒之后调用：" + ehcacheService.getTimestamp("param"));
//	        Thread.sleep(4000);
//	        System.out.println("再过4秒之后调用：" + ehcacheService.getTimestamp("param"));
			return "suc";
		
	}
	
	@RequestMapping("/test1")
	public String test1() {

		return "/test1";
	}
	
	@RequestMapping("/test2")
	public String test2() {

		return "/test2";
	}
	
	@RequestMapping("/403")
	public String sso() {

		return "/403";
	}
  
    @RequestMapping(value = "/captcha-image")  
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        HttpSession session = request.getSession();  
        response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");  
        
        String capText = captchaProducer.createText();  
        System.out.println(capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);  
        System.out.println("验证码: " + capText ); 
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;  
    }  
    
    
    @RequestMapping(value = "/captchaVerify",method=RequestMethod.POST)  
    @ResponseBody
    public Map<String,Object> captchaVerify( String kaptchaCode,HttpServletRequest request, HttpServletResponse response) throws Exception {  
    	Map<String,Object> result=new HashMap<String, Object>();
    	System.out.println(kaptchaCode);
    	HttpSession session = request.getSession();  
    	String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY); 
    	System.out.println("kaptchaCode:"+kaptchaCode+"  ;code:"+code);
    	if(StringUtils.isNotBlank(kaptchaCode)&&kaptchaCode.equalsIgnoreCase(code)){
    		result.put("isOK", "OK");
    	}else{
    		result.put("isOK", "WRONG");
    	}
    	return result;
    } 
}
