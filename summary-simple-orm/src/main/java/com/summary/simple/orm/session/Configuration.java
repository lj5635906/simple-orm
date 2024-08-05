package com.summary.simple.orm.session;

import com.summary.simple.orm.mapping.MappedStatement;
import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件解析信息
 *
 * @author jie.luo
 * @since 2024/8/5
 */
@Data
public class Configuration {

    /**
     * 数据源
     */
    private DataSource dataSource;
    /**
     * 解析 mapper.xml SQL信息
     * key = namespace + '.' + id
     */
    private Map<String, MappedStatement> mappedStatementMap = new HashMap<>();

}
