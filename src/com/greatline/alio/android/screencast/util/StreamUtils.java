package com.greatline.alio.android.screencast.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StreamUtils {
	@SuppressWarnings("rawtypes")
	public static void transfertResource(Class c, String resourceName,
			File output) {
		InputStream resStream = c.getResourceAsStream(resourceName);
		if (resStream == null)
			throw new RuntimeException("Cannot find resource " + resourceName);
		try {
			FileOutputStream fos = new FileOutputStream(output);
			while (true) {
				int val = resStream.read();
				if (val <= -1)
					break;
				fos.write(val);
			}
			fos.close();
			resStream.close();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}
