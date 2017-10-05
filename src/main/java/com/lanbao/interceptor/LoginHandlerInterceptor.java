package com.lanbao.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lanbao.entity.TbUser;
import com.lanbao.entity.User;
import com.lanbao.utils.Const; 
/**
 * 
* 类名称：LoginHandlerInterceptor.java
* 类描述： 
* @author lanbao
* 作者单位： 
* 联系方式：
* 创建时间：2015年1月1日
* @version 1.6
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub 
		String path = request.getServletPath(); 
		if(path.matches(Const.NO_INTERCEPTOR_PATH)){
			return true;
		}else{
			//return true;
			//shiro管理的session
			Subject currentUser = SecurityUtils.getSubject();  
			Session session = currentUser.getSession();
			TbUser user = (TbUser)session.getAttribute(Const.SESSION_USER);
			if(user!=null){
				path = path.substring(1, path.length());
				/*boolean b = Jurisdiction.hasJurisdiction(path);
				if(!b){
					response.sendRedirect(request.getContextPath() + Const.LOGIN);
				}*/
				return true;
			}else{
				//登陆过滤
				response.sendRedirect(request.getContextPath() + Const.LOGIN);
				return false;		
				//return true;
			} 
		}
	}
	
}
