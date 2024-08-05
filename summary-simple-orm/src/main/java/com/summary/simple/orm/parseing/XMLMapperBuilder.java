package com.summary.simple.orm.parseing;

import com.summary.simple.orm.mapping.MappedStatement;
import com.summary.simple.orm.mapping.SqlCommandType;
import com.summary.simple.orm.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * *Mapper.xml SQL文件解析类
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class XMLMapperBuilder {

    private final Configuration configuration;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 解析 *Mapper.xml SQL 文件
     *
     * @param inputStream *Mapper.xml SQL 文件字节码
     */
    public void parseMapper(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);

        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        for (SqlCommandType value : SqlCommandType.values()) {
            List<Element> selectNodes = rootElement.selectNodes("//" + value.toString().toLowerCase());
            for (Element element : selectNodes) {
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                String parameterType = element.attributeValue("parameterType");
                String sql = element.getTextTrim();

                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setId(id);
                mappedStatement.setResultType(resultType);
                mappedStatement.setParameterType(parameterType);
                mappedStatement.setSql(sql);
                mappedStatement.setSqlCommandType(value);

                configuration.getMappedStatementMap().put(namespace + "." + id, mappedStatement);
            }
        }

    }
}
