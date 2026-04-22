# Vue3 + Electron 快速集成笔记

> 使用 **Vite + Vue3 + TypeScript + Electron** 快速构建跨平台桌面应用。

---

## 一、脚手架方式（推荐）

### electron-vite 官方脚手架

```bash
# 选择 vue-ts 模板，一键生成完整项目
npm create @quick-start/electron my-vue-app -- --template vue-ts
cd my-vue-app
npm install
npm run dev
```

启动后会自动打开 Electron 窗口，支持主进程和渲染进程同步热重载。

### electron-vite 脚手架内置能力

| 能力 | 说明 |
|------|------|
| 热重载 | 主进程 / 预加载 / 渲染进程全部支持 HMR |
| 路径别名 | 预配置 `@` 指向 `src/` |
| 打包优化 | 自动分离主进程和渲染进程产物 |
| TypeScript | 开箱即用 |
| 安全默认值 | `contextIsolation: true`，`nodeIntegration: false` |

---

## 二、手动集成

### 第一步：创建 Vue3 + Vite 项目

```bash
npm create vite@latest my-electron-vue -- --template vue-ts
cd my-electron-vue
npm install
```

### 第二步：安装依赖

```bash
# Electron 核心
npm install electron --save-dev
npm install electron-builder --save-dev

# Vite 插件（主进程热重载 + 渲染进程 Node.js 支持）
npm install vite-plugin-electron vite-plugin-electron-renderer --save-dev
```

### 第三步：配置 vite.config.ts

```ts
// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import electron from 'vite-plugin-electron'
import renderer from 'vite-plugin-electron-renderer'
import path from 'path'

export default defineConfig({
  plugins: [
    vue(),
    electron([
      {
        entry: 'electron/main.ts',
        onstart: ({ startup }) => startup(),
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
        entry: 'electron/preload.ts',
        onstart: ({ reload }) => reload(),
        vite: {
          build: {
            outDir: 'dist-electron',
            sourcemap: 'inline',
            minify: false
          }
        }
      }
    ]),
    renderer()
  ],
  resolve: {
    alias: { '@': path.resolve(__dirname, 'src') }
  }
})
```

### 第四步：更新 package.json

```json
{
  "name": "my-electron-vue",
  "version": "1.0.0",
  "main": "dist-electron/main.js",
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build",
    "electron:build": "npm run build && electron-builder",
    "electron:build:win": "npm run build && electron-builder --win",
    "electron:build:mac": "npm run build && electron-builder --mac"
  }
}
```

### 第五步：创建主进程入口

```bash
mkdir electron
touch electron/main.ts
touch electron/preload.ts
```

---

## 三、项目结构

```
my-electron-vue/
├── electron/
│   ├── main.ts              # 主进程入口
│   ├── preload.ts           # 预加载脚本
│   └── ipc/
│       ├── window.ts        # 窗口控制处理
│       ├── dialog.ts        # 文件对话框处理
│       └── index.ts         # 统一注册
├── src/
│   ├── main.ts              # Vue 应用入口
│   ├── App.vue
│   ├── router/
│   │   └── index.ts
│   ├── stores/
│   │   └── app.ts
│   ├── composables/
│   │   ├── useElectron.ts   # Electron API 封装
│   │   └── useWindowControl.ts
│   ├── components/
│   │   └── TitleBar.vue     # 自定义标题栏
│   ├── views/
│   └── types/
│       └── electron.d.ts    # 类型声明
├── build/                   # 打包资源（图标等）
├── public/
├── vite.config.ts
├── electron-builder.yml
└── package.json
```

---

## 四、主进程配置

```ts
// electron/main.ts
import { app, BrowserWindow, shell } from 'electron'
import path from 'path'
import { registerIpcHandlers } from './ipc/index'

const isDev = process.env.NODE_ENV === 'development'

let mainWindow: BrowserWindow | null = null

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1280,
    height: 720,
    minWidth: 900,
    minHeight: 600,
    show: false,
    frame: false,              // 无边框窗口（配合自定义标题栏使用）
    backgroundColor: '#ffffff',
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      contextIsolation: true,
      nodeIntegration: false,
      sandbox: false           // preload 中需要 require 时设为 false
    }
  })

  if (isDev && process.env['ELECTRON_RENDERER_URL']) {
    mainWindow.loadURL(process.env['ELECTRON_RENDERER_URL'])
    mainWindow.webContents.openDevTools({ mode: 'detach' })
  } else {
    mainWindow.loadFile(path.join(__dirname, '../dist/index.html'))
  }

  mainWindow.once('ready-to-show', () => {
    mainWindow?.show()
    mainWindow?.focus()
  })

  // 拦截新窗口，改为外部浏览器打开
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url)
    return { action: 'deny' }
  })

  // 注册所有 IPC 处理函数
  registerIpcHandlers(mainWindow)
}

app.whenReady().then(() => {
  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
  createWindow()
})

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
    mainWindow = null
  }
})
```

---

## 五、预加载脚本

```ts
// electron/preload.ts
import { contextBridge, ipcRenderer } from 'electron'

contextBridge.exposeInMainWorld('electronAPI', {
  // 窗口控制
  window: {
    minimize: () => ipcRenderer.send('window:minimize'),
    maximize: () => ipcRenderer.send('window:maximize'),
    close: () => ipcRenderer.send('window:close'),
    isMaximized: (): Promise<boolean> => ipcRenderer.invoke('window:isMaximized'),
    onMaximizeChange: (cb: (maximized: boolean) => void) => {
      ipcRenderer.on('window:maximizeChange', (_e, v) => cb(v))
    }
  },
  // 文件对话框
  dialog: {
    openFile: (options?: Electron.OpenDialogOptions): Promise<string[]> =>
      ipcRenderer.invoke('dialog:openFile', options),
    saveFile: (content: string, defaultPath?: string): Promise<{ success: boolean; filePath?: string }> =>
      ipcRenderer.invoke('dialog:saveFile', { content, defaultPath })
  },
  // 文件系统
  fs: {
    readFile: (filePath: string): Promise<string> =>
      ipcRenderer.invoke('fs:readFile', filePath),
    writeFile: (filePath: string, content: string): Promise<void> =>
      ipcRenderer.invoke('fs:writeFile', filePath, content)
  },
  // 应用信息
  app: {
    getVersion: (): Promise<string> => ipcRenderer.invoke('app:getVersion'),
    getPath: (name: string): Promise<string> => ipcRenderer.invoke('app:getPath', name)
  }
})
```

```ts
// src/types/electron.d.ts
interface ElectronAPI {
  window: {
    minimize(): void
    maximize(): void
    close(): void
    isMaximized(): Promise<boolean>
    onMaximizeChange(cb: (maximized: boolean) => void): void
  }
  dialog: {
    openFile(options?: any): Promise<string[]>
    saveFile(content: string, defaultPath?: string): Promise<{ success: boolean; filePath?: string }>
  }
  fs: {
    readFile(filePath: string): Promise<string>
    writeFile(filePath: string, content: string): Promise<void>
  }
  app: {
    getVersion(): Promise<string>
    getPath(name: string): Promise<string>
  }
}

declare global {
  interface Window {
    electronAPI: ElectronAPI
  }
}
```

---

## 六、Vue3 中使用 Electron API

### 基础 Composable

```ts
// src/composables/useElectron.ts
export const isElectron = (): boolean =>
  typeof window !== 'undefined' && !!window.electronAPI

export function useElectronApp() {
  const api = window.electronAPI

  return {
    isElectron: isElectron(),
    window: api?.window,
    dialog: api?.dialog,
    fs: api?.fs,
    app: api?.app
  }
}
```

### 窗口控制 Composable

```ts
// src/composables/useWindowControl.ts
import { ref, onMounted, onUnmounted } from 'vue'

export function useWindowControl() {
  const isMaximized = ref(false)

  onMounted(async () => {
    if (!window.electronAPI) return
    isMaximized.value = await window.electronAPI.window.isMaximized()
    window.electronAPI.window.onMaximizeChange((maximized) => {
      isMaximized.value = maximized
    })
  })

  const minimize = () => window.electronAPI?.window.minimize()
  const maximize = () => window.electronAPI?.window.maximize()
  const close = () => window.electronAPI?.window.close()

  return { isMaximized, minimize, maximize, close }
}
```

### 自定义标题栏组件

```vue
<!-- src/components/TitleBar.vue -->
<template>
  <div class="title-bar">
    <div class="drag-region">
      <span class="app-title">{{ title }}</span>
    </div>
    <div class="window-controls">
      <button class="btn-min" @click="minimize" title="最小化">
        <svg width="10" height="1" viewBox="0 0 10 1"><line x1="0" y1="0" x2="10" y2="0" stroke="currentColor" stroke-width="1.5"/></svg>
      </button>
      <button class="btn-max" @click="maximize" :title="isMaximized ? '向下还原' : '最大化'">
        <svg v-if="!isMaximized" width="10" height="10" viewBox="0 0 10 10"><rect x="0.75" y="0.75" width="8.5" height="8.5" stroke="currentColor" stroke-width="1.5" fill="none"/></svg>
        <svg v-else width="10" height="10" viewBox="0 0 10 10"><path d="M2 0H10V8M0 2H8V10H0V2Z" stroke="currentColor" stroke-width="1.5" fill="none"/></svg>
      </button>
      <button class="btn-close" @click="close" title="关闭">
        <svg width="10" height="10" viewBox="0 0 10 10"><line x1="0" y1="0" x2="10" y2="10" stroke="currentColor" stroke-width="1.5"/><line x1="10" y1="0" x2="0" y2="10" stroke="currentColor" stroke-width="1.5"/></svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useWindowControl } from '@/composables/useWindowControl'

defineProps<{ title?: string }>()
const { isMaximized, minimize, maximize, close } = useWindowControl()
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
  height: 32px;
  background: #1e1e2e;
  -webkit-app-region: drag;
  user-select: none;
}

.drag-region {
  flex: 1;
  padding-left: 12px;
}

.app-title {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
}

.window-controls {
  display: flex;
  -webkit-app-region: no-drag;
}

.window-controls button {
  width: 46px;
  height: 32px;
  border: none;
  background: transparent;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.15s;
}

.window-controls button:hover { background: rgba(255, 255, 255, 0.1); }
.btn-close:hover { background: #e81123 !important; color: #fff !important; }
</style>
```

### 在 App.vue 中集成

```vue
<!-- src/App.vue -->
<template>
  <div class="app-container">
    <TitleBar title="My App" v-if="isElectron" />
    <main class="app-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { isElectron } from '@/composables/useElectron'
import TitleBar from '@/components/TitleBar.vue'
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }

.app-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.app-main {
  flex: 1;
  overflow: auto;
}
</style>
```

---

## 七、IPC 通信实践

### 按模块组织 IPC 处理函数

```ts
// electron/ipc/window.ts
import { ipcMain, BrowserWindow } from 'electron'

export function registerWindowHandlers(win: BrowserWindow) {
  ipcMain.on('window:minimize', () => win.minimize())

  ipcMain.on('window:maximize', () => {
    const maximized = win.isMaximized()
    maximized ? win.unmaximize() : win.maximize()
  })

  ipcMain.on('window:close', () => win.close())
  ipcMain.handle('window:isMaximized', () => win.isMaximized())

  // 最大化状态变化时通知渲染进程
  win.on('maximize', () => win.webContents.send('window:maximizeChange', true))
  win.on('unmaximize', () => win.webContents.send('window:maximizeChange', false))
}
```

```ts
// electron/ipc/dialog.ts
import { ipcMain, dialog, BrowserWindow } from 'electron'
import fs from 'fs/promises'

export function registerDialogHandlers(win: BrowserWindow) {
  ipcMain.handle('dialog:openFile', async (_e, options = {}) => {
    const { canceled, filePaths } = await dialog.showOpenDialog(win, {
      properties: ['openFile'],
      ...options
    })
    return canceled ? [] : filePaths
  })

  ipcMain.handle('dialog:saveFile', async (_e, { content, defaultPath }) => {
    const { canceled, filePath } = await dialog.showSaveDialog(win, {
      defaultPath: defaultPath ?? 'output.txt'
    })
    if (!canceled && filePath) {
      await fs.writeFile(filePath, content, 'utf-8')
      return { success: true, filePath }
    }
    return { success: false }
  })
}
```

```ts
// electron/ipc/index.ts
import { BrowserWindow, app, ipcMain } from 'electron'
import { registerWindowHandlers } from './window'
import { registerDialogHandlers } from './dialog'
import fs from 'fs/promises'

export function registerIpcHandlers(win: BrowserWindow) {
  registerWindowHandlers(win)
  registerDialogHandlers(win)

  // 文件系统
  ipcMain.handle('fs:readFile', (_e, filePath: string) =>
    fs.readFile(filePath, 'utf-8')
  )
  ipcMain.handle('fs:writeFile', (_e, filePath: string, content: string) =>
    fs.writeFile(filePath, content, 'utf-8')
  )

  // 应用信息
  ipcMain.handle('app:getVersion', () => app.getVersion())
  ipcMain.handle('app:getPath', (_e, name: string) => app.getPath(name as any))
}
```

---

## 八、Vue Router 适配

```ts
// src/router/index.ts
import { createRouter, createWebHashHistory } from 'vue-router'

// 注意：Electron 中必须使用 Hash 路由，不能用 History 路由
// 因为生产环境加载的是本地 file:// 协议
const router = createRouter({
  history: createWebHashHistory(),    // ✅ Electron 中使用
  // history: createWebHistory(),     // ❌ 不能用（本地 file:// 协议不支持）
  routes: [
    {
      path: '/',
      name: 'Home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/settings',
      name: 'Settings',
      component: () => import('@/views/SettingsView.vue')
    }
  ]
})

export default router
```

---

## 九、Pinia 状态管理

### 存储 Electron 相关状态

```ts
// src/stores/app.ts
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { isElectron } from '@/composables/useElectron'

export const useAppStore = defineStore('app', () => {
  const version = ref('')
  const appPath = ref('')
  const isReady = ref(false)

  const env = computed(() => isElectron() ? 'electron' : 'web')

  async function init() {
    if (!isElectron()) {
      isReady.value = true
      return
    }
    try {
      version.value = await window.electronAPI.app.getVersion()
      appPath.value = await window.electronAPI.app.getPath('userData')
    } finally {
      isReady.value = true
    }
  }

  return { version, appPath, isReady, env, init }
})
```

```ts
// src/main.ts
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'
import { useAppStore } from './stores/app'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')

// 初始化 Electron 状态
const appStore = useAppStore()
appStore.init()
```

---

## 十、打包配置

### electron-builder.yml

```yaml
appId: com.example.vue-electron-app
productName: MyVueApp
copyright: Copyright © 2024

directories:
  output: release/${version}

files:
  - dist/**/*
  - dist-electron/**/*

extraMetadata:
  main: dist-electron/main.js

win:
  target:
    - target: nsis
      arch: [x64]
  icon: build/icon.ico

mac:
  target:
    - target: dmg
      arch: [x64, arm64]
  icon: build/icon.icns
  category: public.app-category.utilities

linux:
  target:
    - target: AppImage
    - target: deb
  icon: build/icon.png

nsis:
  oneClick: false
  allowToChangeInstallationDirectory: true
  createDesktopShortcut: true
  createStartMenuShortcut: true
  installerLanguages: ['zh_CN', 'en_US']
  language: 2052   # 默认中文
```

### 完整 scripts

```json
{
  "scripts": {
    "dev": "vite",
    "build": "vue-tsc && vite build",
    "electron:dev": "cross-env NODE_ENV=development electron .",
    "electron:build": "npm run build && electron-builder",
    "electron:build:win": "npm run build && electron-builder --win --x64",
    "electron:build:mac": "npm run build && electron-builder --mac",
    "electron:build:linux": "npm run build && electron-builder --linux"
  }
}
```

### 环境变量配置

```bash
# .env.development
VITE_APP_ENV=development

# .env.production
VITE_APP_ENV=production
VITE_API_BASE_URL=https://api.example.com
```

```ts
// electron/main.ts 中读取
const isDev = process.env.NODE_ENV === 'development'

// Vue 中读取
const env = import.meta.env.VITE_APP_ENV
```

---

## 附：与 React 版本的主要差异

| 对比项 | Vue3 版本 | React 版本 |
|--------|-----------|------------|
| 逻辑封装 | `composables/useXxx.ts`（Composition API） | `hooks/useXxx.ts`（Custom Hook） |
| 状态管理 | Pinia | Redux / Zustand |
| 路由 | `createWebHashHistory`（必须） | `HashRouter`（必须） |
| 模板方式 | SFC（`.vue` 文件） | JSX / TSX |
| 标题栏拖拽 | `-webkit-app-region: drag`（同） | `-webkit-app-region: drag`（同） |
| 事件监听清理 | `onUnmounted` | `useEffect` return 函数 |
| 应用初始化 | `main.ts` + Pinia init | `main.tsx` + Context / Provider |

> ✅ 预加载脚本（`preload.ts`）和主进程（`main.ts`）的写法在两种框架下完全相同，只有渲染进程部分有差异。
