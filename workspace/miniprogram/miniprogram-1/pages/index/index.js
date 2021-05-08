//index.js
//获取应用实例
const app = getApp()
var util = require('../../utils/util.js')

var circleCount = 0;  
Page({
  data: {
    time: ''
  },

  onLoad: function () {
    var date = util.formatTime(new Date());
    // console.log(date);
    date = date.replace(/\//g, '.');
    this.setData({
      time: date
    })

    var anim = wx.createAnimation({
      delay: 0,
      duration: 700,
      timingFunction: 'linear', 
    })
    
    setInterval(function() {  
      if (circleCount % 2 == 0) {  
        anim.scale(0.8).step();  
      } else {  
        anim.scale(1).step();  
      }  
        
      this.setData({  
        ani: anim.export()  //输出动画
      });  
        
      circleCount++;  
      if (circleCount == 1000) {  
        circleCount = 0;  
      }  
    }.bind(this), 700);
  }
})
