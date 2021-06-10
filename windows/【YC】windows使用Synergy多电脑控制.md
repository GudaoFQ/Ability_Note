## Synergy使用说明

### 下载说明
<details>
<summary>1.官网地址</summary>

- [官网地址付费购买](https://symless.com/synergy)

</details>

<details>
<summary>2.免费地址</summary>

- [github免费下载](https://www.brahma.world/synergy-stable-builds/)

</details>

### 问题说明
* 【client问题】说明：failed to connect to server: Timed out <br>
  ![client无法连接server问题](../resource/windows/windows-synergy-client无法连接server问题.png)
* 解决：此种情况一般是因为client无法连接server服务的ip
    * 查看连接的server服务ip，看本机是否能ping通，此时一般是ping不通的；如果ping不同，直接到server服务器中查看ip地址，修改即可（server防火墙最好也关闭）<br>
      ![client无法连接server问题解决](../resource/windows/windows-synergy-client无法连接server问题解决.png)

* 【client问题】说明：server refused client with name "Gudao" <br>
  ![client在server服务中名称不存在](../resource/windows/windows-synergy-client在server服务中名称不存在.png)
* 解决：此种情况一般是因为server中的Config Server不存在client服务
  * Config Server中添加client的服务名称<br>
    ![client在server服务中名称不存在解决](../resource/windows/windows-synergy-client在server服务中名称不存在解决.png)

* 【server问题】说明：failed to init synwinhk.dll, another program may be using it <br>
  ![server已经被启动问题](../resource/windows/windows-synergy-server已经被启动问题.png)
* 解决：
  * 在windows的服务管理中重启synergy服务<br>
    ![server已经被启动问题解决](../resource/windows/windows-synergy-server已经被启动问题解决.png)
