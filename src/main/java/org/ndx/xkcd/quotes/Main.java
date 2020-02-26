package org.ndx.xkcd.quotes;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class Main {
	public static class XkcdException extends RuntimeException {

		public XkcdException() {
			super();
		}

		public XkcdException(String message, Throwable cause) {
			super(message, cause);
		}

		public XkcdException(String message) {
			super(message);
		}

		public XkcdException(Throwable cause) {
			super(cause);
		}
	}

	public static void main(String[] args) {
		throw new RuntimeException("TODO");
	}

	public String loadText(int number) {
		String fullPath = String.format("transcripts/%d.txt", number);
		try {
			try(InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
				if(stream==null)
					throw new XkcdException(String.format("Can't find file %s", fullPath));
				return IOUtils.toString(stream, "UTF-8");
			}
		} catch(IOException io) {
			throw new XkcdException("Can't load "+fullPath, io);
		}
	}

}
