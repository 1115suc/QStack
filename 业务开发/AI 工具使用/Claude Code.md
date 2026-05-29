# Claude Code 命令完整说明书

> 适用于 Claude Code CLI（终端版），整理日期：2026/05/27
> 所有命令均在 Claude Code 的输入框中以 `/` 开头输入。

---

## 一、模型与运行模式

| 命令 | 作用 |
|------|------|
| `/model` | 切换当前会话使用的模型（Opus / Sonnet / Haiku 等），不同模型在能力、速度、价格上有差异 |
| `/fast` | 切换 Fast 模式。在 Opus 4.6 / 4.7 上启用后输出更快，**不会**降级到小模型 |
| `/config` | 打开配置面板，调整主题、编辑器、通知、默认模型等基础设置 |
| `/vim` | 在输入框中开启 / 关闭 Vim 键位模式（支持 normal、insert 等模式切换） |
| `/terminal-setup` | 配置终端外观与行为，例如 Shift+Enter 换行、字体渲染等 |

---

## 二、会话与上下文管理

| 命令 | 作用 |
|------|------|
| `/clear` | **清空当前会话上下文**，开启全新对话。历史消息不再被引用，常用于切换话题 |
| `/compact` | **手动压缩当前会话历史**，把早期消息变成摘要以节省 Token，长会话非常有用 |
| `/resume` | 列出并恢复之前的会话（按时间排序） |
| `/continue` | 继续上一次会话（最近一次） |
| `/exit` / `/quit` | 退出 Claude Code（也可按两次 `Ctrl+C`） |

> **`/clear` 与 `/compact` 的区别**：`/clear` 是彻底丢弃历史，`/compact` 是保留要点的压缩。前者干净，后者保留延续性。

---

## 三、账号、状态与计费

| 命令 | 作用 |
|------|------|
| `/login` | 登录 Anthropic 账号（首次使用或切换账号时） |
| `/logout` | 退出当前账号 |
| `/status` | 查看当前账号、模型、工作目录、Token 用量等综合状态 |
| `/cost` | 查看本次会话累计的 Token 消耗与估算费用 |
| `/upgrade` | 升级到付费方案（Pro / Max / Team） |

---

## 四、记忆与项目初始化

| 命令 | 作用 |
|------|------|
| `/memory` | 查看与编辑 Claude 的记忆文件。包含两层：用户级（`~/.claude/`）与项目级（`./.claude/` 或 `CLAUDE.md`） |
| `/init` | 在当前项目根目录生成一份 `CLAUDE.md`，记录项目结构、技术栈、编码约定等，让 Claude 后续更"懂"这个仓库 |
| `#<文本>` | **快速记忆前缀**：在输入框输入 `#` 开头的内容会被作为记忆条目保存，Claude 会询问保存到用户级还是项目级 |

> Claude 会在每次会话开始时自动加载 `CLAUDE.md` 与记忆文件，因此把常用的约定（如"测试用 pytest"、"前端走 pnpm"）写进去能避免反复说明。

---

## 五、权限、Hook、MCP 与子代理

### 1. `/permissions`
管理工具调用权限，控制哪些 Bash 命令、文件操作、MCP 工具可以**自动执行**、需要**询问**、或**拒绝**。
- 例如把 `npm install`、`git status` 加入允许列表，可减少弹窗确认
- 权限分两层：项目级（`.claude/settings.json`）与用户级（`~/.claude/settings.json`）

### 2. `/hooks`
配置 Hook（钩子）—— 在特定事件触发时自动执行的脚本。常用事件：
- `PreToolUse` / `PostToolUse`：工具调用前后
- `UserPromptSubmit`：用户提交输入时
- `Stop`：Claude 停止响应时
- `SessionStart` / `SessionEnd`：会话开始/结束时

> Hook 写入 `settings.json`，由**宿主程序**执行（不是 Claude）。所以"以后每次保存自动 lint"这类自动化必须通过 Hook 实现，而不是写进记忆。

### 3. `/mcp`
管理 MCP（Model Context Protocol）服务器 —— 给 Claude 接入外部能力，例如读 Notion、操作 Figma、查询数据库。
- 查看已连接的 MCP 服务器及其工具
- 启用 / 禁用 / 重新认证
- 在你的环境中已接入 `pencil` MCP（用于 `.pen` 设计文件）

### 4. `/agents`
管理子代理（Subagent）。子代理是带专属系统提示与工具集的"分身"，适合并行任务、保护主上下文。
- 查看可用代理（如 Explore、Plan、general-purpose）
- 创建 / 编辑自定义代理
- 子代理定义存放在 `.claude/agents/` 下的 markdown 文件中

---

## 六、工作目录与 IDE 集成

| 命令 | 作用 |
|------|------|
| `/add-dir` | 把额外目录加入 Claude 的可访问范围（默认只能访问启动时的工作目录） |
| `/ide` | 连接 / 断开 IDE（VS Code、JetBrains 等），开启共享选区、诊断信息、文件状态 |
| `/install-github-app` | 安装 Claude Code 的 GitHub App，启用 PR 评论、Issue 处理等功能 |

---

## 七、代码工程类技能命令

> 以下属于 **Skill（技能）**，需先通过 plugins 或自定义安装。你的环境中已经具备。

| 命令 | 作用 |
|------|------|
| `/review` | 审查当前 PR 或本地代码变更，给出修改意见 |
| `/security-review` | 对当前分支的待提交变更做安全审查（OWASP Top10、注入、敏感信息泄露等） |
| `/verify` | 启动应用、操作功能，**人为验证**改动是否真的生效（区别于单元测试） |
| `/run` | 启动并运行当前项目（按项目类型自动判断：CLI、Server、Electron、网页等） |
| `/simplify` | 审查最近的代码改动，找出可复用、可精简的部分并修正 |
| `/claude-api` | 辅助开发或迁移基于 Anthropic SDK 的代码（含 Prompt Caching、Tool Use 等最佳实践） |

---

## 八、自动化与辅助技能

| 命令 | 作用 |
|------|------|
| `/loop <间隔> <命令>` | 周期性地执行一个命令。例如 `/loop 5m /review` 每 5 分钟跑一次代码审查；常用于轮询 CI、监控部署等 |
| `/update-config` | 通过自然语言修改 `settings.json`（权限、Hook、环境变量），适合不熟悉 JSON 语法的用户 |
| `/keybindings-help` | 自定义快捷键，编辑 `~/.claude/keybindings.json` |
| `/fewer-permission-prompts` | 扫描历史会话，自动把常用只读命令加入项目白名单，**减少权限弹窗** |
| `ui-ux-pro-max` | UI/UX 设计智能助手，覆盖网页与移动端，含 50+ 风格、161 配色、57 字体配对等 |
| `superpowers-lab` | 实验沙盒，用来试验"超能力"特性 |

---

## 九、GitHub 集成

| 命令 | 作用 |
|------|------|
| `/pr-comments` | 查看当前分支对应 PR 的评论 |
| `/install-github-app` | 安装 GitHub App，让 Claude 能在 PR 评论中被 `@claude` 调用 |
| `/release-notes` | 查看 Claude Code 自身的版本更新日志 |

> PR 创建、Issue 评论等更细的操作一般通过 `gh` CLI 来执行，例如 `gh pr create`、`gh pr view`。

---

## 十、诊断与维护

| 命令 | 作用 |
|------|------|
| `/doctor` | **健康检查**：检测 Claude Code 安装与环境是否正常（Node 版本、网络、配置文件、MCP 等） |
| `/bug` | 上报 bug，会自动收集环境信息并打开反馈通道 |
| `/help` | 查看所有可用命令的简短帮助列表 |
| `/upgrade` | 升级 Claude Code 到最新版本 / 升级订阅 |
| `/release-notes` | 查看版本更新内容 |

---

## 十一、特殊输入前缀

这些不是斜杠命令，但属于"特殊输入"，常配合命令使用：

| 前缀 | 作用 | 示例 |
|------|------|------|
| `!` | 直接执行一条 **shell 命令**，输出会进入会话上下文，Claude 能看到 | `! git status` |
| `#` | 把这段文字保存为**记忆** | `# 项目用 pnpm，不要用 npm` |
| `@` | **引用文件 / 目录**，Claude 会读取其内容 | `@src/api/user.ts 帮我看下这个文件` |
| `>` | 把整段输入作为"系统指令"传给 Claude（部分版本支持） | `> 以简短中文回答` |

> `!` 前缀对于交互式命令（如 `gcloud auth login`、`ssh`）尤其有用 —— 你自己交互，输出 Claude 也看得到。

---

## 十二、常用快捷键

| 快捷键 | 作用 |
|--------|------|
| `Esc` | 中断 Claude 当前输出 |
| `Esc` 两次 | 跳回上一条用户消息（编辑后重发） |
| `Ctrl+C` | 取消当前输入；连续两次退出 |
| `Ctrl+D` | 退出 Claude Code |
| `Ctrl+L` | 清空屏幕（不清空上下文） |
| `Shift+Tab` | 切换**计划模式 / 自动接受模式 / 普通模式** |
| `Up` / `Down` | 浏览历史输入 |
| `Shift+Enter` | 换行（需先用 `/terminal-setup` 配置） |
| `Tab` | 自动补全命令 / 文件路径 |

---

## 十三、使用小贴士

1. **新项目第一步用 `/init`**：让 Claude 自动生成 `CLAUDE.md`，省去反复介绍项目结构。
2. **长任务用 `/compact`**：当感觉 Claude 开始"忘事"时，先压缩历史再继续。
3. **切换话题用 `/clear`**：避免上文干扰，回答质量更高。
4. **常用命令加白名单**：用 `/fewer-permission-prompts` 或 `/permissions`，告别频繁弹窗。
5. **自动化用 Hook，而不是记忆**：让"每次保存自动跑 lint"这类规则真正生效。
6. **多目录用 `/add-dir`**：跨仓库工作时把其它目录加进来。
7. **看不懂报错用 `/doctor`**：环境问题先自检再排查。
8. **想固化偏好用 `#`**：例如 `# 回答用中文`、`# 不要写注释`。

---

## 附：命令速查表（按字母排序）

```
/add-dir                  添加可访问目录
/agents                   管理子代理
/bug                      反馈 bug
/claude-api               Anthropic SDK 开发助手
/clear                    清空上下文
/compact                  压缩上下文
/config                   配置面板
/continue                 继续上一次会话
/cost                     查看 Token 消耗
/doctor                   环境健康检查
/exit                     退出
/fast                     切换 Fast 模式
/fewer-permission-prompts 减少权限弹窗
/help                     帮助
/hooks                    管理 Hook
/ide                      IDE 集成
/init                     生成 CLAUDE.md
/install-github-app       安装 GitHub App
/keybindings-help         自定义快捷键
/login                    登录
/logout                   退出登录
/loop                     周期任务
/mcp                      管理 MCP
/memory                   编辑记忆
/model                    切换模型
/permissions              管理权限
/pr-comments              查看 PR 评论
/quit                     退出
/release-notes            版本更新日志
/resume                   恢复历史会话
/review                   代码审查
/run                      启动项目
/security-review          安全审查
/simplify                 简化代码
/status                   查看状态
/terminal-setup           终端配置
/update-config            修改 settings.json
/upgrade                  升级
/verify                   验证改动
/vim                      Vim 模式
```
