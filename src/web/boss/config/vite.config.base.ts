import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import { resolve } from 'path'
import { defineConfig } from 'vite'
import svgLoader from 'vite-svg-loader'
import configArcoStyleImportPlugin from './plugin/arcoStyleImport'

export default defineConfig({
  base: './',
  server: {
    port: 8082,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true, // 修改请求头中的 Origin 为目标地址
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
  plugins: [vue(), vueJsx(), svgLoader({ svgoConfig: {} }), configArcoStyleImportPlugin()],
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, '../src'),
      },
      {
        find: 'assets',
        replacement: resolve(__dirname, '../src/assets'),
      },
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js', // Resolve the i18n warning issue
      },
      {
        find: 'vue',
        replacement: 'vue/dist/vue.esm-bundler.js', // compile template
      },
    ],
    extensions: ['.ts', '.js'],
  },
  define: {
    'process.env': {},
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          hack: `true; @import (reference) "${resolve('src/assets/style/breakpoint.less')}";`,
        },
        javascriptEnabled: true,
      },
    },
  },
})
