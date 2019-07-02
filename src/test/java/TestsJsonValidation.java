import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

class TestsJsonValidation {
	class JSONValidator implements Consumer<Path> {
		private Schema schema;
		int issues = 0;
		JSONValidator(InputStream in) {
			 schema = SchemaLoader.load(new JSONObject(new JSONTokener(in)));
		}
		public void accept(Path f) {
			try {
				schema.validate(new JSONObject(new JSONTokener(new FileInputStream(f.toFile()))));
			} catch(ValidationException e) {
				for(ValidationException ee : e.getCausingExceptions()) System.out.println(ee.getMessage());
				issues += e.getViolationCount();
			} catch (FileNotFoundException e) {
				fail("FileNotFound: "+e.getMessage());
			}
		}
	}

	//No tests here currently. Existing schemas are not very mod-friendly.
}
