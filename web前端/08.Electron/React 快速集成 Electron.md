# React + Electron 快速集成笔记

> 使用 **Vite + React + TypeScript + Electron** 搭建桌面应用，从零到可运行只需几步。

---

## 一、脚手架方式（推荐）

### 使用 electron-vite 官方脚手架

```bash
# 创建项目（选择 react-ts 模板）
npm create @quick-start/electron my-app -- --template react-ts
cd my-app
npm install
npm run dev        # 启动开发
npm run build      # 构建
npm run build:win  # 打包 Windows
```

### 使用 electron-react-boilerplate

```bash
git clone --depth 1 --branch main \
  https://github.com/electron-react-boilerplate/electron-react-boilerplate.git my-app
cd my-app
npm install
npm start
```

> ✅ 推荐 `electron-vite` 脚手架，开箱即用，内置热重载、路径别名、打包配置，适合新项目。

---

## 二、手动集成

### 第一步：创建 React + Vite 项目

```bash
npm create vite@latest my-electron-react -- --template react-ts
cd my-electron-react
npm install
```

### 第二步：安装 Electron 相关依赖

```bash
# 核心依赖
npm install electron --save-dev
npm install electron-builder --save-dev

# Vite 插件（支持主进程热重载）
npm install vite-plugin-electron vite-plugin-electron-renderer --save-dev
```

### 第三步：配置 vite.config.ts

```ts
// vite.config.ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import electron from 'vite-plugin-electron'
import renderer from 'vite-plugin-electron-renderer'
import path from 'path'

export default defineConfig({
  plugins: [
    react(),
    electron([
      {
        // 主进程入口
        entry: 'electron/main.ts',
        onstart(options) {
          options.startup()
        },
        vite: {
          build: {
            outDir: 'dist-electron',
            sourcemap: true,
            minify: false,
            rollupOptions: {
              external: Object.keys(
                process.env.npm_package_dependencies || {}
              )
            }
          }
        }
      },
      {
        // 预加载脚本
        entry: 'electron/preload.ts',
        onstart(options) {
          options.reload()  // 预加载变更时刷新渲染进程
        },
        vite: {
          build: {
            outDir: 'dist-electron',
            sourcemap: 'inline',
            minify: false
          }
        }
      }
    ]),
    renderer()  // 让渲染进程可以使用 Node.js 模块（配合 contextBridge）
  ],
  resolve: {
    alias: { '@': path.resolve(__dirname, 'src') }
  }
})
```

### 第四步：更新 package.json

```json
{
  "name": "my-electron-react",
  "version": "1.0.0",
  "main": "dist-electron/main.js",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "preview": "vite preview",
    "electron:build": "npm run build && electron-builder"
  }
}
```

---

## 三、项目结构

```
my-electron-react/
├── electron/
│   ├── main.ts          # 主进程入口
│   └── preload.ts       # 预加载脚本
├── src/
│   ├── main.tsx         # React 入口
│   ├── App.tsx
│   ├── hooks/
│   │   └── useElectron.ts    # 封装 Electron API 的 Hook
│   ├── components/
│   └── types/
│       └── electron.d.ts     # Window 类型扩展
├── public/
├── index.html
├── vite.config.ts
├── tsconfig.json
└── package.json
```

---

## 四、主进程配置

```ts
// electron/main.ts
import { app, BrowserWindow, ipcMain, shell } from 'electron'
import path from 'path'
import { fileURLToPath } from 'url'

const __dirname = path.dirname(fileURLToPath(import.meta.url))
const isDev = process.env.NODE_ENV === 'development'

let mainWindow: BrowserWindow | null = null

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 720,
    show: false,
    autoHideMenuBar: true,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false
    }
  })

  // 开发环境加载 Vite Dev Server，生产加载构建产物
  if (isDev && process.env['ELECTRON_RENDERER_URL']) {
    mainWindow.loadURL(process.env['ELECTRON_RENDERER_URL'])
  } else {
    mainWindow.loadFile(path.join(__dirname, '../dist/index.html'))
  }

  mainWindow.once('ready-to-show', () => mainWindow?.show())

  // 在外部浏览器中打开链接
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    if (url.startsWith('https:')) shell.openExternal(url)
    return { action: 'deny' }
  })
}

app.whenReady().then(createWindow)

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
    mainWindow = null
  }
})

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})
```

---

## 五、预加载脚本

```ts
// electron/preload.ts
import { contextBridge, ipcRenderer } from 'electron'

const electronAPI = {
  // 窗口控制
  minimize: () => ipcRenderer.send('window:minimize'),
  maximize: () => ipcRenderer.send('window:maximize'),
  close: () => ipcRenderer.send('window:close'),
  isMaximized: () => ipcRenderer.invoke('window:isMaximized'),

  // 文件操作
  openFile: () => ipcRenderer.invoke('dialog:openFile'),
  saveFile: (content: string) => ipcRenderer.invoke('dialog:saveFile', content),
  readFile: (filePath: string) => ipcRenderer.invoke('fs:readFile', filePath),

  // 应用信息
  getVersion: () => ipcRenderer.invoke('app:getVersion'),

  // 事件监听（主进程推送）
  onUpdate: (callback: (data: any) => void) => {
    ipcRenderer.on('updater:available', (_event, data) => callback(data))
  },
  offUpdate: () => ipcRenderer.removeAllListeners('updater:available')
}

contextBridge.exposeInMainWorld('electronAPI', electronAPI)
```

```ts
// src/types/electron.d.ts
export type ElectronAPI = {
  minimize: () => void
  maximize: () => void
  close: () => void
  isMaximized: () => Promise<boolean>
  openFile: () => Promise<string[]>
  saveFile: (content: string) => Promise<{ success: boolean }>
  readFile: (filePath: string) => Promise<string>
  getVersion: () => Promise<string>
  onUpdate: (callback: (data: any) => void) => void
  offUpdate: () => void
}

declare global {
  interface Window {
    electronAPI: ElectronAPI
  }
}
```

---

## 六、React 中使用 Electron API

### 自定义 Hook：useElectron

```ts
// src/hooks/useElectron.ts
import { useState, useCallback, useEffect } from 'react'

// 判断是否在 Electron 环境中
export const isElectron = (): boolean => {
  return typeof window !== 'undefined' && !!window.electronAPI
}

// 窗口控制 Hook
export function useWindowControl() {
  const [isMaximized, setIsMaximized] = useState(false)

  useEffect(() => {
    if (!isElectron()) return
    window.electronAPI.isMaximized().then(setIsMaximized)
  }, [])

  const minimize = useCallback(() => {
    window.electronAPI?.minimize()
  }, [])

  const maximize = useCallback(() => {
    window.electronAPI?.maximize()
    setIsMaximized(prev => !prev)
  }, [])

  const close = useCallback(() => {
    window.electronAPI?.close()
  }, [])

  return { isMaximized, minimize, maximize, close }
}

// 文件操作 Hook
export function useFileDialog() {
  const [loading, setLoading] = useState(false)
  const [filePaths, setFilePaths] = useState<string[]>([])

  const openFile = useCallback(async () => {
    if (!isElectron()) return
    setLoading(true)
    try {
      const paths = await window.electronAPI.openFile()
      setFilePaths(paths)
      return paths
    } finally {
      setLoading(false)
    }
  }, [])

  return { filePaths, loading, openFile }
}
```

### 自定义标题栏组件

```tsx
// src/components/TitleBar.tsx
import { useWindowControl } from '@/hooks/useElectron'

export default function TitleBar() {
  const { isMaximized, minimize, maximize, close } = useWindowControl()

  return (
    <div
      className="titlebar"
      style={{
        height: 32,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        WebkitAppRegion: 'drag',  // 拖拽区域
        padding: '0 8px',
        background: '#1a1a2e'
      }}
    >
      <span style={{ fontSize: 13, color: '#fff' }}>My App</span>
      <div style={{ display: 'flex', WebkitAppRegion: 'no-drag' }}>
        <button onClick={minimize}>－</button>
        <button onClick={maximize}>{isMaximized ? '❐' : '□'}</button>
        <button onClick={close}>✕</button>
      </div>
    </div>
  )
}
```

### 在组件中监听主进程事件

```tsx
// src/components/Updater.tsx
import { useState, useEffect } from 'react'
import { isElectron } from '@/hooks/useElectron'

export default function Updater() {
  const [updateInfo, setUpdateInfo] = useState<any>(null)

  useEffect(() => {
    if (!isElectron()) return

    window.electronAPI.onUpdate((data) => {
      setUpdateInfo(data)
    })

    return () => {
      window.electronAPI.offUpdate()
    }
  }, [])

  if (!updateInfo) return null

  return (
    <div className="update-banner">
      发现新版本 {updateInfo.version}，
      <button onClick={() => window.electronAPI.send('updater:download')}>
        立即更新
      </button>
    </div>
  )
}
```

---

## 七、IPC 通信实践

### 主进程注册处理函数

```ts
// electron/main.ts（或单独抽离 ipc.ts）
import { ipcMain, BrowserWindow, dialog, app } from 'electron'
import fs from 'fs/promises'

export function registerIpcHandlers(win: BrowserWindow) {
  // 窗口控制
  ipcMain.on('window:minimize', () => win.minimize())
  ipcMain.on('window:maximize', () =>
    win.isMaximized() ? win.unmaximize() : win.maximize()
  )
  ipcMain.on('window:close', () => win.close())
  ipcMain.handle('window:isMaximized', () => win.isMaximized())

  // 文件对话框
  ipcMain.handle('dialog:openFile', async () => {
    const { canceled, filePaths } = await dialog.showOpenDialog(win, {
      properties: ['openFile', 'multiSelections']
    })
    return canceled ? [] : filePaths
  })

  ipcMain.handle('dialog:saveFile', async (_e, content: string) => {
    const { canceled, filePath } = await dialog.showSaveDialog(win, {
      defaultPath: 'output.txt'
    })
    if (!canceled && filePath) {
      await fs.writeFile(filePath, content, 'utf-8')
      return { success: true, filePath }
    }
    return { success: false }
  })

  // 应用版本
  ipcMain.handle('app:getVersion', () => app.getVersion())
}
```

---

## 八、打包配置

### electron-builder.yml

```yaml
appId: com.example.react-electron-app
productName: MyReactApp
copyright: Copyright © 2024

directories:
  output: release/${version}
  buildResources: build

files:
  - dist/**/*
  - dist-electron/**/*

win:
  target:
    - target: nsis
      arch: [x64, ia32]
  icon: build/icon.ico

mac:
  target:
    - dmg
    - zip
  icon: build/icon.icns
  hardenedRuntime: true
  entitlements: build/entitlements.mac.plist
  entitlementsInherit: build/entitlements.mac.plist

linux:
  target:
    - AppImage
    - snap
  icon: build/icons/

nsis:
  oneClick: false
  perMachine: false
  allowElevation: true
  allowToChangeInstallationDirectory: true
  createDesktopShortcut: true
```

### 打包命令

```bash
# 只构建当前平台
npm run electron:build

# 指定平台构建
npx electron-builder --win --x64
npx electron-builder --mac --universal    # 通用二进制（Intel + Apple Silicon）
npx electron-builder --linux --x64
```

---

## 九、常用命令速查

```bash
# 开发模式启动（主进程 + 渲染进程热重载）
npm run dev

# 仅构建渲染进程
npm run build

# 打包成可分发安装包
npm run electron:build

# 调试主进程（Chrome DevTools）
electron --inspect=5858 .

# 检查 Electron 版本
npx electron --version

# 查看可用模板
npm create @quick-start/electron -- --help
```

### 开发调试技巧

```ts
// 主进程调试：启动时加 --inspect 参数
// package.json
{
  "scripts": {
    "electron:debug": "electron --inspect=5858 ."
  }
}

// 渲染进程：直接使用 Chrome DevTools（Ctrl+Shift+I）
// 代码中打开 DevTools
win.webContents.openDevTools()
```
