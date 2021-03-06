package com.caret.createlink.item;

import com.caret.createlink.CreateLink;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CreateLink.MOD_ID);

    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst",
            () -> new Item(new Item.Properties().group(ModItemGroup.CREATELINK_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
