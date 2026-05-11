# Miniconda 

> 适用场景：Python / Flask 后端项目开发，虚拟环境管理

---

## 一、Miniconda 简介

Miniconda 是 Anaconda 的轻量级版本，只包含 `conda` 包管理器和 Python 本身，不附带大量预装科学计算库。它的核心能力是：

- **创建并管理相互隔离的虚拟环境**，不同项目可以使用不同版本的 Python 和依赖包，互不干扰。
- **跨平台**，支持 Windows、macOS、Linux。
- 比 Anaconda 更轻量，适合服务器和后端开发场景。

---

## 二、安装 Miniconda

### 1. 下载安装包

前往官网下载对应平台的安装脚本：

```
https://mirrors.tuna.tsinghua.edu.cn/anaconda/miniconda/
```

### 2. Linux / macOS 安装

```bash
# 下载（以 Linux x86_64 为例）
wget https://repo.anaconda.com/miniconda/Miniconda3-latest-Linux-x86_64.sh

# 执行安装
bash Miniconda3-latest-Linux-x86_64.sh

# 安装完成后，让配置生效
source ~/.bashrc   # Linux
source ~/.zshrc    # macOS (zsh)
```

### 3. 验证安装

```bash
conda --version
# 输出示例：conda 24.x.x
```

---

## 三、conda 基础概念

|概念|说明|
|---|---|
|**base 环境**|conda 安装后默认激活的根环境，建议不要在此环境中直接安装项目依赖|
|**虚拟环境**|独立隔离的 Python 运行空间，每个项目建议创建一个独立环境|
|**channel**|包的来源仓库，默认为 `defaults`，也可使用 `conda-forge`|

---

## 四、虚拟环境管理（核心操作）

### 4.1 创建虚拟环境

```bash
# 创建指定 Python 版本的虚拟环境
conda create -n flask-env python=3.11

# 创建时同时安装常用包
conda create -n flask-env python=3.11 flask
```

> `-n` 参数指定环境名称，建议以项目名命名，如 `flask-env`、`qassistant-env`。

### 4.2 激活 / 退出环境

```bash
# 激活环境
conda activate flask-env

# 退出当前环境（回到 base）
conda deactivate
```

激活成功后，命令行前缀会变为：

```
(flask-env) user@host:~$
```

### 4.3 查看所有环境

```bash
conda env list
# 或
conda info --envs
```

输出示例：

```
# conda environments:
#
base                  *  /home/user/miniconda3
flask-env                /home/user/miniconda3/envs/flask-env
qassistant-env           /home/user/miniconda3/envs/qassistant-env
```

### 4.4 删除环境

```bash
conda remove -n flask-env --all
```

### 4.5 重命名环境（conda 不支持直接重命名，用克隆代替）

```bash
# 克隆旧环境
conda create -n new-name --clone old-name

# 删除旧环境
conda remove -n old-name --all
```

---

## 五、包管理

### 5.1 conda 安装包

```bash
# 激活环境后安装
conda activate flask-env
conda install flask

# 安装指定版本
conda install flask=3.0.0

# 从 conda-forge 渠道安装（包更全更新）
conda install -c conda-forge flask
```

### 5.2 pip 安装包（推荐 Flask 项目使用）

在 conda 环境中也可以正常使用 `pip`，且 pip 安装的包也只作用于当前环境：

```bash
conda activate flask-env

# 安装 Flask 及常用扩展
pip install flask
pip install flask-sqlalchemy
pip install flask-migrate
pip install python-dotenv
```

> ⚠️ **建议**：在同一个环境中，尽量统一使用 `pip` 或 `conda` 其中一种安装，混用时 `pip` 优先，避免冲突。

### 5.3 查看已安装的包

```bash
# 查看当前环境所有包
conda list

# 使用 pip 查看
pip list
```

### 5.4 卸载包

```bash
conda remove flask
# 或
pip uninstall flask
```

---

## 六、依赖文件管理

### 6.1 导出环境依赖（用于协作 / 部署）

```bash
# 导出为 environment.yml（conda 格式，跨平台）
conda env export > environment.yml

# 导出为 requirements.txt（pip 格式，更通用）
pip freeze > requirements.txt
```

### 6.2 从依赖文件恢复环境

```bash
# 从 environment.yml 还原
conda env create -f environment.yml

# 从 requirements.txt 安装
pip install -r requirements.txt
```

> **推荐做法**：Flask 项目提交到 Git 时，同时维护一份 `requirements.txt`，方便他人快速搭建相同的开发环境。

---

## 七、Flask 项目完整工作流示例

```bash
# 1. 创建项目专属虚拟环境
conda create -n my-flask-app python=3.11

# 2. 激活环境
conda activate my-flask-app

# 3. 安装 Flask 及依赖
pip install flask flask-sqlalchemy python-dotenv

# 4. 创建项目目录
mkdir my-flask-app && cd my-flask-app

# 5. 开发调试...（略）

# 6. 导出依赖
pip freeze > requirements.txt

# 7. 完成后退出环境
conda deactivate
```

---

## 八、常用命令速查表

|操作|命令|
|---|---|
|查看 conda 版本|`conda --version`|
|更新 conda 自身|`conda update conda`|
|创建环境|`conda create -n 环境名 python=3.11`|
|激活环境|`conda activate 环境名`|
|退出环境|`conda deactivate`|
|查看所有环境|`conda env list`|
|删除环境|`conda remove -n 环境名 --all`|
|安装包|`conda install 包名` 或 `pip install 包名`|
|卸载包|`conda remove 包名` 或 `pip uninstall 包名`|
|查看已安装包|`conda list` 或 `pip list`|
|导出依赖|`pip freeze > requirements.txt`|
|还原依赖|`pip install -r requirements.txt`|

---

## 九、注意事项与最佳实践

1. **不要在 `base` 环境开发**：始终为每个项目创建独立环境，保持 base 环境干净。
2. **Python 版本选择**：Flask 3.x 需要 Python 3.8+，建议使用 3.10 或 3.11。
3. **环境命名规范**：建议用项目名或功能描述命名，如 `flask-blog`、`qassistant-dev`，避免使用 `test`、`env1` 等无意义名称。
4. **及时更新依赖文件**：每次新增或删除依赖后，重新执行 `pip freeze > requirements.txt`。
5. **IDE 集成**：在 VS Code 或 PyCharm 中，选择解释器时指定 conda 环境路径（通常在 `~/miniconda3/envs/环境名/bin/python`）。

---

_笔记整理于 Python / Flask 后端学习阶段，持续更新中。_