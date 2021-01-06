package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import Log;
<#if swagger2>
import io.swagger.annotations.*;
</#if>
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
<#else>
import ${package.Service}.${table.serviceImplName};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ${package.Entity}.${entity};
</#if>
import ${pojoPackage}.dto.${entity}DTO;

import java.io.Serializable;

/**
* <p>
*   <#if table.comment?default("")?trim?length gt 1>${table.comment}<#else>${entity}</#if> Controller类
* </p>
*
* @Author ${author}
* @Date ${date}
* @Version 1.0
*/
<#if swagger2>
@Api(tags = "<#if table.comment?default("")?trim?length gt 1>${table.comment}<#else>${entity}</#if>管理")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Log
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass}<${entity}, ${entity}DTO, ${entity}Query> {
<#else>
public class ${table.controllerName} {

    @Autowired
    private ${table.serviceImplName} ${table.serviceImplName?uncap_first};

    @ApiOperation(value = "新增")
    @PostMapping
    public ${entity}DTO create(@Validated @RequestBody ${entity}DTO dto) {
        return ${table.serviceImplName?uncap_first}.create(dto);
    }

    @ApiOperation(value = "修改")
    @PutMapping
    public void update(@Validated @RequestBody ${entity}DTO dto) {
        ${table.serviceImplName?uncap_first}.update(dto);
    }

    @ApiOperation(value = "根据ID查询")
    @GetMapping("{id}")
    public ${entity}DTO findById(@PathVariable("id") Serializable id) {
        return ${table.serviceImplName?uncap_first}.findById(id);
    }


    @ApiOperation(value = "根据条件分页查询")
    @GetMapping
    public IPage<${entity}DTO> findByCondition(@RequestParam(value="name")String name,
                                               @RequestParam(value="current")Integer current,
                                               @RequestParam(value="size")Integer size) {
        Wrapper queryWrapper = new LambdaQueryWrapper<${entity}>();
        return ${table.serviceImplName?uncap_first}.findByWrapper(queryWrapper, new Page(current, size));
    }

    @ApiOperation(value = "根据ID删除")
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable("id") Serializable id) {
        ${table.serviceImplName?uncap_first}.deleteById(id);
    }
</#if>

}