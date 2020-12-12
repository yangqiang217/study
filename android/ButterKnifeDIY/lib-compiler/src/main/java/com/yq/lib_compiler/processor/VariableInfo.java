package com.yq.lib_compiler.processor;

import javax.lang.model.element.VariableElement;

/**
 * Created by yangqiang on 05/02/2018.
 */

public class VariableInfo {
    int viewId;
    VariableElement mVariableElement;

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public VariableElement getVariableElement() {
        return mVariableElement;
    }

    public void setVariableElement(VariableElement variableElement) {
        mVariableElement = variableElement;
    }
}
