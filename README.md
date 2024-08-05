# simple-orm

简版 Mybatis orm 框架实现

主要思路

**使用端**
提供相关配置文件
SqlMapConfig.xml : 存放数据源信息，引入 mapper.xml
Mapper.xml : SQL语句配置文件

**框架端**
1、读取配置文件
Configuration : 存放数据源信息，Map<String, MappedStatement> 唯一标识(namespace.id),SQL语句
MappedStatement : SQL语句，statement类型，输入参数类型，输出参数类型
2、解析配置文件
创建SqlSessionFactoryBuilder
    使用dom4j解析配置文件，将解析出来的内容封装到 Configuration 与 MappedStatement 
创建 SqlSessionFactory ，并实现 DefaultSqlSessionFactory
3、创建 SqlSession
    实现 DefaultSqlSession，封装CURD方法：insert、delete、update、selectList、selectOne、getMapper(通过动态代理加载具体mapper)
    创建 Executor 执行器，实现 SimpleExecutor 实现具体 JDBC 操作
