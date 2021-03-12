## Activiti7+SpingBoot2搭建

### Activiti介绍

* [Activiti Core](https://activiti.gitbook.io/activiti-7-developers-guide/getting-started/getting-started-activiti-core)
  > 新一代商业自动化平台，提供一组旨在在`分布式`基础架构上运行的 Cloud 原生构建块
  ```shell
  <dependencyManagement>
      <dependencies>
          <dependency>
            <groupId>org.activiti.dependencies</groupId>
            <artifactId>activiti-dependencies</artifactId>
            <version>7.1.0.M2</version>
            <scope>import</scope>
            <type>pom</type>
          </dependency>
      </dependencies>
  </dependencyManagement>
  ```
* [Activiti Cloud](https://activiti.gitbook.io/activiti-7-developers-guide/getting-started/getting-started-activiti-cloud)
  > Activiti Cloud是一组从头开始设计的`Cloud Native`组件,可在分布式环境中使用。我们选择`Kubernetes`作为我们的主要部署基础架构,并且我们将`Spring Cloud / Spring Boot`与`Docker`一起用于这些组件的容器化
  ```shell
  <dependencyManagement>
      <dependencies>
          <dependency>
            <groupId>org.activiti.cloud</groupId>
            <artifactId>activiti-cloud-dependencies</artifactId>
            <version>7.1.0-M11</version>
            <scope>import</scope>
            <type>pom</type>
          </dependency>
      </dependencies>
  </dependencyManagement>
  ```
  * 5个构建基础块
      * Activiti Cloud Runtime Bundle
      * Activiti Cloud Query
      * Activiti Cloud Audit
      * Activiti Cloud Connectors
      * Activiti Cloud Notifications Service (GraphQL)

### Activiti特点
* Activiti 拥有更简洁健壮的接口
* Activiti 依赖更少的jar包
* Activiti 特性就在于它众多的协作工具组件
* Activiti 支持启动引擎后随时热部署
* 在 Eclipse IDE 中支持 Activiti 拥有更友好易用的编辑插件和在线插件
* Activiti 拥有更友好的用户体验
