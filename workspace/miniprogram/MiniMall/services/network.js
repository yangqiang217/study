import {
    baseUrl
} from './config.js'

export default function request(options) {
    return new Promise((resolve, reject) => {
        wx.request({
            url: baseUrl + options.url,
            method: options.method || 'GET', 
            data: options.data || {},
            success: resolve,//resolve本身是回调函数
            fail: reject
          })
    })
};