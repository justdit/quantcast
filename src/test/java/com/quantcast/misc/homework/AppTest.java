package com.quantcast.misc.homework;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest extends TestCase {
	private final App app;
	private final ZoneId zoneId = ZoneId.of("GMT");

	public AppTest(String testName) {
		super(testName);
		app = new App();
	}

	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	public void testEmpty() {
		List<String[]> records = Collections.EMPTY_LIST;
		Set<String> mostActiveCookiesByDate = app.findMostActiveCookies(records, "2018-12-08", zoneId);
		assertTrue(mostActiveCookiesByDate.isEmpty());
	}

	public void testNoResult() {
		List<String[]> records = Arrays.asList(new String[] { "AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00" },
				new String[] { "5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00" },
				new String[] { "AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00" },
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00" },
				new String[] { "fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"},
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00" });

		Set<String> mostActiveCookiesByDate = app.findMostActiveCookies(records, "2019-10-08", zoneId);
		assertTrue(mostActiveCookiesByDate.isEmpty());
	}
	
	public void testOneResult() {
		List<String[]> records = Arrays.asList(new String[] { "AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00" },
				new String[] { "5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00" },
				new String[] { "AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00" },
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00" },
				new String[] { "fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"},
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00" });

		Set<String> mostActiveCookiesByDate = app.findMostActiveCookies(records, "2018-12-09", zoneId);
		assertEquals(1, mostActiveCookiesByDate.size());
		assertTrue(mostActiveCookiesByDate.contains("AtY0laUfhglK3lC7"));
	}
	
	public void testMultipleResults() {
		List<String[]> records = Arrays.asList(new String[] { "AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00" },
				new String[] { "5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00" },
				new String[] { "AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00" },
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00" },
				new String[] { "fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"},
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00" });

		Set<String> mostActiveCookiesByDate = app.findMostActiveCookies(records, "2018-12-08", zoneId);
		assertEquals(3, mostActiveCookiesByDate.size());
		for (String v : Arrays.asList("fbcn5UAVanZf6UtG", "SAZuXPGUrfbcn5UA", "4sMM2LxV07bPJzwf")) {
			assertTrue(mostActiveCookiesByDate.contains(v));
		}
	}
	
	public void testDifferentTZ() {
		List<String[]> records = Arrays.asList(new String[] { "AtY0laUfhglK3lC7", "2018-12-09T14:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-09T10:13:00+00:00" },
				new String[] { "5UAVanZf6UtGyKVS", "2018-12-09T07:25:00+00:00" },
				new String[] { "AtY0laUfhglK3lC7", "2018-12-09T06:19:00+00:00" },
				new String[] { "SAZuXPGUrfbcn5UA", "2018-12-08T22:03:00+00:00" },
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-08T21:30:00+00:00" },
				new String[] { "fbcn5UAVanZf6UtG", "2018-12-08T09:30:00+00:00"},
				new String[] { "4sMM2LxV07bPJzwf", "2018-12-07T23:30:00+00:00" });

		Set<String> mostActiveCookiesByDate = app.findMostActiveCookies(records, "2018-12-08", ZoneId.of("Europe/Amsterdam"));
		assertEquals(1, mostActiveCookiesByDate.size());
		assertTrue(mostActiveCookiesByDate.contains("4sMM2LxV07bPJzwf"));
	}
}
