/*
 * @Author: caszhou
 * @Date: 2023-06-13 13:02:58
 * @Description: 路由主文件
 */
import Vue from 'vue'
import Router from 'vue-router'
import Nprogress from 'nprogress'
import Store from '@/store'
import { Notification } from 'element-ui'
import Config from '@/config'

Vue.use(Router)

/**
 * 重写路由的push方法
 */
const routerPush = Router.prototype.push
Router.prototype.push = function push(location) {
  const identityId = this.app.$route.query[Config.identityId]
  if (!location.query) {
    location.query = {
      [Config.identityId]: identityId
    }
  } else {
    location.query[Config.identityId] = identityId
  }
  return routerPush.call(this, location).catch(error => error)
}

/**
 * @param hidden 是否隐藏当前菜单
 * @param highlightName 当前要高亮的name
 * @param sort 菜单路由所在的位置
 */
export const routes = [
  {
    path: '*',
    name: 'error',
    component: () => import('@/views/404'),
    hidden: true
  }
]

const createRouter = () =>
  new Router({
    mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    routes
  })
const router = createRouter()
/**
 * 导航守卫 + 进度条
 */
Nprogress.configure({ showSpinner: false })
router.beforeEach(async (to, from, next) => {
  try {
    Nprogress.start()
    const { token, ifFree } = to.query
    const ifInitPage = token && ifFree
    if (ifInitPage) {
      const data = await Store.dispatch('initProjectData', { token, ifFree: String(ifFree) === 'true' ? true : false })
      const routes = Store.state.routes
      if (routes.length > 0) {
        next({
          name: routes[0].name,
          query: { [Config.identityId]: data.loginCompanyId + data.loginApplicationId }
        })
      } else {
        throw '获取用户信息异常, 未检测到登录资源'
      }
    } else {
      if (to.query[Config.identityId]) {
        if (!Store.state.userInfo) {
          await Store.dispatch('initProjectData')
          const routes = Store.state.routes
          if (routes.length === 0) {
            throw '获取用户信息异常, 未检测到登录资源'
          }
          next({ path: to.path, query: { ...to.query }, replace: true })
        } else {
          if (to.matched.length === 0) {
            next({ path: '/404' })
          } else {
            Store.commit('setKeepAlive', { to, from })
            next()
          }
        }
      } else {
        throw '页面禁止访问, 您可能无权访问!'
      }
    }
  } catch (error) {
    console.error('beforeEach await:', error)
    if (typeof error === 'string') {
      Notification({
        showClose: true,
        title: '错误',
        message: error,
        type: 'error'
      })
    }
    next()
  }
})

router.afterEach(() => {
  Nprogress.done()
})

export default router
