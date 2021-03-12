## Activiti7搭建问题

### 版本问题
* 7.1.0.M6
    > 新增bpmn流程图时新增的这个不会自动部署<br>
    解决：把数据库删除后可以部署
* 7.1.0.M5 
    > 自动部署时会报错
    ```shell
    Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: Unknown column 'VERSION_' in 'field list'
    ```
* 7.1.0.M1 7.0.0.SR1
    > 新增bpmn流程图时可以自动部署，但是已经存在的会重复部署