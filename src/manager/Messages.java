package manager;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "manager.messages";
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	private Messages() {
	}
	public static String getString(String key) {
		try {
			return new String(RESOURCE_BUNDLE.getString(key).getBytes("8859_1"),"UTF-8");
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
