/*
 * @Author: caszhou
 * @Date: 2020-06-16 09:34:45
 * @Description: 用户中台
 */
import Config from '@/config'
import Http from '@/utils/http'

// sso-idp-server
const idpHttp = new Http(Config.associationServices[1].url)

/**
 * 退出登陆
 */
export function logout() {
  return idpHttp.POST('/logout')
}

/**
 * 获取账号信息 (项目初始化数据调用)
 */
export function accountByAccess(params) {
  return idpHttp.GET('/account/by/access', params)
}

/**
 * 是否有更多的应用 (项目初始化数据调用)
 */
export function accountAccessHaveMore(params) {
  return idpHttp.POST('/account/access/have/more', params)
}

/**
 * 获取主体的资源列表
 */
export function functionCodeAccessList() {
  return idpHttp.POST('/function/code/access/list')
}

/**
 * 获取账号的资源列表 (项目初始化数据调用)
 */
export function functionCodeAccessListByAccount() {
  return idpHttp.POST('/function/code/access/list/by/account')
}

/**
 * 组织架构(人员组织) 部分
 */

/**
 * 组织架构树
 */
export function orgGet(params) {
  return idpHttp.POST('/org/access/get', params)
}

/**
 * 组织架构 - 新增
 */
export function orgAdd(params) {
  return idpHttp.POST('/org/add', params)
}

/**
 * 组织架构 - 编辑
 */
export function orgUpdate(params) {
  return idpHttp.POST('/org/access/update', params)
}

/**
 * 组织架构 - 删除
 */
export function orgDelete(params) {
  return idpHttp.POST('/org/delete', params)
}

/**
 * 人员分页
 */
export function accountByOrgsGet(params) {
  return idpHttp.POST('/account/byOrgs/get', params)
}

/**
 * 人员删除
 */
export function accountDelete(params) {
  return idpHttp.POST('/account/delete', params)
}

/**
 * 人员启用禁用
 */
export function accountLock(params) {
  return idpHttp.POST('/account/lock', params)
}

/**
 * 人员的账号密码重置
 */
export function accountPasswordReset(params) {
  return idpHttp.POST('/account/password/reset', params)
}

/**
 * 人员 - 新增 / 编辑
 */
export function accountAddOrUpdate(params) {
  return idpHttp.POST('/account/access/add/or/update', params)
}

/**
 * 人员 - 查询
 */
export function accountSelect(params) {
  return idpHttp.POST('/account/select/with/role', params)
}

/**
 * 角色分页(关联角色)
 */
export function roleAccountSelectByCompany(params) {
  return idpHttp.POST('/role/access/selectByCompany', params)
}

/**
 * 角色 启用 / 禁用
 */
export function roleLock(params) {
  return idpHttp.POST('/role/lock', params)
}

/**
 * 角色删除
 */
export function roleAccessDelete(params) {
  return idpHttp.POST('/role/access/delete', params)
}

/**
 * 角色 - 新增
 */
export function roleAdd(params) {
  return idpHttp.POST('/role/add', params)
}

/**
 * 角色 - 编辑
 */
export function roleUpdate(params) {
  return idpHttp.POST('/role/access/update/only', params)
}

/**
 * 角色 - 查询
 */
export function roleSelectOne(params) {
  return idpHttp.POST('/role/select/one', params)
}

/**
 * 角色 - 权限更新
 */
export function roleAccessUpdate(params) {
  return idpHttp.POST('/role/access/update', params)
}

/**
 * 账号更新
 */
export function accountOnlyUpdate(params) {
  return idpHttp.POST('/account/only/update', params)
}

/**
 * 发送验证码
 */
export function getPhoneCode(params) {
  return idpHttp.POST('/login/phone/verification/code/get', params)
}

/**
 * 修改账号
 */
export function accountPhoneUpdate(params) {
  return idpHttp.POST('/account/phone/update', params)
}

/** 忘记密码 - 修改密码 */
export function passwordUpdate(params) {
  return idpHttp.POST('/account/password/update', params)
}

/** 应用中心 - 应用列表 */
export function applicationAll(params) {
  return idpHttp.GET('/application/all', params)
}

/** 应用中心 - 应用新增 */
export function applicationAdd(params) {
  return idpHttp.POST('/application/add', params)
}

/** 应用中心 - 应用编辑 */
export function applicationUpdate(params) {
  return idpHttp.POST('/application/update', params)
}

/** 应用中心 - 应用查询 */
export function applicationById(params) {
  return idpHttp.POST('/application/by/id', params)
}

/** 应用中心 - 应用删除 */
export function applicationDelete(params) {
  return idpHttp.POST('/application/delete', params)
}

/** 应用中心 - 获取资源树 */
export function functionCodeList(params) {
  return idpHttp.POST('/function/code/by/app', params)
}

/** 应用中心 - 资源排序 */
export function functionCodeUpdateOrdinal(params) {
  return idpHttp.POST('/function/code/update/ordinal', params)
}

/** 应用中心 - 资源新增 */
export function functionCodeAdd(params) {
  return idpHttp.POST('/function/code/add', params)
}

/** 应用中心 - 资源编辑 */
export function functionCodeUpdate(params) {
  return idpHttp.POST('/function/code/update', params)
}

/** 应用中心 - 资源查询 */
export function functionCodeById(params) {
  return idpHttp.POST('/function/code/by/id', params)
}

/** 应用中心 - 资源删除 */
export function functionCodeDelete(params) {
  return idpHttp.POST('/function/code/delete', params)
}

/** 应用中心 - 产品列表 */
export function productList(params) {
  return idpHttp.POST('/product/list', params)
}

/** 应用中心 - 产品删除 */
export function productDelete(params) {
  return idpHttp.POST('/product/delete', params)
}

/** 应用中心 - 产品新增 */
export function productAdd(params) {
  return idpHttp.POST('/product/add', params)
}

/** 应用中心 - 产品编辑 */
export function productUpdate(params) {
  return idpHttp.POST('/product/update/functionCode/reset', params)
}

/** 应用中心 - 产品详情 */
export function productDetail(params) {
  return idpHttp.POST('/product/detail', params)
}

/** 应用中心 - 服务列表 */
export function serviceList(params) {
  return idpHttp.POST('/service/list', params)
}

/** 应用中心 - 服务删除 */
export function serviceDelete(params) {
  return idpHttp.POST('/service/delete', params)
}

/** 应用中心 - 服务新增 */
export function serviceAdd(params) {
  return idpHttp.POST('/service/add', params)
}

/** 应用中心 - 服务编辑 */
export function serviceUpdate(params) {
  return idpHttp.POST('/service/update/product/reset', params)
}

/** 应用中心 - 服务详情 */
export function serviceDetail(params) {
  return idpHttp.POST('/service/detail', params)
}

/** 应用中心 - 已签约商户 */
export function serviceCompanySigningList(params) {
  return idpHttp.POST('/company/signing/list', params)
}

/** 应用中心 - 签约 */
export function serviceSigning(params) {
  return idpHttp.POST('/service/signing', params)
}
