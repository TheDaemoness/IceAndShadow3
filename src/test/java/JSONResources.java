import mod.iceandshadow3.ContentLists;
import mod.iceandshadow3.IaS3;
import mod.iceandshadow3.basics.BLogicBlock;
import mod.iceandshadow3.basics.BLogicItem;
import mod.iceandshadow3.basics.BStatusEffect;
import mod.iceandshadow3.basics.util.BLogic;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Stream;

import static mod.iceandshadow3.IaS3.MODID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Tests for forgetfulness in adding/updating asset/data JSON files.")
class JSONResources {
	private final Map<String, JSONObject> langfiles = new TreeMap<>();
	private JSONObject soundjson;

	private static class AssetFile extends File {
		private static File[] getFilesInDir(String dir, FileFilter filter) {
			return new File("./main/assets/" + MODID, dir).listFiles(filter);
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

	JSONResources() {
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

	static Stream<AssetFile> streamIcon() {
		FileFilter fileFilter = new WildcardFileFilter("*.png");
		File[] block = Objects.requireNonNull(AssetFile.getFilesInDir("textures/block", fileFilter));
		File[] item = Objects.requireNonNull(AssetFile.getFilesInDir("textures/item", fileFilter));
		ArrayList<AssetFile> total = new ArrayList<>(block.length+item.length);
		for(File f: block) total.add(new AssetFile(f, "textures/block"));
		for(File f: item) total.add(new AssetFile(f, "textures/item"));
		return total.stream();
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
}
