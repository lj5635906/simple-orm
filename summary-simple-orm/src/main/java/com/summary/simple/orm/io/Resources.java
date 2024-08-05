package com.summary.simple.orm.io;

import java.io.InputStream;

/**
 * 加载配置文件
 *
 * @author jie.luo
 * @since 2024/8/5
 */
public class Resources {
    /**
     * 加载配置文件，将配置文件转换为字节码，存储内存中
     *
     * @param resource 配置文件路径
     * @return 配置文件字节码
     */
    public static InputStream getResourceAsStream(String resource) {
        return Resources.class.getClassLoader().getResourceAsStream(resource);
    }
}
