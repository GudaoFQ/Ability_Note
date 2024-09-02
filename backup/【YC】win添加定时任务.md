## Windows添加定时任务

### 环境说明
* 服务器：Windows 10
* 任务周期：每天一次

### 通过创建基本任务生成定时任务
#### 进入任务创建界面
```shell
# win + R 输入“taskschd.msc”
taskschd.msc
```
![进入任务创建界面](../resource/backup/win-进入任务创建界面.png)

#### 创建基本任务
![创建定时基本任务](../resource/backup/win-创建定时基本任务.png)

#### 添加触发器
![添加触发器](../resource/backup/win-添加触发器.png)

#### 添加触发时间
![添加触发时间](../resource/backup/win-添加触发时间.png)

#### 添加操作
![添加操作信息](../resource/backup/win-添加操作信息.png)

#### 添加执行脚本路径
![添加脚本执行位置](../resource/backup/win-添加脚本执行位置.png)

#### 任务添加完成
![任务添加完成](../resource/backup/win-任务添加完成.png)

#### 查看任务列表
![查看任务信息](../resource/backup/win-查看任务信息.png)

#### 开启或关闭日志记录
![开启或关闭日志记录](../resource/backup/win-开启或关闭日志记录.png)

#### 注意
* 每次任务需要刷新后才能看到他的准确“状态”

### 通过创建任务生成定时任务
#### 进入任务创建界面
```shell
# win + R 输入“taskschd.msc”
taskschd.msc
```
![进入任务创建界面](../resource/backup/win-进入任务创建界面.png)

#### 创建任务
![创建任务](../resource/backup/win-创建任务.png)

#### 添加触发器
![创建任务触发器](../resource/backup/win-创建任务触发器.png)

#### 添加任务操作
![添加任务操作](../resource/backup/win-添加任务操作.png)

#### 添加条件配置
![添加条件配置](../resource/backup/win-添加条件配置.png)

#### 修改设置信息
![修改设置信息](../resource/backup/win-修改设置信息.png)

#### 配置执行人账号
![配置执行人账号](../resource/backup/win-配置执行人账号.png)

#### 执行日志
![执行日志](../resource/backup/win-执行日志.png)
