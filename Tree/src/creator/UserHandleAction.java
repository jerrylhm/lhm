package creator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/userHandle")
public class UserHandleAction {

		public String get(String urlStr) {
			String result = "";
			BufferedReader in = null;
			PrintWriter out = null;
			try {
				URL url = new URL(urlStr);
				URLConnection urlCon = url.openConnection();
				urlCon.connect();
				in = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				String param;
				while( (param = in.readLine()) != null ) {
					result += param;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {			
					try {
						
						if(in != null) {
						in.close();
						}
						if(out != null) {
							out.close();
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
			}
			return result;
		}
	
      @RequestMapping("")
      public String index(HttpServletRequest request, Integer nodeId, Model model) {
    	  model.addAttribute("nodeId", nodeId);
    	  String path = request.getContextPath();
    	  String basePath = request.getScheme() + "://"
    				+ request.getServerName() + ":" + request.getServerPort()
    				+ path + "/";
    	  String valueJson = get(basePath + "docking/getValuesByNodeId?nodeId=" + nodeId);
    	  String userHandleJson = get(basePath + "docking/getUserHandle?nodeId=" + nodeId);
    	  if(userHandleJson == null || userHandleJson.equals("")) {
    		  
    	  }else {
    		  
    	  }
    	  
    	  model.addAttribute("userHandleJson", userHandleJson);
    	  model.addAttribute("valueJson", valueJson);
    	  return "handle/user_handle";
      }
}
