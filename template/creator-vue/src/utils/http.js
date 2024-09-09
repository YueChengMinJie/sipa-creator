/**
 * HTTP封装类
 */
import Axios from 'axios'
import Config from '@/config'
import Qs from 'qs'
import Store from '@/store'
import Cookies from 'js-cookie'
import { Notification } from 'element-ui'

export default class Http {
  /**
   * 构造函数
   */
  constructor(baseURL) {
    this.service = Axios.create({
      baseURL,
      timeout: Config.httpTimeOut
    })
    this.cancelToken = Axios.CancelToken
    this.interceptorsRequest()
    this.interceptorsResponse()
  }

  /**
   * 请求方式实现
   */
  async httpRequest(options) {
    const userInfo = Store.state.userInfo
    const params = {
      url: options.url,
      method: options.method,
      headers: {
        'Content-Type': options.ifFormData ? 'application/x-www-form-urlencoded' : 'application/json',
        [Config.headerRequestKey]: Config.headerRequestId(),
        [Config.headerFromKey]: Config.headerFromId,
        [Config.merchantId]: userInfo ? userInfo.loginCompanyId : undefined
      },
      cancelToken: new this.cancelToken(cancel => {
        if (options.cancel) {
          options.cancel(cancel)
        }
      }),
      params: undefined,
      data: undefined,
      onUploadProgress: options.onUploadProgress
    }
    if (options.data?.token) {
      params.headers[Config.headerKey] = `${Config.headerPrefix}${options.data.token}`
      delete options.data.token
    } else {
      params.headers[Config.headerKey] = `${Config.headerPrefix}${Store.getters.getToken}`
    }
    /**
     * 这里对小写的get,post,put,delete方法进行业务封装，如果需要采用原生方法，可采用大写
     */
    const methodMap = {
      get: () => (params.params = options.data),
      post: () => (params.data = options.ifFormData ? Qs.stringify(options.data) : options.data),
      put: () => (params.data = options.ifFormData ? Qs.stringify(options.data) : options.data),
      delete: () => (options.ifFormData ? (params.params = options.data) : (params.data = options.data))
    }
    const methodFn =
      methodMap[options.method] ??
      (() => {
        params.params = options.params
        params.data = options.data
      })
    methodFn()
    const data = await this.service(params)
    return data
  }

  /**
   * GET快速请求
   */
  GET(url, data, ifFormData = false, cancel = () => {}) {
    return this.httpRequest({ url, method: 'get', data, ifFormData, cancel })
  }

  /**
   * GET快速请求
   */
  POST(url, data, ifFormData = false, cancel = () => {}) {
    return this.httpRequest({ url, method: 'post', data, ifFormData, cancel })
  }

  /**
   * GET快速请求
   */
  PUT(url, data, ifFormData = false, cancel = () => {}) {
    return this.httpRequest({ url, method: 'put', data, ifFormData, cancel })
  }

  /**
   * GET快速请求
   */
  DELETE(url, data, ifFormData = false, cancel = () => {}) {
    return this.httpRequest({ url, method: 'delete', data, ifFormData, cancel })
  }

  /**
   * 请求拦截器
   */
  interceptorsRequest() {
    this.service.interceptors.request.use(
      response => {
        return response
      },
      error => {
        return Promise.reject(error)
      }
    )
  }

  /**
   * 响应拦截器
   */
  interceptorsResponse() {
    this.service.interceptors.response.use(
      response => {
        const data = response.data
        // 如果是业务逻辑,则解构返回里面一层
        if (data?.errorCode) {
          if (Config.httpCodeSuccess === data.errorCode) {
            return data
          } else {
            Notification({
              showClose: true,
              title: '错误',
              dangerouslyUseHTMLString: true,
              message:
                Config.env !== 'prod'
                  ? `<code class='app-code'>${response.config.url}</code> <br/> <b>异常信息：</b>${data.msg}`
                  : data.msg,
              type: 'error'
            })
            return Promise.reject(data)
          }
        } else {
          return response
        }
      },
      error => {
        if (String(error) === 'Cancel') {
          return Promise.reject('请求已主动中断')
        }
        if (error.response !== undefined && error.response.status !== undefined) {
          // 获取非法登陆的标记, 防止清楚token后, 出现一样的报错
          const illegalFlag = Cookies.get('illegal-flag')
          // 如果是业务逻辑报错
          const response = error.response.data
          if (Config.httpCodeLoginError.includes(response?.errorCode)) {
            Store.commit('removeToken')
            if (!illegalFlag) {
              const timeStamp = Date.now() + 1500
              // 设置一个非法登陆的标记
              Cookies.set('illegal-flag', timeStamp, { expires: new Date(timeStamp) })
            }
            setTimeout(() => {
              location.href = Config.loginUrl
            }, 1500)
          }
          let errMsg = error.message
          if (response?.errorCode && response?.msg) {
            errMsg = response.msg
          }
          if (!illegalFlag) {
            Notification({
              showClose: true,
              title: '错误',
              dangerouslyUseHTMLString: true,
              message:
                Config.env !== 'prod'
                  ? `<code class='app-code'>${error.config.url}</code> <br/> <b>异常信息：</b>${errMsg}`
                  : errMsg,
              type: 'error'
            })
          }
          return Promise.reject(error)
        } else {
          Notification({
            showClose: true,
            title: '错误',
            message: error.message,
            type: 'error'
          })
          return Promise.reject(error)
        }
      }
    )
  }
}
