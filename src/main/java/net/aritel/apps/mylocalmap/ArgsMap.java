package net.aritel.apps.mylocalmap;

import java.util.HashMap;

@SuppressWarnings("serial")
public class ArgsMap extends HashMap<String, String> {

	public ArgsMap(String[] args) {
		// Transfer System Properties.
		for (Entry<Object, Object> e : System.getProperties().entrySet()) {
			put(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
		}
		
		// args
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			int idx = arg.indexOf('=');
			if (idx == -1) {
				put(Integer.toString(i), arg);
			} else {
				put(arg.substring(0, idx - 1), arg.substring(idx + 1));
			}
		}
	}
	
	public int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(get(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
}
