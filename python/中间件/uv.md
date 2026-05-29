# uv -- Python 包管理工具

> **uv** 是由 Astral（Ruff 的同一团队）使用 Rust 编写的新一代 Python 包管理工具，旨在替代 `pip`、`pip-tools`、`pipx`、`poetry`、`pyenv`、`virtualenv` 等一系列工具，提供统一、极速的 Python 开发体验。

---

## 1. uv 简介与特点

### 核心优势

|特性|说明|
|---|---|
|**极速**|比 pip 快 10~100 倍，Rust 实现，并发下载与解析|
|**统一工具链**|一个命令替代 pip / virtualenv / pyenv / poetry / pipx|
|**全局缓存**|跨项目共享包缓存，节省磁盘空间|
|**锁文件支持**|`uv.lock` 保证可复现构建|
|**兼容 pip**|支持 `pyproject.toml`、`requirements.txt`、`setup.py`|
|**无需预装 Python**|可自动下载并管理 Python 解释器|

### 版本信息

```bash
uv --version
# uv 0.x.x (...)
```

---

## 2. 安装 uv

### macOS / Linux（推荐）

```bash
curl -LsSf https://astral.sh/uv/install.sh | sh
```

### Windows（PowerShell）

```powershell
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
```

### 通过 pip 安装

```bash
pip install uv
```

### 通过 Homebrew（macOS）

```bash
brew install uv
```

### 通过 Scoop（Windows）

```powershell
scoop install uv
```

### 验证安装

```bash
uv --version
uv help
```

### 自我更新

```bash
uv self update
```

---

## 3. Python 版本管理

uv 可以独立安装和管理多个 Python 版本，无需借助 pyenv。

### 查看可用的 Python 版本

```bash
uv python list
# 列出所有可下载/已安装的版本
uv python list --only-installed
# 仅列出已安装版本
```

### 安装指定 Python 版本

```bash
uv python install 3.12
uv python install 3.11 3.12 3.13   # 同时安装多个版本
uv python install pypy@3.10        # 安装 PyPy
```

### 查找某个版本的路径

```bash
uv python find 3.12
# /Users/xxx/.local/share/uv/python/cpython-3.12.x-.../bin/python3
```

### 卸载 Python 版本

```bash
uv python uninstall 3.11
```

### 固定项目使用的 Python 版本

```bash
# 在项目根目录生成 .python-version 文件
uv python pin 3.12
```

`.python-version` 文件内容：

```
3.12
```

---

## 4. 项目管理（推荐工作流）

uv 提供类似 cargo / poetry 的项目管理能力，以 `pyproject.toml` 为核心。

### 4.1 初始化新项目

```bash
uv init my-project
cd my-project
```

生成的目录结构：

```
my-project/
├── .python-version       # 锁定 Python 版本
├── pyproject.toml        # 项目配置
├── README.md
```

#### `pyproject.toml` 初始内容

```toml
[project]
name = "my-project"
version = "0.1.0"
description = "Add your description here"
readme = "README.md"
requires-python = ">=3.12"
dependencies = []

[build-system]
requires = ["hatchling"]
build-backend = "hatchling.build"
```

### 4.2 初始化已有项目（不创建目录）

```bash
cd existing-project
uv init
```

### 4.3 指定初始化选项

```bash
# 不使用 src 布局
uv init --no-package my-scripts

# 指定 Python 版本
uv init --python 3.11 my-project

# 创建库项目（lib 模板）
uv init --lib my-library

# 创建应用项目（app 模板）
uv init --app my-app
```

---

## 5. 虚拟环境管理

### 5.1 创建虚拟环境

```bash
# 在当前目录创建 .venv
uv venv

# 指定 Python 版本
uv venv --python 3.11

# 指定目录名
uv venv myenv

# 指定完整路径
uv venv /path/to/my-env
```

### 5.2 激活虚拟环境

```bash
# Linux / macOS
source .venv/bin/activate

# Windows CMD
.venv\Scripts\activate.bat

# Windows PowerShell
.venv\Scripts\Activate.ps1
```

### 5.3 退出虚拟环境

```bash
deactivate
```

### 5.4 不激活直接使用（推荐）

uv 在执行命令时会**自动探测**并使用当前项目的 `.venv`：

```bash
# 无需手动 activate，uv 自动使用 .venv
uv run python main.py
uv run pytest
uv run python -c "import django; print(django.__version__)"
```

---

## 6. 包安装与依赖管理

### 6.1 添加依赖（推荐）

将依赖写入 `pyproject.toml` 并安装：

```bash
uv add requests
uv add "fastapi>=0.100.0"
uv add "django~=4.2"
uv add numpy pandas matplotlib  # 同时添加多个
```

### 6.2 添加开发依赖

```bash
uv add --dev pytest black ruff
uv add --dev "pytest>=7.0"
```

`pyproject.toml` 中生成：

```toml
[project]
dependencies = [
    "requests>=2.31.0",
    "fastapi>=0.100.0",
]

[dependency-groups]
dev = [
    "pytest>=7.0",
    "black>=23.0",
    "ruff>=0.1.0",
]
```

### 6.3 添加可选依赖

```bash
uv add --optional docs sphinx
```

### 6.4 移除依赖

```bash
uv remove requests
uv remove --dev pytest
```

### 6.5 直接用 pip 风格安装（不推荐在项目中用）

```bash
uv pip install requests
uv pip install -r requirements.txt
uv pip install ".[dev]"          # 安装包含 dev extra 的本地包
uv pip install -e .              # 可编辑安装
```

### 6.6 安装到指定环境

```bash
uv pip install --python 3.11 requests
uv pip install --python .venv requests
```

### 6.7 卸载包

```bash
uv pip uninstall requests
```

### 6.8 查看已安装包

```bash
uv pip list
uv pip show requests
uv pip freeze              # 输出 requirements 格式
```

---

## 7. 依赖锁定与同步

### 7.1 生成锁文件

```bash
# 根据 pyproject.toml 生成 uv.lock（uv add 时会自动生成）
uv lock
```

`uv.lock` 是跨平台的精确锁文件，应提交到版本控制。

### 7.2 同步环境

将虚拟环境精确同步到锁文件状态：

```bash
uv sync                      # 同步所有依赖（含 dev）
uv sync --no-dev             # 仅同步生产依赖
uv sync --extra docs         # 包含指定 extra
uv sync --frozen             # 不更新锁文件，严格使用现有锁
```

### 7.3 更新依赖

```bash
uv lock --upgrade            # 升级所有依赖到最新兼容版本
uv lock --upgrade-package requests   # 仅升级某个包
```

### 7.4 从 requirements.txt 迁移

```bash
# 将 requirements.txt 中的依赖导入 pyproject.toml
uv add -r requirements.txt

# 或生成 requirements.txt 格式输出（用于兼容场景）
uv pip compile pyproject.toml -o requirements.txt
uv pip compile requirements.in -o requirements.txt
```

### 7.5 pip-compile 风格（pip-tools 替代）

```bash
# 编译锁定依赖
uv pip compile requirements.in --output-file requirements.txt

# 同步环境到 requirements.txt
uv pip sync requirements.txt
```

---

## 8. 脚本运行（uvx / uv run）

### 8.1 `uv run` —— 在项目环境中运行

```bash
# 运行 Python 脚本
uv run main.py

# 运行模块
uv run -m pytest

# 临时添加依赖运行（不写入 pyproject.toml）
uv run --with requests python -c "import requests; print(requests.get('https://httpbin.org/get').status_code)"

# 指定 Python 版本运行
uv run --python 3.11 main.py
```

### 8.2 内联脚本依赖（PEP 723）

uv 支持在脚本文件中声明依赖：

```python
# script.py
# /// script
# requires-python = ">=3.11"
# dependencies = [
#   "requests>=2.31.0",
#   "rich",
# ]
# ///

import requests
from rich import print

resp = requests.get("https://httpbin.org/get")
print(resp.json())
```

```bash
# uv 会自动创建隔离环境并安装依赖
uv run script.py
```

### 8.3 `uvx` —— 临时运行工具（替代 pipx run）

```bash
# 不安装，直接运行工具
uvx ruff check .
uvx black .
uvx httpie https://httpbin.org/get
uvx --from cowsay cowsay "Hello uv!"

# 指定版本
uvx ruff@0.1.0 check .
```

---

## 9. 工具管理（替代 pipx）

uv 可以将命令行工具安装到全局隔离环境，类似 pipx。

### 9.1 安装工具

```bash
uv tool install ruff
uv tool install black
uv tool install httpie
uv tool install "ruff>=0.1.0"

# 安装后可全局使用
ruff --version
black --version
```

### 9.2 查看已安装工具

```bash
uv tool list
```

### 9.3 更新工具

```bash
uv tool upgrade ruff
uv tool upgrade --all   # 升级所有工具
```

### 9.4 卸载工具

```bash
uv tool uninstall ruff
```

### 9.5 工具安装路径

```bash
# 工具二进制文件位于
~/.local/bin/          # Linux / macOS
%APPDATA%\uv\bin\      # Windows
```

---

## 10. 配置文件详解

### 10.1 `pyproject.toml` 完整示例

```toml
[project]
name = "my-app"
version = "1.0.0"
description = "My Python application"
readme = "README.md"
license = { text = "MIT" }
authors = [{ name = "Su", email = "su@example.com" }]
requires-python = ">=3.11"

# 生产依赖
dependencies = [
    "fastapi>=0.110.0",
    "uvicorn[standard]>=0.27.0",
    "sqlalchemy>=2.0.0",
    "pydantic>=2.0.0",
]

# 可选依赖（extras）
[project.optional-dependencies]
docs = ["mkdocs>=1.5.0", "mkdocs-material>=9.0.0"]
test = ["pytest>=7.4.0", "httpx>=0.26.0"]

# 命令行入口
[project.scripts]
my-app = "my_app.main:app"

# 构建系统
[build-system]
requires = ["hatchling"]
build-backend = "hatchling.build"

# uv 专属配置
[tool.uv]
dev-dependencies = [
    "pytest>=7.4.0",
    "pytest-asyncio>=0.23.0",
    "ruff>=0.3.0",
    "black>=24.0.0",
    "mypy>=1.8.0",
]

# 包索引配置（私有源）
[[tool.uv.index]]
name = "pypi"
url = "https://pypi.org/simple"
default = true

[[tool.uv.index]]
name = "private"
url = "https://private.example.com/simple"
```

### 10.2 全局配置文件

位置：

- **Linux/macOS**：`~/.config/uv/uv.toml`
- **Windows**：`%APPDATA%\uv\uv.toml`

```toml
# uv.toml
[pip]
index-url = "https://mirrors.aliyun.com/pypi/simple/"   # 使用阿里云镜像

[python]
preference = "managed"    # 优先使用 uv 管理的 Python

[cache]
dir = "/path/to/custom/cache"   # 自定义缓存目录
```

### 10.3 环境变量

|环境变量|说明|
|---|---|
|`UV_PYTHON`|指定默认 Python 版本|
|`UV_INDEX_URL`|覆盖 PyPI 源|
|`UV_CACHE_DIR`|自定义缓存目录|
|`UV_NO_CACHE`|禁用缓存（值为 `1`）|
|`UV_SYSTEM_PYTHON`|允许使用系统 Python|
|`UV_LINK_MODE`|链接模式（`copy`/`symlink`/`hardlink`）|

```bash
# 示例：使用国内镜像
export UV_INDEX_URL=https://mirrors.aliyun.com/pypi/simple/

# 或在命令中临时指定
uv add requests --index-url https://pypi.tuna.tsinghua.edu.cn/simple/
```

---

## 11. 常用命令速查

### 项目管理

```bash
uv init [name]                  # 初始化项目
uv add <package>                # 添加依赖
uv add --dev <package>          # 添加开发依赖
uv remove <package>             # 移除依赖
uv sync                         # 同步环境
uv lock                         # 生成/更新锁文件
uv run <script>                 # 运行脚本
uv run -m <module>              # 运行模块
uv build                        # 构建分发包
uv publish                      # 发布到 PyPI
```

### Python 管理

```bash
uv python list                  # 列出可用版本
uv python install 3.12          # 安装 Python
uv python uninstall 3.11        # 卸载 Python
uv python pin 3.12              # 固定项目 Python 版本
uv python find                  # 查找当前使用的 Python 路径
```

### 虚拟环境

```bash
uv venv                         # 创建 .venv
uv venv --python 3.11           # 指定版本
uv venv myenv                   # 自定义名称
```

### pip 兼容层

```bash
uv pip install <package>        # 安装包
uv pip install -r req.txt       # 从文件安装
uv pip uninstall <package>      # 卸载
uv pip list                     # 列出已安装
uv pip show <package>           # 查看包信息
uv pip freeze                   # 导出 requirements 格式
uv pip compile req.in           # 编译依赖
uv pip sync req.txt             # 同步到 requirements
```

### 工具管理

```bash
uvx <tool>                      # 临时运行工具
uv tool install <tool>          # 全局安装工具
uv tool uninstall <tool>        # 卸载工具
uv tool list                    # 列出已安装工具
uv tool upgrade <tool>          # 升级工具
uv tool upgrade --all           # 升级所有工具
```

### 缓存管理

```bash
uv cache dir                    # 查看缓存目录
uv cache clean                  # 清理全部缓存
uv cache clean ruff             # 清理某个包的缓存
uv cache prune                  # 删除过期缓存
```

---

## 12. 与其他工具对比

|功能|pip|poetry|conda|uv|
|---|---|---|---|---|
|包安装|✅|✅|✅|✅|
|虚拟环境|❌（需 venv）|✅|✅|✅|
|Python 版本管理|❌|❌（需 pyenv）|✅|✅|
|锁文件|❌（需 pip-tools）|✅|✅|✅|
|全局工具安装|❌（需 pipx）|❌|❌|✅|
|速度|慢|中|慢|**极快**|
|语言实现|Python|Python|Python|**Rust**|
|pyproject.toml|部分|✅|❌|✅|
|requirements.txt|✅|部分|部分|✅|

### 速度对比（官方数据）

```
安装 scipy 场景（冷缓存）：
pip        → ~45s
poetry     → ~42s
uv         →  ~3s   🚀（约快 15 倍）

安装 scipy 场景（热缓存）：
pip        → ~12s
uv         → ~0.5s  🚀（约快 24 倍）
```

---

## 13. 最佳实践

### 13.1 标准项目工作流

```bash
# 1. 初始化项目
uv init my-project
cd my-project

# 2. 固定 Python 版本
uv python pin 3.12

# 3. 添加依赖
uv add fastapi uvicorn
uv add --dev pytest ruff black

# 4. 开发时运行
uv run uvicorn main:app --reload
uv run pytest

# 5. 提交 pyproject.toml 和 uv.lock 到 Git
git add pyproject.toml uv.lock
git commit -m "Add dependencies"

# 6. 团队成员克隆后同步环境
git clone ...
cd my-project
uv sync          # 自动创建 .venv 并安装所有依赖
```

### 13.2 使用国内镜像加速

```bash
# 临时使用
uv add requests --index-url https://mirrors.aliyun.com/pypi/simple/

# 全局配置（写入 ~/.config/uv/uv.toml）
[pip]
index-url = "https://pypi.tuna.tsinghua.edu.cn/simple"

# 常用国内镜像
# 清华：  https://pypi.tuna.tsinghua.edu.cn/simple
# 阿里云：https://mirrors.aliyun.com/pypi/simple/
# 腾讯：  https://mirrors.cloud.tencent.com/pypi/simple
# 中科大：https://pypi.mirrors.ustc.edu.cn/simple/
```

### 13.3 CI/CD 集成（GitHub Actions）

```yaml
# .github/workflows/ci.yml
name: CI

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Install uv
        uses: astral-sh/setup-uv@v3
        with:
          version: "latest"
          enable-cache: true

      - name: Set up Python
        run: uv python install

      - name: Install dependencies
        run: uv sync --frozen

      - name: Run tests
        run: uv run pytest

      - name: Lint
        run: uv run ruff check .
```

### 13.4 Docker 集成

```dockerfile
FROM python:3.12-slim

# 安装 uv
COPY --from=ghcr.io/astral-sh/uv:latest /uv /usr/local/bin/uv

WORKDIR /app

# 复制依赖文件
COPY pyproject.toml uv.lock ./

# 安装依赖（不含 dev，使用锁文件，不更新锁）
RUN uv sync --frozen --no-dev

# 复制源代码
COPY src/ ./src/

CMD ["uv", "run", "python", "-m", "my_app"]
```

### 13.5 `.gitignore` 配置

```gitignore
# Python
__pycache__/
*.py[cod]
*.pyo
*.pyd

# 虚拟环境（不提交）
.venv/
venv/
env/

# uv 缓存（不提交）
.uv/

# 提交 uv.lock（保证可复现构建）
# !uv.lock  ← 不要忽略它
```

### 13.6 常见问题

**Q: uv.lock 应该提交到 Git 吗？**

> ✅ 应该提交。`uv.lock` 保证所有环境安装完全相同的包版本，是可复现构建的基础。

**Q: uv 和 pip 可以混用吗？**

> ⚠️ 不推荐混用。在使用 `uv sync` 管理的项目中直接用 `pip install` 会导致环境状态与锁文件不一致。

**Q: 如何查看依赖树？**

> ```bash
> uv tree
> uv tree --depth 2   # 限制显示深度
> ```

**Q: 如何检查过期依赖？**

> ```bash
> uv pip list --outdated
> ```

---

## 参考资料

- 官方文档：https://docs.astral.sh/uv/
- GitHub 仓库：https://github.com/astral-sh/uv
- 更新日志：https://github.com/astral-sh/uv/blob/main/CHANGELOG.md
- PEP 723（内联脚本依赖）：https://peps.python.org/pep-0723/

---

> 笔记生成时间：2026-05-28 | uv 版本参考：0.4.x+