package com.quantcast.misc.homework;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.opencsv.CSVReader;

public class CookiesLog {
	private static final Logger logger = LoggerFactory.getLogger(CookiesLog.class);

	public static List<String[]> parseFromCVS(String filename) throws CSVParseException, FileNotFoundException {
		List<String[]> result = new ArrayList<>();
		
		try (CSVReader csvReader = new CSVReader(new FileReader(filename))) {
			String[] header = csvReader.readNext();

			if (header == null || header.length != 2 || !"cookie".equalsIgnoreCase(header[0])
					|| !"timestamp".equalsIgnoreCase(header[1])) {
				logger.warn("File header is not as expected [\"cookie\", \"timestamp\"]");
			}

			String[] records = null;
			while ((records = csvReader.readNext()) != null) {
				if (records.length != 2 || Strings.isNullOrEmpty(records[0]) || Strings.isNullOrEmpty(records[1])) {
					logger.warn("Skippping line at {} is not well formed. Expected <cookie,timestamp> formatted line",
							csvReader.getLinesRead());
					continue;
				}
				
				result.add(records);
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw new CSVParseException("Failed to parse csv file(" + filename + ")", e);
		}
		
		return result;
	}
}
