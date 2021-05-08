// pages/wxml/wxml.js
Page({

    /**
     * Page initial data
     */
    data: {
        firstName: "y",
        lastName: "q",
        age: 20,
        timeNow: new Date().toLocaleString(),
        changeColor: false,

        isShow: true,

        twoDArray: [
            [1, 2],
            [4, 5]
        ]
    },

    onChangeColor() {
        this.setData({
            changeColor: !this.data.changeColor
        })
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        setInterval(() => {
            this.setData({
                timeNow: new Date().toLocaleString()
            })
        }, 1000);
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

    }
})