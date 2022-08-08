##spring-framework集成常用工具及框架

### 用于日常学习和交流
@author: lengedyun
* [person github](https://github.com/ObstinateCloud)
* [person csdn](https://blog.csdn.net/qq_32429805)
<hr>
------------更新记录------------
------------通用内容------------
<ol>
<li>基础框架创建，拆分为多模块，引入热部署依赖</li>
<li>增加系统模块，主要用于web交互</li>
<li>增加swagger依赖:<br>
 swagger访问地址: http://localhost:8080/swagger-ui/index.html#/
</li>
<li>增加Redis依赖<br>
  1.单机模式<br>
  2.集群模式<br>
  3.主从模式<br>
</li>
<li>
  集成mybatis-plus,集成mybatis-plus专用测试类<br>
</li>
<li>
  mysql单数据源模式<br>
</li>
<li>
  集成hutool工具包
</li>
<li>
  集成mybatis-plus多数据源
</li>
<li>
  集成elasticsearch
   <br>RestHighLevelClient 7.17版本之前,自定义版本
   <pre>
           &lt;dependency>
                &lt;groupId>org.elasticsearch&lt;/groupId>
                &lt;artifactId>elasticsearch&lt;/artifactId>
                &lt;version>x.x.x&lt;/version>
           &lt;/dependency>
           &lt;dependency>
               &lt;groupId>org.elasticsearch.client&lt;/groupId>
               &lt;artifactId>elasticsearch-rest-high-level-client&lt;/artifactId>
               &lt;version>x.x.x&lt;/version>
           &lt;/dependency>
           </pre>
   <br>Java API Client 7.17版本之后
</li>
<li>
  集成定时任务
  <br>xxl-job
</li>
<li>
  logback配置
</li>
<li>
  -集成mq
  <br>rocket mq
</li>
<li>
  -集成文件存储
</li>
<li>集成easy-poi</li>
<li>
 集成Security
</li>
</ol>
------------专用内容------------
<ol>
<li>
 集成alitomcat
</li>
<li>
 集成hsf
</li>
</ol>
------------spring cloud------------
<ol>
<li>
 集成feign
</li>
<li>
 集成nacos
</li>
<li>
 集成Security
</li>
<li>
 集成Hystrix
</li>
<li>
 集成Ribbon
</li>
<li>
 集成Spring Cloud Config
</li>
</ol>
