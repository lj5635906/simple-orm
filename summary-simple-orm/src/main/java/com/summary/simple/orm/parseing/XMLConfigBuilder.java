package com.summary.simple.orm.parseing;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.summary.simple.orm.io.Resources;
import com.summary.simple.orm.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * SqlMapConfig.xml解析类
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class XMLConfigBuilder {

    private final Configuration configuration;

    public XMLConfigBuilder() {
        this.configuration = new Configuration();
    }

    /**
     * 解析 SqlMapConfig.xml 配置文件
     *
     * @param inputStream 配置文件字节码
     * @return Configuration
     */
    public Configuration parse(InputStream inputStream) throws DocumentException, PropertyVetoException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();

        Properties properties = new Properties();
        List<Element> propertyElements = rootElement.selectNodes("//property");
        propertyElements.forEach(propertyElement -> {
            String name = propertyElement.attributeValue("name");
            String value = propertyElement.attributeValue("value");
            properties.setProperty(name, value);
        });

        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(properties.getProperty("driverClass"));
        dataSource.setJdbcUrl(properties.getProperty("url"));
        dataSource.setUser(properties.getProperty("user"));
        dataSource.setPassword(properties.getProperty("password"));
        configuration.setDataSource(dataSource);

        // 解析 mapper.xml 配置文件
        List<Element> mapperElements = rootElement.selectNodes("//mapper");
        for (Element mapperElement : mapperElements) {
            // mapper.xml 路径
            String mapperPath = mapperElement.attributeValue("resource");
            InputStream mapperInputStream = Resources.getResourceAsStream(mapperPath);

            new XMLMapperBuilder(this.configuration).parseMapper(mapperInputStream);
        }
        return configuration;
    }

}
