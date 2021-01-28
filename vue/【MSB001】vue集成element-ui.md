## Vue挂载Element-UI

### 挂载详情
#### 安装element-ui插件
#### 方法一
* 安装命令
    ```shell
    npm install element-ui -S
    ```
    ![element_ui挂载](../resource/vue/vue-element_ui挂载.png)

* 查看配置文件package.json，是否有element-ui组件的版本号
![element_ui在package.json中的版本查看](../resource/vue/vue-element_ui在package.json中的版本查看.png)

* 安装的element-ui信息都能在`node_modules`查看
![element_ui位置查看](../resource/vue/vue-element_ui位置查看.png)

* main.js中引入element-ui/在路由中引入element-ui
```html
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI);
```

#### 方法二
> 查看element-ui官网，提供了一套自动搭载在vue上的命令
* 地址:
    <https://element.eleme.cn/#/zh-CN/component/quickstart><br>
    ![element_ui自动命令](../resource/vue/vue-element_ui自动命令.png)
* 执行命令
    ```shell
    vue add element
    ```
    ![element_ui自动集成](../resource/vue/vue-element_ui自动集成.png)
* 启动项目`npm run dev`
    ![element_ui搭载后界面](../resource/vue/vue-element_ui搭载后界面.png)
