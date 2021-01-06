package ${package.Service};

import org.springframework.stereotype.Service;
import ${superServiceImplClassPackage};
import java.io.Serializable;

import ${package.Entity}.${entity};
import ${pojoPackage}.dto.${entity}DTO;

/**
 * <p>
 *   <#if table.comment?default("")?trim?length gt 1>${table.comment}<#else>${entity}</#if>服务类
 * </p>
 *
* @Author ${author}
* @Date ${date}
* @Version 1.0
 */
@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${entity}, ${entity}DTO> {

    /**
    * 新增
    *
    * @param dto
    * @return
    */
    public ${entity}DTO create(${entity}DTO dto) {
        return super.create(dto);
    }

    /**
    * 根据ID更新
    *
    * @param dto
    * @return
    */
    public void update(${entity}DTO dto) {
        super.update(dto);
    }

    /**
    * 根据ID查询
    *
    * @param id
    * @return
    */
    public ${entity}DTO findById(Serializable id) {
        return super.findById(id);
    }

    /**
    * 根据ID删除
    *
    * @param id
    * @return
    */
    public int deleteById(Serializable id) {
        return super.deleteById(id);
    }

}