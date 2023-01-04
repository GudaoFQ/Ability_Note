#!/bin/bash
current_path=$(cd "$(dirname "$0")";pwd)

read -p $(echo -e "\033[34m请输入sql文件的绝对路径：\033[0m") sql_path
read -p $(echo -e "\033[34m请输入mysql容器名称：\033[0m") mysql_container_name
read -p $(echo -e "\033[34m请输入mysql帐号：\033[0m") mysql_name
read -p $(echo -e "\033[34m请输入mysql密码：\033[0m") mysql_pass
read -p $(echo -e "\033[34m请输入mysql端口：\033[0m") mysql_port
read -p $(echo -e "\033[34m请输入数据库名称：\033[0m") db_name

docker exec -i $mysql_container_name mkdir /auto_sql

docker cp $current_path/sqlfile.sh $mysql_container_name:/auto_sql

for file in $sql_path/*
do
  if [[ $file =~ .*sql$ ]]; then
    this_file=${file##*/}
    echo -e "\033[34msql文件：$this_file开始执行...\033[0m"
    docker cp $sql_path/$this_file $mysql_container_name:/auto_sql
    docker exec $mysql_container_name  /bin/bash -c "./auto_sql/sqlfile.sh $mysql_name $mysql_pass $mysql_port $db_name $this_file"
    echo -e "\033[34m执行完成\033[0m"
  fi
done
  
docker exec -i $mysql_container_name rm -rf /auto_sql
