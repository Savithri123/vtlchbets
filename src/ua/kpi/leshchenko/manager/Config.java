package ua.kpi.leshchenko.manager;

import java.util.ResourceBundle;

public class Config {

	private static Config instance;
	private ResourceBundle resource;
	private static final String BUNDLE_NAME = "ua.kpi.leshchenko.manager.config";
	public static final String DRIVER = "DRIVER";
	public static final String DB_USERNAME = "DB_USERNAME";
	public static final String DB_PASSWORD = "DB_PASSWORD";
	public static final String URL = "URL";
	public static final String CONNECTION_POOL_SIZE = "CONNECTION_POOL_SIZE";

	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
			instance.resource = ResourceBundle.getBundle(BUNDLE_NAME);
		}
		return instance;
	}

	public String getProperty(String key) {
		return (String) resource.getObject(key);
	}
}
