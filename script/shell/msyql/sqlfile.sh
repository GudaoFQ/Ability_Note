name=$1
password=$2
port=$3
db_name=$4
sql_name=$5

if mysql -u$name -p$password -e "use $db_name" 2> /dev/null; then
  echo "$db_name已存在，不需要创建，直接执行$sql_name"
else
  echo "创建数据库$db_name"
  mysql -u$name -p$password -P$port --default-character-set=utf8 -e "create database $db_name"
fi;

mysql -u$name -p$password -P$port -D$db_name --default-character-set=utf8 -e "source /auto_sql/$sql_name"