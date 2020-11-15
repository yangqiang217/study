package com.yq.testmvp.biz;

import com.yq.testmvp.bean.User;

/**
 * Created by yq on 17-2-7.
 */

public interface OnLoginListener {

    void loginSuccess(User user);
    void loginFailed();
}
