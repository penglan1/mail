package pers.penglan.mail.controller.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pers.penglan.mail.controller.session.UserSessionOperator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author PENGL
 * 2020-03-28
 */
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*session检查*/
        HttpSession session = request.getSession(false);
        if (session == null || UserSessionOperator.getUserGlobalInfo(session) == null) {
            if (request.getMethod().equalsIgnoreCase("get"))
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            else {
                String jsonMessage = "{\"status\":\"failure\",\"message\":\"当前会话已失效，请刷新页面进行重新登入\"}";
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(jsonMessage);
            }
            return false;
        }

        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //LogManager.getLogger("mail").debug(ExceptionUtility.getTrace(ex));
    }
}
