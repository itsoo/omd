package com.omron.omd.model.generator;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.Sqlite3Dialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

import javax.sql.DataSource;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 * <p>虽然保留了 Bean 层的代码生成器，然而并没有什么卵用</p>
 * <p>因为 Sqlite 对 JDBC 的实现并不好</p>
 * <p>好用的是因为它是嵌入式数据库，小巧轻便但 Java 驱动蛋疼</p>
 * <p>--- 留作参考</p>
 *
 * @author zxy
 */
public class ModelGenerator {

    private static DataSource getDataSource() {
        PropKit.use("config.properties");
        // 配置数据库连接 URL
        String url = PropKit.get("url");
        String path = ModelGenerator.class.getClassLoader().getResource("").getPath();
        // 处理执行不同路径的 path
        path = path.replace("classes/", "omd/WEB-INF/");
        DruidPlugin druidPlugin = new DruidPlugin(url.replace("$path/", path), PropKit.get("username"), PropKit.get("password"));
        // 配置 sqlite 驱动
        druidPlugin.setDriverClass("org.sqlite.JDBC");
        druidPlugin.start();
        return druidPlugin.getDataSource();
    }

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPackageName = "com.omron.omd.model.base";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/omron/omd/model/base";
        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "com.omron.omd.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";
        // 创建数据源
        DataSource dataSource = getDataSource();
        // 创建生成器
        Generator generator = new Generator(dataSource, baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 添加不需要生成的表名
        generator.addExcludedTable("sqlite_master", "sqlite_sequence");
        // 设置是否在 Model 中生成 dao 对象
        generator.setGenerateDaoInModel(true);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(true);
        // 设置需要被移除的表名前缀用于生成 modelName
        generator.setRemovedTableNamePrefixes("t_");
        // 设置数据库方言
        generator.setDialect(new Sqlite3Dialect());
        // 生成
        generator.generate();
    }
}
