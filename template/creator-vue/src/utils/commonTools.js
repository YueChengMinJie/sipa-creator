/*
 * @Author: caszhou
 * @Date: 2021-04-01 14:02:59
 * @Description: 常用公共方法(包括金额，时间，日期转换，对象操作，uuid, 颜色取反)
 */
import Big from 'big.js'
import DayJs from 'dayjs'

/**
 * 颜色取反, 返回传入相反的颜色
 * @param color 十六进制的颜色
 */
export function colorReverse(color) {
  color = '0x' + color.replace(/#/g, '')
  const str = '000000' + (0xffffff - color).toString(16)
  return '#' + str.substring(str.length - 6, str.length)
}

/**
 * UUID生成方法
 */
export function uuid() {
  let d = Date.now()
  if (window.performance && typeof window.performance.now === 'function') {
    d += performance.now()
  }
  const uuid = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'.replace(/[x]/g, function (c) {
    const r = (d + Math.random() * 16) % 16 | 0
    d = Math.floor(d / 16)
    return (c === 'x' ? r : (r & 0x3) | 0x8).toString(16)
  })
  return uuid
}

/**
 * 获取浏览器滚动条宽度
 */
export function getScrollBarWidth() {
  const outer = document.createElement('div')
  outer.style.overflow = 'scroll'
  outer.style.visibility = 'hidden'
  outer.style.width = '100px'
  outer.style.height = '100%'
  outer.style.position = 'absolute'
  outer.style.top = '-9999px'
  document.body.appendChild(outer)
  const widthNoScroll = outer.offsetWidth

  const inner = document.createElement('div')
  inner.style.width = '100%'
  outer.appendChild(inner)

  const widthWithScroll = inner.offsetWidth
  outer.parentNode.removeChild(outer)
  // 父元素出现滚动条，子元素无滚动条，父元素减去子元素的宽度就是滚动条宽度
  const scrollBarWidth = widthNoScroll - widthWithScroll

  return scrollBarWidth
}

/**
 * 根据id查询树形结构的node
 * @param {Array} tree 要递归的tree
 * @param {Number} targetId 要查询的id
 * @param {Array} children 子节点
 * @returns {Object}
 */
export function findNodeById(tree, targetId, children = 'children') {
  for (const node of tree) {
    if (node.id === targetId) return node
    if (node[children]) {
      const foundNode = findNodeById(node[children], targetId)
      if (foundNode) return foundNode
    }
  }
  return null
}

/**
 * 获取tree中某个id的位置后拼接关系， 默认拼接所有ID
 * @param {Array} tree 要递归的tree
 * @param {String} id 匹配的id
 * @param {String} findKey 要拼接的属性, 默认是id
 */
export function recursionFindKey(tree, id, findKey = 'id') {
  let ids = []
  for (const item of tree) {
    ids.push(item[findKey])
    if (item[findKey] === id) {
      return ids
    } else {
      if (item.children) {
        const data = recursionFindKey(item.children, id, findKey)
        if (data) {
          return ids.concat(data)
        }
      }
      ids = []
    }
  }
  return null
}

/**
 * 获取某个对象在tree中的位置的父元素节点
 * @param {Object} tree 当前树的父节点， 如果是第一层请嵌套一层, 之后用tree[0]写法
 * @param {String} nodeId 要查节点的id
 * @param {String} nodeKey nodeId所匹配的tree的key, 默认用id做比较
 * @param {String} children 遍历的children
 */
export function getNodeParentPosition(tree, nodeId, treeKey = 'id', children = 'children') {
  for (const item of tree[children]) {
    if (item[treeKey] === nodeId) {
      return tree
    } else if (item[children]) {
      const treeInfo = getNodeParentPosition(item, nodeId, treeKey, children)
      if (treeInfo) {
        return treeInfo
      }
    }
  }
  return null
}

/**
 * 快速给对象赋值 只会赋值a里面有的属性
 * @param {*} a 被赋值的对象
 * @param {*} b 数据源
 * @returns a
 */
export function objectDeepCopy(a, b) {
  for (let key in a) {
    if (Object.prototype.hasOwnProperty.call(b, key)) {
      if (typeof a[key] === 'object' && typeof b[key] === 'object') {
        if (Array.isArray(a[key]) && Array.isArray(b[key])) {
          a[key] = b[key].slice()
        } else {
          a[key] = objectDeepCopy(a[key], b[key])
        }
      } else {
        a[key] = b[key]
      }
    }
  }
  return a
}

/**
 * 深拷贝 拷贝源对象返回新对象
 * @param {Object} source 源对象
 * @param {Object} uniqueRefs 优化 用于处理循环引用的漏洞
 * @returns {Object}
 */
export function deepClone(source, uniqueRefs = new WeakMap()) {
  if (source === null || typeof source !== 'object') return source
  if (uniqueRefs.has(source)) {
    return uniqueRefs.get(source)
  }
  const targetObj = Array.isArray(source) ? [] : {}
  uniqueRefs.set(source, targetObj)
  for (const key in source) {
    if (Object.prototype.hasOwnProperty.call(source, key)) {
      targetObj[key] = deepClone(source[key], uniqueRefs)
    }
  }
  return targetObj
}

/**
 * @param {number} data 金额
 * @returns {string} 四舍五入后的结果
 */
export function filterPrice(data) {
  const num = new Big(data)
  return num.div(100).toFixed(2)
}

/**
 * @param {number} data 金额
 * @returns {string} 还原分
 */
export function restorePrice(data) {
  const num = new Big(data)
  return num.times(100).toNumber()
}

/**
 * 获取年月日数组
 */
export function dateArray(data) {
  if (!data) return
  const date = DayJs(data)
  const Y = date.format('YYYY') + '/'
  const M = date.format('MM') + '/'
  const D = date.format('DD')
  return [Y, M, D]
}

/**
 * 获取年月日日期
 */
export function dateDayStr(data) {
  if (!data) return
  const date = DayJs(data)
  const Y = date.format('YYYY') + '/'
  const M = date.format('MM') + '/'
  const D = date.format('DD')
  return Y + M + D
}

/**
 * 获取年月日时分秒详细日期
 */
export function dateTimeStr(data) {
  if (!data) return
  const date = DayJs(data)
  const Y = date.format('YYYY') + '/'
  const M = date.format('MM') + '/'
  const D = date.format('DD') + ' '
  const h = date.format('HH') + ':'
  const m = date.format('mm') + ':'
  const s = date.format('ss')
  return Y + M + D + h + m + s
}

// 解决级联选择器卡顿问题
export function handleCascaderStuck() {
  this.$nextTick(() => {
    const $el = document.querySelectorAll('.el-cascader-panel .el-cascader-node[aria-owns]')
    Array.from($el).map(item => item.removeAttribute('aria-owns'))
  })
}
