const TOKEN = 'token'

App({

  /**
   * 当小程序初始化完成时，会触发 onLaunch（全局只触发一次）
   */
  onLaunch: function () {
    console.log('app onlaunch')

    const token = wx.getStorageSync(TOKEN);
    if (token && token.lenth != 0) {
      //验证token过期
      wx.request({
        url: '验证token的接口',
        success: (res) => {
          if (!res.data.errorCode) {
            //token有效
            this.globalData.token = token;
          } else {
            //token过期
            this.login()
          }
        }
      })
    } else {
      this.login()
    }
  },

  login() {
    wx.login({
      success: (res) => {
        console.log("login succ")
        console.log(res.code)//code只有5min有效期

        const code = res.code
        //然后把code（最好和账号+密码）发送给自己的服务器
        wx.request({
          url: 'xx',
          data: {
            code: code
          },
          success: (res) => {
            const token = res.data.token;
            this.globalData.token = token;
            wx.setStorageSync(TOKEN, token);
          }
        })
      },
    })
  },

  /**
   * 当小程序启动，或从后台进入前台显示，会触发 onShow
   */
  onShow: function (options) {
    switch (options.scene) {//进入场景
      case 1001:
        break;
    }
  },

  /**
   * 当小程序从前台进入后台，会触发 onHide
   */
  onHide: function () {

  },

  /**
   * 当小程序发生脚本错误，或者 api 调用失败时，会触发 onError 并带上错误信息
   */
  onError: function (msg) {

  },

  globalData: {
    globalName: 'yq',
    globalAge: 18,

    //登录token
    token: ''
  }
})
