package org.ndx.xkcd.quotes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;

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
			try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fullPath)) {
				if (stream == null)
					throw new XkcdException(String.format("Can't find file %s", fullPath));
				return IOUtils.toString(stream, "UTF-8");
			}
		} catch (IOException io) {
			throw new XkcdException("Can't load " + fullPath, io);
		}
	}

	private Optional<List<String>> allTexts = Optional.empty();

	public List<String> loadAllTexts() {
		if (allTexts.isEmpty()) {
			List<String> returned = new ArrayList<>();
			int index = 1;
			try {
				do {
					returned.add(loadText(index++));
				} while (true);
			} catch (XkcdException e) {
				// This is normal
			}
			allTexts = Optional.of(returned);
		}
		return allTexts.get();
	}

	public Entry<Integer, String> find_by_proximity_to(String string) {
		List<String> texts = loadAllTexts();
		Entry<Integer, Entry<Integer, String>> best = Map.entry(Integer.MAX_VALUE, Map.entry(Integer.MIN_VALUE, "no text, initial value for Main::find_by_proximity_to"));
		for (int index = 0; index < texts.size(); index++) {
			Entry<Integer, String> tested = Map.entry(index, texts.get(index));
			int distance = LevenshteinDistance.getDefaultInstance().apply(string, tested.getValue());
			if(tested.getValue().contains(string)) {
				distance = distance - string.length();
			}
			if (distance < best.getKey()) {
				best = Map.entry(distance, tested);
			}
		}
		return best.getValue();
	}

}
