package com.canvasclient.maps;

import com.canvasclient.resource.IOHelper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;

public class MapHandler {
	
	public static void downloadMap(String mapURL, String mapName) {
		try {
			final File savesDir = new File(IOHelper.getMinecraftDirectory() + File.separator + "saves" + File.separator);
			File tempZip = new File(IOHelper.getMinecraftDirectory() + File.separator + "saves" + File.separator + "temp.zip");
			tempZip.createNewFile();
			FileUtils.copyURLToFile(new URL(mapURL), tempZip);
			ZipFile zipFile = new ZipFile(tempZip);
			File outputDir = savesDir;
		    try {
		    	Enumeration<? extends ZipArchiveEntry> entries = zipFile.getEntries();
		    	while (entries.hasMoreElements()) {
		    		ZipArchiveEntry entry = entries.nextElement();
		    		File entryDestination = new File(outputDir, entry.getName());
		    		if (entry.isDirectory()) {
		    			entryDestination.mkdirs();
		    		} else {
		    			entryDestination.getParentFile().mkdirs();
		    			InputStream in = zipFile.getInputStream(entry);
		    			OutputStream out = new FileOutputStream(entryDestination);
		    			IOUtils.copy(in, out);
		    			IOUtils.closeQuietly(in);
		    			out.close();
		    		}
		    	}
		    } finally {
		    	zipFile.close();
		    }
		    tempZip.delete();
		    //NotificationManager.list.add(new Notification(null, String.format("Finished downloading map \u00a73%s", mapName), false));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
