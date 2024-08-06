package net.xiaojibazhanshi.slimebounce;


import net.xiaojibazhanshi.slimebounce.runnables.SlimeBounceRunnable;
import net.xiaojibazhanshi.slimebounce.util.BounceFactory;
import org.bukkit.plugin.java.JavaPlugin;


public final class SlimeBounce extends JavaPlugin {

    @Override
    public void onEnable() {
        BounceFactory.setMainClassInstance(this);

        SlimeBounceRunnable slimeBounceRunnable = new SlimeBounceRunnable();
        slimeBounceRunnable.start(this);
    }

}
