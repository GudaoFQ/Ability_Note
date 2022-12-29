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