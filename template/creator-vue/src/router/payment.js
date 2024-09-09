/*
 * @Author: caszhou
 * @Date: 2023-06-15 17:26:17
 * @Description: 支付中心
 */
export default {
  path: '/payment',
  name: 'payment',
  component: '/layout',
  meta: { title: '支付中心', icon: 'payment-index' },
  redirect: { name: 'paymentMerchantInputPieceIndex' },
  sort: 2,
  children: [
    {
      path: 'merchantInputPiece',
      name: 'paymentMerchantInputPiece',
      meta: { title: '商户进件' },
      component: '/layout/children',
      redirect: { name: 'paymentMerchantInputPieceIndex' },
      children: [
        {
          path: 'index',
          name: 'paymentMerchantInputPieceIndex',
          meta: { title: '商户进件', highlightName: 'paymentMerchantInputPiece', keepAlive: true },
          component: '/payment/merchantInputPiece/index'
        },
        {
          path: 'inputPieceDetail',
          name: 'paymentMerchantInputPieceDetail',
          meta: { title: '进件信息', highlightName: 'paymentMerchantInputPiece' },
          component: '/payment/inputPieceDetail/index',
          hidden: true
        },
        {
          path: 'log',
          name: 'paymentMerchantInputPieceLog',
          meta: { title: '日志', highlightName: 'paymentMerchantInputPiece', keepAlive: true },
          component: '/payment/merchantInputPiece/log',
          hidden: true
        },
        {
          path: 'operateCloud',
          name: 'paymentMerchantInputPieceOperateCloud',
          meta: { title: '进件云直通', highlightName: 'paymentMerchantInputPiece' },
          component: '/payment/merchantInputPiece/operateCloud',
          hidden: true
        }
      ]
    },
    {
      path: 'userInputPiece',
      name: 'paymentUserInputPiece',
      meta: { title: '用户进件' },
      component: '/layout/children',
      redirect: { name: 'paymentUserInputPieceIndex' },
      children: [
        {
          path: 'index',
          name: 'paymentUserInputPieceIndex',
          meta: { title: '用户进件', highlightName: 'paymentUserInputPiece', keepAlive: true },
          component: '/payment/userInputPiece/index'
        },
        {
          path: 'operateAdapay',
          name: 'paymentUserInputPieceOperateAdapay',
          meta: { title: '进件汇付', highlightName: 'paymentUserInputPiece' },
          component: '/payment/userInputPiece/operateAdapay',
          hidden: true
        },
        {
          path: 'inputPieceDetail',
          name: 'paymentUserInputPieceDetail',
          meta: { title: '进件信息', highlightName: 'paymentUserInputPiece' },
          component: '/payment/inputPieceDetail/index',
          hidden: true
        },
        {
          path: 'log',
          name: 'paymentUserInputPieceLog',
          meta: { title: '日志', highlightName: 'paymentUserInputPiece', keepAlive: true },
          component: '/payment/userInputPiece/log',
          hidden: true
        }
      ]
    },
    {
      path: 'bill',
      name: 'paymentBill',
      meta: { title: '电子账簿' },
      component: '/payment/bill/index'
    },
    {
      path: 'billDetail',
      name: 'paymentBillDetail',
      meta: { title: '账簿订单明细' },
      component: '/payment/billDetail'
    },
    {
      path: 'inputPieceWhite',
      name: 'paymentInputPieceWhite',
      meta: { title: '进件白名单' },
      component: '/payment/inputPieceWhite/index'
    }
  ]
}
