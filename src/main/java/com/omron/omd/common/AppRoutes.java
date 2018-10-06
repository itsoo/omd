package com.omron.omd.common;

import com.jfinal.config.Routes;
import com.omron.omd.controller.HomeController;
import com.omron.omd.controller.IndexController;
import com.omron.omd.controller.RoleController;
import com.omron.omd.controller.UserController;

/**
 * AppRoutes
 *
 * @author zxy
 */
class AppRoutes {

    void configRoute(Routes routes) {
        routes.add(new AdminRoutes());
    }

    class AdminRoutes extends Routes {

        @Override
        public void config() {
            setBaseViewPath(AppConst.BASE_VIEW_PATH);
            add("/", IndexController.class);
            add("/home", HomeController.class);
            add("/role", RoleController.class);
            add("/user", UserController.class);
        }
    }
}
