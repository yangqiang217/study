import {
    getBaseUrl
} from "./config.js"

export default function request(options) {
    return new Promise((resolve, reject) => {
        wx.request({
            url: getBaseUrl() + options.url,
            method: options.method || "GET",
            data: options.data || {},
            success: resolve,
            fail: reject
        });
    });
}