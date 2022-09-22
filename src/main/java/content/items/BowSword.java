package content.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import scirpts.registers.ModItems;

public class BowSword extends SwordItem {
    public BowSword() {
        super(Tiers.IRON, 3, -3F, new Properties().tab(CreativeModeTab.TAB_COMBAT).durability(225));
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity entity, int timeDuration) {
        if (!(entity instanceof Player player)) {
            return;
        }
        ItemStack projectiles = player.getProjectile(new ItemStack(Items.BOW, 1));
        int i = this.getUseDuration(itemStack) - timeDuration;
        i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(itemStack, level, player, i, !projectiles.isEmpty() || player.getAbilities().instabuild);
        if (i < 0) return;
        float f = BowItem.getPowerForTime(i);
        System.out.println(f);
        if (!level.isClientSide && !((double)f < 0.1D) && (projectiles.getCount() > 2 || player.getAbilities().instabuild)) {
            level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            ArrowItem arrowitem = (ArrowItem) (projectiles.getItem() instanceof ArrowItem ? projectiles.getItem() : Items.ARROW);
            used(player, projectiles, itemStack);
            for (float deg = -15F; deg <= 15; deg += 15) {
                AbstractArrow abstractarrow = arrowitem.createArrow(level, projectiles, player);
                abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, deg);
                level.addFreshEntity(abstractarrow);
            }
        }
    }

    private void used(Player player, ItemStack projectiles, ItemStack itemStack){
        if (!player.getAbilities().instabuild) {
            projectiles.shrink(3);
            itemStack.hurtAndBreak(1, player, (p_40992_) -> {
                p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
            if (projectiles.isEmpty()) {
                player.getInventory().removeItem(projectiles);
            }
        }
    }


    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) return ret;

        if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }
}
