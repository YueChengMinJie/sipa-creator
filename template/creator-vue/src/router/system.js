/*
 * @Author: caszhou
 * @Date: 2023-07-19 15:51:42
 * @Description: 权限中心
 */

export default {
  path: '/system',
  name: 'system',
  component: '/layout',
  meta: { title: '权限中心', icon: 'system-index' },
  redirect: { name: 'systemUserInfo' },
  sort: 99,
  children: [
    {
      path: 'userInfo',
      name: 'systemUserInfo',
      meta: {
        title: '个人信息'
      },
      component: '/system/userInfo'
    },
    {
      path: 'role',
      name: 'systemRole',
      meta: { title: '角色权限' },
      component: '/layout/children',
      redirect: { name: 'systemRoleIndex' },
      children: [
        {
          path: 'index',
          name: 'systemRoleIndex',
          meta: { title: '角色权限', highlightName: 'systemRole', keepAlive: true },
          component: '/system/role/index'
        },
        {
          path: 'view',
          name: 'systemRoleView',
          meta: { title: '角色查看', highlightName: 'systemRole' },
          component: '/system/role/view',
          hidden: true
        },
        {
          path: 'operate',
          name: 'systemRoleOperate',
          meta: { title: '角色操作', highlightName: 'systemRole' },
          component: '/system/role/operate',
          hidden: true
        },
        {
          path: 'authority',
          name: 'systemRoleAuthority',
          meta: { title: '权限设置', highlightName: 'systemRole' },
          component: '/system/role/authority',
          hidden: true
        }
      ]
    },
    {
      path: 'staff',
      name: 'systemStaff',
      meta: { title: '人员组织' },
      component: '/layout/children',
      redirect: { name: 'systemStaffIndex' },
      children: [
        {
          path: 'index',
          name: 'systemStaffIndex',
          meta: { title: '人员组织', highlightName: 'systemStaff', keepAlive: true },
          component: '/system/staff/index'
        },
        {
          path: 'operate',
          name: 'systemStaffOperate',
          meta: { title: '人事操作', highlightName: 'systemStaff' },
          component: '/system/staff/operate/index',
          hidden: true
        }
      ]
    }
  ]
}
