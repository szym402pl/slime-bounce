package net.xiaojibazhanshi.slimebounce.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.xiaojibazhanshi.slimebounce.SlimeBounce;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class BounceFactory {

    private static SlimeBounce instance;

    public static void setMainClassInstance(SlimeBounce main) {
        instance = main;
    }

    private static Cache<UUID, Long> cooldown =
            CacheBuilder.newBuilder().expireAfterWrite(400, TimeUnit.MILLISECONDS).build();

    private static CompletableFuture<Vector> calculateBounceVectorAsync(Slime slime, Vector playerMotion) {
        return CompletableFuture.supplyAsync(() -> {
            if (playerMotion.length() < 0.05) return null;

            double maxY = 0.85;
            double bounceMagnitude = slime.getSize() * 0.325;

            Vector bounceDirection = playerMotion.clone().normalize().multiply(new Vector(0.5, -1, 0.5));
            Vector finalDirection = bounceDirection.multiply(bounceMagnitude);

            if (finalDirection.getY() < 0.0) return null;
            if (finalDirection.getY() > maxY) finalDirection.setY(maxY);

            return finalDirection;
        });
    }

    public static void bouncePlayer(Slime slime, Player player, boolean damageTheSlime) {
        if (cooldown.asMap().containsKey(player.getUniqueId())) { return; }
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 500);

        calculateBounceVectorAsync(slime, player.getVelocity().clone()).thenAccept(outputVelocity -> {
            if (outputVelocity == null) return;

            player.getServer().getScheduler().runTask(instance, () -> {
                player.setVelocity(outputVelocity);
                if (damageTheSlime) { slime.damage(slime.getSize()); }
            });

        });
    }

    public static Slime getFirstNearbySlime(Player player) {
        double range = 0.4;

        for (Entity entity : player.getNearbyEntities(range, range + 0.25, range)) {
            if (entity.getType() != EntityType.SLIME) { continue; }

            return (Slime) entity;
        }

        return null;
    }

    public static boolean isValidContact(Slime slime, Player player) {
        double playerY = player.getLocation().getY();
        double slimeY = slime.getLocation().getY();

        return playerY > (slimeY + (slime.getHeight() * 0.65)) && player.getFallDistance() >= 0.275 && !slime.isDead();
    }

}