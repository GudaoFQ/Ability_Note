## 字符串

### 字符串类型
* 单引号：与双引号没啥区别，不支持换行打印
  * ' 信息 '
  * " 信息 "
* 双引号：与单引号没啥区别，不支持换行打印
  * '' 信息 ''
  * "" 信息 ""
* 三引号：三引号形式的字符串支持换行
  * ''' 信息 '''
  * """ 信息 """

#### 支持换行效果测试
```python
infoStr = '''测试
    
    代码'''
print(infoStr)

# 结果：
测试
    
    代码
```

### 字符串下标
> `“下标”`又叫`“索引”`，就是编号。比如火车座位号，座位号的作用：按照编号快速找到对应的座位。同理，下标的作用即是通过下标快速找到对应的数据
* 下标从==0==开始<br>
![下标说明](../resource/python/python-下标说明.png)

#### 例子
```python
infoStr = "abdcefg"
print(f"下标0的数据：{infoStr[0]}，下标1的数据{infoStr[1]}...下标5的信息{infoStr[5]}")

# 结果：
下标0的数据：a，下标1的数据b...下标5的信息f
```

### 字符串切片
> 切片是指对操作的对象截取其中一部分的操作。**字符串、列表、元组**都支持切片操作

####语法
```python
序列[开始位置下标:结束位置下标:步长]
```
* 不包含结束位置下标对应的数据，正负整数均可
* 步长是选取间隔，正负整数均可，默认步长为1

#### 例子
```python
infoStr = "abcdefgnijk"

# 相当于在[a,g)中,每次选完一个后，选取某个索引下的值后的后面**第二**个索引的值
print(infoStr[0:6:2])# ace
# 相当于在[b,d)中取值，步长为1
print(infoStr[1:3])# bc
# -2表示倒数第一个数据
print(infoStr[0:-2])# abcdefgni
# 步长为-1表示从最后一个往前取，步长1
print(infoStr[::-1])# kjingfedcba
# 步长为-2表示从最后一个往前取，步长2
print(infoStr[::-2])# kigeca
# 取值全部
print(infoStr[:])# abcdefgnijk
# 在[a,d)中取值
print(infoStr[:3])# abc
# 在[d,k]中取值
print(infoStr[3:])# defghijk
```

### 字符串常用方法

------------------------------

<details>
<summary>find()</summary>

> 区分大小写；检测某个子串是否包含在这个字符串中，如果在返回这个子串开始的位置下标，否则则返回-1
* 语法`字符串序列.find(子串, 开始位置下标, 结束位置下标)`
  * 开始和结束位置下标可以省略，表示在整个字符串序列中查找
* 例子
  ```python
  infoStr = "This Info Create From Gudao"
  
  # 第一个o的索引值
  print(infoStr.find("o"))# 8
  # 获取[9,21)中第一个o的索引值
  print(infoStr.find("o",9,21))# 19
  # 获取[9,最后值]中第一个o的索引值
  print(infoStr.find("o",21))# 26
  # 获取Info出现的位置
  print(infoStr.find("Info"))# 5
  # 获取Infos出现的位置，没有返回-1
  print(infoStr.find("Infos"))# -1
  
  # 从右边开始查找，获取第一个o的索引值
  print(infoStr.rfind("o"))
  ```
* rfind()
  * 和find()功能相同，但查找方向为**右侧**开始
</details>
  
<details>
<summary>index()</summary>

> 区分大小写；检测某个子串是否包含在这个字符串中，如果在返回这个子串开始的位置下标，否则则报**异常**
* 语法`字符串序列.index(子串, 开始位置下标, 结束位置下标)`
    * 开始和结束位置下标可以省略，表示在整个字符串序列中查找
* 例子
  ```python
  infoStr = "This Info Create From Gudao"

  # 第一个o的索引值
  print(infoStr.index("o"))# 8
  # 获取[9,21)中第一个o的索引值
  print(infoStr.index("o",9,21))# 19
  # 获取[9,最后值]中第一个o的索引值
  print(infoStr.index("o",21))# 26
  # 获取Info出现的位置
  print(infoStr.index("Info"))# 5
  # 获取Infos出现的位置，没有返回-1
  print(infoStr.index("Infos"))# 报错
  
  # 从右边开始查找，获取第一个o的索引值
  print(infoStr.rindex("o"))# 26
  ```
* rindex()
  * 和index()功能相同，但查找方向为**右侧**开始
</details>

<details>
<summary>count()</summary>

> 返回某个子串在字符串中出现的次数
* 语法`字符串序列.count(子串, 开始位置下标, 结束位置下标)`
    * 开始和结束位置下标可以省略，表示在整个字符串序列中查找
* 例子
  ```python
  infoStr = "This Info Create From Gudao"

  # o出现的次数
  print(infoStr.count("o"))# 3
  # 获取[9,21)中o出现的次数
  print(infoStr.count("o",9,21))# 1
  # 获取Infos出现的次数，没有返回0
  print(infoStr.count("Infos"))# 0
  ```
</details>

------------------------------

<details>
<summary>replace()</summary>

> 替换
* 语法`字符串序列.replace(旧子串, 新子串, 替换次数)`
    * 替换次数如果 >= 查出子串出现次数，则替换次数为该子串出现次数
* 例子
  ```python
  infoStr = "This Info Create From Gudao"
  
  # 将所有的o替换为T_T，注意：原先字符串是不会被修改的
  test1 = infoStr.replace("o", "T_T")# This InfT_T Create FrT_Tm GudaT_T
  print(test1)
  
  # 将前两个o替换为T_T
  test2 = infoStr.replace("o", "T_T", 2)# This InfT_T Create FrT_Tm Gudao
  print(test2)
  ```
* 注意：数据按照是否能直接修改分为**可变类型**和**不可变类型**两种。字符串类型的数据修改的时候不能改变原有字符串，属于不能直接修改数据的类型即是不可变类型
</details>

<details>
<summary>split()</summary>

> 按照指定字符分割字符串
* 语法`字符串序列.split(分割字符, num)`
  * num表示的是分割字符出现的次数，即将来返回数据个数为num+1个
* 例子
  ```python
  infoStr = "This Info Create From Gudao"

  # 将字符串通过空格的拆分成列表
  test1 = infoStr.split(" ")
  print(test1)# ['This', 'Info', 'Create', 'From', 'Gudao']
  
  # 将字符串通过空格的拆分成长度为3的列表
  test2 = infoStr.split(" ", 2)
  print(test2)# ['This', 'Info', 'Create From Gudao']
  ```
  * 注意：如果分割字符是原有字符串中的子串，分割后则丢失该子串
</details>

<details>
<summary>join()</summary>

> 用一个字符或子串合并字符串，即是将多个字符串合并为一个新的字符串
* 语法`字符或子串.join(多字符串组成的序列)`
* 例子
  ```python
  infoStr = "test"
  tList = ["info", "value", "key", "just", "back"]
  uList = ("info", "value", "key")
  
  # 将infoStr拆成列表，将...加入
  print("...".join(infoStr))# t...e...s...t
  # 列表插入
  print(",".join(tList))# info,value,key,just,back
  # 元组插入
  print(",".join(uList))# info,value,key
  ```
</details>

<details>
<summary>capitalize()</summary>

> 将字符串第一个字符转换成大写
* 语法`字符串.capitalize()`
* 例子
  ```python
  mystr = "tesT"

  # 结果：Test
  print(mystr.capitalize())
  ```
* 注意：capitalize()函数转换后，只将字符串第一个字符大写，其他的字符全都小写
</details>

<details>
<summary>title()</summary>

> 将字符串每个单词首字母转换成大写
* 语法`字符串.title()`
* 例子
  ```python
  strins = "tesT info"
  
  # 结果：Test Info
  print(strins.title())
  ```
* 注意：title()函数转换后，只将每个单词第一个字符大写，其他的字符全都小写
</details>

<details>
<summary>lower()</summary>

> 将字符串中大写转小写
* 语法`字符串.lower()`
* 例子
  ```python
  strins = "tesT info"
  
  # 结果：test info
  print(strins.lower())
  ```
</details>

<details>
<summary>upper()</summary>

> 将字符串中小写转大写
* 语法`字符串.upper()`
* 例子
  ```python
  strins = "tesT info"
  
  # 结果：TEST INFO
  print(strins.upper())
  ```
</details>

<details>
<summary>lstrip()</summary>

> 删除字符串左侧空白字符
* 语法`字符串.lstrip()`
* 例子
  ```python
  strins = "   tesT "
  
  # 结果："tesT " 
  print(strins.lstrip())
  ```
</details>

<details>
<summary>rstrip()</summary>

> 删除字符串右侧空白字符
* 语法`字符串rstrip.()`
* 例子
  ```python
  strins = "   tesT "
  
  # 结果："   tesT"
  print(strins.rstrip())
  ```
</details>

<details>
<summary>strip()</summary>

> 删除字符串两侧空白字符
* 语法`字符串.strip()`
* 例子
  ```python
  strins = "   tesT "
  
  # 结果："tesT"
  print(strins.strip())
  ```
</details>

<details>
<summary>ljust()</summary>

> 返回一个原字符串左对齐,并使用指定字符(默认空格)填充至对应长度 的新字符串
* 语法`字符串序列.ljust(长度, 填充字符)`
* 例子
  ```python
  strins = "test"
  
  # 结果：test......
  print(strins.ljust(10, "."))
  # 结果：......test
  print(strins.rjust(10, "."))
  # 结果：...test...
  print(strins.center(10, "."))
  ```
* rjust：返回一个原字符串右对齐,并使用指定字符(默认空格)填充至对应长度 的新字符串，语法和ljust()相同
* center：返回一个原字符串居中对齐,并使用指定字符(默认空格)填充至对应长度 的新字符串，语法和ljust()相同
</details>

------------------------------

<details>
<summary>startswith()</summary>

> 区分大小写；检查字符串是否是以指定子串开头，是则返回 True，否则返回 False。如果设置开始和结束位置下标，则在指定范围内检查
* 语法`字符串序列.startswith(子串, 开始位置下标, 结束位置下标)`
* 例子
  ```python
  strins = "test"

  # True
  print(strins.startswith("t"))
  # False
  print(strins.startswith("t", 1, 3))
  
  # True
  print(strins.endswith("t"))
  # False，不包含索引为3的值
  print(strins.endswith("t", 1, 3))
  ```
* endswith：区分大小写；检查字符串是否是以指定子串结尾，是则返回 True，否则返回 False。如果设置开始和结束位置下标，则在指定范围内检查；与startswith用法相同
</details>

<details>
<summary>isalpha()</summary>

> 如果字符串至少有一个字符并且所有字符都是字母则返回 True, 否则返回 False
* 语法`字符串.isalpha()`
* 例子
  ```python
  strina = "test"
  strinb = "test123"
  
  # 结果：True
  print(strina.isalpha())
  # 结果：False
  print(strinb.isalpha())
  ```
</details>

<details>
<summary>isdigit()</summary>

> 如果字符串只包含数字则返回 True 否则返回 False
* 语法`字符串.isdigit()`
* 例子
  ```python
  strina = "123"
  strinb = "test123"
  
  # 结果：True
  print(strina.isdigit())
  # 结果：False
  print(strinb.isdigit())
  ```
</details>

<details>
<summary>isalnum()</summary>

> 如果字符串至少有一个字符并且所有字符都是字母或数字则返 回 True,否则返回 False
* 语法`字符串.isalnum()`
* 例子
  ```python
  strina = "123!@#"
  strinb = "test123"
  
  # 结果：
  # 结果：True
  print(strina.isalnum())
  # 结果：False
  print(strinb.isalnum())
  ```
</details>

<details>
<summary>isspace()</summary>

> 如果字符串中只包含空白，则返回 True，否则返回 False
* 语法`字符串.isspace()`
* 例子
  ```python
  strina = "   "
  strinb = "test 123"
  
  # 结果：
  # 结果：True
  print(strina.isspace())
  # 结果：False
  print(strinb.isspace())
  ```
</details>