import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Tests for general mistakes in metadata + configuration files in the repo.")
class TestsMetadata {
	@DisplayName("update.json should contain the version in the processed mods.toml")
	@Test
	void updateJsonContainsVersion() throws FileNotFoundException {
		//Quick and very dirty check to ensure that config.gradle and upgrade.json don't end up desynced.
		String versioninfo = null;
		try(final Scanner tomlscanner = new Scanner(new File("./main/META-INF/mods.toml"))) {
			while (tomlscanner.hasNextLine()) {
				final String line = tomlscanner.nextLine();
				if (line.contains("version=")) {
					versioninfo = line.split("\"")[1];
				}
			}
			if (versioninfo == null) {
				fail("Didn't find version info in mods.toml.");
			} else if("@version@".equals(versioninfo)) {
				fail("The Gradle preprocess for version info is not working. Found @version@.");
			}
		}
		versioninfo = '"'+versioninfo+'"';
		try(final Scanner updatejsonscanner = new Scanner(new File("../../update.json"))) {
			while (updatejsonscanner.hasNextLine()) {
				final String line = updatejsonscanner.nextLine();
				if (line.contains(versioninfo)) return;
			}
		}
		fail("update.json needs to contain the version "+versioninfo+".");
	}

	@DisplayName("The bundled server.properties should not include comments")
	@Test
	void serverPropertiesCleaned() {
		try(final Scanner serverfile = new Scanner(new File("../../run/server.properties"))) {
			while(serverfile.hasNextLine()) {
				if(serverfile.nextLine().charAt(0) == '#') fail("Remove any full-line comments from server.properties!");
			}
		} catch (IOException e) { /* No readable server.properties? No problem. */ }
	}

}
