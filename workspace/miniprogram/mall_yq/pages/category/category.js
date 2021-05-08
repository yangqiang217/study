// pages/category/category.js
const CONFIG = require('../../config.js')
const WXAPI = require('apifm-wxapi')
const AUTH = require('../../utils/auth')

Page({

    /**
     * Page initial data
     */
    data: {
      categories: [],
      categorySelected: {
        name: "",
        id: ""
      },
      currentGoods: [],
      onLoadStatus: true,
      scrolltop: 0
    },

    /**
     * Lifecycle function--Called when page load
     */
    onLoad: function (options) {

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
        if (isLogined) {
          this.setData({
            wxlogin: isLogined
          });
          TOOLS.showTabBarBadge();
        }
      });
      const _categoryId = wx.getStorageSync("_categoryId");
      wx.removeStorageSync("_categoryId");
      if (_categoryId) {
        this.data.categorySelected.id = _categoryId;
      } else {
        this.data.categorySelected.id = null;
      }
      this.categories();
    },

    async categories() {
      wx.showLoading({
        title: "loading"
      });
      const res = await WXAPI.goodsCategory();
      wx.hideLoading();
      let categories = [];
      let categoryName = "";
      let categoryId = "";
      if (res.code == 0) {
        if (this.data.categorySelected.id) {
          const _curCategory = res.data.find(ele => {
            return ele.id == this.data.categorySelected.id;
          });
          categoryName = _curCategory.name;
          categoryId = _curCategory.id;
        }
        for (let i = 0; i < res.data.length; i++) {
          let item = res.data[i];
          categories.push(item);
          if (i == 0 && !this.data.categorySelected.id) {
            categoryName = item.name;
            categoryId = item.id;
          }
        }
      }
      this.setData({
        categories: categories,
        categorySelected: {
          name: categoryName,
          id: categoryId
        }
      });
      this.getGoodsList();
    },
    async getGoodsList() {
      wx.showLoading({
        title: "loading"
      });
      const res = await WXAPI.goods({
        categoryId: this.data.categorySelected.id,
        page: 1,
        pageSize: 100000
      });
      wx.hideLoading();
      if (res.code == 700) {
        this.setData({
          currentGoods: null
        });
        return;
      }
      this.setData({
        currentGoods: res.data
      });
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

    //事件
    bindinput: function(e) {
      this.setData({
        inputVal: e.detail.value
      });
    },
    bindconfirm: function(e) {
      this.setData({
        inputVal: e.detail.value
      });
      wx.navigateTo({
        url: '/pages/goods/list?name=' + this.data.inputVal,
      })
    },

    toDetailsTap: function(e) {
      wx.navigateTo({
        url: "/pages/goods-details/index?id=" + e.currentTarget.dataset.id
      });
    },
    onCategoryClick: function(e) {
      const id = e.target.dataset.id;
      if (id === this.data.categorySelected.id) {
        this.setData({
          scrolltop: 0
        });
      } else {
        let categoryName = "";
        for (let i = 0; i < this.data.categories.length; i++) {
          let item = this.data.categories[i];
          if (item.id == id) {
            categoryName = item.name;
            break;
          }
        }
        this.setData({
          categorySelected: {
            name: categoryName,
            id: id
          },
          scrolltop: 0
        });
        this.getGoodsList();
      }
    }
})