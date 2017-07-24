package com.canvasclient.resource;

import com.canvasclient.Canvas;
import com.canvasclient.api.APIHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;

public class IOHelper {

	private static final File DIRECTORY = new File("./Canvas");
	private static final String FILE_SUFFIX = ".json";
	private static final File DIRECTORY_CLIENT = new File(DIRECTORY + "/client" + FILE_SUFFIX);
    private static final int BUFFER_SIZE = 4096;
	
	public static void saveConfigurationFiles() {
		if (!DIRECTORY.exists()) {
			DIRECTORY.mkdirs();
		}
		
		saveClientConfiguration();
	}
	
	public static void loadConfigurationFiles() {
		if (!DIRECTORY.exists()) {
			saveConfigurationFiles();
			return;
		}
		loadSounds();
	}
	
	public static void saveClientConfiguration() {
		if (!DIRECTORY.exists()) {
			DIRECTORY.mkdirs();
		}
		
		if (Canvas.getCanvas().getUserControl() != null) {
			if (!DIRECTORY_CLIENT.exists()) {
				try {
					DIRECTORY_CLIENT.createNewFile();
				} catch (Exception e) {}
			}
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("client_id", Canvas.getCanvas().getUserControl().clientID);
			
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(jsonObject.toJSONString());
			String pretty = gson.toJson(je);
			
			try {
				DIRECTORY_CLIENT.delete();
				DIRECTORY_CLIENT.createNewFile();
				
				FileWriter file = new FileWriter(DIRECTORY_CLIENT.getAbsolutePath());
				file.write(pretty);
				file.flush();
				file.close();
			} catch (Exception exception) {}
		} else {
		}
	}
	
	public static String loadClientID() {
		if (DIRECTORY_CLIENT.exists()) {
			JSONParser parser = new JSONParser();
			
			try {
				FileReader fileReader = new FileReader(DIRECTORY_CLIENT.getAbsolutePath());
				Object file = parser.parse(fileReader);
				JSONObject jsonObject = (JSONObject) file;
				
				fileReader.close();
				return (String) jsonObject.get("client_id");
			} catch (Exception e) {
				return "";
			}
		} else {
			return "";
		}
	}
	
	public static void loadSounds() {
		if (!ResourceHelper.soundNotification.exists()) {
			try {
				ResourceHelper.soundNotification.getParentFile().mkdirs();
				ResourceHelper.soundNotification.createNewFile();
				
				URL link = new URL(APIHelper.NOTIFICATION_SOUND);
		
				InputStream in = new BufferedInputStream(link.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while (-1 != (n = in.read(buf))) {
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();
		
				FileOutputStream fos = new FileOutputStream(ResourceHelper.soundNotification);
				fos.write(response);
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
    public static File getMinecraftDirectory() {
    	String userHome = System.getProperty("user.home", ".");
    	String os = System.getProperty("os.name").toLowerCase();
    	File dir = null;
    	if (os.contains("win")) {
    		String appdata = System.getenv("APPDATA");
    		if (appdata == null) {
    			dir = new File(userHome, ".minecraft/");
    		}
    		else
    			dir = new File(appdata, ".minecraft/");
    	} else if (os.contains("mac")) {
    		dir = new File(userHome, "Library/Application Support/minecraft");
    	} else if ((!os.contains("linux")) && (!os.contains("unix"))) {
    		if ((os.contains("sunos")) || (os.contains("solaris"))) {
    			dir = new File(userHome, ".minecraft/");
        	} else {
        		dir = new File(userHome, "minecraft/");
        	}
    	}
    	if ((!dir.exists()) && (!dir.mkdirs())) {
    		return null;
     	}
    	return dir;
    }
}
