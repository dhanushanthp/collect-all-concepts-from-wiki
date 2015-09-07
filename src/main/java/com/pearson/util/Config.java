package com.pearson.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public static Properties getConfig() throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream("/opt/data-extractor/config/config.properties");
		prop.load(input);
		return prop;
	}
}
