package mod.iceandshadow3.lib.compat;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import com.google.common.hash.HashCode;
import mod.iceandshadow3.IaS3;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

import javax.annotation.Nonnull;

public abstract class FileGen {
	protected final String name;
	protected FileGen(String name) {
		this.name = name;
	}

	public static Path getDataPath(Path root) {
		return root.resolve("data/"+IaS3.MODID);
	}
	public static Path getAssetsPath(Path root) {
		return root.resolve("assets/"+IaS3.MODID);
	}

	@Nonnull
	protected abstract Map<Path, byte[]> getData(Path root);

	public void attachTo(DataGenerator gen) {
		gen.addProvider(new IDataProvider() {
			@Override
			public void act(@Nonnull DirectoryCache cache) throws IOException {
				for(Map.Entry<Path, byte[]> pair : getData(gen.getOutputFolder()).entrySet()) {
					final Path path = pair.getKey();
					final byte[] data = pair.getValue();
					final String hashed = String.valueOf(IDataProvider.HASH_FUNCTION.hashBytes(data).asLong());
					cache.func_208316_a(path, hashed);
					final boolean exists = Files.exists(path);
					final String previousHash = exists ? cache.getPreviousHash(path) : null;
					if (previousHash == null || !hashed.contentEquals(previousHash)) {
						if (!exists) Files.createDirectories(path.getParent());
						try (final OutputStream fos = Files.newOutputStream(path)) {
							fos.write(data);
						} catch (IOException e) {
							IaS3.logger().error("Cannot update " + path + ": " + e.getMessage());
						}
					}
				}
			}

			@Override
			@Nonnull
			public String getName() {
				return name;
			}
		});
	}
}
