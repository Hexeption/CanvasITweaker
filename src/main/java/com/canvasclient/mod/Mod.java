package com.canvasclient.mod;

import org.lwjgl.input.Keyboard;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Mod {

    private String name = getClass().getAnnotation(ModInfo.class).name();
    private String description = getClass().getAnnotation(ModInfo.class).descritpion();
    private Category category = getClass().getAnnotation(ModInfo.class).category();
    private int bind = getClass().getAnnotation(ModInfo.class).bind();
    private boolean state;

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Category getCategory() {

        return category;
    }

    public void setCategory(Category category) {

        this.category = category;
    }

    public int getBind() {

        return bind;
    }

    public void setBind(int bind) {

        this.bind = bind;
    }

    public boolean isState() {

        return state;
    }

    public void setState(boolean state) {

        onToggle();
        if(state){
            this.state = state;
            onEnable();
        }else{
            this.state = false;
            onDisable();
        }
    }

    public void onToggle() {

    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void toggle(){
        setState(!this.state);
    }

    public enum Category {
        TEST(0x12039423);

        public int color;

        Category(int color) {

            this.color = color;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModInfo {

        String name();

        String descritpion();

        Category category();

        int bind() default Keyboard.KEY_NONE;
    }

}
