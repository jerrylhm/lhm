package com.creator.junitTest;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.creator.db.scene.SceneDao;
import com.creator.db.template.TemplateDao;
import com.creator.db.tpcontent.TPContentDao;
import com.creator.db.tree.TreeDao;
import com.creator.db.type.TypeDao;
import com.creator.db.type.TypeEnum;
import com.creator.db.userhandle.UserHandleDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:Mysql_bean.xml"})
public class JunitTest {
 
	@Autowired
	private TreeDao treeDao;
    @Autowired
    private UserHandleDao userHandleDao;
	@Autowired
	private TypeDao typeDao;
    @Autowired
    private TemplateDao templateDao;
	@Autowired
	private TPContentDao tpContentDao;
	@Autowired
	private SceneDao sceneDao;
	
	@Test
	public void test() {
		for(TypeEnum t : TypeEnum.values()){
			System.out.println(t.getName());
		}
	}
	
}
