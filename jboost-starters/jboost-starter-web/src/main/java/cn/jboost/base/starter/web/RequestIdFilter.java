package cn.jboost.base.starter.web;

import cn.hutool.core.lang.ObjectId;
import cn.jboost.base.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 添加RequestId，下游可通过 @{WebUtil.getRequestId()} 获取
 * @Author ronwxy
 * @Date 2020/7/2 9:35
 * @Version 1.0
 */
public class RequestIdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String reqId = httpServletRequest.getHeader(WebUtil.REQ_ID_HEADER);
        //没有则生成一个
        if (StringUtils.isEmpty(reqId)) {
            reqId = ObjectId.next();
        }
        WebUtil.setRequestId(reqId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            WebUtil.removeRequestId();
        }
    }
}
