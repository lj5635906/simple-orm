package com.summary.simple.orm.session;

import com.summary.simple.orm.parseing.XMLConfigBuilder;
import com.summary.simple.orm.session.defaults.DefaultSqlSessionFactory;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

/**
 * 解析配置文件、并构建SqlSessionFactory
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class SqlSessionFactoryBuilder {

    /**
     * 构建 SqlSessionFactory
     *
     * @param inputStream 配置文件字节码
     * @return SqlSessionFactory
     */
    public SqlSessionFactory build(InputStream inputStream) throws PropertyVetoException, DocumentException {

        // 1.解析配置文件
        XMLConfigBuilder configBuilder = new XMLConfigBuilder();
        Configuration configuration = configBuilder.parse(inputStream);

        // 2.构建 SqlSessionFactory
        return new DefaultSqlSessionFactory(configuration);
    }
}
