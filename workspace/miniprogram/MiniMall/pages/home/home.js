// pages/home/home.js
import {
    getMultiData,
    getGoodsData
} from '../../services/home.js'

const types = ['pop', 'new', 'sell']
const TOP_DISTANCE = 1000

Page({

    /**
     * Page initial data
     */
    data: {
        banners: [],
        recommends: [],
        titles: ['流行', '新款', '精选'],

        goods: {
            'pop': {
                page: 0,
                list: []
            },
            'new': {
                page: 0,
                list: []
            },
            'sell': {
                page: 0,
                list: []
            }
        },

        currentType: 'pop',

        showBackTop: false,

        isTabFixed: false,
        tabScrollTop: 0
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        this._getMultiData()
        this._getGoodsData('pop')
        this._getGoodsData('new')
        this._getGoodsData('sell')
    },

    _getMultiData() {
        getMultiData()
            .then(res => {
                const banners = res.data.data.banner.list;
                const recommends = res.data.data.recommend.list;
                this.setData({
                    banners,//es6增强写法，可以不写:XX 
                    recommends
                })
            }).catch(res => {

        })
    },

    _getGoodsData(type) {
        //！！！！此接口作者改了
        const page = this.data.goods[type].page + 1;

        getGoodsData(type, page)
            .then(res => {
                const list = res.data.data.list;

                const oldList = this.data.goods[type].list;
                oldList.push(...list);//遍历加入
                const typeKey = 'goods.${type}.list';
                const pageKey = 'goods.${type}.page';
                this.setData({
                    [typeKey]: oldList,
                    [pageKey]: page
                })
            }).catch(res => {

        })
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
        this._getGoodsData(this.data.currentType)
    },

    /**
     * Called when user click on the top right corner to share
     */
    onShareAppMessage: function () {

    },

    onPageScroll: function (options) {
        const scrollTop = options.scrollTop;
        const flag = scrollTop >= TOP_DISTANCE;
        if (flag != this.data.showBackTop) {
            this.setData({
                showBackTop: scrollTop >= TOP_DISTANCE
            })
        }

        const flag2 = scrollTop >= this.data.tabScrollTop;
        if (flag2 != this.data.isTabFixed) {
            this.setData({
                isTabFixed: flag2
            })
        }
    },

    handleTabClick(event) {
        // console.log(event.detail.idx)
        this.setData({
            currentType: types[event.detail.idx]
        })
    },

    handleImageLoad() {
        //得到banner下面网络图片的高度
        wx.createSelectorQuery().select('#tab-control').boundingClientRect(rect => {
            this.data.tabScrollTop = rect.top
        }).exec();
    }
})