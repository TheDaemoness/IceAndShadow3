package mod.iceandshadow3;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ExtensionToolMode implements BeforeAllCallback {
	private static boolean didit = false;
	@Override
	public void beforeAll(ExtensionContext context) throws Exception {
		if(didit) return;
		didit = true;
		IaS3.ToolMode.init();
	}
}
