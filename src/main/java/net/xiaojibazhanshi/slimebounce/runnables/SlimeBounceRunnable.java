package net.xiaojibazhanshi.slimebounce.runnables;

import net.xiaojibazhanshi.slimebounce.SlimeBounce;
import net.xiaojibazhanshi.slimebounce.util.BounceFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.scheduler.BukkitRunnable;


public class SlimeBounceRunnable extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Slime slime = BounceFactory.getFirstNearbySlime(player);
            if (slime == null) continue;

            if (BounceFactory.isValidContact(slime, player)) {
                BounceFactory.bouncePlayer(slime, player, true);
            }
        }
    }

    public void start(SlimeBounce main) {
        runTaskTimer(main, 0, 2);
    }
}
