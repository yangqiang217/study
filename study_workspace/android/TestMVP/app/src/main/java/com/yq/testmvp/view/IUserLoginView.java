package com.yq.testmvp.view;

import com.yq.testmvp.bean.User;

/**
 * Created by yq on 17-2-7.
 */

public interface IUserLoginView {

    String getUserName();
    String getPassword();
    void clearUserName();
    void clearPassword();
    void showLoading();
    void hideLoading();
    void toMainActivity(User user);
    void showFailedError();
}
