import mod.iceandshadow3.ContentLists;
import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.BStatusEffect;
import mod.iceandshadow3.basics.util.BLogic;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import static mod.iceandshadow3.IaS3.MODID;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Tests for forgetfulness in adding/updating assets.")
class JSONAssets {
	private final Map<String, JSONObject> langfiles = new TreeMap<>();
	private JSONObject soundjson;

	JSONAssets() {
		final File langdir = new File("./main/assets/" + MODID + "/lang");
		for(File lang : Objects.requireNonNull(langdir.listFiles())) {
			try(final FileInputStream fis = new FileInputStream(lang)) {
				langfiles.put(lang.getName(), new JSONObject(new JSONTokener(fis)));
			} catch (IOException e) {
				fail("Cannot open for reading: "+lang);
			}
		}

		try(final FileInputStream fis = new FileInputStream("./main/assets/"+MODID+"/sounds.json")) {
			soundjson = new JSONObject(new JSONTokener(fis));
		} catch (IOException e) {
			fail("Cannot open sounds.json");
		}
	}

	static Stream<BLogicBlock> streamLogicBlock() {
		IaS3.ToolMode.init();
		return ContentLists.block.stream();
	}
	static Stream<BLogicItem> streamLogicItem() {
		IaS3.ToolMode.init();
		return ContentLists.item.stream();
	}
	static Stream<BStatusEffect> streamStatus() {
		IaS3.ToolMode.init();
		return ContentLists.status.stream();
	}
	static Stream<String> streamSoundName() {
		IaS3.ToolMode.init();
		return ContentLists.soundname.stream();
	}

	@ParameterizedTest(name = "{0} should have localized names.")
	@MethodSource({"streamLogicBlock", "streamLogicItem"})
	void logicHasNames(BLogic base) {
		final String prefix = base.getPathPrefix()+'.'+IaS3.MODID;
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			for(int i = 0; i < base.countVariants(); ++i) {
				final String name = prefix+'.'+base.getName(i);
				if(!lang.getValue().has(name)) fail(lang.getKey()+" is missing a mapping for "+name);
			}
		}
	}

	@ParameterizedTest(name = "{0} should have a localized name.")
	@MethodSource({"streamStatus"})
	void statusHasName(BStatusEffect base) {
		final String name = "effect."+IaS3.MODID+'.'+base.name();
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			if(!lang.getValue().has(name)) fail(lang.getKey()+" is missing a mapping for "+name);
		}
	}

	@ParameterizedTest(name = "{0} should have an icon.")
	@MethodSource("streamStatus")
	void statusHasIcon(BStatusEffect what) {
		final String path = "./main/assets/" + MODID + "/textures/mob_effect/"+what.name()+".png";
		if(!new File(path).exists()) fail(what + " is missing an icon");
	}

	void assetExistenceTest(BLogic what, String where, String ismissingwhat) {
		for(int i = 0; i < what.countVariants(); ++i) {
			final String name = what.getName(i);
			final String path = "./main/assets/" + MODID + where + name+".json";
			if(!new File(path).exists()) fail(name+ismissingwhat);
		}
	}

	@ParameterizedTest(name = "{0} should have item models or the correct overrides.")
	@MethodSource({"streamLogicBlock", "streamLogicItem"})
	void logicHasItemModels(BLogic base) {
		if(base instanceof BLogicBlock && base.isTechnical()) return;
		assetExistenceTest(base, "/models/item/", " is missing an item model");
	}

	@ParameterizedTest(name = "{0} should have block models.")
	@MethodSource("streamLogicBlock")
	void logicHasBlockModels(BLogicBlock base) {
		//At the moment, the "correct overrides" do not exist.
		assetExistenceTest(base, "/models/block/", " is missing a block model");
	}

	@ParameterizedTest(name = "{0} should have blockstate files.")
	@MethodSource("streamLogicBlock")
	void logicHasBlockstateFiles(BLogicBlock base) {
		assetExistenceTest(base, "/blockstates/", " is missing a blockstates.json file.");
	}

	@ParameterizedTest(name = "{0} (sound name) should exist in sounds.json")
	@MethodSource("streamSoundName")
	void soundsJsonHasSound(String name) {
		if(!soundjson.has(name)) fail(name+" isn't in sounds.json");
	}
}
