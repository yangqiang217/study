// components/w-back-top/w-back-top.js
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

    },

    /**
     * Component methods
     */
    methods: {
        handleBackTop() {
            wx.pageScrollTo({
                scrollTop: 0
            })
        }
    }
})
