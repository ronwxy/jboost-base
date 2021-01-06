package cn.jboost.base.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import cn.jboost.base.generator.config.ConfigUtil;
import cn.jboost.base.generator.config.ConfigProperties;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ronwxy
 * @Date 2020/6/10 12:29
 * @Version 1.0
 */
public class Application {

    public static void main(String[] args) {
        // 读取配置
        ConfigUtil.loadProperties();
        ConfigProperties properties = ConfigUtil.getConfigProperties();

        AutoGenerator generator = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = getGlobalConfig(properties);
        generator.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = getDataSourceConfig(properties);
        generator.setDataSource(dsc);

        // 包配置
        PackageConfig pc = getPackageConfig(properties);
        generator.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = getInjectionConfig(gc, pc);
        generator.setCfg(cfg);

        TemplateConfig tc = new TemplateConfig().setService(null); //不生成service接口
        if(properties.getGenerateXml() == null || !properties.getGenerateXml()) {
            tc.setXml(null);
        }
        generator.setTemplate(tc);

        // 策略配置
        StrategyConfig strategy = getStrategyConfig(properties);
        generator.setStrategy(strategy);

        // 选择 freemarker 引擎需要指定如下，注意 pom 依赖必须有！
        generator.setTemplateEngine(new FreemarkerTemplateEngine());
        generator.execute();
    }



    // 全局配置
    private static GlobalConfig getGlobalConfig(ConfigProperties properties) {
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(properties.getOutputDir());
        gc.setAuthor(properties.getAuthor());
        gc.setSwagger2(true);
        gc.setOpen(false);
        gc.setServiceImplName("%sService");
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setFileOverride(properties.getFileOverride());
        return gc;
    }

    // 数据源配置
    private static DataSourceConfig getDataSourceConfig(ConfigProperties properties) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(properties.getDatasource().getUrl());
        dsc.setDriverName(properties.getDatasource().getDriverName());
        dsc.setUsername(properties.getDatasource().getUsername());
        dsc.setPassword(properties.getDatasource().getPassword());
        return dsc;
    }

    // 包配置
    private static PackageConfig getPackageConfig(ConfigProperties properties) {
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(properties.getModuleName());
        pc.setParent(properties.getParentPackage());
        pc.setServiceImpl("service");
        pc.setEntity("pojo.entity");
        pc.setXml(pc.getMapper()); //xml与mapper包名一致
        return pc;
    }

    // 自定义配置
    private static InjectionConfig getInjectionConfig(GlobalConfig gc, PackageConfig pc) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                String pojoPackage = this.getConfig().getPackageInfo().get(ConstVal.ENTITY).replace(".entity", "");
                map.put("pojoPackage", pojoPackage);
                map.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                this.setMap(map);
            }

            @Override
            public Map<String, Object> prepareObjectMap(Map<String, Object> objectMap) {
                if (getMap() != null) {
                    objectMap.putAll(getMap());
                }
                return objectMap;
            }
        };
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig("/templates/dto.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称
                StringBuilder stb = new StringBuilder(gc.getOutputDir());
                stb.append(File.separator).append(pc.getParent().replace(".", File.separator))
                        .append(File.separator).append(StringUtils.isNotEmpty(pc.getModuleName()) ? pc.getModuleName() + File.separator : "").append("pojo")
                        .append(File.separator).append("dto")
                        .append(File.separator).append(tableInfo.getEntityName()).append("DTO.java");
                return stb.toString();
            }
        });
        focList.add(new FileOutConfig("/templates/converter.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名称
                StringBuilder stb = new StringBuilder(gc.getOutputDir());
                stb.append(File.separator).append(pc.getParent().replace(".", File.separator))
                        .append(File.separator).append(StringUtils.isNotEmpty(pc.getModuleName()) ? pc.getModuleName() + File.separator : "").append("pojo")
                        .append(File.separator).append("dto")
                        .append(File.separator).append("converter")
                        .append(File.separator).append(tableInfo.getEntityName()).append("Converter.java");
                return stb.toString();
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    // 策略配置
    private static StrategyConfig getStrategyConfig(ConfigProperties properties) {
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
//        strategy.setSuperControllerClass(properties.getSuperControllerClass());
        strategy.setSuperServiceImplClass(properties.getSuperServiceImplClass());
        if (properties.getInclude() != null && properties.getInclude().length > 0) {
            strategy.setInclude(properties.getInclude());
        } else {
            strategy.setExclude(properties.getExclude());
        }
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(properties.getTablePrefix());
        return strategy;
    }
}
