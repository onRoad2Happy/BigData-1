package com.wyd.BigData;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

public class Global {
	public PropertiesConfiguration config;
	private JavaStreamingContext ssc;

	public Global() {
		try {
			config = new PropertiesConfiguration();
			config.setEncoding("utf-8");
			config.load("config.properties");
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private static Global instance;

	public static Global getInstance() {
		if (instance == null) {
			synchronized (Global.class) {
				if (instance == null) {
					instance = new Global();
				}
			}
		}
		return instance;
	}

	public PropertiesConfiguration getConfig() {
		return config;
	}

	public void setConfig(PropertiesConfiguration config) {
		this.config = config;
	}

	public JavaStreamingContext getSsc() {
		return ssc;
	}

	public void setSsc(JavaStreamingContext ssc) {
		this.ssc = ssc;
	}


}
