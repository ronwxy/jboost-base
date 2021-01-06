package cn.jboost.base.generator.config;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @Author ronwxy
 * @Date 2020/6/11 10:25
 * @Version 1.0
 */
@Data
public class ConfigProperties {
    private String author;
    private String outputDir;
    private String parentPackage;
    private String moduleName;
    private String tablePrefix;
    private String includeTables;
    private String excludeTables;
    private Boolean generateXml;
    private Boolean fileOverride;
    private Boolean swagger2;
    private DataSource datasource;
    private String superControllerClass;
    private String superServiceImplClass;

    @Data
    public static class DataSource {
        private String driverName;
        private String url;
        private String username;
        private String password;
    }

    public String[] getInclude() {
       return parse(includeTables);
    }

    public String[] getExclude() {
        return parse(excludeTables);
    }

    private String[] parse(String content) {
        if(StringUtils.isEmpty(content)) {
            return null;
        }
        return content.split(",");
    }
}
