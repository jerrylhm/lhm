package com.lhm.interceptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.function.BiConsumer;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;

import com.lhm.entity.Page;


@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class})})
public class PageInterceptor implements Interceptor{

	//拦截器分页：目标是修改PreparedStatementHandler里的Boundql的sql语句成分页sql语句
	public Object intercept(Invocation invocation) throws Throwable {
		
        if(invocation.getTarget() instanceof RoutingStatementHandler){  
        	//捕获的对象就是RoutingStatementHandler
            RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();    
            
            StatementHandler delegate = null;
            MappedStatement mappedStatement = null;
            Configuration configuration = null;
            ParameterHandler parameterHandler = null;
            Page page = null;
            
            //利用反射获取该RoutingStatementHandler所使用的delegate，因为拦截的是prepare，所以delegate是PreparedStatementHandler
			delegate = (StatementHandler) getAttr(statementHandler.getClass(), "delegate", statementHandler);
			//这个就是修改的目标boundsql
            BoundSql boundSql = delegate.getBoundSql();
            
            //------第一步需要计算出总数据条数是多少------
            
            //param是sql语句传入的参数，单个参数的时候param就是该参数，多个参数的时候param是一个map
            Object param = boundSql.getParameterObject();
            //根据param拿到里面的参数page
            page = getPage(param);
            //当含有page参数的时候表示它需要分页
            if(page != null) {
                int countSqlIndex = boundSql.getSql().indexOf("from");
                countSqlIndex = countSqlIndex <= 0?boundSql.getSql().indexOf("FROM"):countSqlIndex;
                //这个就是查询总数据条数的sql，里面可能含有？，这表示不能直接运行该sql语句，必须传入和原sql一样的参数
                //既然要运行sql就需要jdbc中的PreparedStatement的executeQuery()方法，在这之前需要设置好SQL参数
                //mybatis的parameterHandler中的setParameters可以为PreparedStatement快速注入参数,所以下面代码的目标是生成parameterHandler
                String countSql = "SELECT COUNT(*) " + boundSql.getSql().substring(countSqlIndex, boundSql.getSql().length());
                Connection connection = (Connection)invocation.getArgs()[0]; 
                //parameterHandler是mappedStatement的一个属性，所以先生成mappedStatement
                //mappedStatement是PreparedStatementHandler的父类BaseStatementHandler的一个属性，利用反射获取
                mappedStatement = (MappedStatement)getAttr(PreparedStatementHandler.class, "mappedStatement", delegate);
                //获取parameterHandler有2种方法，第一种是通过configuration的方法，第二种是通过反射获取mappedStatement中的属性
                //这里使用第一种方法，调用configuration的newParameterHandler方法
    			configuration = mappedStatement.getConfiguration();
                BoundSql CountBoundSql = new BoundSql(configuration, countSql, boundSql.getParameterMappings(), param);
                parameterHandler = configuration.newParameterHandler(mappedStatement, param, CountBoundSql);
                PreparedStatement p = connection.prepareStatement(countSql);
                parameterHandler.setParameters(p);
                ResultSet rs = p.executeQuery();
                if(rs.next()) {
                	int count = rs.getInt(1);
                	page.setTotalNumber(count);
                }
              //-----------------------------------------
                
              //------第二步生成分页sql并替换掉boundSql的sql------
                String pageSql = boundSql.getSql() + " LIMIT " + page.getPageNumber() * (page.getCurrentPage() - 1) + "," + page.getPageNumber();
                setAttr(boundSql.getClass(), "sql", boundSql, pageSql);
              //--------------------------------------------
            }
        }  
        return invocation.proceed(); 
	}

	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	public void setProperties(Properties arg0) {
		
	}

	/**
	 * 从mybatis sql语句参数中获取page
	 * @param paramterObject
	 * @return
	 */
	public Page getPage(Object paramterObject) {
		Page page = null;
        if(paramterObject instanceof ParamMap) {
        	ParamMap paramMap = (ParamMap)paramterObject;
        	for(Object key:paramMap.keySet()) {
        		Object p = paramMap.get(key);
        		if(!key.equals("param") && p instanceof Page) {
        			page = (Page) p;
        		}
        	}
        }else if(paramterObject instanceof Page) {
        	page = (Page) paramterObject;
        }
		return page;
	}
	
	/**
	 * 利用反射获取对象属性值
	 * @param c 需要反射的类
	 * @param fieldName	属性名称
	 * @param obj	反射的对象
	 * @return	属性的值
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public Object getAttr(Class c, String fieldName, Object obj) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = c.getDeclaredFields();
        Object result = null;
        for (Field field : fields) {
			if(field.getName().equals(fieldName)) {
				field.setAccessible(true);
				result = field.get(obj);
			}
		}
        if(result == null && c.getSuperclass() != null) {
        	result = getAttr(c.getSuperclass(), fieldName,obj);
        }
        return result;
	}
	
	/**
	 * 利用反射设置对象属性
	 * @param c 需要反射的类
	 * @param fieldName	属性名称
	 * @param obj	反射的对象
	 * @param value	属性的新值
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public boolean setAttr(Class c, String fieldName, Object obj, Object value) throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
			if(field.getName().equals(fieldName)) {
				field.setAccessible(true);
				field.set(obj, value);
				return true;
			}
		}
        return false;
	}
}