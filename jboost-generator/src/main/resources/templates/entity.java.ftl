package ${package.Entity};

import java.time.LocalDateTime;
import java.io.Serializable;
<#if entityLombokModel>
import lombok.Data;
</#if>

/**
 * <p>
 *   <#if table.comment!?length gt 0>${table.comment}<#else>${entity}</#if>实体类
 * </p>
 *
* @Author ${author}
* @Date ${date}
* @Version 1.0
 */
<#if entityLombokModel>
@Data
</#if>
public class ${entity} implements Serializable {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.keyFlag>
        <#assign keyPropertyName="${field.propertyName}"/>
    </#if>

    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    <#-- 乐观锁注解 -->
    <#if (versionFieldName!"") == field.name>
    @Version
    </#if>
    <#-- 逻辑删除注解 -->
    <#if (logicDeleteFieldName!"") == field.name>
    @TableLogic
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if entityColumnConstant>
    <#list table.fields as field>
    public static final String ${field.name?upper_case} = "${field.name}";

    </#list>
</#if>
}
