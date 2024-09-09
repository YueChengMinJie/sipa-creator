/*
 * @Author: caszhou
 * @Date: 2021-05-11 14:44:35
 * @Description: 入口文件
 */

import Vue from 'vue'
import App from './App.vue'
import Store from './store'
import Router from './router'
import ElementUI from 'element-ui'
import 'nprogress/nprogress.css'
import 'normalize.css'
import './styles/index.scss'
import './assets/icons'

// import { getLocationRoutes } from '@/utils/routeTools'
// console.log(JSON.stringify(getLocationRoutes(true)))

ElementUI.Input.props.showWordLimit.default = true
Vue.use(ElementUI, { size: 'small' })
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  store: Store,
  router: Router
}).$mount('#app')
