package cn.jboost.base.common.constant;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @Author ronwxy
 * @Date 2020/5/19 14:28
 * @Version 1.0
 */
public enum UserStatusEnum implements IEnum<Integer> {
    ENABLED(1), //正常
    DISABLED(0); //禁用

    private int value;


    UserStatusEnum(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
