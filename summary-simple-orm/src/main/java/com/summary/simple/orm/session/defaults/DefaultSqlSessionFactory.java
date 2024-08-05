package com.summary.simple.orm.session.defaults;

import com.summary.simple.orm.session.Configuration;
import com.summary.simple.orm.session.SqlSession;
import com.summary.simple.orm.session.SqlSessionFactory;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
