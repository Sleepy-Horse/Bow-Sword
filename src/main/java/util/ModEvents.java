package util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import scirpts.Main;

@Mod.EventBusSubscriber(modid = Main.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void extraJump(LivingEvent event) {
        if(event instanceof LivingEvent.LivingJumpEvent){
            LivingEntity entity = event.getEntity();
            float power = 10F;
            PrimedTnt tnt = new PrimedTnt(entity.level, entity.getX(), entity.getY(), entity.getZ(), entity);
            tnt.level.explode(tnt, tnt.getX(), tnt.getY(), tnt.getZ(), power, Explosion.BlockInteraction.DESTROY);
        }
    }
}

