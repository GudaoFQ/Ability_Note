## 记录工作中能使用到，但是又不好记的函数使用方式与sql

### 获取最后一个`/`后面的数据
> 获取字符串`/data/code/2022-09-15/16632365681/pig-v3.0.4/pig-upms/pig-upms-biz/pom.xml`中最后一个`/`后面的数据
```sql
# 需要将sql中的str替换成需要截取的字符串
select REVERSE(LEFT(REVERSE(str),LOCATE('/',REVERSE(str))-1));
```
* LEFT：从左边截取(被截取字段，截取长度)
* REVERSE：字符串翻转(str)，str变成rts
* LOCATE：查找某个字符串出现的位置(查找的字符串,被查字符串)

### mysql命令行查看端口
```shell
show global variables like 'port'
```

### mysql跳过密码登录
```shell
# 在 my.ini 或 my.cnf 的 [mysqld] 下添加 skip-grant-tables
[mysqld]
skip-grant-tables
# 重启mysql，直接输入用户，密码随便写就能登录
```

### 通过`mysql`命令直接执行`.sql`文件
> 在脚本中执行，会报一个密码不安全（因为实在脚本中对密码进行了明文显示）
```shell
# mysql –u用户名 –p密码 –D数据库 <  [sql脚本文件路径全名]
mysql -uroot -prootroot -Dgudao < /test/info.sql
```
* -u：用户名称
* -p：密码
* -D：数据库名称（选填）

### 判断数据库是否存在
```shell
if mysql -uroot -p密码 -e "use 数据库名称" 2> /dev/null; then
  echo "数据库存在"
else
  echo "数据库不存在"
fi;
```
* 2> /dev/null的意思就是将标准错误删掉
* 0：标准输入（stdin）
* 1：标准输出（stdout）
* 2：标准错误（stderr）