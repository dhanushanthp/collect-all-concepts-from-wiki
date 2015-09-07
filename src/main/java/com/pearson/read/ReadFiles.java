package com.pearson.read;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.pearson.util.Config;

public class ReadFiles {
	public static void main(String[] args) throws NumberFormatException, IOException {
		int count = 0;
		for (int i = 0; i < 100; i = i + Integer.parseInt(Config.getConfig().getProperty("num_of_result")) ) {
			String fileName = count + "_" + i;
			BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader("/opt/data-extractor/Concepts/"+ fileName));
				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
			System.out.println("NEXT LINE __--- ");
			count++;
		}
	}
}
