package cn.jboost.base.common.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.ObjectId;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author ronwxy
 * @Date 2020/7/2 9:36
 * @Version 1.0
 */
public class WebUtil {

    public static final String REQ_ID_HEADER = "Req-Id";
    private static final String UNKNOWN = "unknown";

    private static final ThreadLocal<String> reqIdThreadLocal = new ThreadLocal<>();

    public static void setRequestId(String requestId) {
        reqIdThreadLocal.set(requestId);
    }

    public static String getRequestId(){
        String requestId = reqIdThreadLocal.get();
        if(requestId == null) {
            requestId = ObjectId.next();
            reqIdThreadLocal.set(requestId);
        }
        return requestId;
    }

    public static void removeRequestId() {
        reqIdThreadLocal.remove();
    }

    public static String findWebParam(String paramName) {
        HttpServletRequest request = currentRequest();
        String paramCandidate = request.getParameter(paramName);
        if (paramCandidate == null) {
            paramCandidate = request.getHeader(paramName);
        }
        if (paramCandidate == null) {
            Object attr = request.getAttribute(paramName);
            if (attr != null) {
                paramCandidate = attr.toString();
            }
        }
        return paramCandidate;
    }

    public static HttpServletRequest currentRequest() {
        return ((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static HttpServletResponse currentResponse() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 获取ip地址
     * @return
     */
    public static String getClientIP() {
        HttpServletRequest request = currentRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip)?"127.0.0.1":ip;
    }

    public static void outputJson(Object object, boolean success, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", object);
        result.put("success", success);
        outputJson(result, request, response);
    }

    public static void outputJson(Object object) {
        outputJson(object, currentRequest(), currentResponse());
    }

    public static void outputJson(Object object, HttpServletRequest request, HttpServletResponse response) {
        String contentType = "application/json;charset=UTF-8";
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");

        try (Writer writer = response.getWriter()) {
            JSONUtil.toJsonStr(object, writer);
            writer.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void outputText(Object object) {
        outputText(object, currentRequest(), currentResponse());
    }

    public static void outputText(Object object, HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain;charset=" + StandardCharsets.UTF_8.name());
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");

        try (OutputStream out = response.getOutputStream()) {
            String text = object instanceof String ? (String) object : object.toString();
            IoUtil.write(out,StandardCharsets.UTF_8,true,text);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void outputBinary(String originalFilename, InputStream in, String contentType) throws IOException {
        outputBinary(originalFilename, in, contentType, currentRequest(), currentResponse());
    }

    public static void outputBinary(String originalFilename, InputStream in, String contentType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String agent = request.getHeader("USER-AGENT");
        String filename;
        if (agent.contains("MSIE") || agent.contains("Trident")) {
            filename = URLEncoder.encode(originalFilename, "UTF-8");
        } else { // specialized for ie 11 below
            filename = new String(originalFilename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        if (!StringUtils.isEmpty(filename)) {
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
        } else {
            response.setHeader("Content-Disposition", "inline");
        }
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setHeader("Pragma", "no-cache");
        try (OutputStream out = response.getOutputStream(); InputStream inputStream = in) {
            IOUtils.copy(inputStream, out);
            out.flush();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
