package io.jmix.petclinic.screen.main;

import io.jmix.ui.icon.JmixIcon;

import javax.annotation.Nullable;

public enum PetclinicThemeSwitchBtnMode {

    LIGHT("light", JmixIcon.MOON_O, "icon-only secondary dark-switch"),
    DARK("dark", JmixIcon.SUN_O, "icon-only secondary light-switch");

    private final String name;
    private final JmixIcon icon;
    private final String styleName;

    PetclinicThemeSwitchBtnMode(String name, JmixIcon icon, String styleName) {
        this.name = name;
        this.icon = icon;
        this.styleName = styleName;
    }

    @Nullable
    public static PetclinicThemeSwitchBtnMode fromId(String id) {
        for (PetclinicThemeSwitchBtnMode at : PetclinicThemeSwitchBtnMode.values()) {
            if (at.getName().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public JmixIcon getIcon() {
        return icon;
    }

    public String getStyleName() {
        return styleName;
    }

    public String getName() {
        return name;
    }
}
