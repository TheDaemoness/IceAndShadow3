import mod.iceandshadow3.ContentLists;
import mod.iceandshadow3.ExtensionToolMode;
import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.lib.BLogicBlock;
import mod.iceandshadow3.lib.BLogicItem;
import mod.iceandshadow3.lib.BStatusEffect;
import mod.iceandshadow3.lib.base.BLogic;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Stream;

import static mod.iceandshadow3.IaS3.MODID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Tests for forgetfulness in adding/updating asset/data JSON files.")
@ExtendWith({ExtensionToolMode.class})
class TestsResources {
	private final Map<String, JSONObject> langfiles = new TreeMap<>();
	private JSONObject soundjson;

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

		try(final FileInputStream fis = new FileInputStream("./main/assets/"+MODID+"/sounds.json")) {
			soundjson = new JSONObject(new JSONTokener(fis));
		} catch (IOException e) {
			fail("Cannot open sounds.json");
		}
	}

	static Stream<BLogicBlock> streamLogicBlock() {
		return ContentLists.block.stream();
	}
	static Stream<BLogicItem> streamLogicItem() {
		return ContentLists.item.stream();
	}
	static Stream<BStatusEffect> streamStatus() {
		return ContentLists.status.stream();
	}
	static Stream<String> streamSoundName() {
		return ContentLists.soundname.stream();
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
	void logicHasNames(BLogic base) {
		final String prefix = base.getPathPrefix()+'.'+IaS3.MODID;
		for(Map.Entry<String, JSONObject> lang : langfiles.entrySet()) {
			for(int i = 0; i < base.countVariants(); ++i) {
				final String name = prefix+'.'+base.getName(i);
				try {
					lang.getValue().getString(name);
				} catch(JSONException e) {
					fail(lang.getKey()+": "+e.getMessage());
				}
			}
		}
	}

	@ParameterizedTest(name = "{0} should have a short localized name.")
	@MethodSource({"streamStatus"})
	void statusHasName(BStatusEffect base) {
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
	void statusHasIcon(BStatusEffect what) {
		final String path = "./main/assets/" + MODID + "/textures/mob_effect/"+what.name()+".png";
		if(!new File(path).exists()) fail(what + " is missing an icon");
	}

	void resourceExistenceTest(BLogic what, String base, String where, boolean expected, String ismissingwhat) {
		for(int i = 0; i < what.countVariants(); ++i) {
			final String name = what.getName(i);
			final String path = base + MODID + where + name+".json";
			if(new File(path).exists() != expected) fail(name+ismissingwhat);
		}
	}
	void assetExistenceTest(BLogic what, String where, String ismissingwhat) {
		resourceExistenceTest(what, "./main/assets/", where, true, ismissingwhat);
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

	@ParameterizedTest(name = "Test for loot tables for {0}")
	@MethodSource("streamLogicBlock")
	void logicMaybeHasLootTable(BLogicBlock bl) {
		bl.shouldHaveLootTable().forBoolean(bool ->
			resourceExistenceTest(bl, "./main/data/", "/loot_tables/blocks/", bool, bool ?
				" is missing a loot table." :
				" has a loot table when it shouldn't."
			)
		);
	}

	@ParameterizedTest(name = "{0} (sound name) should exist in sounds.json")
	@MethodSource("streamSoundName")
	void soundsJsonHasSound(String name) {
		if(!soundjson.has(name)) fail(name+" isn't in sounds.json");
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
}
