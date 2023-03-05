## Node基于Windows安装

#### node.js官网下载
官网：<https://nodejs.org/en/download/releases/><br>
版本下载目录：<https://nodejs.org/en/download/releases/>
![官网版本入口](../resource/node/node-官网版本入口.jpg)

#### zip方式安装
![安装包](../resource/node/node-安装包.jpg)

#### 环境变量配置
![环境变量配置](../resource/node/node-环境变量配置.jpg)

#### 配置完成验证
> 通过命令`node --version`<br>
![版本验证](../resource/node/node-版本验证.jpg)

#### 安装npm阿里镜像
```shell
# 通过cnpm使用
npm install -g cnpm --registry=https://registry.npm.taobao.org
```
![npm淘宝镜像验证](../resource/node/node-npm淘宝镜像验证.png)
