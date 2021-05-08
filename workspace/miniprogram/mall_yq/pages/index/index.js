//index.js
const WXAPI = require("apifm-wxapi")
const CONFIG = require("../../config.js")
//获取应用实例
const app = getApp()

Page({
  data: {
    banners: [],
    inputVal: "",
    categories: [],
    activeCategoryId: 0,
    goodsRecommend: [],
    pingtuanList: [],
    goods: [],

    curPage: 1,
    pageSize: 20,
  },

  onLoad: function () {
    this.initBanners();
    this.initNotice();
    this.initCategory();
    this.initRecGoods();
    this.initPingtuanGoods();
  },

  async initBanners() {
    const _data = {};
    // 读取头部轮播图
    const res1 = await WXAPI.banners({
      type: "index"
    });
    if (res1.code == 700) {
      wx.showModal({
        title: '提示',
        content: '请在后台添加 banner 轮播图片，自定义类型填写 index',
        showCancel: false
      });
    } else {
      _data.banners = res1.data;
    }

    // 读取首页广告位
    const res2 = await WXAPI.banners({
      type: "indexAD"
    });
    if (res2.code === 0) {
      _data.adInfo = res2.data[0];
    }
    this.setData(_data);
  },

  initNotice() {
    WXAPI.noticeList({pageSize: 5}).then(res => {
      if (res.code === 0) {
        this.setData({
          noticeList: res.data
        });
      }
    })
  },

  async initCategory() {
    const res = await WXAPI.goodsCategory();
    let categ = [];
    if (res.code === 0) {
      const _c = res.data.filter(ele => {
        return ele.level == 1;
      });
      categ = categ.concat(_c);
    }
    this.setData({
      categories: categ,
      activeCategoryId: 0,
      curPage: 1
    });
    this.getGoodsList(0);
  },
  async getGoodsList(categoryId, append) {
    if (categoryId == 0) {
      categoryId = "";
    }
    wx.showLoading({
      mask: true
    });
    const res = await WXAPI.goods({
      categoryId: categoryId,
      nameLike: this.data.inputVal,
      page: this.data.curPage,
      pageSize: this.data.pageSize
    });
    wx.hideLoading();
    if (res.code == 404 || res.code == 700) {
      let newData = {
        loadingMoreHidden: false
      };
      if (!append) {
        newData.goods = [];
      }
      this.setData(newData);
      console.log(this.data);
      
      return;
    }
    let goods = [];
    if (append) {
      goods = this.data.goods;
    }
    for (let i = 0; i < res.data.length; i++) {
      goods.push(res.data[i]);
    }
    this.setData({
      loadingMoreHidden: true,
      goods: goods,
    });
  },

  initRecGoods() {
    const that = this;
    WXAPI.goods({
      recommendStatus: 1
    }).then(res => {
      if (res.code === 0) {
        that.setData({
          goodsRecommend: res.data
        });
      }
    });
  },

  initPingtuanGoods() {
    WXAPI.goods({
      pingtuan: true
    }).then(res => {
      if (res.code === 0) {
        this.setData({
          pingtuanList: res.data
        });
      }
    });
  },

  //事件处理函数
  tapBanner: function(e) {
    const url = e.currentTarget.dataset.url;
    if (url) {
      wx.navitageTo({url});
    }
  },
  searchinput: function(e) {
    this.setData({
      inputVal: e.detail.value
    });
  },
  searchconfirm: function(e) {
    this.setData({
      inputVal: e.detail.value
    });
    wx.navitageTo({
      url: "page/goods/list?name=" + this.data.inputVal
    });
  },

  categoryItemClick: function(e) {
    wx.setStorageSync("_categoryId", e.currentTarget.id);
    wx.switchTab({
      url: '/page/category/category'
    });
  },

  couponClick: function(e) {
    wx.navitageTo({
      url: "/pages/coupons/index"
    });
  },

  toDetailsTap: function(e) {
    wx.navigateTo({
      url: "/pages/goods-details/index?id=" + e.currentTarget.dataset.id
    })
  },

  onReachBottom: function() {
    this.setData({
      curPage: this.data.curPage + 1
    });
    this.getGoodsList(this.data.activeCategoryId, true);
  },
  onPullDownRefresh: function() {
    this.setData({
      curPage: 1
    });
    this.getGoodsList(this.data.activeCategoryId);
    wx.stopPullDownRefresh();
  }
})
