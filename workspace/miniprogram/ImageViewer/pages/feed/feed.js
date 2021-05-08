// pages/feed/feed.js
import request from '../../services/networks.js'
import { COVER_LIST_URL, SINGLE_IMG_URL, getBaseUrl } from '../../services/config.js';
import { isEmpty } from '../../utils/stringUtils.js';


let coverlist = [];
let currPage = 0;
let waterFallView = {};
let during_search = false;//防止搜索结果列表滑到底部又请求分页了

Page({

    /**
     * Page initial data
     */
    data: {
        inputVal: "", // 搜索框内容
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        waterFallView = this.selectComponent("#water-fall");
        console.log("request cover");
        
        this._requestCovers(true, 0);
    },

    _requestCovers(refresh, targetPage) {
        request({
            url: COVER_LIST_URL,
            data: {
                page: targetPage
            }
        }).then(res => {
            currPage = targetPage;
            this._fillData(refresh, res.data.coverlist);
            wx.stopPullDownRefresh()
        }).catch(res => {
            this.showToast(res.errMsg);
            console.log(res);
        });
    },

    _fillData: function (refresh, newDataList, search=false) {
        for (let i = 0; i < newDataList.length; i++) {
            const item = newDataList[i];
            item.imgUrl = getBaseUrl() + SINGLE_IMG_URL + item.img_path;
        }

        if (!search) {
            if (refresh) {
                coverlist.length = 0;
            }
            coverlist.push.apply(coverlist, newDataList);
        }
        
        waterFallView.fillData(refresh, newDataList);

        this.showToast("get " + newDataList.length + " data, total: " + coverlist.length);
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
        this._requestCovers(true, 0);
    },

    /**
     * Called when page reach bottom
     */
    onReachBottom: function () {
        if (!during_search) {
            this._requestCovers(false, currPage + 1);
        }
    },

    onPageScroll: function (e) {
    },

    /**
     * Called when user click on the top right corner to share
     */
    onShareAppMessage: function () {

    },

    onInput(e) {
        this._search(e.detail.value);
        // console.log("input: " + e.detail.value);
    },
    onInputConfirm(e) {
        this._search(e.detail.value);
        // console.log("confirm: " + e.detail.value);
    },
    _search: function (searchKey) {
        if (isEmpty(searchKey)) {
            waterFallView.fillData(true, coverlist);
            during_search = false;
            return;
        }
        request({
            url: COVER_LIST_URL,
            data: {
                model_name: searchKey
            }
        }).then(res => {
            this._fillData(true, res.data.coverlist, true);
            during_search = true;
            this.showToast("searched " + res.data.coverlist.length + " results");
        }).catch(res => {
            console.log(res);
        });
    },

    onCoverClick(e) {
        wx.navigateTo({
            url: "/pages/detail/detail?group_name=" + e.detail.group_path
        })
    },


    showToast(msg) {
        wx.showToast({
            title: msg,
            icon: "none"
        })
    }
})