package ${pojoPackage}.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
</#if>

/**
* <p>
*   <#if table.comment!?length gt 0>${table.comment}<#else>${entity}</#if> DTO类
* </p>
*
* @Author ${author}
* @Date ${date}
* @Version 1.0
*/
<#if entityLombokModel>
@Data
</#if>
<#if swagger2>
@ApiModel(value="${entity}对象", description="${table.comment!}")
</#if>
public class ${entity}DTO implements Serializable {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    <#if swagger2>
    @ApiModelProperty(value = "${field.comment}",dataType = "${field.propertyType}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
}
