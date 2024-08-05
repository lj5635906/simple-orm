package com.summary.simple.orm.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL 转换
 *
 * @author jie.luo
 * @since 2024/8/5
 */
@Data
@AllArgsConstructor
public class BoundSql {

    /**
     * 解析过后的SQL
     */
    private String sqlTest;
    /**
     * #{} 里面解析出来的参数名称
     */
    private List<ParameterMapping> parameterMappings = new ArrayList<>();
}
