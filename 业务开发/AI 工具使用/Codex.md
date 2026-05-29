# Codex CLI 命令说明书

> 适用于 Codex CLI 终端版。整理日期：2026/05/27。  
> 本文基于本机 `codex-cli 0.129.0`，并参考 OpenAI 官方 Codex CLI 文档。

---

## 一、交互式 Slash 命令

这些命令是在进入 `codex` 终端界面后，在输入框中以 `/` 开头输入的命令。

| 命令 | 作用 |
|------|------|
| `/model` | 切换当前会话模型，也可调整 reasoning effort |
| `/fast` | 开启、关闭或查看 Fast 服务层，例如 `/fast on`、`/fast off`、`/fast status` |
| `/status` | 查看当前模型、权限策略、工作目录、token/上下文使用情况 |
| `/permissions` | 调整 Codex 执行命令、写文件时是否需要你批准 |
| `/approve` | 对最近一次被自动审查拒绝的操作，批准重试一次 |
| `/plan` | 切换到计划模式，让 Codex 先给方案再动手 |
| `/personality` | 切换回答风格，例如更简洁或更协作 |
| `/clear` | 清空当前终端显示并开始新对话 |
| `/new` | 在同一个 CLI 会话里开始新对话，但不清屏 |
| `/compact` | 压缩长上下文，保留摘要以节省 token |
| `/resume` | 恢复之前保存的会话 |
| `/fork` | 从当前会话分叉出新线程，保留原会话 |
| `/side` | 开一个临时侧边对话，不打断主线任务 |
| `/diff` | 查看当前 Git 工作区 diff，包括未跟踪文件 |
| `/review` | 让 Codex 审查当前工作区变更 |
| `/init` | 生成 `AGENTS.md`，用于保存项目级长期指令 |
| `/mention` | 引用或附加某个文件/目录到上下文 |
| `/mcp` | 查看当前配置的 MCP 服务和工具；`/mcp verbose` 可查看详情 |
| `/plugins` | 浏览、查看、启用或禁用插件 |
| `/skills` | 浏览并使用本地技能 |
| `/apps` | 浏览连接器/app，并插入到提示词中 |
| `/hooks` | 查看和管理生命周期 hooks |
| `/memories` | 配置记忆注入和记忆生成 |
| `/ide` | 引入 IDE 当前打开文件、选区等上下文 |
| `/agent` | 切换当前活跃的 agent/subagent 线程 |
| `/ps` | 查看后台终端任务 |
| `/stop` | 停止当前会话的后台终端任务；`/clean` 是别名 |
| `/copy` | 复制 Codex 最近一次完成的输出，也可按 `Ctrl+O` |
| `/raw` | 切换 raw scrollback 模式，方便复制终端输出 |
| `/vim` | 切换输入框 Vim 模式 |
| `/keymap` | 查看和修改 TUI 快捷键 |
| `/statusline` | 配置底部状态栏显示项 |
| `/title` | 配置终端窗口/标签标题显示项 |
| `/theme` | 切换语法高亮主题 |
| `/experimental` | 开关实验性功能 |
| `/debug-config` | 查看配置层级、策略来源、MCP、rules 等诊断信息 |
| `/sandbox-add-read-dir` | Windows 专用，给沙箱额外目录读取权限 |
| `/feedback` | 提交日志/诊断反馈 |
| `/logout` | 登出当前 Codex 账号 |
| `/quit` / `/exit` | 退出 Codex CLI |

> 最准确的命令列表以你当前 Codex CLI 中输入 `/` 后弹出的菜单为准。不同版本、配置、插件环境下可用命令可能不同。

---

## 二、外层 codex 命令

这些命令是在系统终端中执行的，不是在 Codex 输入框里输入。

| 命令 | 作用 |
|------|------|
| `codex` | 启动交互式 Codex CLI |
| `codex "你的任务"` | 直接带初始 prompt 启动交互会话 |
| `codex exec "任务"` / `codex e "任务"` | 非交互模式运行一次任务，适合脚本和自动化 |
| `codex review` | 非交互式代码审查 |
| `codex login` | 登录 Codex |
| `codex login status` | 查看登录状态 |
| `codex logout` | 清除本地登录凭据 |
| `codex resume` | 恢复历史交互会话 |
| `codex resume --last` | 继续最近一次会话 |
| `codex fork` | 从历史会话分叉新会话 |
| `codex fork --last` | 从最近一次会话分叉 |
| `codex mcp list/get/add/remove/login/logout` | 管理 MCP 服务 |
| `codex plugin marketplace ...` | 管理插件 marketplace |
| `codex features list/enable/disable` | 查看、启用、禁用 feature flags |
| `codex completion` | 生成 shell 自动补全脚本 |
| `codex update` | 更新 Codex CLI |
| `codex app` | 启动 Codex 桌面端 |
| `codex apply` / `codex a` | 应用 Codex Cloud 任务生成的最新 diff |
| `codex cloud` | 浏览或处理 Codex Cloud 任务，实验性功能 |
| `codex sandbox windows/linux/macos` | 在 Codex 提供的沙箱中运行命令 |
| `codex debug models` | 输出 Codex 可见的模型目录 |
| `codex mcp-server` | 把 Codex 作为 MCP server 启动 |
| `codex app-server` | 启动 app server，实验性/调试用 |
| `codex help` | 查看帮助 |
| `codex --version` | 查看版本 |

---

## 三、常用启动参数

这些参数通常跟在 `codex` 或 `codex exec` 后面使用。

| 参数 | 作用 |
|------|------|
| `-m, --model <MODEL>` | 指定本次会话模型，例如 `codex -m gpt-5.4` |
| `-C, --cd <DIR>` | 指定工作目录 |
| `--add-dir <DIR>` | 增加额外可写目录 |
| `-s, --sandbox read-only/workspace-write/danger-full-access` | 设置沙箱权限 |
| `-a, --ask-for-approval untrusted/on-request/never` | 设置审批策略 |
| `--search` | 开启实时网页搜索 |
| `-i, --image <FILE>` | 给初始 prompt 附加图片 |
| `-p, --profile <PROFILE>` | 使用 `config.toml` 里的配置 profile |
| `-c key=value` | 临时覆盖配置，例如 `-c model="gpt-5.4"` |
| `--oss` | 使用本地开源模型 provider |
| `--local-provider ollama/lmstudio` | 指定本地 provider |
| `--no-alt-screen` | 关闭 alternate screen，保留终端滚动历史 |
| `--dangerously-bypass-approvals-and-sandbox` | 跳过审批和沙箱，非常危险，只适合外部已隔离环境 |

### 审批策略说明

| 策略 | 说明 |
|------|------|
| `untrusted` | 只有少量可信命令会自动执行，其他命令需要你批准 |
| `on-request` | 由 Codex 判断什么时候需要向你申请批准 |
| `never` | 永不请求批准，失败会直接返回给模型处理 |

### 沙箱模式说明

| 模式 | 说明 |
|------|------|
| `read-only` | 只读沙箱，适合纯分析、代码审查 |
| `workspace-write` | 允许写当前工作区，适合日常开发 |
| `danger-full-access` | 完全访问权限，风险高，谨慎使用 |

---

## 四、常用示例

### 启动 Codex

```powershell
codex
```

### 在指定目录启动 Codex

```powershell
codex -C D:\Develop\Project\FrontProject\test
```

### 指定模型启动

```powershell
codex -m gpt-5.4 -C D:\Develop\Project\FrontProject\test
```

### 非交互执行一次任务

```powershell
codex exec "检查这个项目为什么测试失败"
```

### 审查当前未提交改动

```powershell
codex review --uncommitted
```

### 继续最近一次会话

```powershell
codex resume --last
```

### 查看 MCP 服务

```powershell
codex mcp list
```

### 查看功能开关

```powershell
codex features list
```

### 查看版本

```powershell
codex --version
```

---

## 五、Windows PowerShell 注意事项

如果 PowerShell 提示无法加载 `codex.ps1`，例如：

```text
无法加载文件 ...\codex.ps1，因为在此系统上禁止运行脚本
```

可以直接使用 `codex.cmd` 代替：

```powershell
codex.cmd --help
codex.cmd -C D:\Develop\Project\FrontProject\test
codex.cmd exec "检查这个项目"
```

这是 PowerShell 执行策略拦截 `.ps1` 脚本导致的，不一定是 Codex 安装损坏。

---

## 六、使用建议

1. 新项目先用 `/init` 生成 `AGENTS.md`，把项目结构、技术栈、常用命令、编码约定固化下来。
2. 长会话中感觉上下文太长时，用 `/compact` 压缩历史，而不是直接丢掉全部上下文。
3. 切换完全不同任务时，用 `/clear` 或 `/new` 开新上下文，减少旧任务干扰。
4. 想确认 Codex 当前能看到什么、能做什么时，用 `/status` 和 `/debug-config`。
5. 修改代码前可以先用 `/plan`，让 Codex 给出执行方案再开始改。
6. 做代码审查时优先使用 `/review` 或外层 `codex review --uncommitted`。
7. 需要脚本化或 CI 场景时，优先使用 `codex exec`。
8. Windows 下如果 `codex` 被 PowerShell 拦截，直接换成 `codex.cmd`。