/*
 * @Author: caszhou
 * @Date: 2023-08-08 15:03:42
 * @Description: 应用中心
 */
export default {
  path: '/application',
  name: 'application',
  component: '/layout',
  meta: { title: '应用中心', icon: 'application-index' },
  redirect: { name: 'applicationManage' },
  sort: 4,
  children: [
    {
      path: 'manage',
      name: 'applicationManage',
      meta: { title: '应用管理' },
      component: '/layout/children',
      redirect: { name: 'applicationManageIndex' },
      children: [
        {
          path: 'index',
          name: 'applicationManageIndex',
          meta: { title: '应用管理', highlightName: 'applicationManage', keepAlive: true },
          component: '/application/manage/index'
        },
        {
          path: 'operate',
          name: 'applicationManageOperate',
          meta: { title: '应用操作', highlightName: 'applicationManage' },
          component: '/application/manage/operate',
          hidden: true
        }
      ]
    },
    {
      path: 'resource',
      name: 'applicationResource',
      meta: { title: '资源管理' },
      component: '/layout/children',
      redirect: { name: 'applicationResourceIndex' },
      children: [
        {
          path: 'index',
          name: 'applicationResourceIndex',
          meta: { title: '资源管理', highlightName: 'applicationResource', keepAlive: true },
          component: '/application/resource/index'
        },
        {
          path: 'operate',
          name: 'applicationResourceOperate',
          meta: { title: '资源操作', highlightName: 'applicationResource' },
          component: '/application/resource/operate',
          hidden: true
        }
      ]
    },
    {
      path: 'product',
      name: 'applicationProduct',
      meta: { title: '产品管理' },
      component: '/layout/children',
      redirect: { name: 'applicationProductIndex' },
      children: [
        {
          path: 'index',
          name: 'applicationProductIndex',
          meta: { title: '产品管理', highlightName: 'applicationProduct', keepAlive: true },
          component: '/application/product/index'
        },
        {
          path: 'operate',
          name: 'applicationProductOperate',
          meta: { title: '产品操作', highlightName: 'applicationProduct' },
          component: '/application/product/operate',
          hidden: true
        },
        {
          path: 'detail',
          name: 'applicationProductDetail',
          meta: { title: '产品详情', highlightName: 'applicationProduct' },
          component: '/application/product/detail',
          hidden: true
        }
      ]
    },
    {
      path: 'service',
      name: 'applicationService',
      meta: { title: '服务管理' },
      component: '/layout/children',
      redirect: { name: 'applicationServiceIndex' },
      children: [
        {
          path: 'index',
          name: 'applicationServiceIndex',
          meta: { title: '服务管理', highlightName: 'applicationService', keepAlive: true },
          component: '/application/service/index'
        },
        {
          path: 'operate',
          name: 'applicationServiceOperate',
          meta: { title: '服务操作', highlightName: 'applicationService' },
          component: '/application/service/operate',
          hidden: true
        },
        {
          path: 'detail',
          name: 'applicationServiceDetail',
          meta: { title: '服务详情', highlightName: 'applicationService' },
          component: '/application/service/detail',
          hidden: true
        },
        {
          path: 'sign',
          name: 'applicationServiceSign',
          meta: { title: '签约管理', highlightName: 'applicationService' },
          component: '/application/service/sign/index',
          hidden: true
        }
      ]
    }
  ]
}
