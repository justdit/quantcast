package com.quantcast.misc.homework;

import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	public static void main(String[] args)   {
		logger.info("Starting...");

		String filename = "";
		String date = "";
		
		try {
			final Options options = new Options();
			options.addOption(new Option("d", true, "date"));
			
			final CommandLineParser cliParser = new BasicParser();
			final CommandLine commandLine = cliParser.parse(options, args);
			
			if (!commandLine.hasOption('d') || commandLine.getArgs().length != 1) {
				printUsageAndExit();
			}

			date = commandLine.getOptionValue('d');
			filename = commandLine.getArgs()[0];

		} catch (ParseException e) {
			printUsageAndExit();
		}
		
		logger.info("Looking for records for date {} in file {}", date, filename);
		
		try {
			final List<String[]> records = CookiesLog.parseFromCVS(filename);
			final Set<String> result = new App().findMostActiveCookies(records, date);
			result.forEach(System.out::println);

		} catch (FileNotFoundException | CSVParseException e) {
			System.err.print("Failed to parse given csv file: " + e.getMessage());
		
		} catch (DateTimeException e) {
			System.err.println("Invalid date format passed as an input: " + e.getMessage());
		}
	}
	
	private static void printUsageAndExit() {
		System.err.println("most_active_cookie <cookie.log> -d <date in yyyy-mm-dd>");
		System.exit(1);
	}

	public Set<String> findMostActiveCookies(List<String[]> records, String date) {
		/**
		 * NOTE: it's least surprising for a user of CLI to use their timezone
		 * Though this breaks the 2nd test case from a task description as there GMT is being used by default.
		 */
//		final ZoneId zoneId = ZoneId.of("UTC");
		final ZoneId zoneId = ZoneId.systemDefault();
		return findMostActiveCookies(records, date, zoneId);
	}
	
    Set<String> findMostActiveCookies(List<String[]> records, String date, ZoneId zoneId) {
		final LocalDate localDate = LocalDate.parse(date);
		
		final ZonedDateTime dateStart = ZonedDateTime.of(localDate.minusDays(1), LocalTime.MAX, zoneId);
		final ZonedDateTime dateEnd = ZonedDateTime.of(localDate.plusDays(1), LocalTime.MIN, zoneId);
		
		Map<String, Integer> cookiesCount = records
			.stream()
			.filter(cookieRecord -> {
				ZonedDateTime z = ZonedDateTime.parse(cookieRecord[1], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
				return z.isAfter(dateStart) && z.isBefore(dateEnd);
			})
			.collect(Collectors.toMap(r -> r[0], r -> 1, (value, acc) -> value + acc));
			
		Optional<Integer> maybeMax = cookiesCount.values().stream().max(Integer::compare);

		Set<String> result = Collections.EMPTY_SET;
		if (maybeMax.isPresent()) {
			final int max = maybeMax.get();
			result = cookiesCount
					.entrySet()
					.stream()
					.filter(entry -> entry.getValue() == max)
					.map(Entry::getKey)
					.collect(Collectors.toSet());
		}
		
		return result;
		
	}
}
