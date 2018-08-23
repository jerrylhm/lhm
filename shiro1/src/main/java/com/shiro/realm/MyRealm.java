package com.shiro.realm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.shiro.dao.PermissionDaoImpl;
import com.shiro.dao.RoleDaoImpl;
import com.shiro.dao.UserDaoImpl;
import com.shiro.entity.Permission;
import com.shiro.entity.Role;
import com.shiro.entity.User;

public class MyRealm extends AuthorizingRealm {

	@Autowired
	private UserDaoImpl userDao;
	@Autowired
	private RoleDaoImpl roleDao;
	@Autowired
	private PermissionDaoImpl permissionDao;
	
	{
		super.setName("MyRealm");
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String username = (String) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
		Set<String> roles = coverRolesToSet(roleDao.findByUsername(username));
		List<Permission> ls = new ArrayList<Permission>();
		for (String role : roles) {
			ls.addAll(permissionDao.findByRole(role));
		}
		Set<String> permissions = coverPermissionsToSet(ls);
		authenticationInfo.setRoles(roles);
		authenticationInfo.setStringPermissions(permissions);
		return authenticationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = userDao.findByUsername(username);
		String password = user.getPassword();
		if(username == null || password == null) {
			throw new UnknownAccountException();
		}
		
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				username, password, getName());
		authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("aa"));
		return authenticationInfo;
	}
	
	private Set<String> coverRolesToSet(List<Role> ls) {
		Set<String> sets = new HashSet<String>();
		for (Role role : ls) {
			sets.add(role.getRole_name());
		}
		return sets;
	}
	
	private Set<String> coverPermissionsToSet(List<Permission> ls) {
		Set<String> sets = new HashSet<String>();
		for (Permission permission : ls) {
			sets.add(permission.getPermission());
		}
		return sets;
	}
	
	/**
	 * 清除权限缓存
	 */
	public void clearCache() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}
}
