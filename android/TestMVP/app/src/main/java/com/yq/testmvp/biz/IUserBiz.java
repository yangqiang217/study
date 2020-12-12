package com.yq.testmvp.biz;

/**
 * Created by yq on 17-2-7.
 */

public interface IUserBiz {

    public void login(String username, String password, OnLoginListener loginListener);
}
