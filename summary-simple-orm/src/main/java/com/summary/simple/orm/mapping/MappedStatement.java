package com.summary.simple.orm.mapping;

import lombok.Data;

/**
 * 解析Mapper.xml的信息
 *
 * @author jie.luo
 * @since 2024/8/5
 */
@Data
public class MappedStatement {
    /**
     * SQL 语句类型
     */
    private SqlCommandType sqlCommandType;
    /**
     * SQL语句唯一标识
     */
    private String id;
    /**
     * SQL响应类型
     */
    private String resultType;
    /**
     * SQL参数类型
     */
    private String parameterType;
    /**
     * SQL语句
     */
    private String sql;
}
