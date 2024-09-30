package at.minify.skymini.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class EntityTracker {


    public static EntityArmorStand searchEntity(String contains, float radius, Entity entitypos) {
        Entity mob = entitypos;
        if (entitypos == null) {
            mob = Minecraft.getMinecraft().thePlayer;
        }
        AxisAlignedBB box = mob.getEntityBoundingBox().expand(radius, radius, radius);
        for (Entity entity : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(mob, box)) {
            if(!(entity instanceof EntityArmorStand)) {
                continue;
            }
            EntityArmorStand armorStand = (EntityArmorStand) entity;
            if (!armorStand.isInvisible()) {
                continue;
            }
            if(!armorStand.hasCustomName()) {
                continue;
            }
            String nametag = armorStand.getCustomNameTag().replaceAll("§.","");
            if(nametag.contains(contains)) {
                return armorStand;
            }
        }
        return null;
    }

    public static String searchEntityNames(String contains, float radius, Entity entitypos) {
        Entity mob = entitypos;
        StringBuilder output = new StringBuilder();
        if (entitypos == null) {
            mob = Minecraft.getMinecraft().thePlayer;
        }
        AxisAlignedBB box = mob.getEntityBoundingBox().expand(radius, radius, radius);
        for (Entity entity : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(mob, box)) {
            if(!(entity instanceof EntityArmorStand)) {
                continue;
            }
            EntityArmorStand armorStand = (EntityArmorStand) entity;
            if (!armorStand.isInvisible()) {
                continue;
            }
            if(!armorStand.hasCustomName()) {
                continue;
            }
            String nametag = armorStand.getCustomNameTag();
            if(nametag.contains(contains)) {
                output.append(nametag);
            }
        }
        return output.toString();
    }


    public static Integer searchCount(String contains, float radius) {
        int value = 0;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        AxisAlignedBB box = player.getEntityBoundingBox().expand(radius, radius, radius);
        for (Entity entity : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(player, box)) {
            if(!(entity instanceof EntityArmorStand)) {
                return null;
            }
            EntityArmorStand armorStand = (EntityArmorStand) entity;
            if (!armorStand.isInvisible()) {
                return null;
            }
            if(!armorStand.hasCustomName()) {
                return null;
            }
            String nametag = armorStand.getCustomNameTag();
            boolean contain = true;
            String[] parts = contains.split(" ");
            for (String part : parts) {
                if (!nametag.contains(part)) {
                    contain = false;
                    break;
                }
            }
            if (contain) {
                value++;
            }
        }
        return value;
    }

    public static String getYaw(Entity loc1, Entity loc2) {
        double dx = loc2.posX - loc1.posX;
        double dz = loc2.posZ - loc1.posZ;
        double angle = Math.toDegrees(Math.atan2(-dx, dz)) + 180;
        double yaw2 = loc1.rotationYaw;
        double relativeAngle = (angle - yaw2 + 360) % 360;
        double normalizedAngle = (relativeAngle + 360) % 360;
        if (normalizedAngle >= 337.5 || normalizedAngle < 22.5) { return "⬇";
        } else if (normalizedAngle >= 22.5 && normalizedAngle < 67.5) { return "⬋";
        } else if (normalizedAngle >= 67.5 && normalizedAngle < 112.5) { return "⬅";
        } else if (normalizedAngle >= 112.5 && normalizedAngle < 157.5) { return "⬉";
        } else if (normalizedAngle >= 157.5 && normalizedAngle < 202.5) { return "⬆";
        } else if (normalizedAngle >= 202.5 && normalizedAngle < 247.5) { return "⬈";
        } else if (normalizedAngle >= 247.5 && normalizedAngle < 292.5) { return "➡";
        } else if (normalizedAngle >= 292.5/* && normalizedAngle < 337.5*/) { return "⬊";
        } else { return "-";
        }
    }

    public static int getDistance(Entity entity1, Entity entity2) {
        double deltaX = entity1.posX - entity2.posX;
        double deltaY = entity1.posY - (entity2.posY - 4);
        double deltaZ = entity1.posZ - entity2.posZ;

        double distance = MathHelper.sqrt_double(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        return (int) Math.floor(distance);
    }

}

/*public static Entity searchvisibleEntity(Entity entitypos) {
        Entity mob = entitypos != null ? entitypos : Minecraft.getMinecraft().thePlayer;
        AxisAlignedBB box = mob.getEntityBoundingBox().expand(5, 5, 5);

        Entity nearestEntity = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : Minecraft.getMinecraft().theWorld.getEntitiesWithinAABBExcludingEntity(mob, box)) {
            if (!entity.isInvisible()) {
                double distanceSq = entity.getDistanceSqToEntity(mob);
                if (distanceSq < nearestDistanceSq) {
                    nearestEntity = entity;
                    nearestDistanceSq = distanceSq;
                }
            }
        }

        return nearestEntity;
    }*/