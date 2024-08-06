package net.xiaojibazhanshi.slimebounce.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.util.Vector;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BounceFactory {

    private static Cache<UUID, Long> cooldown =
            CacheBuilder.newBuilder().expireAfterWrite(400, TimeUnit.MILLISECONDS).build();

    private static Vector calculateBounceVector(Slime slime, Player player) {
        double maxY = 0.9;

        Vector playerMotion = player.getVelocity();
        if (playerMotion.length() < 0.05) return null;

        double bounceMagnitude = slime.getSize() * 0.325;

        Vector bounceDirection = playerMotion.clone().normalize().multiply(new Vector(0, -1, 0));
        Vector finalDirection = bounceDirection.multiply(bounceMagnitude);

        if (finalDirection.getY() < 0.0) return null;
        if (finalDirection.getY() > 0.85) finalDirection.setY(maxY);

        return finalDirection;
    }

    public static void bouncePlayer(Slime slime, Player player, boolean damageTheSlime) {
        if (cooldown.asMap().containsKey(player.getUniqueId())) {
            return;
        }

        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 500);
        Vector velocity = calculateBounceVector(slime, player);

        if (velocity != null) {
            player.setVelocity(velocity);

            if (damageTheSlime) {
                slime.damage(slime.getSize());
            }
        }
    }

    public static Slime getFirstNearbySlime(Player player) {
        double range = 0.4;

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity.getType() != EntityType.SLIME) {
                continue;
            }

            return (Slime) entity;
        }
        return null;
    }

    public static boolean isValidContact(Slime slime, Player player) {
        double playerY = player.getLocation().getY();
        double slimeY = slime.getLocation().getY();

        return playerY > (slimeY + (slime.getHeight() * 0.65)) && player.getFallDistance() >= 0.3 && !slime.isDead();
    }

}