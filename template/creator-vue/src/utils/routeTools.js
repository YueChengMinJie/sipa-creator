/*
 * @Author: caszhou
 * @Date: 2023-07-06 16:44:05
 * @Description: 路由转换工具
 */

/**
 * 获取本地除index.js以外的路由信息
 * @returns routes
 */
export function getLocationRoutes(ifNoRewriteComponent) {
  const files = require
    .context('@/router', false, /\.js$/)
    .keys()
    .filter(file => file !== './index.js')
    .map(file => require(`@/router/${file.substr(2)}`).default)
  const localRoutes = []
  const montageRoutes = (routes, arr) => {
    arr.forEach(item => {
      const attr = {
        ...item,
        component: ifNoRewriteComponent ? item.component : () => import('@/views' + item.component),
        children: []
      }
      if (item.children && item.children.length > 0) {
        montageRoutes(attr.children, item.children)
      }
      routes.push(attr)
    })
  }
  montageRoutes(localRoutes, files)
  localRoutes.sort((a, b) => {
    const aNum = a.sort || localRoutes.findIndex(route => route.name === a.name)
    const bNum = b.sort || localRoutes.findIndex(route => route.name === b.name)
    return aNum - bNum
  })
  return localRoutes
}

/**
 * 过滤出合适的菜单
 */
export function menuFilter(data) {
  const menus = []
  const internalFun = (data, menuRoutes) => {
    data.forEach(item => {
      if (!item.hidden) {
        const children = []
        if (item.children && item.children.length > 0) {
          internalFun(item.children, children)
          if (children.length > 0) {
            menuRoutes.push({
              ...item,
              children
            })
          }
        } else if (item.component) {
          menuRoutes.push({
            ...item,
            children: undefined
          })
        }
      }
    })
  }
  internalFun(data, menus)
  return menus
}

export function composeRoutes(data) {
  const routes = []
  let topLevelName = ''
  const montageRoutes = (routes, arr) => {
    arr.forEach(item => {
      const attr = {
        path: item.url,
        name: item.aliasName,
        meta: {
          icon: item.icon,
          title: item.name,
          highlightName: item.highlightName || undefined,
          keepAlive: item.otherMeta ? item.otherMeta.keepAlive : undefined
        },
        component: () => import('@/views' + item.componentName),
        children: [],
        sort: item.sort,
        hidden: item.hidden
      }
      if (item.redirectName || item.redirectPath) {
        attr.redirect = item.redirectName ? { name: item.redirectName } : item.redirectPath
      }
      if (item.functionCodeVoList && item.functionCodeVoList.length > 0) {
        // 当没有配置部分list时，重写redirect
        const name = getDisplayableMenuName(item)
        if (name) {
          attr.redirect = { name: name }
        }
        const ifUniqueChild = getIfUniqueChild(item)
        if (!topLevelName && ifUniqueChild) {
          topLevelName = item.aliasName
        }
        montageRoutes(attr.children, item.functionCodeVoList)
      } else {
        if (topLevelName) {
          attr.meta.highlightName = topLevelName
          topLevelName = ''
        }
      }
      routes.push(attr)
    })
    routes.sort((a, b) => {
      const aNum = a.sort || routes.findIndex(route => route.name === a.name)
      const bNum = b.sort || routes.findIndex(route => route.name === b.name)
      return aNum - bNum
    })
  }
  montageRoutes(routes, data)
  return routes
}

function getDisplayableMenuName(data) {
  if (data.functionCodeVoList && data.functionCodeVoList.length > 0) {
    const list = data.functionCodeVoList.filter(item => !item.hidden)
    for (const item of list) {
      const res = getDisplayableMenuName(item)
      if (res) {
        return res
      }
    }
    return null
  } else {
    return data.hidden ? null : data.aliasName
  }
}

function getIfUniqueChild(data) {
  if (data.functionCodeVoList?.length) {
    const list = data.functionCodeVoList.filter(item => !item.hidden)
    const num = list.length
    if (num === 0 || num > 1) {
      return false
    } else {
      return getIfUniqueChild(list[0])
    }
  } else {
    return true
  }
}
