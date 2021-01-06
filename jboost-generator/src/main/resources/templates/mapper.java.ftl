package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};

/**
 * <p>
 *   <#if table.comment!?length gt 0>${table.comment}<#else>${entity}</#if> Mapper接口类
 * </p>
 *
* @Author ${author}
* @Date ${date}
* @Version 1.0
 */
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
