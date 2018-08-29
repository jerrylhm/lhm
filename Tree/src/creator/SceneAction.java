package creator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.creator.db.nodeattr.NodeAttr;
import com.creator.db.permission.Permission;
import com.creator.db.scene.Scene;
import com.creator.db.scene.SceneDao;
import com.creator.db.scene.SceneDto;
import com.creator.db.template.TemplateDto;
import com.creator.db.tree.Tree;
import com.creator.db.tree.TreeDao;
import com.creator.db.type.Type;
import com.creator.db.type.TypeDao;
import com.creator.db.type.TypeEnum;
import com.creator.util.ZTree;
import com.fasterxml.jackson.databind.util.BeanUtil;

@Controller
@RequestMapping("/scene")
public class SceneAction {
	
	@Autowired
	private SceneDao sceneDao;
	@Autowired
	private TreeDao treeDao;
	@Autowired 
	private TypeDao typeDao;
	
	/**
	 * 场景管理首页
	 * @param request
	 * @param sceneDto
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest request, SceneDto sceneDto, Model model) {
		Integer userId = (Integer) request.getSession().getAttribute("USERID");
	    model.addAttribute("like", sceneDto.getLike());	   
	    //设置每页显示数据数量
		sceneDto.getPage().setPageNumber(10);
		//该用户创建的树节点
	    List<Tree> ls = treeDao.queryTreeNodeByUserId(userId);
	    List<Integer> treesStr = new ArrayList<Integer>();
	    for (Tree tree : ls) {
		   treesStr.add(tree.getNode_id());
	    }
	    sceneDto.setTreesStr(StringUtils.join(treesStr.toArray(),","));
		List<SceneDto> scenes = sceneDao.queryByPage(sceneDto);
		for (SceneDto scene : scenes) {
			scene.setTreeName(treeDao.queryById(scene.getSc_treeid()).get(0).getNode_name()); 
			String actionIds = scene.getSc_action();
			String[] actionArray = actionIds.split(",");
			String actionName = "";
			for (String action : actionArray) {
				if(!action.equals("")) {
					List<Tree> trees = treeDao.queryById(Integer.valueOf(action));
					if(trees.size() > 0) {
						actionName =  actionName + trees.get(0).getNode_name() + ",";
					}
				}
			}
			if(actionName.endsWith(",")) {
				actionName = actionName.substring(0, actionName.length()-1);
			}
			scene.setActionName(actionName);
		}
		/*
         * 获取所有树节点列表
         */
        List<Map<String,Object>> trees = treeDao.queryRootByUserId(userId);
        model.addAttribute("trees", trees);
		model.addAttribute("scenes", scenes);
		model.addAttribute("searchParam", sceneDto);
		return "scene/index";
	}
	
	/**
	 * 根据树id获取ZTree简单json数据
	 * @param request
	 * @param treeId
	 * @return
	 */
	@RequestMapping("getZtreeJSON")
	@ResponseBody
	public JSONArray getZtreeJSON(HttpServletRequest request,Integer treeId) {
		//项目路径
				String path = request.getContextPath();
				String basePath = request.getScheme() + "://"
						+ request.getServerName() + ":" + request.getServerPort()
						+ path + "/";
				 //返回到前台的zTree的json数据
		        List<ZTree> zTrees = new ArrayList<ZTree>();	
		        	//该树的所有节点信息
		        	List<Tree> trees = treeDao.queryByTreeId(treeId);
		        	for (Tree tree2 : trees) {
		        		//把节点信息放入ztree对象里面
		        		ZTree ztree = new ZTree();
						ztree.setId(tree2.getNode_id());
						ztree.setpId(tree2.getNode_pid());
						ztree.setName(tree2.getNode_name());
						if(!tree2.getNode_type().equals(TypeEnum.ACTION.getId()) && !tree2.getNode_type().equals(TypeEnum.ENUM.getId())
								&& !tree2.getNode_type().equals(TypeEnum.ENUMACTION.getId())) {
							ztree.setChkDisabled(true);
						}
						//点击节点跳转的地址（暂时无法使用）
						ztree.setUrl(tree2.getNode_url());
						//点击节点跳转的地址（可以使用）
						ztree.setMyUrl(ztree.getUrl());
						//跳转窗口
						ztree.setTarget("treeValue");
						//是否拥有该节点权限
						ztree.setHasP(true);
						//是否是该树的创建者
						ztree.setIsCreator(true);
						Type type = typeDao.findById(tree2.getNode_type());
						//节点类型
						ztree.setType(type);
						//节点title
						ztree.setTitle(tree2.getNode_name() + "(" + type.getType_name_cn() + ")");
						//节点sn
						ztree.setSn(tree2.getNode_sn());
						//设备所属类
						ztree.setNodeClass(tree2.getNode_class());
						zTrees.add(ztree);
					}
		        //返回ztree的简单json数据
		        return JSONArray.fromObject(zTrees);
	}
	
	/**
	 * 添加场景
	 * @param request
	 * @param scene
	 * @param treeId
	 * @return
	 */
	@RequestMapping("addScene")
	@ResponseBody
	public String addScene(HttpServletRequest request,Scene scene,Integer treeId) {
		try {
			sceneDao.add(scene);
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}
	
	/**
	 * 删除场景
	 * @param request
	 * @param id
	 * @param treeId
	 * @return
	 */
	@RequestMapping("deleteScene")
	@ResponseBody
	public String deleteScene(HttpServletRequest request,int id,Integer treeId) {
		
		return String.valueOf(sceneDao.delete(id));
	}
	
	/**
	 * 更新场景
	 * @param request
	 * @param scene
	 * @param treeId
	 * @return
	 */
	@RequestMapping("updateScene")
	@ResponseBody
	public String updateScene(HttpServletRequest request,Scene scene,Integer treeId) {
		List<Scene> scs = sceneDao.findById(scene.getSc_id());
		if(scs.size() > 0) {
			Scene sc = scs.get(0);
			sc.setSc_name(scene.getSc_name());
			sc.setSc_action(scene.getSc_action());
			return String.valueOf(sceneDao.update(sc));
		}
		return "0";
	}
}
