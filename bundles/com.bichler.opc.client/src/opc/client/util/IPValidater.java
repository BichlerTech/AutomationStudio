package opc.client.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class IPValidater {
	private static Pattern validIPV4Pattern = null;
	private static Pattern validIPV6Pattern = null;
	private static final String IPV4PATTERN = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	private static final String IPV6PATTERN = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
	static {
		try {
			validIPV4Pattern = Pattern.compile(IPV4PATTERN, Pattern.CASE_INSENSITIVE);
			validIPV6Pattern = Pattern.compile(IPV6PATTERN, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException e) {
			Logger.getLogger(IPValidater.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * private constructor to hide the default one
	 */
	private IPValidater() {
	}

	/**
	 * Determine if the given string is a valid IPv4 address. This method uses
	 * pattern matching to see if the given string could be a valid IP address.
	 *
	 * @param ipAddress A string that is to be examined to verify whether or not it
	 *                  could be a valid IP address.
	 * @return <code>true</code> if the string is a value that is a valid IP
	 *         address, <code>false</code> otherwise.
	 */
	public static boolean isIPv4LiteralAddress(String ipAddress) {
		Matcher m1 = validIPV4Pattern.matcher(ipAddress);
		return m1.matches();
	}

	/**
	 * Determine if the given string is a valid IPv6 address. This method uses
	 * pattern matching to see if the given string could be a valid IP address.
	 *
	 * @param ipAddress A string that is to be examined to verify whether or not it
	 *                  could be a valid IP address.
	 * @return <code>true</code> if the string is a value that is a valid IP
	 *         address, <code>false</code> otherwise.
	 */
	public static boolean isIPv6LiteralAddress(String ipAddress) {
		Matcher m2 = validIPV6Pattern.matcher(ipAddress);
		return m2.matches();
	}
}
