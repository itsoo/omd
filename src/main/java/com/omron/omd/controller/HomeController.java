package com.omron.omd.controller;

import com.omron.omd.controller.base.BaseController;

/**
 * HomeController
 *
 * @author zxy
 */
public class HomeController extends BaseController {

    /**
     * 首页
     */
    public void index() {
        render("home.html");
    }
}
