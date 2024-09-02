# 备份源服务器信息（需要备份的服务器信息）
$SourceServers = @("192.168.5.xx")
# 源服务器备份文件夹位置：/cygdrive/盘符/文件位置
$SourceDir = "/cygdrive/c/Users/Gudao/Documents/cwrsync_6.3.1_x64/"
# ssh路径（使用rsync自带的ssh，win安装了openssh、git也会有自己的ssh命令，此处不指定准确位置，会产生冲突报错）
$SshPath = "C:\Users\Gudao\Documents\cwrsync_6.3.1_x64\bin\ssh.exe"
# 备份目标服务器信息
$BackupServerUser = "root"
$BackupServerIp = "192.168.0.xx"
# 备份目标服务器备份文件夹位置
$BackupDir = "/home"
# 确保已安装rsync for Windows
if (!(Get-Command rsync -ErrorAction SilentlyContinue))
{
    Write-Host "Please install rsync for Windows."
    exit
}
foreach ($Server in $SourceServers)
{
    $BackupPath = "$BackupDir/$Server"
    # 使用rsync进行备份
    $DestinationPath = "root@${BackupServerIp}:${BackupPath}"
    $RsyncCommand = "rsync -avz --delete -e `"$SshPath`" `"$SourceDir`" `"$DestinationPath`""

    Write-Host "command: $RsyncCommand"
    Write-Host "Backup in progress: $Server..."

    # 使用 Invoke-Expression 来执行rsync命令
    Invoke-Expression $RsyncCommand

    if ($LASTEXITCODE -eq 0)
    {
        Write-Host "Backup of $Server successfully completed."
    }
    else
    {
        Write-Host "Backup of $Server failed."
    }
}
