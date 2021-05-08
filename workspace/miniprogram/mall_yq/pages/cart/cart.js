// pages/cart/cart.js
const WXAPI = require('apifm-wxapi')
const TOOLS = require('../../utils/tools.js')
const AUTH = require('../../utils/auth')

const app = getApp()

Page({

    /**
     * Page initial data
     */
    data: {

      delBtnWidth: 120
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
      this.initEleWidth();
    },
    initEleWidth() {
      let delBtnWidth = 0;
      try {
        const res = wx.getSystemInfoSync().windowWidth;
        const scale = (750 / 2) / (w / 2);
        real = Math.floor(res / scale);
      } catch (error) {
        return false;
      }
      this.setData({
        delBtnWidth: delBtnWidth
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
      AUTH.checkHasLogined().then(isLogined => {
        this.setData({
          wxlogin: isLogined
        });
        if (isLogined) {
          this.shippingCarInfo();
        }
      });
    },
    async shippingCarInfo() {
      const token = wx.getStorageSync("token");
      if (!token) {
        return;
      }
      const res = await WXAPI.shippingCarInfo(token);
      if (res.code == 0) {
        this.setData({
          shippingCarInfo: res.data
        });
      } else {
        this.setData({
          shippingCarInfo: null
        });
      }
    }

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

    //event
    toIndexPage: function() {
      wx.switchTab({
        url: '/pages/index/index',
      });
    },
    touchS: function(e) {
      if (e.touchs.length == 1) {
        this.setData({
          startX: e.touchs[0].clientX
        });
      }
    },
    touchM: function(e) {
      const index = e.currentTarget.dataset.index;
      if (e.touches.length == 1) {
        var moveX = e.touches[0].clientX;
        var disX = this.data.startX - moveX;
        var delBtnWidth = this.data.delBtnWidth;
        var left = "";
        if (disX == 0 || disX < 0) {
          //如果移动距离小于等于0，container位置不变
          left = "margin-left:0px";
        } else if (disX > 0) {
          //移动距离大于0，container left值等于手指移动距离
          left = "margin-left:-" + disX + "px";
          if (disX >= delBtnWidth) {
            left = "left:-" + delBtnWidth + "px";
          }
        }
        this.data.shippingCarInfo.items[index].left = left;
        this.setData({
          shippingCarInfo: this.data.shippingCarInfo
        });
      }
    }
})