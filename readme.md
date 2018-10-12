## IDEA 导入项目

在数据库中创建数据库 library，修改数据库密码为admin

File -> New -> Project From Exiting Sources -> (目录下的) pom.xml

然后项目自动导入依赖 (可能右下角会有选项弹窗，点 enable auto import)

等待自动导入后，运行 src/main/java/com/example/library_management_system/LibraryManagementSystemApplication.java 中的main 方法，没有报错，则导入成功

## RabbitMQ 使用 (Windows)

执行目录：`C:\Program Files\RabbitMQ Server\rabbitmq_server-3.7.8\sbin`

启动：`rabbitmq-plugins.bat enable rabbitmq_management`

	浏览器中访问：`http://localhost:15672`

关闭：`rabbitmq-plugins.bat disable rabbitmq_management`

username: guest

password: guest

### Linux Mysql 存入中文数据

报错：java.sql.SQLException: Illegal mix of collations (latin1_swedish_ci,IMPLICIT) and (utf8_general_ci,COERCIBLE) for operation '='

解决：

```
// (application.properties)防止出现Java中连接数据库时汉字都变成问号问题
spring.datasource.url=jdbc:mysql://localhost:3306/library?useUnicode=true&characterEncoding=utf8
```

