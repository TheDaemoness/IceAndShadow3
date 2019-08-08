package mod.iceandshadow3;

import mod.iceandshadow3.lib.base.INamed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Tests for mistakes in mod initialization.")
@ExtendWith({ExtensionToolMode.class})
class TestsInit {
	@Test
	@DisplayName("No bug() calls should occur on successful tool-mode initialization.")
	void noBugOutputAtStart() {
		Assertions.assertFalse(IaS3.bugged(), "bug() was called at least once during initialization.");
	}

	static <In extends INamed> void testNames(Iterator<In> input) {
		final HashMap<String, In> seen = new HashMap<>();
		while(input.hasNext()) {
			final In elem = input.next();
			final String[] names = elem.getNames();
			for(String name : names) {
				final In old = seen.putIfAbsent(name, elem);
				if(old != null) {
					if(old == elem) fail("ID "+name+" is used twice by "+elem);
					else fail("ID " + name + " is used by both "+old+" and "+elem);
				}
			}
		}
	}

	@DisplayName("There should be no duplicate item IDs.")
	@Test
	void uniqueNamesForLogicItems() {
		testNames(ContentLists.logicsWithItems().iterator());
	}

	@DisplayName("There should be no duplicate block IDs.")
	@Test
	void uniqueNamesForLogicBlocks() {
		testNames(ContentLists.block.iterator());
	}

	@DisplayName("There should be no duplicate status effect IDs.")
	@Test
	void uniqueNamesForStatuses() {
		testNames(ContentLists.status.iterator());
	}
}
