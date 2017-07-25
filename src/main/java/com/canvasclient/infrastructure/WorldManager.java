package com.canvasclient.infrastructure;

import com.canvasclient.utilities.IMinecraft;
import net.minecraft.client.AnvilConverterException;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldSummary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldManager implements IMinecraft {

    private static final Logger logger = LogManager.getLogger();
	private static List<WorldSummary> worlds = new CopyOnWriteArrayList<WorldSummary>();
	
	public static void loadWorlds() {
		new Thread(() -> {
            try
            {
                ISaveFormat var1 = mc.getSaveLoader();
                worlds = var1.getSaveList();
                Collections.sort(worlds);
            }
            catch (AnvilConverterException var2)
            {
                logger.error("Couldn\'t load level list", var2);
                //mc.displayGuiScreen(new GuiErrorScreen("Unable to load worlds", var2.getMessage()));
                return;
            }
        }).start();
	}
	
	public static List<WorldSummary> getList() {
		return worlds;
	}
	
	public static void removeWorld(WorldSummary s) {
		worlds.remove(s);
	}
	
	public static void removeWorld(int index) {
		worlds.remove(worlds.get(index));
	}
	
	public static WorldSummary getWorld(int index) {
		if (worlds.size() == 0 || index >= worlds.size()) {
			return null;
		}
		return worlds.get(index);
	}
	
	public static WorldSummary getWorld(String fileName) {
		for(WorldSummary w : worlds) {
			if (w.getFileName().equalsIgnoreCase(fileName)) {
				return w;
			}
		}
		return null;
	}
	
	public static int getIndex(WorldSummary s) {
		return worlds.indexOf(s);
	}
	
	public static int getSize() {
		return getList().size();
	}
}
