# QStack

QStack 是一个以 Obsidian Markdown 组织的全栈开发学习笔记库，内容覆盖 Java 后端、Web 前端、Python / AI Agent、业务开发实践与面试复习。仓库更偏向知识库和长期沉淀，不是单一可运行项目。

## 内容概览

| 模块 | 说明 | 笔记数量 |
| --- | --- | ---: |
| `web后端` | Java、数据库、Spring 体系、工具框架、中间件、设计模式与示例代码 | 146 |
| `web前端` | HTML、CSS、JavaScript、TypeScript、Node.js、Vue、React、Electron、Uniapp 与前端工具链 | 32 |
| `python` | Python 基础环境、Miniconda / uv、FastAPI / Flask、AI Agent、RAG、LangGraph、MCP 等 | 29 |
| `业务开发` | 支付服务、缓存服务、多数据源、Starter 开发、前端工程化、AI 工具使用 | 7 |
| `面试笔记` | Java、MySQL、实习经历与面试复盘 | 42 |

## 目录结构

```text
QStack/
├── web后端/
│   ├── 01.Java/
│   │   ├── JUC/
│   │   ├── JVM/
│   │   ├── IO流/
│   │   ├── JavaAPI/
│   │   ├── JDK新特性/
│   │   ├── 反射与注解/
│   │   ├── 多线程/
│   │   └── 集合/
│   ├── 02.数据库/
│   │   ├── 01.Mysql/
│   │   ├── 02.Redis/
│   │   ├── 03.MongoDB/
│   │   ├── 04.Neo4j/
│   │   ├── 05.ElasticSearch/
│   │   └── 06.PostgreSQL/
│   ├── 03.Spring框架/
│   │   ├── 01.Spring/
│   │   ├── 02.SpringMVC/
│   │   ├── 03.SpringBoot/
│   │   ├── 04.SpringCloud/
│   │   ├── 05.SpringSecurity/
│   │   ├── 06.SpringCache/
│   │   ├── 07.SpringEmail/
│   │   ├── 08.SpringTest/
│   │   ├── 09.SpringAi/
│   │   └── 10.LangChain4j/
│   ├── 04.工具框架/
│   ├── 05.中间件/
│   ├── 06.设计模式/
│   └── 设计模式代码/
├── web前端/
│   ├── 01.HTML/
│   ├── 02.CSS/
│   ├── 03.JavaScript/
│   ├── 04.TypeScript/
│   ├── 05.NodeJS/
│   ├── 06.Vue/
│   ├── 07.React/
│   ├── 08.Electron/
│   ├── 09.Uniapp/
│   └── 10.其他框架/
├── python/
│   ├── Agent/
│   ├── python/
│   └── 中间件/
├── 业务开发/
│   ├── AI 工具使用/
│   ├── MyBatis 操作多数据库/
│   ├── Spring-Boot-Start 开发/
│   ├── 前端工程化/
│   ├── 支付服务/
│   └── 缓存服务/
└── 面试笔记/
    ├── 01.Java篇/
    ├── 02.MySQL/
    └── 实习笔记/
```

## 核心模块

### Web 后端

`web后端` 是当前笔记库中内容最多的部分，主要围绕 Java 后端开发体系展开。

- Java 基础与进阶：集合、IO、反射、注解、多线程、JUC、JVM、JDK 新特性。
- 数据库：MySQL、Redis、MongoDB、Neo4j、ElasticSearch、PostgreSQL。
- Spring 生态：Spring、SpringMVC、SpringBoot、SpringCloud、SpringSecurity、SpringCache、SpringEmail、SpringTest、Spring AI、LangChain4j。
- 工具框架：MyBatis、Netty、Swagger / Knife4j、EasyExcel、ShardingSphere-JDBC、Caffeine、Druid、FreeMarker、DataX、EagleMap。
- 中间件：Maven、Git、Linux、Docker、RabbitMQ、Kafka、Zookeeper、Dubbo、MinIO、XXL-JOB、Nginx、RocketMQ。
- 设计模式：23 种 GoF 设计模式笔记，以及独立的 `设计模式代码` Maven 示例工程。

### Web 前端

`web前端` 按前端学习路径组织，适合从基础语法到框架实践逐步学习。

- 基础三件套：HTML、CSS、JavaScript。
- 类型与运行环境：TypeScript、Node.js、nvm。
- 主流框架：Vue、React。
- 跨端与桌面：Uniapp、Electron。
- 工具链：ESLint、Vite、Element Plus、Axios。

### Python 与 AI Agent

`python` 模块包含 Python 环境管理和 AI 应用开发相关内容。

- Python 环境：pip 镜像源、Miniconda、uv。
- Agent 方向：RAG 工程、Agent 工程、LangGraph、MCP 与工具协议、工程化部署与可观测性、项目实战与面试。

### 业务开发

`业务开发` 聚焦真实开发场景中的方案整理和落地实践。

- 支付服务
- 缓存服务
- MyBatis 多数据源
- SpringBoot Starter 开发
- 前端工程化初始化
- Claude Code / Codex 等 AI 工具使用

### 面试笔记

`面试笔记` 用于沉淀面试高频知识点、实习记录和复盘材料，当前包含 Java、MySQL 与实习笔记相关内容。

## 推荐学习路线

### Java 后端路线

```text
Java 基础
  -> 集合 / IO / 反射 / 注解
  -> 多线程 / JUC / JVM
  -> MySQL / Redis
  -> Spring / SpringMVC / SpringBoot
  -> MyBatis / Maven / Git
  -> Docker / Linux / Nginx
  -> RabbitMQ / Kafka / Dubbo / SpringCloud
  -> 设计模式
  -> 业务开发实践
```

### 前端路线

```text
HTML / CSS
  -> JavaScript
  -> TypeScript
  -> Vue 或 React
  -> Pinia / Vuex / Axios / Element Plus
  -> Vite / ESLint
  -> Uniapp 或 Electron
```

### Python / AI Agent 路线

```text
Python 环境管理
  -> uv / Miniconda
  -> RAG 基础
  -> Agent 工程
  -> LangGraph
  -> MCP 与工具协议
  -> 工程化部署与可观测性
```

### 面试复习路线

```text
Java 核心基础
  -> JVM / JUC
  -> MySQL
  -> Redis
  -> SpringBoot / SpringCloud
  -> 项目业务复盘
  -> 实习经历整理
```

## 使用方式

推荐使用 Obsidian 打开本仓库：

1. 克隆或下载仓库。
2. 使用 Obsidian 打开 `QStack` 目录。
3. 按目录模块阅读，也可以通过 Obsidian 全文搜索定位具体知识点。
4. 对包含图片资源的笔记，建议保持原目录结构，避免图片链接失效。

如果只需要阅读 Markdown，也可以直接使用 VS Code、Typora 或其他 Markdown 编辑器打开。

## 适合人群

- 正在系统学习 Java 后端或全栈开发的学习者。
- 需要整理 Spring、数据库、中间件、前端框架知识体系的开发者。
- 准备 Java / 后端 / 全栈方向面试的求职者。
- 希望沉淀业务开发方案、AI 工具使用经验和工程化实践的个人开发者。

## 维护说明

- 笔记按技术方向和学习阶段分层存放。
- 图片资源通常放在对应目录下的 `img`、`assets` 等子目录中。
- `web后端/设计模式代码` 是 Maven 示例工程，可单独作为代码示例查看。
- 新增笔记时建议沿用当前编号和目录命名风格，便于长期维护和检索。

## 仓库定位

QStack 的目标不是替代官方文档，而是把日常学习、项目实践、问题排查和面试复习沉淀成一个可持续扩展的个人技术知识库。
