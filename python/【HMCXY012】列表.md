## 列表
> 格式：[数据1, 数据2, 数据3, 数据4......]
> 列表可以一次性存储多个数据，且可以为不同数据类型

### 列表下标
> `“下标”`又叫`“索引”`，就是编号。比如火车座位号，座位号的作用：按照编号快速找到对应的座位。同理，下标的作用即是通过下标快速找到对应的数据
* 下标从==0==开始<br>
  ![下标说明](../resource/python/python-下标说明.png)
```python
tList = ["info", "value", "key", "just", "back"]

print(tList[1])# value
print(tList[3])# just 
```

### 字符串常用方法 
<details>
<summary>index()</summary>

> 返回指定数据所在位置的下标
* 语法：`列表序列.index(数据, 开始位置下标, 结束位置下标)`
  * 例子
    ```python
    listInfo = ["info", "value", "key", "just", "back"]
    # 结果：2
    print(listInfo.index("key", 0, 4))
    ```
  * 注意：如果查找的数据不存在则报错
</details>

<details>
<summary>count()</summary>

> 统计指定数据在当前列表中出现的次数
* 语法：`列表序列.count(数据)`
    * 例子
      ```python
      listInfo = ["info", "value", "key", "just", "back", "info"]
    
      # 结果：2
      print(listInfo.count("info"))
      ```
</details>

<details>
<summary>len()</summary>

> 统计指定数据在当前列表中出现的次数
* 语法：`len(列表序列)`
    * 例子
      ```python
      listInfo = ["info", "value", "key", "just", "back", "info"]
    
      # 结果：6
      print(len(listInfo))
      ```
</details>

