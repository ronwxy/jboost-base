package ${pojoPackage}.dto.converter;

import ${pojoPackage}.dto.${entity}DTO;
import ${package.Entity}.${entity};
import BaseConverter;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
*   <#if table.comment!?length gt 0>${table.comment}<#else>${entity}</#if> 实体、DTO转换接口
* </p>
*
* @Author ${author}
* @Date ${date}
* @Version 1.0
*/
@Mapper(componentModel = BaseConverter.SPRING,uses = {},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ${entity}Converter extends BaseConverter<${entity}, ${entity}DTO>{
}

