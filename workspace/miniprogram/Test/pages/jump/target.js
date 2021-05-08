// pages/jump/target.js
Page({

    /**
     * Page initial data
     */
    data: {

    },

    handleBackByCode() {
        wx.navigateBack({
            // delta: 2//返回多少层，非必须
        })
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        //页面跳转传递的参数就在options里
        console.log(options)
    },

    /**
     * Lifecycle function--Called when page unload
     */
    onUnload: function () {
        //假如返回修改from页面的title字段 
        const pages = getCurrentPages();
        const formerPage = pages[pages.length - 2];
        formerPage.setData({
            title: "changed by next page"
        })
    },
})