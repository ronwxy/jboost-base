package cn.jboost.base.generator.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

/**
 * @Author ronwxy
 * @Date 2020/6/10 12:35
 * @Version 1.0
 */
public class ConfigUtil {

    private static ConfigProperties configProperties;

    public static void loadProperties() {

            InputStream input =  ConfigUtil.class.getResourceAsStream("/generator.yaml"); //new FileInputStream("resources/generator.yaml");
            Yaml yaml = new Yaml();
            configProperties = yaml.loadAs(input, ConfigProperties.class);
    }

    public static ConfigProperties getConfigProperties(){
        return configProperties;
    }

}
