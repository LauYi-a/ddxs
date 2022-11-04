package com.ddx.auth.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class MpGeneratorConfig {

	public static void main(String[] args) {
		String path = "D://code";
		String modelDir = "/com/ddx/sys/model";
		String auth = "YI.LAU";
		String packages = "com.ddx.sys";
		String controller = "controller";
		String mapper = "mapper";
		String service = "service";
		String entity = "entity";
		String serviceImpl = "service.impl";
		String[] tableList = new String[] {"sys_test"};

		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://127.0.0.1:3306/ddx-auth?characterEncoding=utf-8&useSSL=false";
		String username = "root";
		String password = "ddx123";
		String schema = "ddx-auth";

		try {
			Class.forName(driverName);
			Connection con = DriverManager.getConnection(url, username,password);
			con.close();
		} catch (ClassNotFoundException e) {
			log.error("找不到驱动程序", e);
		} catch (Exception e) {
			log.error("连接失败", e);
		}

		AutoGenerator autoGenerator = new AutoGenerator();
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(path);   //生成文件输出根目录
		gc.setOpen(true);//生成完成后不弹出文件框
		gc.setFileOverride(true);  //文件覆盖
		gc.setActiveRecord(false);// 不需要ActiveRecord特性的请改为false
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(false);// XML columList
		gc.setAuthor(auth);// 作者

		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		gc.setControllerName("%sController");
		gc.setServiceName("I%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		autoGenerator.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);   //设置数据库类型，我是postgresql
		dsc.setDriverName(driverName);
		dsc.setUsername(username);
		dsc.setPassword(password);
		dsc.setUrl(url);  //指定数据库
		dsc.setSchemaName(schema);
		autoGenerator.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);      // 表名生成策略
		strategy.setInclude(tableList);     // 需要生成的表
		strategy.setSuperServiceClass(null);
		strategy.setSuperServiceImplClass(null);
		strategy.setSuperMapperClass(null);
		strategy.setEntityLombokModel(true);
		strategy.entityTableFieldAnnotationEnable(true);
		autoGenerator.setStrategy(strategy);

		//字段添加自动填充注解
		List tableFillList=new ArrayList();
		tableFillList.add(new TableFill("create_time", FieldFill.INSERT));//创建时间
		tableFillList.add(new TableFill("create_id", FieldFill.INSERT));//创建人ID
		tableFillList.add(new TableFill("create_name", FieldFill.INSERT));//创建人账户
		tableFillList.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));//修改时间
		tableFillList.add(new TableFill("update_id", FieldFill.INSERT_UPDATE));//修改人ID
		tableFillList.add(new TableFill("update_name", FieldFill.INSERT_UPDATE));//修改人账户
		strategy.setTableFillList(tableFillList);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
				map.put("modelDir", packages);
				this.setMap(map);
			}
		};

		// 自定义输出配置
		List<FileOutConfig> focList = new ArrayList<>();
		focList.add(new FileOutConfig("/templates/queryreq.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return path +modelDir+"/req/"+tableInfo.getEntityName().substring(0, 1).toLowerCase()+tableInfo.getEntityName().substring(1)
						+ File.separator + tableInfo.getEntityName() + "QueryReq.java";
			}
		});
		focList.add(new FileOutConfig("/templates/addreq.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return path +modelDir+"/req/"+tableInfo.getEntityName().substring(0, 1).toLowerCase()+tableInfo.getEntityName().substring(1)
						+ File.separator + tableInfo.getEntityName() + "AddReq.java";
			}
		});
		focList.add(new FileOutConfig("/templates/editreq.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return path +modelDir+"/req/"+tableInfo.getEntityName().substring(0, 1).toLowerCase()+tableInfo.getEntityName().substring(1)
						+ File.separator + tableInfo.getEntityName() + "EditReq.java";
			}
		});
		cfg.setFileOutConfigList(focList);
		autoGenerator.setCfg(cfg);

		// 配置模板
		TemplateConfig templateConfig = new TemplateConfig();
		// 使用自定义模板，不想要生成就设置为null,如果不设置null会使用默认模板
		templateConfig.setEntity("templates/entity.java.vm");
		templateConfig.setController("templates/controller.java.vm");
		templateConfig.setService("templates/service.java.vm");
		templateConfig.setServiceImpl("templates/serviceImpl.java.vm");
		templateConfig.setMapper("templates/mapper.java.vm");
		templateConfig.setXml("templates/mapper.xml.vm");
		autoGenerator.setTemplate(templateConfig);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(packages);
		pc.setController(controller);
		pc.setService(service);
		pc.setServiceImpl(serviceImpl);
		pc.setMapper(mapper);
		pc.setEntity(entity);
		pc.setXml("xml");
		autoGenerator.setPackageInfo(pc);

		// 执行生成
		autoGenerator.execute();
	}
}
