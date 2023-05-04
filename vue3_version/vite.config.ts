/*
 * @Author: zyy 1741540959@qq.com
 * @Date: 2023-01-06 15:02:03
 * @LastEditors: zyy
 * @LastEditTime: 2023-02-04 15:28:27
 * @Description: file content
 */
import { fileURLToPath, URL } from 'node:url'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import { defineConfig } from 'vite'
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'
import vue from '@vitejs/plugin-vue'

// element-ui 自动导入
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    AutoImport({
      resolvers: [
        // 自动导入element-plus组件
        ElementPlusResolver(),
        // 自动导入图标组件
        IconsResolver({
          prefix: 'Icon'
        })
      ],
    }),
    Components({
      resolvers: [
        // 自动导入element-plus组件
        ElementPlusResolver(),
        // 自动导入图标组件
        IconsResolver({
          enabledCollections: ['ep']
        })
      ],
    }),
    // 自动导入图标组件
    Icons({
      autoInstall: true,
    }),
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 配置项相比webpack的代理也有所改变
  server: {
    hmr: true,//开启热更新
    proxy: {
      '/api': {
        target: 'http://139.9.135.28:8081',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
      '/ws':{
        target:'ws://139.9.135.28:8081',
        ws:true
      }
    }
  }
})
