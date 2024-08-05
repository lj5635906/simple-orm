package com.summary.simple.orm.mapping;

import lombok.Data;

/**
 * @author jie.luo
 * @since 2024/8/5
 */
@Data
public class ParameterMapping {

    /**
     * 解析出来的参数名称
     */
    private String content;

    public ParameterMapping(String content) {
        this.content = content;
    }

}
