// components/my-waterfall-view/my-waterfall-view.js
import { COLUMN_COUNT } from '../../services/config.js';

let leftList = new Array();
let middleList = new Array();
let rightList = new Array();
let leftHeight = 0, middleHeight = 0, rightHeight = 0, 
    itemWidth = 0;

Component({
    /**
     * Component properties
     */
    properties: {

    },

    /**
     * Component initial data
     */
    data: {
        leftList: [],
        middleList: [],
        rightList: [],

        //如果只有两列，flex-grow导致两列平分了不好看
        fullColumn: false
    },

    attached: function() {
        wx.getSystemInfo({
            success: (res)=>{
                let percentage = 750 / res.windowWidth;
                let margin = 2 / percentage;//css里middle和right各一个margin
                itemWidth = (res.windowWidth - margin) / COLUMN_COUNT;
            }
        });
    },

    /**
     * Component methods
     */
    methods: {
        onItemClick: function(e) {
            const clickedItem = e.currentTarget.dataset.clickeditem;
            this.triggerEvent("coverclick", clickedItem);
        },

        fillData: function(refresh, newDataList) {
            if (refresh) {
                leftList.length = 0;
                middleList.length = 0;
                rightList.length = 0;

                leftHeight = 0;
                middleHeight = 0;
                rightHeight = 0;
            }
            for (let i = 0, len = newDataList.length; i < len; i++) {
                let coverItem = newDataList[i];

                coverItem.img_width = parseInt(coverItem.img_width);
                coverItem.img_height = parseInt(coverItem.img_height);

                coverItem.itemWidth = itemWidth;
                coverItem.itemHeight = itemWidth * coverItem.img_height / coverItem.img_width;
                
                
                if (leftHeight <= rightHeight && leftHeight <= middleHeight) {
                    leftList.push(coverItem);
                    leftHeight += coverItem.itemHeight;
                } else if (middleHeight < leftHeight && middleHeight <= rightHeight) {
                    middleList.push(coverItem);
                    middleHeight += coverItem.itemHeight;
                } else {
                    rightList.push(coverItem);
                    rightHeight += coverItem.itemHeight;
                }
            }

            this.setData({
                leftList: leftList,
                middleList: middleList,
                rightList: rightList,

                fullColumn: leftList.length != 0 && middleList.length != 0 && rightList.length != 0
            });
        }
    }
})
