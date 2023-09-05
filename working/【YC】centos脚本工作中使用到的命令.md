## 记录工作中能使用到，但是又不好记的shell脚本命令

### 获取当前目录绝对路径/上级目录绝对路径
```shell
# 当前目录路径方法一
$(cd "$(dirname "$0")";pwd)
# 当前目录路径方法二
$PWD
# 上级目录路径
$(dirname "$PWD")
```

### 删除指定文件夹下，名称不为'xxx'的文件
```shell
# 删除`/test/info/`文件夹下名称不是`.tar.gz`的文件
find /test/info/ -type f -not -name '*.star.gz' | xargs rm
```

### 判断某个文件是否以xxx结尾
```shell
# 判断tet.sql是否以.sql结尾（使用正则判断的）
if [[ "需要判断的信息（可以填写变量）" =~ .*sql$ ]]; then
  echo "是以sql结尾"
fi
```

### 获取路径中的最后一个文件或文件夹名称（就是截取最后一个'/'后的信息）
```shell
${变量名称##*/}
```

### 判断某个文件/文件夹是否存在
```shell
# mkdir跟文件夹路径（/data/sca）此时如果data没有创建，那么mkdir命令会报错
if [ ! -d "文件夹名称/路径（filename）" ]; then
  mkdir 文件夹名称/路径
fi
```
* -d：filename为目录，则为真
* -f：filename为常规文件，则为真
* -e：filename存在，则为真
* -s：filename 如果文件长度不为0，则为真

### 获取ssh端口号
```shell
# 获取ssh配置信息中关于端口的最后一条
sshPortLine=`cat /etc/ssh/sshd_config |grep Port | tail -1`
# 过滤无用信息只留端口号
sshPort=${sshPortLine/"Port "/""}
```

### 获取时间相关信息
```shell
# 20210424152101
$(date "+%Y%m%d%H%M%S")
# 2021-04-24 15:24:57
$(date "+%Y-%m-%d %H:%M:%S")
#获取昨天的日期
$(date -d last-day "+%Y-%m-%d")
#获取明天日期 
$(date -d next-day "+%Y-%m-%d")
$(date -d tomorrow +%Y%m%d)
#获取上个小时的日期
$(date -d last-hour "+%Y-%m-%d %H")
#两天前时间
$(date -d "2 days ago" "+%Y-%m-%d")
#查明一个特定的日期是星期几
$(date -d "nov 22")
#两星期以后的日期
$(date -d "2 weeks")
#100天以前的日期
$(date -d '-100 days') 
#50天后的日期
$(date -d '50 days')
```

### 获取某个字符串中的json数据信息
```shell
cat example.txt | sed 's/,/\n/g' | grep "dev_id" | sed 's/:/\n/g' | sed '1d' | sed 's/}//g'
```

### 程序运行日志输出到控制台并将其写入文件中
```shell
# 将shell命令的执行日志输出到控制台并将其保存到log.txt文件中去
shell命令 | tee log.txt
```