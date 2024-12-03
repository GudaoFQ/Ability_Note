## 记录工作中能使用到，但是又不好记的centos命令

### 防火墙（cmd管理员）
#### 添加防火墙端口规则
```shell
# 开放入站规则
netsh advfirewall firewall add rule name="Open Port 12135" dir=in action=allow protocol=TCP localport=12135

# 开放出站规则
netsh advfirewall firewall add rule name="Open Port 12135 Out" dir=out action=allow protocol=TCP localport=12135
```

### 验证端口是否已经开放
```shell
# rule为添加防火墙中的规则
netsh advfirewall firewall show rule name="Open Port 12135"
```

### 删除防火墙端口规则
```shell
netsh advfirewall firewall delete rule name="Open Port 12135"
netsh advfirewall firewall delete rule name="Open Port 12135 Out"
```

### 防火墙（prowershell管理员）
#### 添加防火墙端口规则
```shell
# 开放入站规则
# 修改DisplayName和LocalPort后的端口信息
New-NetFirewallRule -DisplayName "Allow Port 12135" -Direction Inbound -LocalPort 12135 -Protocol TCP -Action Allow
```

### 验证端口是否已经开放
```shell
# DisplayName为添加防火墙中的规则
Get-NetFirewallRule -DisplayName "Allow Port 12135"
```

### 删除防火墙端口规则
```shell
Remove-NetFirewallRule -DisplayName "Allow Port 12135"
```