package com.canvasclient.mod;

import com.canvasclient.utilities.LogHelper;
import com.google.common.collect.Lists;
import org.reflections.Reflections;

import java.util.List;

public class ModManager {

    private final List<Mod> MODS = Lists.newArrayList();

    public void init() {

        initMods();
        LogHelper.info(String.format("Mods Loaded: %s!", MODS.size()));
        MODS.forEach(mod -> LogHelper.info(String.format("%s Loaded!", mod.getName())));

    }

    private void initMods() {

        new Reflections("com.canvasclient.mod.mods").getSubTypesOf(Mod.class).forEach(mod -> {
            try {
                this.MODS.add(mod.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Mod> getMods(){
        return MODS;
    }
}
