## 工作中Centos中出现的Error信息

### syntax error near unexpected token `$'in\r''
```shell
# Error日志
-bash: install.sh: line 27: syntax error near unexpected token `$'in\r''
'bash: install.sh: line 27: `  case $choice in

# 解决
sed -i 's/\r//' 脚本.sh
```

### `vi`命令中通过`wq`保存一直保存不了，报错：CONVERSION ERROR in line 2;[dos] 5L, 102C written
```shell
# 说明
通过vi进入脚本中，所有的中文都是乱码，通过wq保存，一直保存不上

# 解决
## 将文本格式设置为utf-8
:set fileencoding = utf-8
```

### docker build构建报错：container_linux.go:290: starting container process caused "process_linux.go:259: applying cgroup configuration for process caused \"Cannot set property TasksAccounting, or unknown property.\""
```shell
通过docker build -t 创建docker镜像，然后报错如上

# 解决
## 升级yum源
yum update -y
```