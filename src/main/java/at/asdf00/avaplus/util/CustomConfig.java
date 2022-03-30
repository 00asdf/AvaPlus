package at.asdf00.avaplus.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class CustomConfig extends Configuration {
    public CustomConfig (File file) {
        super(file);
    }

    public long getLong(String name, String category, long defaultValue, long minValue, long maxValue, String comment) {
        return getLong(name, category, defaultValue, minValue, maxValue, comment, name);
    }
    public long getLong(String name, String category, long defaultValue, long minValue, long maxValue, String comment, String langKey)  {
        Property prop = this.get(category, name, defaultValue);
        prop.setLanguageKey(langKey);
        prop.setComment(comment + " [range: " + minValue + " ~ " + maxValue + ", default: " + defaultValue + "]");
        prop.setMinValue(minValue);
        prop.setMaxValue(maxValue);
        return prop.getLong(defaultValue) < minValue ? minValue : (prop.getLong(defaultValue) > maxValue ? maxValue : prop.getLong(defaultValue));
    }
}
