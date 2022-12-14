package content.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BowSword extends SwordItem {
    private final float shots_per_second;
    private long time;

    public BowSword() {
        super(Tiers.IRON, 3, -3F, new Properties().tab(CreativeModeTab.TAB_COMBAT).durability(225));
        this.time = 0;
        this.shots_per_second = 1F;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        ItemStack projectiles = player.getProjectile(new ItemStack(Items.BOW, 1));
        if (!level.isClientSide && level.getGameTime() - this.time > 20 / this.shots_per_second && (!projectiles.isEmpty() || player.getAbilities().instabuild)) {
            this.time = level.getGameTime();
            level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            ArrowItem arrowitem = (ArrowItem) (projectiles.getItem() instanceof ArrowItem ? projectiles.getItem() : Items.ARROW);
            used(player, projectiles, itemStack);
            for (float deg = -15F; deg <= 15; deg += 15) {
                AbstractArrow abstractarrow = arrowitem.createArrow(level, projectiles, player);
                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, deg);
                level.addFreshEntity(abstractarrow);
            }
        }
        return InteractionResultHolder.pass(itemStack);
    }

    private void used(Player player, ItemStack projectiles, ItemStack itemStack){
        if (!player.getAbilities().instabuild) {
            projectiles.shrink(1);
            itemStack.hurtAndBreak(1, player, (p_40992_) -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            if (projectiles.isEmpty()) {
                player.getInventory().removeItem(projectiles);
            }
        }
    }
}
