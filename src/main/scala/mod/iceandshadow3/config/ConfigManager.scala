package mod.iceandshadow3.config

import java.io.{Closeable, File, FileNotFoundException, PrintWriter}
import java.util.Scanner

import mod.iceandshadow3.IaS3

import scala.collection.JavaConverters._

/** A re-solving of a long-solved problem. Well, depending on who you ask.
*/
class ConfigManager[ConfigType <: BConfig](private val config: ConfigType) extends Closeable {
	private val magic = "config"
	private val cache = new java.util.HashMap[String, String]
	private val configFile = new File("config/"+IaS3.MODID+'.'+config.name+".cfg")
	private var needs_write: Boolean = if (configFile.exists()) read() else true
	def flush(): Unit = if(needs_write) write()

	flush()

	override def close(): Unit = flush()
	def filename(): String = configFile.getName
	def needsWrite(): Boolean = needs_write
	
	def get(): ConfigType = config
	
	def set(option: String, value: String): Boolean = {
		try {
			val changed = config.set(option, value)
			if(!changed) {
				cache.put(option, value)
				needs_write = true
			}
			return changed
		} catch {
			//Quietly suppress these errors until we have a better way of reporting them than IaS3.logger.error
			case _: BadConfigException =>
			case _: IllegalArgumentException =>
		}
		false
	}
	
	//TODO: We can get clever with writing, and allow writing that matches the format (incl. comments) of a read file.

	protected[iceandshadow3] def read(): Boolean = {
		try {
			val lis = new Scanner(configFile)
			if (!lis.hasNextLine) {
				IaS3.logger().warn(filename()+" is empty. Will overwrite.")
				return true
			}
			var file_minor_version: Int = 0
			var file_major_version: Int = 0
			val config_ver: Array[String] = lis.nextLine().split("\\s+", 5)
			if (config_ver.length != 5) {
				throw new BadConfigException(
					filename() + " doesn't start with valid version info")
			}
			if (!config_ver(0).contentEquals(magic)) {
				throw new BadConfigException(
					filename() + " doesn't start with version info")
			}
			if (java.lang.Integer.parseInt(config_ver(1)) != IaS3.VER_CFG_FMT) {
				throw new BadConfigException(
					filename() + " has an incompatible format")
			}
			if(!config_ver(2).contentEquals(config.name)) {
				throw new BadConfigException(
					filename() + " specifies that it is a " + config_ver(2) + " configuration")
			}
			file_major_version = java.lang.Integer.parseInt(config_ver(3))
			file_minor_version = java.lang.Integer.parseInt(config_ver(4))
			if (file_major_version != config.versionMajor) {
				IaS3.logger().warn(
					filename() +
					" is of a different major version than this mod supports. Delete/rename the current config to generate a new one. Expect configuration errors.")
			} else if (file_minor_version > config.versionMajor) {
				IaS3.logger().warn(
					filename() +
					" is of a newer minor version than this mod supports. Expect configuration errors.")
			} else if (file_minor_version < config.versionMinor) {
				IaS3.logger().info(
					filename() +
					" is of an older minor version than this mod supports. Will update.")
			}
			var line: Int = 0
			while (lis.hasNextLine) {
				line += 1
				var orig: String = lis.nextLine()
				val commentstart: Int = orig.indexOf('#')
				if (commentstart != -1) {
					orig = orig.substring(0, commentstart)
				}
				var option = ""
				if (!orig.isEmpty) try {
					val key_value: Array[String] = orig.split("\\s+", 2)
					if (key_value.length != 2) {
						throw new BadConfigException(s"Line $line is incomplete")
					}
					option = key_value(0)
					config.set(option, key_value(1))
				} catch {
					case e: BadConfigException =>
						IaS3.logger().error(
						s"Line $line in " + filename() + " is invalid: "+e.getMessage+". Ignoring.");
					case _: IllegalArgumentException =>
						IaS3.logger().error(
							s"Line $line in " + filename() + s" references an unknown option $option. Ignoring.");
				}
			}
			return file_major_version == config.versionMajor && file_minor_version < config.versionMinor
		} catch {
			case _: FileNotFoundException =>
				IaS3.logger().error(
					"Couldn't open " + filename() +
					" for reading. Will use default values. Please check the file's read permissions.")
			case e: BadConfigException =>
				IaS3.logger().error(
					e.getMessage + ". Will use default values. Delete " +
					filename() + " to generate a new default config.")
		}
		false
	}

	protected[iceandshadow3] def write(): Unit = {
		configFile.delete()
		val ecris = new PrintWriter(configFile)
		try {
			ecris.println(magic+' '+IaS3.VER_CFG_FMT+' '+config.name+' '+config.versionMajor+' '+config.versionMinor)
			ecris.println("# https://github.com/TheDaemoness/IceAndShadow3/wiki/Configuration")
			for (entry <- config.options.asScala) {
				ecris.println()
				ecris.println("# "+config.getComment(entry))
				ecris.println(entry + " " + cache.getOrDefault(entry, config.get(entry)))
			}
			needs_write = false
		} catch {
			case _: FileNotFoundException =>
				IaS3.logger().error("Could not open config file for writing")
		} finally {
			ecris.close()
		}
	}
}
