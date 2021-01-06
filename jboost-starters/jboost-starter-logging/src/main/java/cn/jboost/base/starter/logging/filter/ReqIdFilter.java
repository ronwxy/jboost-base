package cn.jboost.base.starter.logging.filter;

import cn.hutool.core.lang.ObjectId;
import cn.jboost.base.common.util.WebUtil;
import cn.jboost.base.starter.logging.util.LoggerConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;

/**
 * 在MDC中添加reqId过滤器
 *
 * @Author ronwxy
 * @Date 2020/5/28 18:35
 * @Version 1.0
 */
public class ReqIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String reqId = WebUtil.getRequestId();
        //没有则生成一个
        if (StringUtils.isEmpty(reqId)) {
            reqId = ObjectId.next();
        }
        //存入MDC中
        MDC.put(LoggerConstants.REQ_ID_MDC_KEY, reqId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }

    }

}
