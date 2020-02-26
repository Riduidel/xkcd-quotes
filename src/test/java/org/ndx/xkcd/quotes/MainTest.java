package org.ndx.xkcd.quotes;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ndx.xkcd.quotes.Main.XkcdException;

class MainTest {
	private static Main main;

	@BeforeAll public static void loadMain() {
		main = new Main();
	}

	@Test void can_load_a_file() {
		// Assemble
		// Act
		String text = main.loadText(1);
		// Assert
		assertThat(text).isNotBlank();
	}

	@Test void cannot_load_an_invalid_file() {
		// Assemble
		org.junit.jupiter.api.Assertions.assertThrows(XkcdException.class, () -> {
			// Act
			String text = main.loadText(-1);
			// Assert
			assertThat(text).isEmpty();
		});
	}

	@Test void can_load_all_files() {
		// Assemble
		// Act
		List<String> texts = main.loadAllTexts();
		// Assert
		assertThat(texts).isNotEmpty()
			.hasSizeGreaterThan(1000);
	}
	
	@Test void can_find_nearest_text_for() {
		// Assemble
		Main main = new Main();
		// Act
		Entry<Integer, String> texts = main.find_by_proximity_to("A smaller frame with a zoom out of the boy in the barrel seen from afar. The barrel drifts into the distance. Nothing else can be seen.");
		// Assert
		assertThat(texts).satisfies(requirements -> {
			// The "-1" part is here because the list is 0-indexed, while our images are 1-indexed
			assertThat(requirements).extracting(Entry::getKey).isEqualTo(1-1);
			assertThat(requirements).extracting(Entry::getValue).isNotNull();
		});
	}
}
