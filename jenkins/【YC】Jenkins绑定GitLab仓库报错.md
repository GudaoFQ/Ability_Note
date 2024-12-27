# Jenkins绑定GitLab仓库报错

## 说明
* 服务器版本：统信（Union OS Server 20）
* Jenkins版本：2.346.3-2-lts
* GitLab：使用了https协议，并且证书不受信任

## 问题
```shell
无法连接仓库：Command "git ls-remote -h https://gitlab.com/xxx/xxx.git HEAD" failed with exit code 128
stdout:
stderr: unable to access 'https://gitlab.com/xxx/xxx.git/info/refs': server certificate verification failed. CAfile: none CRLfile: none
```

### 关闭git中的SSL验证
```shell
# 进入jenkins容器
docker exec -it jenkins bash
# 关闭SSL验证，仅建议在测试环境使用
git config --global http.sslVerify false
# 验证配置是否生效，如果显示 http.sslverify=false 就说明配置成功了
git config --list | grep http.sslVerify
```