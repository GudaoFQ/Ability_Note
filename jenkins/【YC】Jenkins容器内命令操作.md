# Jenkins容器内命令操作

## 说明
* 服务器系统版本：统信（Union OS Server 20）
* Jenkins版本：2.346.3-2-lts
* Jenkins镜像是Debian系统，而且工具特别少，vi、ping、nano、tree都没有

### 向文件中添加内容方式
```shell
# 将192.168.0.11 use.test.com追加到/etc/hosts
echo "192.168.0.11 use.test.com" | sudo sh -c 'cat >> /etc/hosts'
```

### 使用root用户启动Jenkins容器
```shell
# 在docker run中添加参数
-u root
```