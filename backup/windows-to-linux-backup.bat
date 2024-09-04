:: 备份脚本，修改完成后请将所有注释删除
@echo off
:: chcp 65001 命令将代码页设置为 UTF-8
chcp 65001 >nul

:: 设置源服务器ip地址（需要备份的主机ip）
set SourceServers=192.168.5.36
:: 设置需要备份的目录：/cygdrive/盘符/文件夹路径
set SourceDir=/cygdrive/c/Users/Gudao/Documents/cwrsync_6.3.1_x64/
:: ssh路径（使用rsync自带的ssh，win安装了openssh、git也会有自己的ssh命令，此处不指定准确位置，会产生冲突报错）
set SshPath=C:\Users\Gudao\Documents\cwrsync_6.3.1_x64\bin\ssh.exe
:: 备份目标服务器信息
set BackupServerUser=root
set BackupServerIp=192.168.0.66
:: 备份目标服务器备份文件夹位置
set BackupDir=/home

for %%s in (%SourceServers%) do (
    :: 打印命令 "%BackupServerUser%@%BackupServerIp%:%BackupDir%/%%s" 相当于：root@ip:/home
    echo command: rsync -avz --delete -e "%SshPath%" "%SourceDir%" "%BackupServerUser%@%BackupServerIp%:%BackupDir%/%%s"
    echo Backup in progress: %%s...

    :: 使用rsync进行备份
    cmd /c rsync -avz --delete -e "%SshPath%" "%SourceDir%" "%BackupServerUser%@%BackupServerIp%:%BackupDir%/%%s"

    if errorlevel 1 (
        echo Backup of %%s failed.
    ) else (
        echo Backup of %%s successfully completed.
    )
)