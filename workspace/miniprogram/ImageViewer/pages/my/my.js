// pages/my/my.js
import { KEY_BASE_URL, getBaseUrl, setBaseUrl } from '../../services/config.js';
import { isEmpty } from '../../utils/stringUtils.js';


Page({

    /**
     * Page initial data
     */
    data: {
        baseUrl: ""
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        this.setData({
            baseUrl: getBaseUrl()
        });
    },

    /**
     * Lifecycle function--Called when page is initially rendered
     */
    onReady: function () {

    },

    /**
     * Lifecycle function--Called when page show
     */
    onShow: function () {

    },

    /**
     * Lifecycle function--Called when page hide
     */
    onHide: function () {

    },

    /**
     * Lifecycle function--Called when page unload
     */
    onUnload: function () {

    },

    /**
     * Page event handler function--Called when user drop down
     */
    onPullDownRefresh: function () {

    },

    /**
     * Called when page reach bottom
     */
    onReachBottom: function () {

    },

    /**
     * Called when user click on the top right corner to share
     */
    onShareAppMessage: function () {

    },

    onBaseUrlInput(e) {
        this.setData({
            baseUrl: e.detail.value
        });
    },
    onBaseUrlConfirm(e) {
        setBaseUrl(this.data.baseUrl);
        wx.showToast({
            icon: "none",
            title: 'save success',
        })
    },
})