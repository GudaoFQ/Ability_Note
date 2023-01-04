@echo off
echo "注意：sql文件路径中不能存在中文"
set /p sql_path=请输入执行sql所在文件夹：
SET /p dbhost=主机名：
SET /p dbuser=用户名：
SET /p dbpasswd=用户密码：
SET /p dbname=数据库名称：
SET /p dbport=数据库端口：

if not exist "%sql_path%\" echo ( 文件夹路径无效 & pause&exit )

mysql -h%dbhost% -u%dbuser% -p%dbpasswd% -P%dbport% -e "create database %dbname%"

for /r "%sql_path%" %%j in (*.sql) do (
    echo "%%j开始执行"
    mysql -h%dbhost% -u%dbuser% -p%dbpasswd% -P%dbport% -D%dbname% --default-character-set=utf8 -e "source %%j"
    echo "完成"
)
pause >nul