/*
 * Title:        PathMapper
 * Description:
 *
 * This software is published under the terms of the OpenSymphony Software
 * License version 1.1, of which a copy has been included with this
 * distribution in the LICENSE.txt file.
 */

package com.opensymphony.module.sitemesh.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * The PathMapper is used to map file patterns to keys, and find an approriate
 * key for a given file path. The pattern rules are consistent with those
 * defined in the Servlet 2.3 API on the whole. Wildcard patterns are also
 * supported, using any combination of * and ?.
 * 
 * <h3>Example</h3>
 * 
 * <blockquote><code>
 * PathMapper pm = new PathMapper();<br>
 * <br>
 * pm.put("one","/");<br>
 * pm.put("two","/mydir/*");<br>
 * pm.put("three","*.xml");<br>
 * pm.put("four","/myexactfile.html");<br>
 * pm.put("five","/*\/admin/*.??ml");<br>
 * <br>
 * String result1 = pm.get("/mydir/myfile.xml"); // returns "two";<br>
 * String result2 = pm.get("/mydir/otherdir/admin/myfile.html"); // returns "five";<br>
 * </code></blockquote>
 * 
 * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
 * @author <a href="mailto:mcannon@internet.com">Mike Cannon-Brookes</a>
 * @author <a href="mailto:hani@formicary.net">Hani Suleiman</a>
 * @version $Revision: 1.3 $
 */
public class PathMapper {
	private Map mappings = new HashMap();

	private Map patterns = new HashMap();

	public static final String PATTERN_TYPE_DEFAULT = "default";

	public static final String PATTERN_TYPE_REGEX = "regex";

	/** Add a key and appropriate matching pattern. */

	public void put(String key, String pattern, String type) {
		if (key != null) {
			mappings.put(pattern, key);
			patterns.put(pattern, type);
		}
	}

	/** Retrieve appropriate key by matching patterns with supplied path. */
	public String get(String path) {
		if (path == null)
			path = "/";
		String mapped = findKey(path, mappings, patterns);
		if (mapped == null)
			return null;
		return (String) mappings.get(mapped);
	}

	/** Find exact key in mappings. */
	private static String findKey(String path, Map mappings, Map patterns) {
		String result = findExactKey(path, mappings);
		if (result == null)
			result = findComplexKey(path, mappings, patterns);
		if (result == null)
			result = findDefaultKey(mappings);
		return result;
	}

	/** Check if path matches exact pattern ( /blah/blah.jsp ). */
	private static String findExactKey(String path, Map mappings) {
		if (mappings.containsKey(path))
			return path;
		return null;
	}

	private static String findComplexKey(String path, Map mappings, Map patterns) {
		Iterator i = mappings.keySet().iterator();
		String result = null, key = null;
		while (i.hasNext()) {
			key = (String) i.next();
			String type = (String) patterns.get(key);
			if (key.length() > 1
					&& (key.indexOf('?') != -1 || key.indexOf('*') != -1)
					&& match(key, type, path, false)) {
				if (result == null || key.length() > result.length()) {
					// longest key wins
					result = key;
				}
			}
		}
		return result;
	}

	/** Look for root pattern ( / ). */
	private static String findDefaultKey(Map mappings) {
		String[] defaultKeys = { "/", "*", "/*" };
		for (int i = 0; i < defaultKeys.length; i++) {
			if (mappings.containsKey(defaultKeys[i]))
				return defaultKeys[i];
		}
		return null;
	}

	private static boolean match(String pattern, String type, String str,
			boolean isCaseSensitive) {
		if (PATTERN_TYPE_REGEX.equalsIgnoreCase(type))
			return matchRegex(pattern, str, isCaseSensitive);
		else
			return matchDefault(pattern, str, isCaseSensitive);
	}

	private static boolean matchDefault(String pattern, String str,
			boolean isCaseSensitive) {
		char[] patArr = pattern.toCharArray();
		char[] strArr = str.toCharArray();
		int patIdxStart = 0;
		int patIdxEnd = patArr.length - 1;
		int strIdxStart = 0;
		int strIdxEnd = strArr.length - 1;
		char ch;

		boolean containsStar = false;
		for (int i = 0; i < patArr.length; i++) {
			if (patArr[i] == '*') {
				containsStar = true;
				break;
			}
		}

		if (!containsStar) {
			// No '*'s, so we make a shortcut
			if (patIdxEnd != strIdxEnd) {
				return false; // Pattern and string do not have the same size
			}
			for (int i = 0; i <= patIdxEnd; i++) {
				ch = patArr[i];
				if (ch != '?') {
					if (isCaseSensitive && ch != strArr[i]) {
						return false; // Character mismatch
					}
					if (!isCaseSensitive
							&& Character.toUpperCase(ch) != Character
									.toUpperCase(strArr[i])) {
						return false; // Character mismatch
					}
				}
			}
			return true; // String matches against pattern
		}

		if (patIdxEnd == 0) {
			return true; // Pattern contains only '*', which matches anything
		}

		// Process characters before first star
		while ((ch = patArr[patIdxStart]) != '*' && strIdxStart <= strIdxEnd) {
			if (ch != '?') {
				if (isCaseSensitive && ch != strArr[strIdxStart]) {
					return false; // Character mismatch
				}
				if (!isCaseSensitive
						&& Character.toUpperCase(ch) != Character
								.toUpperCase(strArr[strIdxStart])) {
					return false; // Character mismatch
				}
			}
			patIdxStart++;
			strIdxStart++;
		}
		if (strIdxStart > strIdxEnd) {
			// All characters in the string are used. Check if only '*'s are
			// left in the pattern. If so, we succeeded. Otherwise failure.
			for (int i = patIdxStart; i <= patIdxEnd; i++) {
				if (patArr[i] != '*') {
					return false;
				}
			}
			return true;
		}

		// Process characters after last star
		while ((ch = patArr[patIdxEnd]) != '*' && strIdxStart <= strIdxEnd) {
			if (ch != '?') {
				if (isCaseSensitive && ch != strArr[strIdxEnd]) {
					return false; // Character mismatch
				}
				if (!isCaseSensitive
						&& Character.toUpperCase(ch) != Character
								.toUpperCase(strArr[strIdxEnd])) {
					return false; // Character mismatch
				}
			}
			patIdxEnd--;
			strIdxEnd--;
		}
		if (strIdxStart > strIdxEnd) {
			// All characters in the string are used. Check if only '*'s are
			// left in the pattern. If so, we succeeded. Otherwise failure.
			for (int i = patIdxStart; i <= patIdxEnd; i++) {
				if (patArr[i] != '*') {
					return false;
				}
			}
			return true;
		}

		// process pattern between stars. padIdxStart and patIdxEnd point
		// always to a '*'.
		while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
			int patIdxTmp = -1;
			for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
				if (patArr[i] == '*') {
					patIdxTmp = i;
					break;
				}
			}
			if (patIdxTmp == patIdxStart + 1) {
				// Two stars next to each other, skip the first one.
				patIdxStart++;
				continue;
			}
			// Find the pattern between padIdxStart & padIdxTmp in str between
			// strIdxStart & strIdxEnd
			int patLength = (patIdxTmp - patIdxStart - 1);
			int strLength = (strIdxEnd - strIdxStart + 1);
			int foundIdx = -1;
			strLoop: for (int i = 0; i <= strLength - patLength; i++) {
				for (int j = 0; j < patLength; j++) {
					ch = patArr[patIdxStart + j + 1];
					if (ch != '?') {
						if (isCaseSensitive
								&& ch != strArr[strIdxStart + i + j]) {
							continue strLoop;
						}
						if (!isCaseSensitive
								&& Character.toUpperCase(ch) != Character
										.toUpperCase(strArr[strIdxStart + i + j])) {
							continue strLoop;
						}
					}
				}

				foundIdx = strIdxStart + i;
				break;
			}

			if (foundIdx == -1) {
				return false;
			}

			patIdxStart = patIdxTmp;
			strIdxStart = foundIdx + patLength;
		}

		// All characters in the string are used. Check if only '*'s are left
		// in the pattern. If so, we succeeded. Otherwise failure.
		for (int i = patIdxStart; i <= patIdxEnd; i++) {
			if (patArr[i] != '*') {
				return false;
			}
		}
		return true;
	}

	private static boolean matchRegex(String pattern, String str,
			boolean isCaseSensitive) {
		if (!isCaseSensitive) {
			pattern = pattern.toLowerCase();
			str = str.toLowerCase();
		}
		PatternCompiler compiler = new Perl5Compiler();
		PatternMatcher matcher = new Perl5Matcher();
		try {
			Pattern p = compiler.compile(pattern, Perl5Compiler.READ_ONLY_MASK);
			return matcher.matches(str, p);
		} catch (MalformedPatternException e) {
			throw new IllegalArgumentException("Malformed regular expression: "
					+ pattern);
		}
	}

}