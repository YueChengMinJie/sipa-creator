/*
 * @Author: zqy
 * @Date: 2023-09-20 17:26:17
 * @Description: 审批中西
 */
export default {
  path: '/approve',
  name: 'approve',
  component: '/layout',
  meta: { title: '审批中心', icon: 'payment-index' },
  redirect: { name: 'approveBonusIndex' },
  sort: 5,
  children: [
    {
      path: 'bonus',
      name: 'approveBonusIndex',
      meta: { title: '福利充值审批' },
      component: '/approve/bonus/index'
    }
  ]
}
