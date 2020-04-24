package kr.datasolution.bigstation.utils.rule.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyUtils {

    /**
     * read properties from resources (src/main/resources)
     *
     * ex. getPropertyFromResources("application.properties")
     * @param propertyFileName
     * @return Properties
     */
    public static Properties getPropertyFromResources(String propertyFileName) {
        Properties properties = new Properties();

        try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFileName)){
            getProperty(properties, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    /**
     * read properties from given path (src/main/resources)
     *
     * ex. getPropertyFromExternal("/home/conf/properties/application.properties")
     *
     * @param propertyFilePath
     * @return
     */
    public static Properties getPropertyFromExternal(String propertyFilePath) {
        Properties properties = new Properties();

        try(InputStream inputStream = Files.newInputStream(Paths.get(propertyFilePath))){
            getProperty(properties, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static Properties getProperty(Properties properties, InputStream inputStream) throws IOException {
        properties.load(inputStream);
        return properties;
    }

}
