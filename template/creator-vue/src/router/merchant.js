/*
 * @Author: caszhou
 * @Date: 2021-03-26 15:00:28
 * @Description: 商户管理
 */

export default {
  path: '/merchant',
  name: 'merchant',
  component: '/layout',
  meta: { title: '商户中心', icon: 'merchant-index' },
  redirect: { name: 'merchantManageIndex' },
  sort: 1,
  children: [
    {
      path: 'manage',
      name: 'merchantManage',
      meta: { title: '商户管理' },
      component: '/layout/children',
      redirect: { name: 'merchantManageIndex' },
      children: [
        {
          path: 'index',
          name: 'merchantManageIndex',
          meta: { title: '商户管理', highlightName: 'merchantManage', keepAlive: true },
          component: '/merchant/manage/index'
        },
        {
          path: 'operate',
          name: 'merchantManageOperate',
          meta: { title: '操作', highlightName: 'merchantManage' },
          component: '/merchant/manage/operate',
          hidden: true
        },
        {
          path: 'detail',
          name: 'merchantManageDetail',
          meta: { title: '查看详情', highlightName: 'merchantManage' },
          component: '/merchant/manage/detail',
          hidden: true
        },
        {
          path: 'operateCloud',
          name: 'merchantOperateCloud',
          meta: { title: '进件云直通', highlightName: 'merchantManage' },
          component: '/payment/merchantInputPiece/operateCloud',
          hidden: true
        },
        {
          path: 'operateAdapay',
          name: 'merchantOperateAdapay',
          meta: { title: '进件汇付', highlightName: 'merchantManage' },
          component: '/payment/userInputPiece/operateAdapay',
          hidden: true
        },
        {
          path: 'inputPieceDetail',
          name: 'merchantInputPieceDetail',
          meta: { title: '进件信息', highlightName: 'merchantManage' },
          component: '/payment/inputPieceDetail/index',
          hidden: true
        }
      ]
    },
    {
      path: 'organization',
      name: 'merchantOrganization',
      meta: { title: '组织管理' },
      component: '/layout/children',
      redirect: { name: 'merchantOrganizationIndex' },
      children: [
        {
          path: 'index',
          name: 'merchantOrganizationIndex',
          meta: { title: '组织管理', highlightName: 'merchantOrganization', keepAlive: true },
          component: '/merchant/organization/index'
        },
        {
          path: 'operate',
          name: 'merchantOrganizationOperate',
          meta: { title: '操作', highlightName: 'merchantOrganization' },
          component: '/merchant/manage/operate',
          hidden: true
        },
        {
          path: 'detail',
          name: 'merchantOrganizationDetail',
          meta: { title: '查看详情', highlightName: 'merchantOrganization' },
          component: '/merchant/manage/detail',
          hidden: true
        },
        {
          path: 'inputPieceDetail',
          name: 'merchantOrganizationInputPieceDetail',
          meta: { title: '进件信息', highlightName: 'merchantOrganization' },
          component: '/payment/inputPieceDetail/index',
          hidden: true
        },
        {
          path: 'operateAdapay',
          name: 'merchantOrganizationOperateAdapay',
          meta: { title: '组织进件汇付', highlightName: 'merchantOrganization' },
          component: '/payment/userInputPiece/operateAdapay',
          hidden: true
        }
      ]
    },
    {
      path: 'saasSetting',
      name: 'merchantSaasSetting',
      meta: { title: 'SAAS配置' },
      component: '/layout/children',
      redirect: { name: 'merchantSaasSettingIndex' },
      children: [
        {
          path: 'index',
          name: 'merchantSaasSettingIndex',
          meta: { title: 'SAAS配置', highlightName: 'merchantSaasSetting', keepAlive: true },
          component: '/merchant/saas/index'
        },
        {
          path: 'operate',
          name: 'merchantSaasSettingOperate',
          meta: { title: '操作', highlightName: 'merchantSaasSetting' },
          component: '/merchant/saas/operate',
          hidden: true
        },
        {
          path: 'detail',
          name: 'merchantSaasSettingDetail',
          meta: { title: '查看详情', highlightName: 'merchantSaasSetting' },
          component: '/merchant/saas/detail',
          hidden: true
        }
      ]
    },
    {
      path: 'white',
      name: 'merchantWhite',
      meta: { title: '垫资白名单' },
      component: '/layout/children',
      redirect: { name: 'merchantWhiteIndex' },
      children: [
        {
          path: 'index',
          name: 'merchantWhiteIndex',
          meta: { title: '垫资白名单', highlightName: 'merchantWhite' },
          component: '/merchant/white/index'
        }
      ]
    },
    {
      path: 'rateSetting',
      name: 'merchantRateSetting',
      meta: { title: '费率设置' },
      component: '/layout/children',
      redirect: { name: 'merchantRateSettingIndex' },
      children: [
        {
          path: 'index',
          name: 'merchantRateSettingIndex',
          meta: { title: '费率设置', highlightName: 'merchantRateSetting' },
          component: '/merchant/rateSetting/index'
        }
      ]
    }
  ]
}
