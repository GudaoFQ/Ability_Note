# OceanBase - 三节点部署说明

## 资源准备

| 节点IP       | 配置       | 操作系统   |
| ------------ | ---------- | ---------- |
| 192.168.0.95 | x86 8C 16G | CentOS 7.9 |
| 192.168.0.94 | x86 8C 16G | CentOS 7.9 |
| 192.168.0.96 | x86 8C 16G | CentOS 7.9 |

## 日志
> 解压压缩包：tar -zxvf oceanbase-standalone-all-in-one-4.2.5_bp4_hf3_20250707.el8.x86_64.tar.gz

```diff
[root@localhost oceanbase-all-in-one]# ll
total 12
drwxr-xr-x. 2 root root   58 Jul  3 18:53 bin
drwxr-xr-x. 3 root root   17 Jul  3 18:53 obclient
drwxr-xr-x. 4 root root   28 Jul  3 18:53 obd
-rw-r--r--. 1 root root  666 Jul  3 18:53 README.md
drwxr-xr-x. 2 root root 4096 Jul  3 18:54 rpms
-rw-r--r--. 1 root root   23 Jul  3 18:53 VERSION
[root@localhost oceanbase-all-in-one]# cd bin/
[root@localhost bin]# ll
total 16
-rw-r--r--. 1 root root  209 Jul  3 18:53 env.sh
-rwxr-xr-x. 1 root root 6405 Jul  3 18:53 install.sh
-rwxr-xr-x. 1 root root 2058 Jul  3 18:53 uninstall.sh
[root@localhost bin]# ./install.sh
install obd as root
No previous obd installed, try install..., wait a moment
name: grafana
version: 7.5.17
release:1
arch: x86_64
md5: 1bf1f338d3a3445d8599dc6902e7aeed4de4e0d6
size: 177766248
add /data/oceanbase-all-in-one/rpms/grafana-7.5.17-1.el7.x86_64.rpm to local mirror
name: obagent
version: 4.2.2
release:100000042024011120.el7
arch: x86_64
md5: 19739a07a12eab736aff86ecf357b1ae660b554e
size: 72919140
add /data/oceanbase-all-in-one/rpms/obagent-4.2.2-100000042024011120.el7.x86_64.rpm to local mirror
name: ob-configserver
version: 1.0.0
release:2.el7
arch: x86_64
md5: feca6b9c76e26ac49464f34bfa0780b5a8d3f4a0
size: 24259515
add /data/oceanbase-all-in-one/rpms/ob-configserver-1.0.0-2.el7.x86_64.rpm to local mirror
name: ob-deploy
version: 3.3.0
release:3.el7
arch: x86_64
md5: 11f74ff02fa99f22d73f2b89bb1ee189b7ca00af
size: 180322679
add /data/oceanbase-all-in-one/rpms/ob-deploy-3.3.0-3.el7.x86_64.rpm to local mirror
name: obproxy-ce
version: 4.3.4.0
release:1.el7
arch: x86_64
md5: fba87ccf12faba9ba599cd7b0ca4a8149d1abb0e
size: 122385822
add /data/oceanbase-all-in-one/rpms/obproxy-ce-4.3.4.0-1.el7.x86_64.rpm to local mirror
name: ob-sysbench
version: 1.0.20
release:21.el7
arch: x86_64
md5: 34eb6ecba0ebc4c31c4cfa01162045cbbbec55f7
size: 1566511
add /data/oceanbase-all-in-one/rpms/ob-sysbench-1.0.20-21.el7.x86_64.rpm to local mirror
name: obtpcc
version: 5.0.0
release:1.el7
arch: x86_64
md5: 8624590be4bfe16f28bdd9fc5e4849cda19577d6
size: 1890344
add /data/oceanbase-all-in-one/rpms/obtpcc-5.0.0-1.el7.x86_64.rpm to local mirror
name: obtpch
version: 3.0.0
release:1.el7
arch: x86_64
md5: 3e3e88f87527677998fedf25087f5c87779dee62
size: 1856985
add /data/oceanbase-all-in-one/rpms/obtpch-3.0.0-1.el7.x86_64.rpm to local mirror
name: oceanbase-ce
version: 4.2.1.8
release:108000022024072217.el7
arch: x86_64
md5: 499b676f2ede5a16e0c07b2b15991d1160d972e8
size: 457041540
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-4.2.1.8-108000022024072217.el7.x86_64.rpm to local mirror
name: oceanbase-ce
version: 4.3.5.2
release:102020032025070315.el7
arch: x86_64
md5: e1014b804a2e7a348962718bc7ab1caefed3583c
size: 961400335
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-4.3.5.2-102020032025070315.el7.x86_64.rpm to local mirror
name: oceanbase-ce-libs
version: 4.2.1.8
release:108000022024072217.el7
arch: x86_64
md5: d02f4bfd321370a02550424293beb1be31204038
size: 468528
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-libs-4.2.1.8-108000022024072217.el7.x86_64.rpm to local mirror
name: oceanbase-ce-libs
version: 4.3.5.2
release:102020032025070315.el7
arch: x86_64
md5: 7b01818b2e293903709b0032ba192ac56b608660
size: 7848
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-libs-4.3.5.2-102020032025070315.el7.x86_64.rpm to local mirror
name: oceanbase-ce-utils
version: 4.2.1.8
release:108000022024072217.el7
arch: x86_64
md5: 6f87392f95b399a21382323f256cfda5969375c4
size: 403350984
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-utils-4.2.1.8-108000022024072217.el7.x86_64.rpm to local mirror
name: oceanbase-ce-utils
version: 4.3.5.2
release:102020032025070315.el7
arch: x86_64
md5: 72fb2761d45853ffeab875760a507570fed55891
size: 1727624640
add /data/oceanbase-all-in-one/rpms/oceanbase-ce-utils-4.3.5.2-102020032025070315.el7.x86_64.rpm to local mirror
name: oceanbase-diagnostic-tool
version: 3.4.0
release:12025051517.el7
arch: x86_64
md5: c1a7d5038425acc9324b1716da1abca94b7d68d7
size: 71624836
add /data/oceanbase-all-in-one/rpms/oceanbase-diagnostic-tool-3.4.0-12025051517.el7.x86_64.rpm to local mirror
name: ocp-agent-ce
version: 4.3.5
release:20250319105844.el7
arch: aarch64
md5: ae91444cd63f89bb4933d33f2eea801d50bb4fde
size: 122993656
add /data/oceanbase-all-in-one/rpms/ocp-agent-ce-4.3.5-20250319105844.el7.aarch64.rpm to local mirror
name: ocp-agent-ce
version: 4.3.5
release:20250319105844.el7
arch: x86_64
md5: 8c82c1cfb3ebfa951caa660f9d92fa1261a80c45
size: 178952150
add /data/oceanbase-all-in-one/rpms/ocp-agent-ce-4.3.5-20250319105844.el7.x86_64.rpm to local mirror
name: ocp-express
version: 4.2.2
release:100000022024011120.el7
arch: x86_64
md5: 09ffcf156d1df9318a78af52656f499d2315e3f7
size: 78426196
add /data/oceanbase-all-in-one/rpms/ocp-express-4.2.2-100000022024011120.el7.x86_64.rpm to local mirror
name: ocp-server-ce
version: 4.3.5
release:20250319105844.el7
arch: noarch
md5: 5c670871a262a5c95649ca8e2ad4b237e2a8aa43
size: 646926381
add /data/oceanbase-all-in-one/rpms/ocp-server-ce-4.3.5-20250319105844.el7.noarch.rpm to local mirror
name: openjdk-jre
version: 1.8.0_322
release:b09.el7
arch: x86_64
md5: 051aa69c5abb8697d15c2f0dcb1392b3f815f7ed
size: 69766947
add /data/oceanbase-all-in-one/rpms/openjdk-jre-1.8.0_322-b09.el7.x86_64.rpm to local mirror
name: prometheus
version: 2.37.1
release:10000102022110211.el7
arch: x86_64
md5: 58913c7606f05feb01bc1c6410346e5fc31cf263
size: 211224073
add /data/oceanbase-all-in-one/rpms/prometheus-2.37.1-10000102022110211.el7.x86_64.rpm to local mirror
Trace ID: 7be847e8-632f-11f0-9d54-00505697697e
If you want to view detailed obd logs, please run: obd display-trace 7be847e8-632f-11f0-9d54-00505697697e
Disable remote ok
Trace ID: 7fd16f9c-632f-11f0-a55e-00505697697e
If you want to view detailed obd logs, please run: obd display-trace 7fd16f9c-632f-11f0-a55e-00505697697e

add auto set env logic to profile: /root/.bash_profile

#########################################################################################
 Install Finished
=========================================================================================
Setup Environment:              source ~/.oceanbase-all-in-one/bin/env.sh
Quick Start:                    obd demo
Use Web Service to install:     obd web
Use Web Service to upgrade:     obd web upgrade
More Details:                   obd -h
=========================================================================================
[root@localhost bin]# source ~/.oceanbase-all-in-one/bin/env.sh
[root@localhost bin]# which obd
/usr/bin/obd
[root@localhost bin]# which obclient
/usr/bin/obclient
[root@localhost bin]#


[root@localhost conf]# obd cluster deploy obtest -c topology.yaml
+--------------------------------------------------------------------------------------------+
|                                          Packages                                          |
+--------------+---------+------------------------+------------------------------------------+
| Repository   | Version | Release                | Md5                                      |
+--------------+---------+------------------------+------------------------------------------+
| oceanbase-ce | 4.3.5.2 | 102020032025070315.el7 | e1014b804a2e7a348962718bc7ab1caefed3583c |
| obproxy-ce   | 4.3.4.0 | 1.el7                  | fba87ccf12faba9ba599cd7b0ca4a8149d1abb0e |
| obagent      | 4.2.2   | 100000042024011120.el7 | 19739a07a12eab736aff86ecf357b1ae660b554e |
| prometheus   | 2.37.1  | 10000102022110211.el7  | 58913c7606f05feb01bc1c6410346e5fc31cf263 |
| grafana      | 7.5.17  | 1                      | 1bf1f338d3a3445d8599dc6902e7aeed4de4e0d6 |
+--------------+---------+------------------------+------------------------------------------+
Repository integrity check ok
Load param plugin ok
Open ssh connection ok
Initializes obagent work home ok
Initializes observer work home ok
Initializes obproxy work home ok
Initializes prometheus work home ok
Initializes grafana work home ok
Parameter check ok
Remote oceanbase-ce-4.3.5.2-102020032025070315.el7-e1014b804a2e7a348962718bc7ab1caefed3583c repository install ok
Remote oceanbase-ce-4.3.5.2-102020032025070315.el7-e1014b804a2e7a348962718bc7ab1caefed3583c repository lib check ok
Remote obproxy-ce-4.3.4.0-1.el7-fba87ccf12faba9ba599cd7b0ca4a8149d1abb0e repository install ok
Remote obproxy-ce-4.3.4.0-1.el7-fba87ccf12faba9ba599cd7b0ca4a8149d1abb0e repository lib check ok
Remote obagent-4.2.2-100000042024011120.el7-19739a07a12eab736aff86ecf357b1ae660b554e repository install ok
Remote obagent-4.2.2-100000042024011120.el7-19739a07a12eab736aff86ecf357b1ae660b554e repository lib check ok
Remote prometheus-2.37.1-10000102022110211.el7-58913c7606f05feb01bc1c6410346e5fc31cf263 repository install ok
Remote prometheus-2.37.1-10000102022110211.el7-58913c7606f05feb01bc1c6410346e5fc31cf263 repository lib check ok
Remote grafana-7.5.17-1-1bf1f338d3a3445d8599dc6902e7aeed4de4e0d6 repository install ok
Remote grafana-7.5.17-1-1bf1f338d3a3445d8599dc6902e7aeed4de4e0d6 repository lib check ok
obtest deployed
Please execute ` obd cluster start obtest ` to start
Trace ID: dea0a0c4-62f3-11f0-a5b7-00505697697e
If you want to view detailed obd logs, please run: obd display-trace dea0a0c4-62f3-11f0-a5b7-00505697697e
[root@localhost conf]# obd cluster start obtest
Get local repositories ok
Load cluster param plugin ok
Open ssh connection ok
[WARN] OBD-1007: (192.168.0.95) The recommended number of core file size is unlimited (Current value: 0)
[WARN] OBD-1007: (192.168.0.95) The recommended number of stack size is unlimited (Current value: 8192)
[WARN] OBD-1007: (192.168.0.94) The recommended number of core file size is unlimited (Current value: 0)
[WARN] OBD-1007: (192.168.0.94) The recommended number of stack size is unlimited (Current value: 8192)
[WARN] OBD-1007: (192.168.0.96) The recommended number of core file size is unlimited (Current value: 0)
[WARN] OBD-1007: (192.168.0.96) The recommended number of stack size is unlimited (Current value: 8192)
[WARN] OBD-1012: (192.168.0.95) clog and data use the same disk (/)
[WARN] OBD-1012: (192.168.0.94) clog and data use the same disk (/)
[WARN] OBD-1012: (192.168.0.96) clog and data use the same disk (/)
Check before start obagent ok
Check before start prometheus ok
Check before start grafana ok
cluster scenario: htap
Start observer ok
observer program health check ok
Connect to observer 192.168.0.95:2881 ok
oceanbase bootstrap ok
obshell start ok
obshell program health check ok
obshell bootstrap ok
start obproxy ok
obproxy program health check ok
Connect to obproxy ok
Start obagent ok
obagent program health check ok
Send /root/.obd/repository/obagent/4.2.2/19739a07a12eab736aff86ecf357b1ae660b554e/conf/prometheus_config/rules to /home/ob/deploy/prometheus/rules ok
Start promethues ok
prometheus program health check ok
Start grafana ok
grafana program health check ok
Connect to grafana ok
Grafana modify password ok
Connect to observer 192.168.0.95:2881 ok
Wait for observer init ok
+------------------------------------------------+
|                  oceanbase-ce                  |
+--------------+---------+------+-------+--------+
| ip           | version | port | zone  | status |
+--------------+---------+------+-------+--------+
| 192.168.0.94 | 4.3.5.2 | 2881 | zone2 | ACTIVE |
| 192.168.0.95 | 4.3.5.2 | 2881 | zone1 | ACTIVE |
| 192.168.0.96 | 4.3.5.2 | 2881 | zone3 | ACTIVE |
+--------------+---------+------+-------+--------+
obclient -h192.168.0.94 -P2881 -uroot@sys -p'FkQlCnym2KBd7dM91AsV' -Doceanbase -A

cluster unique id: 258be0be-d7b0-5b5a-81d5-bf5d008cd75d-19817ce79b1-02050304

Connect to obproxy ok
+------------------------------------------------------------------+
|                            obproxy-ce                            |
+--------------+------+-----------------+-----------------+--------+
| ip           | port | prometheus_port | rpc_listen_port | status |
+--------------+------+-----------------+-----------------+--------+
| 192.168.0.95 | 2883 | 2884            | 2885            | active |
+--------------+------+-----------------+-----------------+--------+
obclient -h192.168.0.95 -P2883 -uroot@proxysys -p'Oyq3XC2uDo' -Doceanbase -A

Connect to Obagent ok
+-----------------------------------------------------------------+
|                             obagent                             |
+--------------+--------------------+--------------------+--------+
| ip           | mgragent_http_port | monagent_http_port | status |
+--------------+--------------------+--------------------+--------+
| 192.168.0.95 | 8089               | 8088               | active |
| 192.168.0.94 | 8089               | 8088               | active |
| 192.168.0.96 | 8089               | 8088               | active |
+--------------+--------------------+--------------------+--------+
Connect to Prometheus ok
+----------------------------------------------------------+
|                        prometheus                        |
+--------------------------+-------+--------------+--------+
| url                      | user  | password     | status |
+--------------------------+-------+--------------+--------+
| http://192.168.0.95:9090 | admin | 'tvz7RNeRNJ' | active |
+--------------------------+-------+--------------+--------+
Connect to grafana ok
+---------------------------------------------------------------------+
|                               grafana                               |
+--------------------------------------+-------+-------------+--------+
| url                                  | user  | password    | status |
+--------------------------------------+-------+-------------+--------+
| http://192.168.0.95:3000/d/oceanbase | admin | 'oceanbase' | active |
+--------------------------------------+-------+-------------+--------+
obtest running
Trace ID: ee7fbcfa-62f3-11f0-ae87-00505697697e
If you want to view detailed obd logs, please run: obd display-trace ee7fbcfa-62f3-11f0-ae87-00505697697e

```

## topology.yaml配置

```shell
## Only need to configure when remote login is required
user:
  # 需要修改为服务器的帐号密码：可以是非root用户
  username: test
  password: rootroot
#   key_file: your ssh-key file path if need
#   port: your ssh port, default 22
#   timeout: ssh connection timeout (second), default 30
oceanbase-ce:
  servers:
    - name: server1
      # Please don't use hostname, only IP can be supported
      ip: 192.168.0.95
    - name: server2
      ip: 192.168.0.94
    - name: server3
      ip: 192.168.0.96
  global:
    # Please set devname as the network adaptor's name whose ip is  in the setting of severs.
    # if set severs as "127.0.0.1", please set devname as "lo"
    # if current ip is 192.168.1.10, and the ip's network adaptor's name is "eth0", please use "eth0"
    # 根据实际的网络卡名称修改devname（通过ip a查看真是IP所在的网卡）
    devname: ens192
    # if current hardware's memory capacity is smaller than 50G, please use the setting of "mini-single-example.yaml" and do a small adjustment.
    memory_limit: 18G # The maximum running memory for an observer
    # The reserved system memory. system_memory is reserved for general tenants. The default value is 30G.
    system_memory: 8G
    datafile_size: 50G # Size of the data file.
    log_disk_size: 20G # The size of disk space used by the clog files.
    syslog_level: INFO # System log level. The default value is INFO.
    enable_syslog_wf: false # Print system logs whose levels are higher than WARNING to a separate log file. The default value is true.
    enable_syslog_recycle: true # Enable auto system log recycling or not. The default value is false.
    max_syslog_file_count: 4 # The maximum number of reserved log files before enabling auto recycling. The default value is 0.
    # observer cluster name, consistent with obproxy's cluster_name
    appname: obcluster
    production_mode: false
    # root_password: # root user password, can be empty
    # proxyro_password: # proxyro user pasword, consistent with obproxy's observer_sys_password, can be empty
  # In this example , support multiple ob process in single node, so different process use different ports.
  # If deploy ob cluster in multiple nodes, the port and path setting can be same.
  server1:
    mysql_port: 2881 # External port for OceanBase Database. The default value is 2881. DO NOT change this value after the cluster is started.
    rpc_port: 2882 # Internal port for OceanBase Database. The default value is 2882. DO NOT change this value after the cluster is started.
    #  The working directory for OceanBase Database. OceanBase Database is started under this directory. This is a required field.
    # 需要当前用户拥有权限的目录
    home_path: /home/test/deploy/observer
    # The directory for data storage. The default value is $home_path/store.
    # data_dir: /data
    # The directory for clog, ilog, and slog. The default value is the same as the data_dir value.
    # redo_dir: /redo
    zone: zone1
  server2:
    mysql_port: 2881 # External port for OceanBase Database. The default value is 2881. DO NOT change this value after the cluster is started.
    rpc_port: 2882 # Internal port for OceanBase Database. The default value is 2882. DO NOT change this value after the cluster is started.
    #  The working directory for OceanBase Database. OceanBase Database is started under this directory. This is a required field.
    home_path: /home/test/deploy/observer
    # The directory for data storage. The default value is $home_path/store.
    # data_dir: /data
    # The directory for clog, ilog, and slog. The default value is the same as the data_dir value.
    # redo_dir: /redo
    zone: zone2
  server3:
    mysql_port: 2881 # External port for OceanBase Database. The default value is 2881. DO NOT change this value after the cluster is started.
    rpc_port: 2882 # Internal port for OceanBase Database. The default value is 2882. DO NOT change this value after the cluster is started.
    #  The working directory for OceanBase Database. OceanBase Database is started under this directory. This is a required field.
    home_path: /home/test/deploy/observer
    # The directory for data storage. The default value is $home_path/store.
    # data_dir: /data
    # The directory for clog, ilog, and slog. The default value is the same as the data_dir value.
    # redo_dir: /redo
    zone: zone3
obproxy-ce:
  # Set dependent components for the component.
  # When the associated configurations are not done, OBD will automatically get the these configurations from the dependent components.
  depends:
    - oceanbase-ce
  servers:
    - 192.168.0.95
  global:
    listen_port: 2883 # External port. The default value is 2883.
    prometheus_listen_port: 2884 # The Prometheus port. The default value is 2884.
    home_path: /home/test/deploy/obproxy
    # oceanbase root server list
    # format: ip:mysql_port;ip:mysql_port. When a depends exists, OBD gets this value from the oceanbase-ce of the depends.
    # rs_list: 192.168.1.2:2881;192.168.1.3:2881;192.168.1.4:2881
    enable_cluster_checkout: false
    # observer cluster name, consistent with oceanbase-ce's appname. When a depends exists, OBD gets this value from the oceanbase-ce of the depends.
    # cluster_name: obcluster
    skip_proxy_sys_private_check: true
    enable_strict_kernel_release: false
    # obproxy_sys_password: # obproxy sys user password, can be empty. When a depends exists, OBD gets this value from the oceanbase-ce of the depends.
    # observer_sys_password: # proxyro user pasword, consistent with oceanbase-ce's proxyro_password, can be empty. When a depends exists, OBD gets this value from the oceanbase-ce of the depends.
obagent:
  depends:
    - oceanbase-ce
  servers:
    - name: server1
      # Please don't use hostname, only IP can be supported
      ip: 192.168.0.95
    - name: server2
      ip: 192.168.0.94
    - name: server3
      ip: 192.168.0.96
  global:
    home_path: /home/test/deploy/obagent
    ob_monitor_status: active
prometheus:
  depends:
    - obagent
  servers:
    - 192.168.0.95
  global:
    home_path: /home/test/deploy/prometheus
grafana:
  depends:
    - prometheus
  servers:
    - 192.168.0.95
  global:
    home_path: /home/test/deploy/grafana
    login_password: oceanbase
    global:
      # 将 eno1 修改为您服务器上实际的网络接口名称
      devname: ens192  # 假设您的网络接口名为 eth0，请根据实际情况修改
      memory_limit: 12G # 降低内存限制
      system_memory: 6G # 降低系统内存


# 执行命令：
obd cluster destroy obtest
obd cluster deploy obtest -c topology.yaml
obd cluster start obtest

```

## 创建用户命令

```shell
# 创建用户
CREATE USER 'sca'@'%' IDENTIFIED BY 'rootroot123QWE!@#';
-- 授予基本权限
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER 
 ON *.* TO 'sca'@'%';
GRANT LOCK TABLES ON *.* TO 'sca'@'%';
# 查询用户权限
SHOW GRANTS FOR 'sca'@'%';


# 时间查询
SHOW VARIABLES LIKE '%time_zone%';
SELECT NOW();

# 配置max_allowed_packet至256MB，防止查表或入库失败
SET GLOBAL max_allowed_packet = 268435456;
SELECT @@max_allowed_packet;
```