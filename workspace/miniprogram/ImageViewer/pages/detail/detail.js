// pages/detail/detail.js
import request from '../../services/networks.js'
import { DETAIL_LIST_URL, SINGLE_IMG_URL, getBaseUrl } from '../../services/config.js';
import { isEmpty } from '../../utils/stringUtils.js';

let itemWidth = 0;
Page({

    /**
     * Page initial data
     */
    data: {
        imgList: []
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {
        wx.getSystemInfo({
            success: (res)=>{
                itemWidth = res.windowWidth,
                this._requestImgs(options.group_name);
            }
        });
    },

    _requestImgs(group_name) {
        request({
            url: DETAIL_LIST_URL + "/" + group_name,
        }).then(res => {
            const detaillist = res.data.detaillist;
            // console.log(res.data.detaillist);
            for (let i = 0; i < detaillist.length; i++) {
                const item = detaillist[i];
                item.imgOriUrl = getBaseUrl() + SINGLE_IMG_URL + item.img_ori_path;
                item.imgThumbnailUrl = getBaseUrl() + SINGLE_IMG_URL + item.img_thumbnail_path;
                item.itemWidth = itemWidth;
                item.itemHeight = itemWidth * item.img_height / item.img_width;
            }
            this.setData({
                imgList: detaillist
            });
            // this._fillData(true, imgList);
            // console.log(imgList);
            
        }).catch(res => {
            console.log(res);
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

    onDetailClick(e) {
        const clickedItem = e.currentTarget.dataset.clickeditem;
        const oriUrls = [];
        for (let i = 0; i < this.data.imgList.length; i++) {
            const oriUrl = this.data.imgList[i].imgOriUrl;
            if (!isEmpty(oriUrl)) {
                oriUrls.push(oriUrl);
            }
        }
        wx.previewImage({
            current: clickedItem.imgOriUrl, // 当前显示图片的http链接
            urls: oriUrls // 需要预览的图片http链接列表
        })
    }
})