package com.omron.omd.common;

import com.jfinal.config.*;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.activerecord.tx.TxByMethodRegex;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.omron.omd.directive.AuthcDirective;
import com.omron.omd.interceptor.AuthcInterceptor;
import com.omron.omd.interceptor.LoginInterceptor;
import com.omron.omd.model._MappingKit;
import com.omron.omd.service.RoleService;

import java.sql.Connection;

/**
 * AppConfig
 *
 * @author zxy
 */
public class AppConfig extends JFinalConfig {

    /**
     * 配置常量
     */
    @Override
    public void configConstant(Constants constants) {
        PropKit.use("config.properties");
        constants.setDevMode(PropKit.getBoolean("devMode"));
        constants.setError403View("/views/error/403.html");
        constants.setError404View("/views/error/404.html");
        constants.setError500View("/views/error/500.html");
    }

    /**
     * 配置路由
     */
    @Override
    public void configRoute(Routes routes) {
        new AppRoutes().configRoute(routes);
    }

    /**
     * 配置共享模板
     */
    @Override
    public void configEngine(Engine engine) {
        // 配置共享静态方法
        engine.addSharedStaticMethod(com.jfinal.kit.StrKit.class);
        engine.addSharedStaticMethod(com.omron.omd.common.AppConst.class);
        // 配置扩展指令
        engine.addDirective("hasAuthc", AuthcDirective.class);
    }

    /**
     * 配置插件
     */
    @Override
    public void configPlugin(Plugins plugins) {
        // 配置数据库连接 URL
        String url = PropKit.get("url");
        String path = getClass().getClassLoader().getResource("../").getPath();
        // 配置 Druid 数据库连接池插件
        DruidPlugin druidPlugin = new DruidPlugin(url.replace("$path/", path), PropKit.get("username"), PropKit.get("password"));
        // 配置 sqlite 驱动
        druidPlugin.setDriverClass("org.sqlite.JDBC");
        plugins.add(druidPlugin);
        // 配置 ehCache 缓存插件
        plugins.add(new EhCachePlugin());
        // 配置 ActiveRecord 插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
        // MappingKit 配置所有映射
        _MappingKit.mapping(arp);
        // 配置数据库方言
        arp.setDialect(new Sqlite3Dialect());
        // 配置事务隔离级别
        arp.setTransactionLevel(Connection.TRANSACTION_SERIALIZABLE);
        // 配置 SQL 模板路径
        arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + "/mapper");
        arp.addSqlTemplate("all.sql");
        arp.setShowSql(PropKit.getBoolean("devMode"));
        // 配置共享方法
        Engine engine = arp.getEngine();
        engine.addSharedStaticMethod(com.jfinal.kit.StrKit.class);
        plugins.add(arp);
    }

    /**
     * 配置全局拦截器
     */
    @Override
    public void configInterceptor(Interceptors interceptors) {
        // 配置 session 拦截
        interceptors.addGlobalActionInterceptor(new SessionInViewInterceptor());
        // 配置登录拦截
        interceptors.addGlobalActionInterceptor(new LoginInterceptor());
        // 配置鉴权拦截
        interceptors.addGlobalActionInterceptor(new AuthcInterceptor());
        // 配置事务拦截
        interceptors.addGlobalServiceInterceptor(new TxByMethodRegex("(.*save.*|.*add.*|.*update.*|.*delete.*)"));
    }

    /**
     * 配置处理器
     */
    @Override
    public void configHandler(Handlers handlers) {
        // 配置上下文
        handlers.add(new ContextPathHandler("basePath"));
    }

    /**
     * 应用启动后回调
     */
    @Override
    public void afterJFinalStart() {
        // 重置权限缓存
        RoleService.BIZ.refreshAuthc();
    }
}
