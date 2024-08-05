package com.summary.simple.orm.session;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
public interface SqlSessionFactory {

    /**
     * 获取 SqlSession
     *
     * @return SqlSession
     */
    SqlSession openSession();

}
