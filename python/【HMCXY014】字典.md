## 元组
> 格式：('key':'value', 'key':'value', 'key':'value', 'key':'value'......)
> 字典里面的数据是以**键值对**形式出现，字典数据和数据顺序没有关系，即字典**不支持下标**，后期无论数据如何变化，只需要按照对应的键的名字查找数据即可

### 字典的语法
* 字典特点：
  * 使用 `{}` 来包裹数据
  * 数据为键值对的形式出现
  * 键值对之间只用 `:` 隔开
  * 每组键值对之间使用 `,` 隔开
    ```python
    # 多个数据字典
    dict = {'name':'gudao', 'age':18, 'sex':'男'}
  
    # 空字典
    # 方法一
    nonDict1 = {}
    # 方法二
    nonDict2 = dict()
    ```

### 元组常用方法 

------------------------------------

<details>
<summary>新增</summary>

> 返回指定数据所在位置的下标
* 语法：`字典序列[key] = 值`
  * 例子
    ```python
    lDict = {"name": "gudao", "age": 12, "sex": "男"}
    
    # 结果：{'name': 'wang', 'age': 12, 'sex': '男'}
    lDict["name"] = "wang"
    print(lDict)
    ```
  * 注意：
    * 如果key存在则修改这个key对应的值；如果key不存在则新增此键值对
    * 字典为可变类型
</details>

<details>
<summary>del&del()</summary>

------------------------------------

> 删除字典或删除字典中指定键值对
* 语法：`字典序列[key] = 值`
  * 例子
    ```python
    lDict = {"name": "gudao", "age": 12, "sex": "男"}

    # 结果：{'age': 12, 'sex': '男'}
    del(lDict["name"])
    print(lDict)
    
    # 结果：{'sex': '男'}
    del lDict["age"]
    print(lDict)
    ```
  * 注意：
    * 如果key存在则修改这个key对应的值；如果key不存在则新增此键值对
    * 字典为可变类型
</details>