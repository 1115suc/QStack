# Electron + Vue3

> Electron 是一个使用 Web 技术（HTML/CSS/JS）构建跨平台桌面应用的框架，底层由 **Chromium**（渲染）和 **Node.js**（系统能力）驱动。

---

## 一、核心概念

### 进程模型

```
┌─────────────────────────────────────────┐
│              Electron App               │
│                                         │
│  ┌─────────────────┐                    │
│  │   主进程 Main   │  Node.js 完整能力  │
│  │  (main.ts)      │  访问文件系统/OS   │
│  └────────┬────────┘                    │
│           │ IPC 通信                    │
│  ┌────────┴────────┐                    │
│  │ 渲染进程 Renderer│  Chromium 渲染     │
│  │  (Vue3 SPA)     │  受沙箱限制        │
│  └─────────────────┘                    │
│                                         │
│  ┌─────────────────┐                    │
│  │  预加载脚本      │  安全桥接层        │
│  │  preload.ts     │  暴露受控 API      │
│  └─────────────────┘                    │
└─────────────────────────────────────────┘
```

| 进程 | 职责 | 可用 API |
|------|------|----------|
| 主进程 | 创建窗口、系统交互、应用生命周期 | 完整 Node.js + Electron 主进程模块 |
| 渲染进程 | UI 渲染（Vue3 应用） | Web API（受沙箱限制） |
| 预加载脚本 | 安全地将主进程能力暴露给渲染进程 | 部分 Node.js + contextBridge |

### 版本要求

| 工具 | 推荐版本 |
|------|---------|
| Node.js | ≥ 18.x |
| Electron | ≥ 28.x |
| Vue | 3.x |
| Vite | ≥ 5.x |

---

## 二、项目结构

### 推荐目录结构

```
my-electron-app/
├── electron/                 # 主进程代码
│   ├── main.ts               # 主进程入口
│   ├── preload.ts            # 预加载脚本
│   └── modules/              # 功能模块（ipc / updater 等）
│       ├── ipc.ts
│       └── updater.ts
├── src/                      # Vue3 渲染进程代码
│   ├── main.ts
│   ├── App.vue
│   ├── router/
│   ├── stores/
│   └── views/
├── public/
├── dist/                     # Vite 构建输出（渲染进程）
├── dist-electron/            # tsc 构建输出（主进程）
├── release/                  # 打包产物
├── electron-builder.yml      # 打包配置
├── vite.config.ts
└── package.json
```

### package.json 关键配置

```json
{
  "name": "my-electron-app",
  "version": "1.0.0",
  "main": "dist-electron/main.js",
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build && tsc -p tsconfig.electron.json",
    "electron:dev": "electron .",
    "electron:build": "electron-builder"
  },
  "devDependencies": {
    "electron": "^28.0.0",
    "electron-builder": "^24.0.0",
    "vite-plugin-electron": "^0.28.0"
  }
}
```

---

## 三、主进程（Main Process）

### 基础窗口创建

```ts
// electron/main.ts
import { app, BrowserWindow } from 'electron'
import path from 'path'

const isDev = process.env.NODE_ENV === 'development'

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    minWidth: 800,
    minHeight: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,    // 隔离渲染进程与 Node.js 环境（安全必选）
      nodeIntegration: false,    // 禁用渲染进程直接访问 Node.js（安全必选）
      sandbox: true              // 开启沙箱（推荐）
    }
  })

  if (isDev) {
    win.loadURL('http://localhost:5173')
    win.webContents.openDevTools()
  } else {
    win.loadFile(path.join(__dirname, '../dist/index.html'))
  }
}

app.whenReady().then(() => {
  createWindow()

  // macOS：点击 Dock 图标时重新创建窗口
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

// 非 macOS：所有窗口关闭时退出
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit()
})
```

### BrowserWindow 常用配置

```ts
const win = new BrowserWindow({
  width: 1200,
  height: 800,
  x: 100,                        // 窗口初始 x 坐标
  y: 100,                        // 窗口初始 y 坐标
  resizable: true,               // 是否可调整大小
  movable: true,                 // 是否可移动
  minimizable: true,             // 是否可最小化
  maximizable: true,             // 是否可最大化
  fullscreen: false,             // 是否全屏
  frame: true,                   // 是否显示原生窗口边框（false 为无边框窗口）
  transparent: false,            // 窗口是否透明
  titleBarStyle: 'hiddenInset',  // macOS 标题栏样式
  icon: path.join(__dirname, '../public/icon.png'),
  show: false,                   // 先隐藏，ready-to-show 后再显示（避免白屏）
  backgroundColor: '#fff',
  webPreferences: { ... }
})

// 窗口准备好后再显示，避免白屏闪烁
win.once('ready-to-show', () => win.show())
```

### 应用生命周期

```ts
app.on('before-quit', () => { /* 退出前清理 */ })
app.on('will-quit', () => { /* 即将退出 */ })
app.on('quit', (event, exitCode) => { /* 已退出 */ })

// 防止多实例运行
const gotTheLock = app.requestSingleInstanceLock()
if (!gotTheLock) {
  app.quit()
} else {
  app.on('second-instance', () => {
    // 有人试图打开第二个实例时，聚焦第一个实例的窗口
    if (mainWindow) {
      if (mainWindow.isMinimized()) mainWindow.restore()
      mainWindow.focus()
    }
  })
}
```

---

## 四、渲染进程（Renderer Process）

### 预加载脚本（关键）

```ts
// electron/preload.ts
import { contextBridge, ipcRenderer } from 'electron'

// 通过 contextBridge 安全地向渲染进程暴露 API
contextBridge.exposeInMainWorld('electronAPI', {
  // 发送消息给主进程
  send: (channel: string, data?: any) => {
    ipcRenderer.send(channel, data)
  },
  // 发送消息并等待主进程返回
  invoke: (channel: string, data?: any) => {
    return ipcRenderer.invoke(channel, data)
  },
  // 监听主进程发来的消息
  on: (channel: string, callback: (...args: any[]) => void) => {
    ipcRenderer.on(channel, (_event, ...args) => callback(...args))
  },
  // 移除监听
  off: (channel: string, callback: (...args: any[]) => void) => {
    ipcRenderer.removeListener(channel, callback)
  }
})
```

### TypeScript 类型声明

```ts
// src/types/electron.d.ts
export interface ElectronAPI {
  send: (channel: string, data?: any) => void
  invoke: <T = any>(channel: string, data?: any) => Promise<T>
  on: (channel: string, callback: (...args: any[]) => void) => void
  off: (channel: string, callback: (...args: any[]) => void) => void
}

declare global {
  interface Window {
    electronAPI: ElectronAPI
  }
}
```

### 在 Vue3 中使用

```ts
// 检测是否在 Electron 环境中运行
export const isElectron = () => !!window.electronAPI

// 组合式函数封装
export function useElectron() {
  const { electronAPI } = window

  const openFile = async () => {
    return electronAPI.invoke<string[]>('dialog:openFile')
  }

  const minimizeWindow = () => electronAPI.send('window:minimize')
  const maximizeWindow = () => electronAPI.send('window:maximize')
  const closeWindow = () => electronAPI.send('window:close')

  return { openFile, minimizeWindow, maximizeWindow, closeWindow }
}
```

---

## 五、进程通信 IPC

### 渲染进程 → 主进程（单向）

```ts
// 渲染进程发送
window.electronAPI.send('app:minimize')

// 主进程接收（ipcMain.on）
import { ipcMain } from 'electron'
ipcMain.on('app:minimize', () => {
  mainWindow.minimize()
})
```

### 渲染进程 → 主进程（双向，推荐）

```ts
// 渲染进程发送并等待返回
const result = await window.electronAPI.invoke('fs:readFile', '/path/to/file')

// 主进程处理并返回（ipcMain.handle）
import { ipcMain } from 'electron'
import fs from 'fs/promises'

ipcMain.handle('fs:readFile', async (_event, filePath: string) => {
  try {
    const content = await fs.readFile(filePath, 'utf-8')
    return { success: true, data: content }
  } catch (error) {
    return { success: false, error: (error as Error).message }
  }
})
```

### 主进程 → 渲染进程（推送）

```ts
// 主进程推送消息
mainWindow.webContents.send('download:progress', { percent: 50 })

// 渲染进程监听（在组件中）
import { onMounted, onUnmounted } from 'vue'

const handleProgress = (data: { percent: number }) => {
  progress.value = data.percent
}

onMounted(() => {
  window.electronAPI.on('download:progress', handleProgress)
})

onUnmounted(() => {
  window.electronAPI.off('download:progress', handleProgress)
})
```

### IPC 频道命名规范

```ts
// 推荐：使用 namespace:action 格式
'window:minimize'
'window:maximize'
'window:close'
'dialog:openFile'
'dialog:saveFile'
'fs:readFile'
'fs:writeFile'
'app:getVersion'
'download:start'
'download:progress'
```

---

## 六、原生 API

### 系统对话框

```ts
// 主进程
import { dialog } from 'electron'

// 打开文件选择框
ipcMain.handle('dialog:openFile', async () => {
  const { canceled, filePaths } = await dialog.showOpenDialog({
    title: '选择文件',
    properties: ['openFile', 'multiSelections'],
    filters: [
      { name: 'Images', extensions: ['jpg', 'png', 'gif'] },
      { name: 'All Files', extensions: ['*'] }
    ]
  })
  return canceled ? [] : filePaths
})

// 保存对话框
ipcMain.handle('dialog:saveFile', async (_event, content: string) => {
  const { canceled, filePath } = await dialog.showSaveDialog({
    title: '保存文件',
    defaultPath: 'output.txt',
    filters: [{ name: 'Text', extensions: ['txt'] }]
  })
  if (!canceled && filePath) {
    await fs.writeFile(filePath, content, 'utf-8')
    return { success: true }
  }
  return { success: false }
})
```

### 系统托盘

```ts
// electron/modules/tray.ts
import { Tray, Menu, nativeImage } from 'electron'
import path from 'path'

let tray: Tray | null = null

export function createTray(mainWindow: BrowserWindow) {
  const icon = nativeImage.createFromPath(
    path.join(__dirname, '../../public/tray-icon.png')
  )
  tray = new Tray(icon)

  const menu = Menu.buildFromTemplate([
    { label: '显示主窗口', click: () => mainWindow.show() },
    { type: 'separator' },
    { label: '退出', role: 'quit' }
  ])

  tray.setContextMenu(menu)
  tray.setToolTip('My App')
  tray.on('click', () => mainWindow.show())
}
```

### 系统通知

```ts
// 主进程
import { Notification } from 'electron'

ipcMain.on('notification:show', (_event, { title, body }) => {
  new Notification({ title, body }).show()
})

// 渲染进程调用
window.electronAPI.send('notification:show', {
  title: '下载完成',
  body: '文件已保存到桌面'
})
```

### 应用菜单

```ts
// electron/modules/menu.ts
import { Menu, MenuItem } from 'electron'

export function createAppMenu() {
  const template: Electron.MenuItemConstructorOptions[] = [
    {
      label: '文件',
      submenu: [
        { label: '新建', accelerator: 'CmdOrCtrl+N', click: () => {} },
        { label: '打开', accelerator: 'CmdOrCtrl+O', click: () => {} },
        { type: 'separator' },
        { role: 'quit', label: '退出' }
      ]
    },
    {
      label: '编辑',
      submenu: [
        { role: 'undo', label: '撤销' },
        { role: 'redo', label: '重做' },
        { type: 'separator' },
        { role: 'copy', label: '复制' },
        { role: 'paste', label: '粘贴' }
      ]
    },
    {
      label: '帮助',
      submenu: [
        { label: '关于', click: () => { /* 打开关于窗口 */ } }
      ]
    }
  ]

  Menu.setApplicationMenu(Menu.buildFromTemplate(template))
}
```

### 自动更新（electron-updater）

```ts
// electron/modules/updater.ts
import { autoUpdater } from 'electron-updater'
import { BrowserWindow } from 'electron'

export function setupAutoUpdater(win: BrowserWindow) {
  autoUpdater.autoDownload = false   // 手动触发下载

  autoUpdater.on('update-available', (info) => {
    win.webContents.send('updater:available', info)
  })

  autoUpdater.on('download-progress', (progress) => {
    win.webContents.send('updater:progress', progress)
  })

  autoUpdater.on('update-downloaded', () => {
    win.webContents.send('updater:downloaded')
  })

  ipcMain.on('updater:download', () => autoUpdater.downloadUpdate())
  ipcMain.on('updater:install', () => autoUpdater.quitAndInstall())

  // 启动后延迟检查更新
  setTimeout(() => autoUpdater.checkForUpdates(), 3000)
}
```

---

## 七、打包发布

### electron-builder 配置

```yaml
# electron-builder.yml
appId: com.example.myapp
productName: MyApp
copyright: Copyright © 2024

directories:
  output: release
  buildResources: build

files:
  - dist/**/*
  - dist-electron/**/*

win:
  target:
    - target: nsis      # Windows 安装包
      arch: [x64]
  icon: build/icon.ico

mac:
  target:
    - target: dmg
      arch: [x64, arm64]  # Intel + Apple Silicon
  icon: build/icon.icns
  category: public.app-category.productivity

linux:
  target:
    - target: AppImage
    - target: deb
  icon: build/icon.png
  category: Utility

nsis:
  oneClick: false
  allowToChangeInstallationDirectory: true
  installerIcon: build/icon.ico
  createDesktopShortcut: true
  createStartMenuShortcut: true

publish:
  provider: github
  owner: your-name
  repo: your-repo
```

### 构建脚本

```json
{
  "scripts": {
    "build:win": "electron-builder --win",
    "build:mac": "electron-builder --mac",
    "build:linux": "electron-builder --linux",
    "build:all": "electron-builder --win --mac --linux",
    "release": "electron-builder --publish=always"
  }
}
```

### 图标准备

| 平台 | 格式 | 尺寸 |
|------|------|------|
| Windows | .ico | 256×256（多尺寸合并） |
| macOS | .icns | 512×512（多尺寸合并） |
| Linux | .png | 512×512 |

> ✅ 推荐工具：使用 `electron-icon-builder` 从一张 1024×1024 的 PNG 自动生成所有平台图标。

---

## 八、安全实践

### 必须遵守的规则

```ts
// ✅ 正确：始终开启上下文隔离
webPreferences: {
  contextIsolation: true,
  nodeIntegration: false,
  sandbox: true
}

// ❌ 错误：永远不要这样做
webPreferences: {
  nodeIntegration: true,    // 让渲染进程直接访问 Node.js，高危！
  contextIsolation: false   // 禁用隔离，高危！
}
```

### 预加载脚本白名单

```ts
// electron/preload.ts
// ✅ 只暴露必要的受限 API，不要直接暴露 ipcRenderer 对象本身
contextBridge.exposeInMainWorld('electronAPI', {
  openFile: () => ipcRenderer.invoke('dialog:openFile'),
  saveFile: (content: string) => ipcRenderer.invoke('dialog:saveFile', content)
  // 不要暴露：ipcRenderer.invoke 本身（允许任意频道调用）
})

// ❌ 危险写法：直接暴露 ipcRenderer
contextBridge.exposeInMainWorld('ipcRenderer', ipcRenderer)
```

### 主进程 IPC 验证

```ts
// 验证调用来源，防止恶意网页发起请求
ipcMain.handle('fs:readFile', async (event, filePath: string) => {
  // 验证发送者来源
  const sender = event.senderFrame
  if (!sender || sender.url.startsWith('http://') && !isDev) {
    throw new Error('Unauthorized')
  }

  // 验证路径安全性，防止路径遍历攻击
  const safePath = path.resolve(filePath)
  const allowedDir = app.getPath('documents')
  if (!safePath.startsWith(allowedDir)) {
    throw new Error('Access denied: path outside allowed directory')
  }

  return fs.readFile(safePath, 'utf-8')
})
```

### 内容安全策略（CSP）

```ts
// 为所有渲染进程设置 CSP
import { session } from 'electron'

app.whenReady().then(() => {
  session.defaultSession.webRequest.onHeadersReceived((details, callback) => {
    callback({
      responseHeaders: {
        ...details.responseHeaders,
        'Content-Security-Policy': [
          "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'"
        ]
      }
    })
  })
})
```

---

## 九、常见问题

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| 窗口白屏闪烁 | 渲染进程未加载完就显示 | 使用 `show: false` + `ready-to-show` 事件 |
| 打包后找不到资源 | 路径使用了相对路径 | 使用 `path.join(__dirname, ...)` 或 `app.getAppPath()` |
| 无法访问 Node.js | 沙箱 + contextIsolation | 通过预加载脚本 + contextBridge 暴露受控 API |
| 打包体积过大 | 未排除开发依赖 | 将工具库放在 `devDependencies`，检查 `files` 配置 |
| macOS 提示「已损坏」 | 未经公证（notarize） | 配置 Apple 公证，或临时用 `xattr -cr` 解除隔离 |
| 多显示器 HiDPI 模糊 | 分辨率缩放问题 | 设置 `app.commandLine.appendSwitch('high-dpi-support', '1')` |
| 渲染进程内存泄漏 | 未清理事件监听 | `onUnmounted` 时调用 `electronAPI.off()` 清理监听 |
