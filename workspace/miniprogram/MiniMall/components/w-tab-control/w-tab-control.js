// components/mytab/mytab.js
Component({
    /**
     * Component properties
     */
    properties: {
        titles: {
            type: Array,
            value: []
        }
    },

    /**
     * Component initial data
     */
    data: {
        currIndex: 0
    },

    /**
     * Component methods
     */
    methods: {
        onTabItemClick(event) {
            const clickedIdx = event.currentTarget.dataset.index;
            this.setData({
                currIndex: clickedIdx
            })

            this.triggerEvent("tabItemClick", { idx: clickedIdx, title: this.properties.titles[clickedIdx] }, {})
        }
    }
})
