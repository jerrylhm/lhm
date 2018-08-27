package creator;

import org.apache.log4j.Logger;

import com.creator.rest.CodeResult;
import com.creator.rest.ResultProcessor;

/**
 * 用于在处理请求时出错返回错误信息(只用于ajax返回值)
 *
 */
public class BaseAction {
	private Logger logger = Logger.getLogger(BaseAction.class);
	public CodeResult restProcessor(ResultProcessor processor) {
		CodeResult codeResult = null;
		try {
			codeResult = processor.process();
		}catch(Exception e) {
			e.printStackTrace();
			//程序出错，返回错误消息
			logger.error("程序出错！");
			return CodeResult.error();
		}
		return codeResult;
	}
}
