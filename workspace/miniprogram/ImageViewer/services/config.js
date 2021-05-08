import { isEmpty } from "../utils/stringUtils";

const SCREEN_W = 1080;
const COLUMN_COUNT = 3;

const COVER_LIST_URL = "/coverlist/0/" + (SCREEN_W/*必须放在前面 */ / COLUMN_COUNT);
const SINGLE_IMG_URL = "/image/0/"
const DETAIL_LIST_URL = "/detaillist/0/" + SCREEN_W;

const KEY_BASE_URL = "BASE_URL";
let baseUrl = "";

export {
    SCREEN_W as SCREEN_W,
    COLUMN_COUNT as COLUMN_COUNT,

    COVER_LIST_URL as COVER_LIST_URL,
    SINGLE_IMG_URL as SINGLE_IMG_URL,
    DETAIL_LIST_URL as DETAIL_LIST_URL
}

export function getBaseUrl() {
    if (isEmpty(baseUrl)) {
        let bu = wx.getStorageSync(KEY_BASE_URL);
        if (isEmpty(bu)) {
            baseUrl = "http://192.168.1.27:9999";
            wx.setStorage({
                data: baseUrl,
                key: KEY_BASE_URL,
            })
        } else {
            baseUrl = bu;
        }
    }
    
    return baseUrl;
}
export function setBaseUrl(newBaseUrl) {
    if (baseUrl == newBaseUrl) {
        return;
    }
    baseUrl = newBaseUrl;
    wx.setStorage({
        data: baseUrl,
        key: KEY_BASE_URL,
    })
}