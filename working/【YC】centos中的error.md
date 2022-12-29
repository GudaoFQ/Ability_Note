## 工作中Centos中出现的Error信息

### syntax error near unexpected token `$'in\r''
```shell
# Error日志
-bash: install.sh: line 27: syntax error near unexpected token `$'in\r''
'bash: install.sh: line 27: `  case $choice in

# 解决
sed -i 's/\r//' 脚本.sh
```