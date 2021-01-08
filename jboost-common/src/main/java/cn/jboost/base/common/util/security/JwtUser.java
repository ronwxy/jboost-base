package cn.jboost.base.common.util.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.jboost.base.common.constant.UserStatusEnum;
import cn.jboost.base.common.constant.UserTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ronwxy
 * @Date 2020/5/22 17:02
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails {
    private Long id;
    private String username;
    private UserTypeEnum userType;
    @JsonIgnore
    private String salt;
    @JsonIgnore
    private String password;
    private String email;
    private String phone;
    private UserStatusEnum status;
    private Long deptId;
    private List<String> authorityStrs;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtil.isEmpty(authorityStrs)) {
            return null;
        } else {
            return authorityStrs.stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserStatusEnum.ENABLED.equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatusEnum.ENABLED.equals(status);
    }
}
