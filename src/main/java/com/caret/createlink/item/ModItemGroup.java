package com.caret.createlink.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup CREATELINK_GROUP = new ItemGroup("createLinkTab")
    {
        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(ModItems.AMETHYST.get());
        }
    };
}
