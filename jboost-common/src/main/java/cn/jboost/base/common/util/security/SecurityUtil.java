package cn.jboost.base.common.util.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * @Author ronwxy
 * @Date 2020/5/22 18:35
 * @Version 1.0
 */
public class SecurityUtil {

    public static final String SUPER_ADMIN = "super-admin";

    /**
     * 获取
     * 用户登录： {@link JwtUser}
     *
     * @return
     */
    public static UserDetails getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }

    /**
     * 获取
     * 用户名（用户登录）
     * mac地址 （机器人登录）
     * @return 用户名
     */
    public static String getUsername() {
        return getUserDetails().getUsername();
    }

    /**
     * 获取
     * 用户id （用户登录）
     * 机器id （机器人登录）
     * @return 用户id
     */
    public static Long getUserId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("id", Long.class);
    }

    /**
     * 获取部门id，非后台用户可能为null
     *
     * @return
     */
    public static Long getDeptId() {
        Object obj = getUserDetails();
        JSONObject json = new JSONObject(obj);
        return json.get("deptId", Long.class);
    }

    public static boolean isSuperAdmin(){
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) getUserDetails().getAuthorities();
        if(CollectionUtil.isNotEmpty(authorities)) {
            return authorities.stream().filter(a -> SUPER_ADMIN.equalsIgnoreCase(a.getAuthority())).findAny().isPresent();
        }
        return false;
    }
}
