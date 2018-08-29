package creator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/error")
public class ErrorAction {
	

	
	@RequestMapping(value = "404")
	public String notFound(HttpServletRequest request){
		return "error/404";
	}

    @RequestMapping(value="500")
    public String systemError(HttpServletRequest request){
    	return "error/500";
    }
	

}
