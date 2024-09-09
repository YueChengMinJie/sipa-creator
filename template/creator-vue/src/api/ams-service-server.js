/*
 * @Author: caszhou
 * @Date: 2023-06-21 10:12:41
 * @Description: 管理平台
 */
import Config from '@/config'
import Http from '@/utils/http'

const baseHttp = new Http(Config.associationServices[0].url)

/**
 * 省市区字典
 */
export function pccTree(params) {
  return baseHttp.POST('/province/tree/children/three', params)
}
