import mod.iceandshadow3.ContentLists;
import mod.iceandshadow3.ExtensionToolMode;
import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.StatusEffect;
import mod.iceandshadow3.lib.base.LogicCommon;
import mod.iceandshadow3.lib.compat.file.JsonGenAssetsBlock;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import scala.runtime.BoxedUnit;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;

import static mod.iceandshadow3.IaS3.MODID;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for forgetfulness in adding/updating asset/data JSON files.")
@ExtendWith({ExtensionToolMode.class})
class TestsResources {
	private final Map<String, JSONObject> langfiles = new TreeMap<>();
	private static final JSONObject soundjson;

	static {
		JSONObject soundjsonTmp = new JSONObject();
		try {
			soundjsonTmp = new JSONObject(new JSONTokener(new FileInputStream(
			"./main/assets/"+MODID+"/sounds.json"
			)));
		} catch (IOException e) {
			fail("Cannot open sounds.json");
		}
		soundjson = soundjsonTmp;
	}

	private static class AssetFile extends File {
		private static File[] getAssetsInDir(String dir, FileFilter filter) {
			return new File("./main/assets/" + MODID, dir).listFiles(filter);
		}
		private static File[] getDataInDir(String dir, FileFilter filter) {
			return new File("./main/data/" + MODID, dir).listFiles(filter);
		}
		private static Stream<AssetFile> stream(File[] files, String dir) {
			return Arrays.stream(files).map(f -> new AssetFile(f, dir));
		}
		private final String parent;
		private AssetFile(File f, String parent) {
			super(f.getAbsolutePath());
			this.parent = parent;
		}

		@Override
		public String toString() {
			return parent+'/'+this.getName();
		}
	}

	TestsResources() {
		final File langdir = new File("./main/assets/" + MODID + "/lang");
		for(File lang : Objects.requireNonNull(langdir.listFiles())) {
			try(final FileInputStream fis = new FileInputStream(lang)) {
				langfiles.put(lang.getName(), new JSONObject(new JSONTokener(fis)));
			} catch (IOException e) {
				fail("Cannot open for reading: "+lang);
			}
		}
	}

	static Stream<BLogicBlock> streamLogicBlock() {
		return ContentLists.block.stream();
	}
	static Stream<BLogicItem> streamLogicItem() {
		return ContentLists.item.stream();
	}
	static Stream<StatusEffect> streamStatus() {
		return ContentLists.status.stream();
	}
	static Iterator<String> iteratorRecipeNameFromCode() {
		//NOTE: We want an exception to happen here.
		//noinspection OptionalGetWithoutIsPresent
		return ContentLists.getRecipeInfo().get().namesRecipesIterator();
	}
	static Iterator<String> iteratorRecipeUnlockNameFromCode() {
		//NOTE: We want an exception to happen here.
		//noinspection OptionalGetWithoutIsPresent
		return ContentLists.getRecipeInfo().get().namesUnlocksIterator();
	}
	static Stream<String> streamSoundNameFromCode() {
		return ContentLists.namesSound.stream();
	}
	static Stream<String> streamSoundNameFromJSON() {
		return soundjson.keySet().stream();
	}
	static Stream<AssetFile> streamAdvancements() {
		FileFilter fileFilter = new WildcardFileFilter("*.json");
		File[] adv = Objects.requireNonNull(AssetFile.getDataInDir("advancements", fileFilter));
		return AssetFile.stream(adv, "advancements");
	}

	static Stream<AssetFile> streamIcon() {
		FileFilter fileFilter = new WildcardFileFilter("*.png");
		File[] block = Objects.requireNonNull(AssetFile.getAssetsInDir("textures/block", fileFilter));
		File[] item = Objects.requireNonNull(AssetFile.getAssetsInDir("textures/item", fileFilter));
		return Stream.concat(
			AssetFile.stream(block, "textures/block"),
			AssetFile.stream(item, "textures/item")
		);
	}

	@ParameterizedTest(name = "{0} should have localized names.")
	@MethodSource({"streamLogicBlock", "streamLogicItem"})
	void logicHasNames(LogicCommon base) {
		final String prefix = base.pathPrefix()+'.'+IaS3.MODID;
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			final String name = prefix+'.'+base.name();
			try {
				lang.getValue().getString(name);
			} catch(JSONException e) {
				fail(lang.getKey()+": "+e.getMessage());
			}
		}
	}

	@ParameterizedTest(name = "{0} should have a short localized name.")
	@MethodSource({"streamStatus"})
	void statusHasName(StatusEffect base) {
		final String name = "effect."+IaS3.MODID+'.'+base.name();
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			try {
				//TODO: We should do an actual cumulative character width check here, really.
				if(lang.getValue().getString(name).length() > 12) {
					System.err.println(lang.getKey()+" contains a mapping for " + name + " that may be too long");
				}
			} catch(JSONException e) {
				fail(lang.getKey()+": "+e.getMessage());
			}
		}
	}

	@ParameterizedTest(name = "{0} should have an icon.")
	@MethodSource("streamStatus")
	void statusHasIcon(StatusEffect what) {
		final String path = "./main/assets/" + MODID + "/textures/mob_effect/"+what.name()+".png";
		if(!new File(path).exists()) fail(what + " is missing an icon");
	}

	void resourceExistenceTest(String name, String base, String where, boolean expected, String comment) {
		final String path = base + MODID + where + name+".json";
		if(new File(path).exists() != expected) fail(name+comment);
	}
	void assetExistenceTest(String name, String where, String comment) {
		resourceExistenceTest(name, "./main/assets/", where, true, comment);
	}
	void assetExistenceTest(LogicCommon what, String where, String comment) {
		assetExistenceTest(what.name(), where, comment);
	}


	@ParameterizedTest(name = "{0} should have item models or the correct overrides.")
	@MethodSource({"streamLogicBlock", "streamLogicItem"})
	void logicHasItemModels(LogicCommon base) {
		((LogicCommon) base).itemLogic().foreach(logic -> {
			assetExistenceTest(logic, "/models/item/", " is missing an item model");
			return null;
		});
	}

	@ParameterizedTest(name = "{0} should have block models.")
	@MethodSource("streamLogicBlock")
	void logicHasBlockModels(BLogicBlock block) {
		//TODO: This test could actually read the blockstates file and try to extract model names from it.
		block.getGenAssetsBlock().foreach((JsonGenAssetsBlock bjgab) -> {
			bjgab.modelNames().foreach(naem -> {
				assetExistenceTest(naem, "/models/block/", " is missing a model: "+naem+".json");
				return BoxedUnit.UNIT;
			});
			return BoxedUnit.UNIT;
		});
		;
	}

	@ParameterizedTest(name = "{0} should have a blockstates file.")
	@MethodSource("streamLogicBlock")
	void logicHasBlockstateFiles(BLogicBlock base) {
		assetExistenceTest(base, "/blockstates/", " is missing a blockstates.json file.");
	}

	@ParameterizedTest(name = "{0} (sound name) should exist in sounds.json")
	@MethodSource("streamSoundNameFromCode")
	void soundsJsonHasSound(String name) {
		if(!soundjson.has(name)) fail(name+" isn't in sounds.json");
	}

	@ParameterizedTest(name = "{0} (sound name)'s subtitle (if any) should be valid and localized")
	@MethodSource("streamSoundNameFromJSON")
	void soundSubtitleIsValid(String name) {
		try {
			final JSONObject soundEntry = soundjson.getJSONObject(name);
			if (soundEntry.has("subtitle")) {
				final String subtitle = soundEntry.getString("subtitle");
				if(!subtitle.startsWith(MODID+".subtitle.")) {
					fail("Subtitle ID for "+name+" should start with iceandshadow3.subtitle.");
				}
				for (Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
					try {
						lang.getValue().getString(subtitle);
					} catch(JSONException e) {
						fail(lang.getKey()+": "+e.getMessage());
					}
				}
			}
		} catch(JSONException e) {
			fail("Malformed sounds.json: "+e.getMessage());
		}
	}

	@ParameterizedTest(name = "{0} should have a 1:1 aspect ratio or an mcmeta file")
	@MethodSource("streamIcon")
	void iconHasRightMetadata(AssetFile icon) {
		try(final FileInputStream fis = new FileInputStream(icon)) {
			assertEquals(fis.skip(16), 16, "Icon file is corrupt");
			byte[] x = new byte[4];
			byte[] y = new byte[4];
			assertEquals(fis.read(x), 4, "Icon file is corrupt");
			assertEquals(fis.read(y), 4, "Icon file is corrupt");
			// Default ByteBuffer endianness is the same as network byte order.
			if(ByteBuffer.wrap(x).getInt() != ByteBuffer.wrap(y).getInt()) {
				if(!new File(icon.getPath()+".mcmeta").exists()) fail(icon.getPath() + " needs a .mcmeta file");
			}
		} catch(IOException e) {
			fail("Cannot read "+icon.getPath());
		}
	}

	@ParameterizedTest(name = "{0} should have a localized title and description")
	@MethodSource("streamAdvancements")
	void advancementHasI18n(AssetFile adv) {
		final String naem = adv.getName();
		final String id = "iceandshadow3.advancement."+naem.substring(0, naem.lastIndexOf('.'));
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			try {
				lang.getValue().getString(id+".title");
				lang.getValue().getString(id+".description");
			}
			catch(JSONException e) { fail(lang.getKey()+": "+e.getMessage()); }
		}
	}

	private void checkAdvancementDisplayText(AssetFile adv, JSONObject display, String id, String type) {
		try {
			final String title = display.getJSONObject(type).getString("translate");
			final String titleWanted = id + '.' + type;
			if (!title.equals(titleWanted)) fail(adv + ": Illegal "+type+" translation key - should be " + titleWanted);
		} catch(JSONException e) {
			fail(adv+": Malformed "+type+" - "+e.getMessage());
		}
	}

	@ParameterizedTest(name = "{0} should use translation")
	@MethodSource("streamAdvancements")
	void advancementUsesI18n(AssetFile adv) {
		final String naem = adv.getName();
		final String id = "iceandshadow3.advancement."+naem.substring(0, naem.lastIndexOf('.'));
		try(final FileInputStream fis = new FileInputStream(adv)) {
			final JSONObject advancement = new JSONObject(new JSONTokener(fis));
			JSONObject display = null;
			try { display = advancement.getJSONObject("display"); }
			catch(JSONException e) { fail(adv+": Non-displaying advancements should be in a subfolder."); }
			checkAdvancementDisplayText(adv, display, id, "title");
			checkAdvancementDisplayText(adv, display, id, "description");
		} catch (IOException e) {
			fail("Cannot read "+adv.getPath());
		}
	}

	@ParameterizedTest(name = "{0} (builtin recipe) should have a matching JSON file")
	@MethodSource("iteratorRecipeNameFromCode")
	void builtinRecipeIsEnabled(String name) {
		final String path = "data/"+MODID+"/recipes/"+name+".json";
		try(final FileInputStream fis = new FileInputStream("./main/"+path)) {
			final JSONObject advancement = new JSONObject(new JSONTokener(fis));
			try {
				final String type = advancement.getString("type");
				if(!(MODID+":builtin").contentEquals(type)) fail(path+": Builtin recipe shadowed by "+type+" recipe");
			}
			catch(JSONException e) { fail(path+": Malformed recipe file"); }
		} catch(IOException e) {
			fail(path+": Missing a file for builtin recipe");
		}
	}

	@ParameterizedTest(name = "{0} (recipe unlock advancement) should exist")
	@MethodSource("iteratorRecipeUnlockNameFromCode")
	void advancementHasUnlock(String name) {
		final String path = "data/"+MODID+"/advancements/recipes/"+name+".json";
		try(final FileInputStream fis = new FileInputStream("./main/"+path)) {
			//TODO: Validation?
		} catch(IOException e) {
			fail(path+": Missing a file for builtin recipe");
		}
	}
}
