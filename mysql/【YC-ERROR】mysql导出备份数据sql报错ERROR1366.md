## Mysql通过命令行导入备份SQL数据报错：ERROR 1366 (HY000) at line 5724: Incorrect string value: '\xF0\x9F\x98\x80",...' for column 'description' at row 367

### 环境说明
* Mysql 5.7
* CentOs 7.9

### 问题说明
```shell
# 执行命令
mysql -h192.168.5.29  -uroot -p -Dcda5 < 20250407144625.sql
Enter password:

# 报错日志
ERROR 1366 (HY000) at line 5724: Incorrect string value: '\xF0\x9F\x98\x80",...' for column 'description' at row 367
```

这个错误是由于在导入SQL文件时遇到了字符编码问题。错误信息显示在第5724行，尝试插入包含emoji表情符号（如😀，即\xF0\x9F\x98\x80）的字符串到description列，但该列的字符集不支持这些四字节的UTF-8字符。

### 解决

#### 1.排查表的字符集和排序规则是否utf8mb4和utf8mb4_general_ci或utf8mb4_unicode_ci
```shell
# 在建表语句的最后是否设置的是上述字符集：DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci
# 如果不是，进行修改
```
#### 2.SQL导入时指定字符集
```shell
mysql -h数据库ip -uroot -p --default-character-set=utf8mb4 -D数据库名称 < 20250407144625.sql
```

