import axios from 'axios'
import {Message} from "element-ui"

export default ({app}) => {
    axios.defaults.baseURL = process.env.contextPath;

    axios.interceptors.request.use(function (config) {
        if (config.method === 'get') {
            config.url = encodeURI(config.url);
        }
        return config;
    }, function (error) {
        Message.error("请求不可用");
        return Promise.reject(error);
    });

    axios.interceptors.response.use(function ({data}) {
        if (data instanceof Blob) {
            return data
        } else {
            const code = data.code;
            if (code === 0) {
                return data.data;
            } else {
                Message.error(data.msg);
                return Promise.reject(data.errorCode);
            }
        }
    }, function ({response}) {
        const data = response.data;
        Message.error(data.msg);
        return Promise.reject(data.errorCode);
    });
}
