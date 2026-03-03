# nerdctl 命令说明文档

## 概述

nerdctl 是一个与 Docker 兼容的 CLI 工具，用于 containerd。它提供了与 docker 命令相同的用户体验，但使用 containerd 作为后端运行时。

## Containerd与 Docker 的差异

| 功能         | nerdctl    | docker     |
| ------------ | ---------- | ---------- |
| 后端运行时   | containerd | dockerd    |
| 镜像构建     | BuildKit   | 内置构建器 |
| 命名空间支持 | ✓          | ✗          |
| 镜像加密     | ✓          | ✗          |
| 延迟拉取     | ✓          | ✗          |
| P2P 镜像分发 | ✓          | ✗          |

## 安装

```bash
# 1. 下载并安装 containerd
# CentOS/RHEL/Rocky Linux
sudo yum install -y containerd.io
# 或者使用 dnf (较新系统)
sudo dnf install -y containerd.io

# 备选方案手动安装 containerd（如果包管理器中没有）
wget https://github.com/containerd/containerd/releases/download/v1.7.8/containerd-1.7.8-linux-amd64.tar.gz
sudo tar Cxzvf /usr/local containerd-1.7.8-linux-amd64.tar.gz
# 创建 systemd 服务文件
sudo wget -O /etc/systemd/system/containerd.service https://raw.githubusercontent.com/containerd/containerd/main/containerd.service

# 2. 重新加载 systemd 并启动服务
sudo systemctl daemon-reload
sudo systemctl start containerd
sudo systemctl enable containerd

# 3. 下载并安装 nerdctl
wget https://github.com/containerd/nerdctl/releases/download/v1.7.0/nerdctl-1.7.0-linux-amd64.tar.gz
sudo tar Cxzvf /usr/local/bin nerdctl-1.7.0-linux-amd64.tar.gz

# 4. 安装容器网络接口工具
# 尝试通过包管理器安装 CNI 插件
sudo yum install containernetworking-plugins
# 或者
sudo dnf install containernetworking-plugins

# 备选方案手动安装 CNI bin 目录
sudo mkdir -p /opt/cni/bin
# 下载最新的 CNI 插件（请检查最新版本）
CNI_VERSION="v1.3.0"
wget https://github.com/containernetworking/plugins/releases/download/${CNI_VERSION}/cni-plugins-linux-amd64-${CNI_VERSION}.tgz
# 将 CNI 插件解压到正确的目录
sudo tar -xzf cni-plugins-linux-amd64-${CNI_VERSION}.tgz -C /opt/cni/bin/
# 验证插件已安装
ls -la /opt/cni/bin/

# 5. 安装 runc
# 使用 yum/dnf 安装
sudo yum install -y runc
# 或者
sudo dnf install -y runc

# 备选方案手动安装 runc
wget https://github.com/opencontainers/runc/releases/download/v1.1.9/runc.amd64
# 安装到系统路径
sudo install -m 755 runc.amd64 /usr/local/sbin/runc
# 验证安装
runc --version
```

## 基础命令

### 容器管理

#### `nerdctl load` - 导入镜像

```bash
# 基本用法
nerdctl load [OPTIONS]

# 常用选项
-i, --input          # 从文件读取镜像（而不是 STDIN）
-q, --quiet          # 静默模式，不显示导入过程
--platform           # 指定平台（如 linux/amd64）
--namespace          # 指定命名空间（默认为 default）
```

**示例：**

```bash
# 从 tar 文件导入镜像
nerdctl --namespace default load -i nginx.tar

# 从标准输入导入镜像
cat nginx.tar | nerdctl --namespace default load

# 静默导入镜像
nerdctl --namespace default load -q -i nginx.tar

# 导入压缩的镜像文件
gunzip -c nginx.tar.gz | nerdctl --namespace default load

# 批量导入多个镜像
nerdctl --namespace default load -i web-stack.tar

# 指定平台导入镜像
nerdctl --namespace default load --platform linux/amd64 -i nginx.tar

# 导入镜像到指定命名空间
nerdctl --namespace k8s.io load -i nginx.tar
nerdctl --ns k8s.io load -i nginx.tar

# 组合使用命名空间和其他选项
nerdctl --namespace k8s.io load -q -i web-stack.tar

# 导入到生产环境命名空间
nerdctl --namespace production load -i nginx.tar
```

#### `nerdctl run` - 运行容器

```bash
# 基本用法
nerdctl run [OPTIONS] IMAGE [COMMAND] [ARG...]

# 常用选项
-d, --detach          # 后台运行容器
-i, --interactive     # 保持 STDIN 打开
-t, --tty            # 分配伪终端
-p, --publish        # 端口映射 host:container
-v, --volume         # 挂载卷 host:container
--name               # 指定容器名称
--rm                 # 容器退出时自动删除
-e, --env            # 设置环境变量
--network            # 指定网络
--restart            # 重启策略 (no|always|on-failure|unless-stopped)
```

**示例：**

```bash
# 运行 nginx 容器（指定命名空间和网络）
nerdctl --namespace default run -d -p 8080:80 --name my-nginx --network bridge nginx

# 交互式运行 Ubuntu（指定命名空间）
nerdctl --namespace default run -it --rm --network bridge ubuntu bash

# 挂载目录运行容器（指定命名空间和自定义网络）
nerdctl --namespace default run -d -v /host/data:/container/data --network my-network nginx

# 在 Kubernetes 命名空间运行容器
nerdctl --namespace k8s.io run -d -p 8080:80 --name my-nginx --network bridge nginx

# 在生产环境命名空间运行容器
nerdctl --namespace production run -d -p 8080:80 --name prod-nginx --network production-net nginx
```

#### `nerdctl ps` - 列出容器

```bash
# 列出运行中的容器（默认命名空间）
nerdctl --namespace default ps

# 列出所有容器（包括停止的）
nerdctl --namespace default ps -a

# 显示容器大小
nerdctl --namespace default ps -s

# 只显示容器 ID
nerdctl --namespace default ps -q

# 列出 Kubernetes 命名空间的容器
nerdctl --namespace k8s.io ps

# 列出生产环境命名空间的容器
nerdctl --namespace production ps -a
```

#### `nerdctl start/stop/restart` - 容器控制

```bash
# 启动容器（指定命名空间）
nerdctl --namespace default start CONTAINER

# 停止容器（指定命名空间）
nerdctl --namespace default stop CONTAINER

# 重启容器（指定命名空间）
nerdctl --namespace default restart CONTAINER

# 强制停止容器（指定命名空间）
nerdctl --namespace default kill CONTAINER

# 在 Kubernetes 命名空间操作容器
nerdctl --namespace k8s.io start CONTAINER
nerdctl --namespace k8s.io stop CONTAINER
```

#### `nerdctl rm` - 删除容器

```bash
# 删除容器（指定命名空间）
nerdctl --namespace default rm CONTAINER

# 强制删除运行中的容器（指定命名空间）
nerdctl --namespace default rm -f CONTAINER

# 删除所有停止的容器（指定命名空间）
nerdctl --namespace default container prune

# 在 Kubernetes 命名空间删除容器
nerdctl --namespace k8s.io rm CONTAINER

# 在生产环境命名空间清理停止的容器
nerdctl --namespace production container prune
```

#### `nerdctl exec` - 在运行的容器中执行命令

```bash
# 基本用法
nerdctl exec [OPTIONS] CONTAINER COMMAND [ARG...]

# 交互式执行（指定命名空间）
nerdctl --namespace default exec -it CONTAINER bash

# 以特定用户身份执行（指定命名空间）
nerdctl --namespace default exec -u root CONTAINER whoami

# 在 Kubernetes 命名空间执行命令
nerdctl --namespace k8s.io exec -it CONTAINER bash

# 在生产环境命名空间执行命令
nerdctl --namespace production exec -u root CONTAINER whoami
```

#### `nerdctl logs` - 查看容器日志

```bash
# 查看容器日志（指定命名空间）
nerdctl --namespace default logs CONTAINER

# 实时跟踪日志（指定命名空间）
nerdctl --namespace default logs -f CONTAINER

# 显示最后 N 行日志（指定命名空间）
nerdctl --namespace default logs --tail 100 CONTAINER

# 显示时间戳（指定命名空间）
nerdctl --namespace default logs -t CONTAINER

# 在 Kubernetes 命名空间查看日志
nerdctl --namespace k8s.io logs -f CONTAINER

# 在生产环境命名空间查看日志
nerdctl --namespace production logs --tail 100 CONTAINER
```

### 镜像管理

#### `nerdctl images` - 列出镜像

```bash
# 列出所有镜像（指定命名空间）
nerdctl --namespace default images

# 只显示镜像 ID（指定命名空间）
nerdctl --namespace default images -q

# 显示悬空镜像（指定命名空间）
nerdctl --namespace default images --filter dangling=true

# 列出 Kubernetes 命名空间的镜像
nerdctl --namespace k8s.io images

# 列出生产环境命名空间的镜像
nerdctl --namespace production images -q
```

#### `nerdctl pull/push` - 拉取/推送镜像

```bash
# 拉取镜像（指定命名空间）
nerdctl --namespace default pull IMAGE[:TAG]

# 推送镜像（指定命名空间）
nerdctl --namespace default push IMAGE[:TAG]

# 拉取所有标签（指定命名空间）
nerdctl --namespace default pull -a IMAGE

# 在 Kubernetes 命名空间拉取镜像
nerdctl --namespace k8s.io pull IMAGE[:TAG]

# 在生产环境命名空间推送镜像
nerdctl --namespace production push IMAGE[:TAG]
```

#### `nerdctl build` - 构建镜像

```bash
# 从 Dockerfile 构建镜像
nerdctl build [OPTIONS] PATH

# 常用选项
-t, --tag            # 镜像名称和标签
-f, --file           # Dockerfile 路径
--build-arg          # 构建参数
--no-cache           # 不使用缓存
--progress           # 进度显示类型 (auto|plain|tty)
```

**示例：**

```bash
# 构建并标记镜像（指定命名空间）
nerdctl --namespace default build -t my-app:latest .

# 使用指定 Dockerfile（指定命名空间）
nerdctl --namespace default build -f Dockerfile.dev -t my-app:dev .

# 传递构建参数（指定命名空间）
nerdctl --namespace default build --build-arg VERSION=1.0 -t my-app .

# 在生产环境命名空间构建镜像
nerdctl --namespace production build -t my-app:prod .

# 在开发环境命名空间构建镜像
nerdctl --namespace development build -t my-app:dev .
```

#### `nerdctl rmi` - 删除镜像

```bash
# 删除镜像
nerdctl rmi IMAGE

# 强制删除镜像
nerdctl rmi -f IMAGE

# 清理未使用的镜像
nerdctl image prune
```

#### `nerdctl tag` - 标记镜像

```bash
# 为镜像添加标签
nerdctl tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]
```

### 网络管理

#### `nerdctl network` - 网络命令

```bash
# 列出网络
nerdctl network ls

# 创建网络
nerdctl network create [OPTIONS] NETWORK
  --driver        # 网络驱动 (bridge|host|none)
  --subnet        # 子网 CIDR
  --gateway       # 网关地址

# 删除网络
nerdctl network rm NETWORK

# 查看网络详情
nerdctl network inspect NETWORK

# 将容器连接到网络
nerdctl network connect NETWORK CONTAINER

# 断开容器与网络的连接
nerdctl network disconnect NETWORK CONTAINER
```

**示例：**

```bash
# 创建自定义网络（指定命名空间）
nerdctl --namespace default network create --driver bridge my-network

# 在指定网络中运行容器（指定命名空间）
nerdctl --namespace default run -d --network my-network nginx

# 在 Kubernetes 命名空间创建网络
nerdctl --namespace k8s.io network create --driver bridge --subnet 172.18.0.0/16 k8s-net

# 在生产环境命名空间创建隔离网络
nerdctl --namespace production network create --driver bridge --subnet 172.19.0.0/16 --gateway 172.19.0.1 prod-isolated-net

# 在不同命名空间和网络中运行容器
nerdctl --namespace production run -d --network prod-isolated-net --name prod-nginx nginx
```

### 卷管理

#### `nerdctl volume` - 卷命令

```bash
# 列出卷
nerdctl volume ls

# 创建卷
nerdctl volume create [OPTIONS] VOLUME

# 删除卷
nerdctl volume rm VOLUME

# 查看卷详情
nerdctl volume inspect VOLUME

# 清理未使用的卷
nerdctl volume prune
```

**示例：**

```bash
# 创建命名卷
nerdctl volume create my-data

# 使用命名卷运行容器
nerdctl run -d -v my-data:/data nginx
```

## 镜像导入导出使用场景

### 常用的镜像传输流程

1. **离线环境部署**：

   ```bash
   # 在有网络的机器上导出镜像（指定命名空间）
   nerdctl --namespace default save -o nginx.tar nginx:latest
   
   # 传输到离线机器后导入（指定命名空间）
   nerdctl --namespace default load -i nginx.tar
   
   # 导入到生产环境命名空间
   nerdctl --namespace production load -i nginx.tar
   ```

2. **批量镜像迁移**：

   ```bash
   # 导出多个相关镜像
   nerdctl save -o web-stack.tar nginx:latest redis:alpine mysql:8.0
   
   # 在目标机器上导入
   nerdctl load -i web-stack.tar
   ```

3. **容器快照备份**：

   ```bash
   # 导出运行中容器的文件系统
   nerdctl export -o app-backup.tar my-app-container
   
   # 从备份创建新镜像
   nerdctl import app-backup.tar my-app:backup
   ```

4. **镜像压缩传输**：

   ```bash
   # 导出并压缩以减少传输大小
   nerdctl save nginx:latest | gzip > nginx.tar.gz
   
   # 解压并导入
   gunzip -c nginx.tar.gz | nerdctl load
   ```

## 高级功能

### Compose 支持

```bash
# 使用 docker-compose.yml（指定命名空间）
nerdctl --namespace default compose up -d

# 停止 Compose 服务（指定命名空间）
nerdctl --namespace default compose down

# 查看 Compose 服务状态（指定命名空间）
nerdctl --namespace default compose ps

# 在生产环境命名空间使用 Compose
nerdctl --namespace production compose up -d
nerdctl --namespace production compose ps
```

### Kubernetes 集成

```bash
# 在 Kubernetes 命名空间中运行（指定网络）
nerdctl --namespace k8s.io run --network bridge nginx

# 列出 Kubernetes 中的容器
nerdctl --namespace k8s.io ps

# 在 Kubernetes 命名空间创建自定义网络
nerdctl --namespace k8s.io network create --driver bridge k8s-cluster-net

# 使用自定义网络运行容器
nerdctl --namespace k8s.io run -d --network k8s-cluster-net nginx
```

### 镜像加密

```bash
# 加密镜像（指定命名空间）
nerdctl --namespace default image encrypt SOURCE_IMAGE TARGET_IMAGE

# 解密镜像（指定命名空间）
nerdctl --namespace default image decrypt SOURCE_IMAGE TARGET_IMAGE

# 在安全命名空间加密镜像
nerdctl --namespace security image encrypt sensitive-app:latest secure-app:encrypted

# 在生产环境解密镜像
nerdctl --namespace production image decrypt secure-app:encrypted production-app:latest
```

## 系统命令

### `nerdctl info` - 系统信息

```bash
# 显示系统信息（指定命名空间）
nerdctl --namespace default info

# 显示 containerd 版本
nerdctl version

# 查看不同命名空间的系统信息
nerdctl --namespace k8s.io info
nerdctl --namespace production info
```

### `nerdctl system` - 系统管理

```bash
# 显示磁盘使用情况（指定命名空间）
nerdctl --namespace default system df

# 清理系统（删除未使用的容器、网络、镜像等）
nerdctl --namespace default system prune

# 清理所有数据（包括卷）
nerdctl --namespace default system prune -a --volumes

# 清理不同命名空间的资源
nerdctl --namespace k8s.io system prune
nerdctl --namespace production system df
nerdctl --namespace development system prune -a --volumes
```

## 命名空间

nerdctl 支持 containerd 命名空间，可以实现镜像和容器的逻辑隔离：

```bash
# 创建新命名空间
nerdctl namespace create NAMESPACE

# 删除命名空间
nerdctl namespace remove NAMESPACE
nerdctl namespace rm NAMESPACE

# 查看命名空间详细信息
nerdctl namespace inspect NAMESPACE

# 指定命名空间运行命令
nerdctl --namespace NAMESPACE COMMAND

# 查询所有命名空间
nerdctl namespace ls 
nerdctl namespace list
nerdctl ns ls
nerdctl ns list

# 常用命名空间
--namespace default    # 默认命名空间
--namespace k8s.io     # Kubernetes 命名空间
--namespace moby       # Docker 兼容命名空间

# 查看不同命名空间的资源
nerdctl --namespace default ps
nerdctl --namespace k8s.io images
```

### 命名空间使用示例

```bash
# 在不同命名空间运行容器（指定网络）
nerdctl --namespace default run -d --network bridge nginx:latest
nerdctl --namespace k8s.io run -d --network bridge nginx:latest

# 命名空间间的镜像操作
nerdctl --namespace k8s.io pull busybox:latest
nerdctl --namespace k8s.io save -o busybox.tar busybox:latest
nerdctl --namespace default load -i busybox.tar

# 创建专用网络并运行容器
nerdctl --namespace production network create --driver bridge prod-net
nerdctl --namespace production run -d --network prod-net --name prod-nginx nginx:latest

# 开发环境网络配置
nerdctl --namespace development network create --driver bridge --subnet 172.21.0.0/16 dev-net
nerdctl --namespace development run -d --network dev-net --name dev-nginx nginx:latest
```

## 配置文件

nerdctl 配置文件位置：

- `~/.config/nerdctl/nerdctl.toml`
- `/etc/nerdctl/nerdctl.toml`

示例配置：

```toml
debug = false
debug_full = false
address = "/run/containerd/containerd.sock"
namespace = "default"
snapshotter = "overlayfs"
cgroup_manager = "systemd"
hosts_dir = ["/etc/containerd/certs.d", "/etc/docker/certs.d"]
```

## 常用技巧

1. **设置别名**：

   ```bash
   alias docker=nerdctl
   ```

2. **使用配置文件简化命令**：

   ```bash
   # 在配置文件中设置默认命名空间和地址
   echo 'namespace = "default"' > ~/.config/nerdctl/nerdctl.toml
   ```

3. **批量操作**：

   ```bash
   # 停止所有容器（指定命名空间）
   nerdctl --namespace default stop $(nerdctl --namespace default ps -q)
   
   # 删除所有镜像（指定命名空间）
   nerdctl --namespace default rmi $(nerdctl --namespace default images -q)
   
   # 批量操作不同命名空间
   nerdctl --namespace k8s.io stop $(nerdctl --namespace k8s.io ps -q)
   nerdctl --namespace production rmi $(nerdctl --namespace production images -q)
   ```

4. **资源监控**：

   ```bash
   # 查看容器资源使用情况（指定命名空间）
   nerdctl --namespace default stats
   
   # 查看容器进程（指定命名空间）
   nerdctl --namespace default top CONTAINER
   
   # 监控不同命名空间的资源
   nerdctl --namespace k8s.io stats
   nerdctl --namespace production top CONTAINER
   ```

## 故障排除

1. **权限问题**：

   ```bash
   # 确保用户在 docker 组中
   sudo usermod -aG docker $USER
   ```

2. **containerd 未运行**：

   ```bash
   # 启动 containerd 服务
   sudo systemctl start containerd
   ```

3. **网络问题**：

   ```bash
   # 重置网络（指定命名空间）
   nerdctl --namespace default network prune
   
   # 重置不同命名空间的网络
   nerdctl --namespace k8s.io network prune
   nerdctl --namespace production network prune
   
   # 查看网络配置（指定命名空间）
   nerdctl --namespace default network ls
   nerdctl --namespace default network inspect bridge
   ```

## 参考链接

- [nerdctl GitHub 仓库](https://github.com/containerd/nerdctl)
- [containerd 官方文档](https://containerd.io/)
- [BuildKit 文档](https://github.com/moby/buildkit)