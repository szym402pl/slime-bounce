package net.xiaojibazhanshi.slimebounce;


import net.xiaojibazhanshi.slimebounce.runnables.SlimeBounceRunnable;
import org.bukkit.plugin.java.JavaPlugin;


public final class SlimeBounce extends JavaPlugin {

    @Override
    public void onEnable() {
        SlimeBounceRunnable slimeBounceRunnable = new SlimeBounceRunnable();
        slimeBounceRunnable.start(this);
    }

}
