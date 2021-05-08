// pages/home/home.js
import request from "../../services/network.js"//不能写相对路劲

const app = getApp()
console.log('globalName: ' + app.globalData.globalName)
Page({

    /**
     * Page initial data
     * 里面数据声明必须有初始化，不能只写个name
     */
    data: {
        name: app.globalData.globalName,
        counter: 0,
        list: [],
        imagePath: '',

        cities: ['ny', 'london', 'tokyo']
    },

    onPlusClick() {
        //这种方式不会改变界面显示的counter
        // this.data.counter++
        // console.log(this.data.counter)

        this.setData({
            counter: this.data.counter + 1//：后面必须用this.data.counter而不是直接counter
        })
    },

    /**
     * 关于event：https://developers.weixin.qq.com/miniprogram/dev/framework/view/wxml/event.html#%E4%BA%8B%E4%BB%B6%E8%AF%A6%E8%A7%A3
     */
    onGetUserInfo(event) {
        console.log(event)
    },

    onImageLoad() {
        console.log("image load success")
    },

    onSelectImage() {
        wx.chooseImage({
            complete: (res) => {
                this.setData({
                    imagePath: res.tempFilePaths[0]
                })
            },
        })
    },

    onInput(event) {
        console.log("onInput" + event.detail.value)
    },

    onFoucs() {
        console.log("onFoucs" + event)
    },

    onScroll(event) {
        console.log("onScroll: " + event.detail)
    },

    onEventPassClick(event) {
        const dataset = event.currentTarget.dataset;
        const city = dataset.item;
        const index = dataset.index;
        console.log("onEventPassClick, city: " + city + ", index: " + index)
    },

    //事件传递
    handleEventPassCapture1(event) {
        console.log("handleEventPassCapture1")
    },
    handleEventPassTap1(event) {
        console.log("handleEventPassTap1")
    },
    handleEventPassCapture2(event) {
        console.log("handleEventPassCapture2")
    },
    handleEventPassTap2(event) {
        console.log("handleEventPassTap2")
    },
    handleEventPassCapture3(event) {
        console.log("handleEventPassCapture3")
    },
    handleEventPassTap3(event) {
        console.log("handleEventPassTap3")
    },

    handleClickInCpn(event) {
        //取出event传过来的参数
        console.log("data from cpn: " + event.detail.name)
        this.setData({
            counter: this.data.counter + 1
        })
    },

    handleTabItemClick(event) {
        console.log("handleTabItemClick: " + event.detail.idx + ", " + event.detail.title)
    },

    handleChangeDataInCpn() {
        //最终目的：修改my-cpn中的counterChangedByCaller
        //主要是拿到组件这个对象，调用它的方法
        //selectComponent参数是class或者id，推荐id
        const cpn = this.selectComponent("#my-cpn")//id拿
        // const cpn = this.selectComponent(".XXX")//class拿

        //这种方式不是不行，但直接调别人的setData方法不好
        // cpn.setData({
        //     counterChangedByCaller: cpn.data.counterChangedByCaller + 1
        // })

        cpn.incrementCounter(2)
    },

    /**
     * Lifecycle function--Called when page load
     * 1
     */
    onLoad: function (options) {
        console.log('home onLoad')
        //1.用原生方式请求
        // this.requestByWx()
        //2.用封装的方式请求
        request({
            url: "http://192.168.31.15:9999/coverlist/0"
        }).then(res => {
            console.log("request then：")
            console.log(res)
        }).catch(err => {
            console.log("request catch" + err)
        })
    },

    requestByWx() {
        //必须在小程序控制后台配置，或者在detail里设置不要校验域名，文档见：指南-基础能力-网络-使用说明
        wx.request({
            url: 'http://192.168.31.15:9999/coverlist/0',
            // dataType: "json",//默认是json
            // method: "POST",//默认get
            // data: {//get和post的参数都能写在这，get的也可以直接拼在url里
            //     param1: "param1",
            //     param2: 2
            // },
            success: (res) => {
                console.log(res)
                this.setData({
                    list: res.data.coverlist
                })
            },
            fail: (error) => { },
            complete: () => { }
        })
    },

    onPageScroll: function (obj) {
        console.log('onPageScroll' + obj)
    },

    /**
     * Lifecycle function--Called when page is initially rendered
     * 3
     */
    onReady: function () {
        console.log('home onReady')
    },

    /**
     * Lifecycle function--Called when page show
     * 2
     */
    onShow: function () {
        console.log('home onShow')
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
    onShareAppMessage: function (options) {
        return {
            title: 'title',
            path: '/pages/my/my',//打开后进入哪个页面
            imageUrl: '/assets/tabbar/tabbar_icon_home.png'//如果不指定，就是当前分享界面的截图
        }
    }
})