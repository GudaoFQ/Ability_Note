## Vue基本使用

#### Vue框架基础应用
> Vue它是属于前端当中的一个JavaScript框架，源码都封装到了一个尾缀为JS这样一样的文件里面。因此我们需要获取到源码

#### 使用步骤
* 引包
* 页面标签布局
* 创建对应的Vue实例
    * 挂载点设置
    * 实例数据来源...
```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
    <!-- 引入Vue源码（引包） -->
    <script src="./js/vue.min.js"></script>
</head>

<body>
    <!-- 页面标签布局 -->
    <div id="app">
        <h1>我是Vue,我今年{{age}}岁了</h1>
    </div>
</body>

</html>
<script>
    //Vue框架对外暴露了一个Vue构造函数
    //通过这个Vue构造函数，创建实例
    var vm = new Vue({
        //el:挂载点设置（将Vue实例）和结构层中标签进行挂在
        el: "#app",
        //实例数据来源
        data: {
            age: 100
        }
    });
    //修改实例age属性值
    vm.age = 23;
    vm.age = 99999;
</script>
```