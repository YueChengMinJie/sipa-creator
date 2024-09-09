/*
 * @Author: zqy
 * @Date: 2023-09-21 17:26:17
 * @Description: 财务中心
 */
export default {
  path: '/finance',
  name: 'finance',
  component: '/layout',
  meta: { title: '财务中心', icon: 'finance-index' },
  redirect: { name: 'financeWithdrawIndex' },
  sort: 6,
  children: [
    {
      path: 'withdraw',
      name: 'financeWithdrawIndex',
      meta: { title: '提现列表(手动)' },
      component: '/finance/withdraw/index'
    }
  ]
}
