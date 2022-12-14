package scripts.registers;


import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import scripts.Main;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);


    public static final RegistryObject<Item> BowSword = ITEMS.register("bosword",
            content.items.BowSword::new);


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
