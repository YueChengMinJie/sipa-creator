/*
 * @Author: caszhou
 * @Date: 2022-02-08 10:51:05
 * @Description: 脚手架配置文件
 */

'use strict'
const Path = require('path')
const Config = require('./src/config.js')
const CompressionWebpackPlugin = require('compression-webpack-plugin')

function resolve(dir) {
  return Path.join(__dirname, dir)
}
/** 本地测试环境需要配置的跨域问题 */
const ifLocal = process.env.VUE_APP_ENV === 'local'
const proxy = {}
if (ifLocal) {
  Config.associationServices.forEach(item => {
    proxy[item.prefix] = {
      target: item.target,
      changeOrigin: true
    }
  })
}
module.exports = {
  productionSourceMap: ifLocal,
  devServer: {
    port: 7110,
    proxy
  },
  configureWebpack: {
    name: Config.siteName,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    },
    externals: {
      vue: 'Vue',
      vuex: 'Vuex',
      axios: 'axios',
      'vue-router': 'VueRouter',
      'element-ui': 'ELEMENT'
    }
  },
  chainWebpack(data) {
    data.plugins.delete('preload')
    data.plugins.delete('prefetch')

    data.plugin('compressionPlugin').use(
      new CompressionWebpackPlugin({
        test: /\.js$|\.html$|\.css/, // 匹配文件名
        threshold: 1024, // 对超过1k的数据压缩
        deleteOriginalAssets: false // 不删除源文件
      })
    )
    data.module
      .rule('svg')
      .exclude.add(resolve('src/assets/icons')) // index.js 的路径
      .end()
    data.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/assets/icons')) // index.js 的路径
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: 'icon-[name]'
      })
      .end()
  }
}
