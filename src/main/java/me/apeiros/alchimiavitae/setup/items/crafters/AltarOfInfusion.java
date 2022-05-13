package me.apeiros.alchimiavitae.setup.items.crafters;

import io.github.mooy1.infinitylib.core.AbstractAddon;
import io.github.mooy1.infinitylib.machines.CraftingBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.libraries.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.apeiros.alchimiavitae.AlchimiaVitae;
import me.apeiros.alchimiavitae.setup.Items;
import me.apeiros.alchimiavitae.utils.InfusionMap;
import me.apeiros.alchimiavitae.utils.Utils;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AltarOfInfusion extends CraftingBlock {

    // Keys
    public static final NamespacedKey DESTRUCTIVE_CRITS = AbstractAddon.createKey("infusion_destructivecrits");
    public static final NamespacedKey PHANTOM_CRITS = AbstractAddon.createKey("infusion_phantomcrits");
    public static final NamespacedKey TRUE_AIM = AbstractAddon.createKey("infusion_trueaim");
    public static final NamespacedKey FORCEFUL = AbstractAddon.createKey("infusion_forceful");
    public static final NamespacedKey VOLATILE = AbstractAddon.createKey("infusion_volatile");
    public static final NamespacedKey HEALING = AbstractAddon.createKey("infusion_healing");
    public static final NamespacedKey REPLANT = AbstractAddon.createKey("infusion_autoreplant");
    public static final NamespacedKey TOTEM_STORAGE = AbstractAddon.createKey("infusion_totemstorage");
    public static final NamespacedKey KNOCKBACK = AbstractAddon.createKey("infusion_knockback");

    // Tool Utils.ItemGroups
    private static final List<Material> VALID_AXE = Arrays.asList(Material.GOLDEN_AXE, Material.IRON_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    private static final List<Material> VALID_BOW = Arrays.asList(Material.BOW, Material.CROSSBOW);
    private static final List<Material> VALID_HOE = Arrays.asList(Material.GOLDEN_HOE, Material.IRON_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE);
    private static final List<Material> VALID_CHESTPLATE = Arrays.asList(Material.GOLDEN_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
    private static final List<Material> VALID_FISHING_ROD = Collections.singletonList(Material.FISHING_ROD);

    // Slots
    private static final int[] IN_SLOTS = {10, 11, 12, 19, 20, 21, 28, 29, 30};
    private static final int[] IN_SLOTS_EXCLUDING_MID = {10, 11, 12, 19, 21, 28, 29, 30};

    private static final int[] IN_BG = {0, 1, 2, 3, 4, 9, 13, 18, 22, 27, 31, 36, 37, 38, 39, 40};
    private static final int[] CRAFT_BG = {5, 6, 7, 8, 14, 17, 23, 26, 32, 35, 41, 42, 43, 44};

    private static final int[] CRAFT_BUTTON = {15, 16, 24, 25, 33, 34};

    private static final int TOOL_SLOT = 20;

    // Recipes
    private static final InfusionMap recipes = new InfusionMap();

    // Constructor
    public AltarOfInfusion(ItemGroup c) {

        super(c, Items.ALTAR_OF_INFUSION, Utils.RecipeTypes.DIVINE_ALTAR_TYPE, new ItemStack[]{
                Items.EXP_CRYSTAL, SlimefunItems.WITHER_PROOF_GLASS, Items.EXP_CRYSTAL,
                SlimefunItems.REINFORCED_PLATE, new ItemStack(Material.BEACON), SlimefunItems.REINFORCED_PLATE,
                SlimefunItems.BLISTERING_INGOT_3, Items.DIVINE_ALTAR, SlimefunItems.BLISTERING_INGOT_3
        });

        // Get plugin and config
        AlchimiaVitae av = AlchimiaVitae.i();
        Configuration cfg = av.getConfig();

        // Get config values
        boolean destructiveCritsEnabled = cfg.getBoolean("options.infusions.infusion-destructivecrits");
        boolean phantomCritsEnabled = cfg.getBoolean("options.infusions.infusion-phantomcrits");
        boolean trueAimEnabled = cfg.getBoolean("options.infusions.infusion-trueaim");
        boolean forcefulEnabled = cfg.getBoolean("options.infusions.infusion-forceful");
        boolean volatileEnabled = cfg.getBoolean("options.infusions.infusion-volatile");
        boolean healingEnabled = cfg.getBoolean("options.infusions.infusion-healing");
        boolean autoReplantEnabled = cfg.getBoolean("options.infusions.infusion-autoreplant");
        boolean totemStorageEnabled = cfg.getBoolean("options.infusions.infusion-totemstorage");
        /*
         **Useless atm**
         boolean shieldDisruptorEnabled = cfg.getBoolean("options.infusions.infusion-shielddisruptor");
         boolean spikedHookEnabled = cfg.getBoolean("options.infusions.infusion-spikedhook");
        */
        boolean knockbackEnabled = cfg.getBoolean("options.infusions.infusion-knockback");

        // ItemStacks
        CustomItemStack validInfuseAxe = new CustomItemStack(Material.DIAMOND_AXE, "&b&l一個有效的斧頭來注入", "&6金&a, &f鐵&a, &b鑽石&a,", "&a或&c獄髓&a斧頭");
        CustomItemStack validInfuseBow = new CustomItemStack(Material.BOW, "&b&l一個有效的弓來注入", "&a弓或弩");
        CustomItemStack validInfuseHoe = new CustomItemStack(Material.DIAMOND_HOE, "&b&l一個有效的鋤頭來注入", "&6金&a, &f鐵&a, &b鑽石&a,", "&a或&c獄髓&a鋤頭");
        CustomItemStack validInfuseChestplate = new CustomItemStack(Material.DIAMOND_CHESTPLATE, "&b&l一個有效的胸甲來注入", "&6金&a, &f鐵&a, &b鑽石&a,", "&a或&c獄髓&a胸甲");
        /*
         **Useless atm**
         CustomItemStack validInfuseSword = new CustomItemStack(Material.DIAMOND_SWORD, "&b&l一個有效的劍來注入", "&6金&a, &f鐵&a, &b鑽石&a,", "&a或&c獄髓&a劍");
        */
        CustomItemStack validInfuseRod = new CustomItemStack(Material.FISHING_ROD, "&b&l一個有效的釣竿來注入", "&a釣竿");
        SlimefunItemStack item;

        // Register Infusions
        if (destructiveCritsEnabled) {
            recipes.put(new ItemStack[] {
                    new ItemStack(Material.TNT), SlimefunItems.EXPLOSIVE_PICKAXE, new ItemStack(Material.STONECUTTER),
                    Items.DARKSTEEL, SlimefunItems.WITHER_PROOF_OBSIDIAN,
                    new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.WITHER_PROOF_OBSIDIAN, new ItemStack(Material.TNT)
            }, DESTRUCTIVE_CRITS);

            item = new SlimefunItemStack("AV_DESTRUCTIVE_CRITS_INFUSION", Material.TNT, "&c&l破壞性爆擊",
                    "&4爆擊時有 1/20 機率使對手挖掘疲勞 III 持續8秒",
                    "&4爆擊時有 1/5 機率使對手緩速 I 持續15秒",
                    "&4爆擊時有 1/5 機率使對手虛弱 I 持續15秒",
                    "&4爆擊時對對手的護甲造成0-5的額外傷害");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    new ItemStack(Material.TNT), SlimefunItems.EXPLOSIVE_PICKAXE, new ItemStack(Material.STONECUTTER),
                    Items.DARKSTEEL, validInfuseAxe, SlimefunItems.WITHER_PROOF_OBSIDIAN,
                    new ItemStack(Material.REDSTONE_BLOCK), SlimefunItems.WITHER_PROOF_OBSIDIAN, new ItemStack(Material.TNT)
            }, item).register(av);
        }

        if (phantomCritsEnabled) {
            recipes.put(new ItemStack[] {
                    new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.MAGICAL_GLASS, new ItemStack(Material.PHANTOM_MEMBRANE),
                    Items.DARKSTEEL, SlimefunItems.HARDENED_GLASS,
                    new ItemStack(Material.PHANTOM_MEMBRANE), Items.CONDENSED_SOUL, new ItemStack(Material.PHANTOM_MEMBRANE)
            }, PHANTOM_CRITS);

            item = new SlimefunItemStack("AV_PHANTOM_CRITS_INFUSION", Material.PHANTOM_MEMBRANE, "&b幻影性爆擊",
                    "&a有小機率在爆擊時",
                    "&a造成額外傷害, 並忽視護甲");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    new ItemStack(Material.PHANTOM_MEMBRANE), SlimefunItems.MAGICAL_GLASS, new ItemStack(Material.PHANTOM_MEMBRANE),
                    Items.DARKSTEEL, validInfuseAxe, SlimefunItems.HARDENED_GLASS,
                    new ItemStack(Material.PHANTOM_MEMBRANE), Items.CONDENSED_SOUL, new ItemStack(Material.PHANTOM_MEMBRANE)
            }, item).register(av);
        }

        if (trueAimEnabled) {
            recipes.put(new ItemStack[] {
                    SlimefunItems.SYNTHETIC_SHULKER_SHELL, SlimefunItems.INFUSED_MAGNET, SlimefunItems.STAFF_WIND,
                    Items.DARKSTEEL, Items.EXP_CRYSTAL,
                    new ItemStack(Material.SHULKER_BOX), SlimefunItems.INFUSED_ELYTRA, SlimefunItems.STEEL_THRUSTER
            }, TRUE_AIM);

            item = new SlimefunItemStack("AV_TRUE_AIM_INFUSION", Material.SHULKER_SHELL, "&d真正的自動瞄準",
                    "&5部分使用懸浮符咒", "&5用界伏蚌來處決它們的受害者,",
                    "&5注入這種魔法可使弓發射時", "&5箭矢不受重力影響");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    SlimefunItems.SYNTHETIC_SHULKER_SHELL, SlimefunItems.INFUSED_MAGNET, SlimefunItems.STAFF_WIND,
                    Items.DARKSTEEL, validInfuseBow, Items.EXP_CRYSTAL,
                    new ItemStack(Material.SHULKER_BOX), SlimefunItems.INFUSED_ELYTRA, SlimefunItems.STEEL_THRUSTER
            }, item).register(av);
        }

        if (forcefulEnabled) {
            recipes.put(new ItemStack[] {
                    SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.PISTON), SlimefunItems.STAFF_WIND,
                    SlimefunItems.INFUSED_MAGNET, SlimefunItems.STEEL_THRUSTER,
                    SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.PISTON), SlimefunItems.TALISMAN_TRAVELLER
            }, FORCEFUL);

            item = new SlimefunItemStack("AV_FORCEFUL_INFUSION", Material.PISTON, "&2強力性",
                    "&a這種注入使用機械", "&a裝置與電磁來加速",
                    "&a彈射物以極快的速度", "&a箭矢將會以兩倍遠與造成更多傷害");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.PISTON), SlimefunItems.STAFF_WIND,
                    SlimefunItems.INFUSED_MAGNET, validInfuseBow, SlimefunItems.STEEL_THRUSTER,
                    SlimefunItems.ELECTRO_MAGNET, new ItemStack(Material.PISTON), SlimefunItems.TALISMAN_TRAVELLER
            }, item).register(av);
        }

        if (volatileEnabled) {
            recipes.put(new ItemStack[] {
                    new ItemStack(Material.BLAZE_ROD), SlimefunItems.STAFF_FIRE, SlimefunItems.TALISMAN_FIRE,
                    Items.DARKSTEEL, SlimefunItems.LAVA_GENERATOR_2,
                    new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.LAVA_CRYSTAL
            }, VOLATILE);

            item = new SlimefunItemStack("AV_VOLATILE_INFUSION", Material.FIRE_CHARGE, "&4&l揮發性",
                    "&c這種極其危險的注入物會造成", "&c純粹過熱的熔岩所製成的球體,",
                    "&c向目標發射一個迷你地獄火");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    new ItemStack(Material.BLAZE_ROD), SlimefunItems.STAFF_FIRE, SlimefunItems.TALISMAN_FIRE,
                    Items.DARKSTEEL, validInfuseBow, SlimefunItems.LAVA_GENERATOR_2,
                    new ItemStack(Material.TNT), SlimefunItems.SYNTHETIC_DIAMOND, SlimefunItems.LAVA_CRYSTAL
            }, item).register(av);
        }

        if (healingEnabled) {
            recipes.put(new ItemStack[] {
                    Items.BENEVOLENT_BREW, SlimefunItems.MEDICINE, SlimefunItems.VITAMINS,
                    Items.ILLUMIUM, new ItemStack(Material.TOTEM_OF_UNDYING),
                    new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), SlimefunItems.MEDICINE, SlimefunItems.MAGIC_SUGAR
            }, HEALING);

            item = new SlimefunItemStack("AV_HEALING_INFUSION", Material.REDSTONE, "&c治療性",
                    "&c這種注入物將治療被擊中的實體", " &c並恢復它們的&4健康", "" +
                    "&a治療量與弓箭傷害相同");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    Items.BENEVOLENT_BREW, SlimefunItems.MEDICINE, SlimefunItems.VITAMINS,
                    Items.ILLUMIUM, validInfuseBow, new ItemStack(Material.TOTEM_OF_UNDYING),
                    new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), SlimefunItems.MEDICINE, SlimefunItems.MAGIC_SUGAR
            }, item).register(av);
        }

        if (autoReplantEnabled) {
            recipes.put(new ItemStack[] {
                    new ItemStack(Material.COMPOSTER), Items.GOOD_ESSENCE, new ItemStack(Material.WATER_BUCKET),
                    Items.ILLUMIUM, SlimefunItems.FLUID_PUMP,
                    new ItemStack(Material.BONE_BLOCK), Items.GOOD_MAGIC_PLANT, new ItemStack(Material.GRINDSTONE)
            }, REPLANT);

            item = new SlimefunItemStack("AV_AUTO_REPLANT_INFUSION", Material.WHEAT, "&a自動化重種",
                    "&2任何完全生長的農作物",
                    "&2使用此注入過的鋤頭破壞", "&2會&a自動&2重新種植");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    new ItemStack(Material.COMPOSTER), Items.GOOD_ESSENCE, new ItemStack(Material.WATER_BUCKET),
                    Items.ILLUMIUM, validInfuseHoe, SlimefunItems.FLUID_PUMP,
                    new ItemStack(Material.BONE_BLOCK), Items.GOOD_MAGIC_PLANT, new ItemStack(Material.GRINDSTONE)
            }, item).register(av);
        }

        if (totemStorageEnabled) {
            recipes.put(new ItemStack[] {
                    SlimefunItems.NECROTIC_SKULL, Items.CONDENSED_SOUL, Items.BENEVOLENT_BREW,
                    Items.ILLUMIUM, Items.EXP_CRYSTAL,
                    SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.ESSENCE_OF_AFTERLIFE
            }, TOTEM_STORAGE);

            item = new SlimefunItemStack("AV_TOTEM_BATTERY_INFUSION", Material.TOTEM_OF_UNDYING, "&6&l圖騰電池",
                    "&e一個內置的維度, 可容納", "&e8個不死圖騰的能量",
                    "&6在這個裝置中儲存圖騰", "&6Shift-右鍵點擊 &6與手裡拿著圖騰",
                    "&6當配戴有注入此注入物的胸甲時");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    SlimefunItems.NECROTIC_SKULL, Items.CONDENSED_SOUL, Items.BENEVOLENT_BREW,
                    Items.ILLUMIUM, validInfuseChestplate, Items.EXP_CRYSTAL,
                    SlimefunItems.ESSENCE_OF_AFTERLIFE, SlimefunItems.ENERGIZED_CAPACITOR, SlimefunItems.ESSENCE_OF_AFTERLIFE
            }, item).register(av);
        }

        if (knockbackEnabled) {
            recipes.put(new ItemStack[] {
                    SlimefunItems.TALISMAN_WHIRLWIND, new ItemStack(Material.STICKY_PISTON), Items.EXP_CRYSTAL,
                    SlimefunItems.GRANDPAS_WALKING_STICK, new ItemStack(Material.STICKY_PISTON),
                    new ItemStack(Material.SLIME_BALL), SlimefunItems.GRANDPAS_WALKING_STICK, SlimefunItems.TALISMAN_WHIRLWIND
            }, KNOCKBACK);

            item = new SlimefunItemStack("AV_KNOCKBACK_INFUSION", Material.SLIME_BALL, "&a擊退性",
                    "&2被這個釣竿拉動的生物",
                    "&2反而會被推走");

            new SlimefunItem(Utils.ItemGroups.INFUSIONS, item, Utils.RecipeTypes.INFUSION_ALTAR_TYPE, new ItemStack[] {
                    SlimefunItems.TALISMAN_WHIRLWIND, new ItemStack(Material.STICKY_PISTON), Items.EXP_CRYSTAL,
                    SlimefunItems.GRANDPAS_WALKING_STICK, validInfuseRod, new ItemStack(Material.STICKY_PISTON),
                    new ItemStack(Material.SLIME_BALL), SlimefunItems.GRANDPAS_WALKING_STICK, SlimefunItems.TALISMAN_WHIRLWIND
            }, item).register(av);
        }
    }

    @Override
    protected void setup(@NotNull BlockMenuPreset blockMenuPreset) {
        // Input background
        for (int slot : IN_BG) {
            blockMenuPreset.addItem(slot, Items.IN_BG, ChestMenuUtils.getEmptyClickHandler());
        }

        // Input slots
        for (int slot : IN_SLOTS) {
            blockMenuPreset.addMenuClickHandler(slot, (player, i, itemStack, clickAction) -> i == slot || i > 44);
        }

        // Craft button background
        for (int slot : CRAFT_BG) {
            blockMenuPreset.addItem(slot, Items.CRAFT_BG, ChestMenuUtils.getEmptyClickHandler());
        }

        // Craft button
        for (int slot : CRAFT_BUTTON) {
            blockMenuPreset.addItem(slot, Items.CRAFT_BTN);
        }
    }

    @Override
    protected void onNewInstance(@NotNull BlockMenu menu, @NotNull Block b) {
        // Spawn end rod particles
        b.getWorld().spawnParticle(Particle.END_ROD, b.getLocation(), 100, 0.5, 0.5, 0.5);

        // Craft button click handler
        for (int slot : CRAFT_BUTTON) {
            menu.addMenuClickHandler(slot, (player, i, itemStack, clickAction) -> {
                // Craft item
                craft(b, menu, player);
                return false;
            });
        }
    }

    @Override
    protected void onBreak(@NotNull BlockBreakEvent e, @NotNull BlockMenu menu) {
        Location l = menu.getLocation();
        menu.dropItems(l, IN_SLOTS);
    }

    @Override
    protected void craft(@NotNull Block b, @NotNull BlockMenu inv, @NotNull Player p) {
        // Get expected Infusion
        ItemStack[] input = new ItemStack[8];

        int index = 0;
        for (int i : IN_SLOTS_EXCLUDING_MID) {
            input[index] = inv.getItemInSlot(i);
            index++;
        }

        NamespacedKey infusion = recipes.get(input);

        // Invalid Infusion
        if (infusion == null) {
            p.sendMessage(Utils.legacySerialize("<red>無效的注入!"));
            return;
        }

        Material mat = inv.getItemInSlot(TOOL_SLOT).getType();

        // Check if item is valid
        if (mat.isItem()) {
            if (VALID_AXE.contains(mat) ||
                    (((VALID_BOW.contains(mat) ||
                    VALID_HOE.contains(mat) ||
                    VALID_CHESTPLATE.contains(mat)) ||
                    VALID_FISHING_ROD.contains(mat)))) {
                // Valid item
            } else {
                // Invalid item
                p.sendMessage(Utils.legacySerialize("<red>你無法對此工具進行注入!"));
                return;
            }
        } else {
            // Invalid item
            p.sendMessage(Utils.legacySerialize("<red>你無法注入在方塊上!"));
            return;
        }

        // Get the tool
        ItemStack tool = inv.getItemInSlot(TOOL_SLOT);
        if (tool == null || tool.getType().equals(Material.AIR)) {
            // No tool
            p.sendMessage(Utils.legacySerialize("<red>你不能注入空氣!"));
            return;
        }

        // ItemMeta
        ItemMeta meta = tool.getItemMeta();

        // Container
        PersistentDataContainer container = meta.getPersistentDataContainer();

        // Check if tool is already infused
        if (container.has(DESTRUCTIVE_CRITS, PersistentDataType.BYTE) ||
                container.has(PHANTOM_CRITS, PersistentDataType.BYTE) ||
                container.has(TRUE_AIM, PersistentDataType.BYTE) ||
                container.has(FORCEFUL, PersistentDataType.BYTE) ||
                container.has(VOLATILE, PersistentDataType.BYTE) ||
                container.has(HEALING, PersistentDataType.BYTE) ||
                container.has(TOTEM_STORAGE, PersistentDataType.INTEGER) ||
                container.has(REPLANT, PersistentDataType.BYTE) ||
                container.has(KNOCKBACK, PersistentDataType.BYTE)) {
            // Tool is already infused
            p.sendMessage(Utils.legacySerialize("<red>你已經對這件物品添加了注入物!"));
            return;
        }

        // Check if the tool can be infused
        if (canBeInfused(tool, infusion) && !infusion.equals(TOTEM_STORAGE)) {
            // Tool can be infused and the Infusion is not the totem battery
            container.set(infusion, PersistentDataType.BYTE, (byte) 1);

            // Lore
            List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

            // Add lines to lore
            lore.add("");
            lore.add(Utils.legacySerialize("<gray>注入物:"));

            // Add infusion name to lore
            if (infusion.equals(DESTRUCTIVE_CRITS)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <red><bold>破壞性爆擊"));
            } else if (infusion.equals(PHANTOM_CRITS)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <aqua>幻影性爆擊"));
            } else if (infusion.equals(TRUE_AIM)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <light_purple>真正的自動瞄準"));
            } else if (infusion.equals(FORCEFUL)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <dark_green>強力性"));
            } else if (infusion.equals(VOLATILE)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <dark_red><bold>揮發性"));
            } else if (infusion.equals(HEALING)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <red>治療性"));
            } else if (infusion.equals(REPLANT)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <green>自動化重種"));
            } else if (infusion.equals(KNOCKBACK)) {
                lore.add(Utils.legacySerialize("<dark_gray>› <green>擊退性"));
            }

            // Set lore and meta
            meta.setLore(lore);
            tool.setItemMeta(meta);
        } else if (canBeInfused(tool, infusion) && infusion.equals(TOTEM_STORAGE)) {
            // Tool can be infused and the Infusion is the totem battery
            container.set(infusion, PersistentDataType.INTEGER, 0);

            // Lore
            List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();

            // Add lines to lore
            lore.add("");
            lore.add(Utils.legacySerialize("<gray>注入物:"));

            // Add infusion name to lore
            lore.add(Utils.legacySerialize("<dark_gray>› <gold><bold>圖騰電池"));

            // Set lore and meta
            meta.setLore(lore);
            tool.setItemMeta(meta);
        } else {
            // Tool cannot be infused
            p.sendMessage(Utils.legacySerialize("<red>你不能將此注入物添加於該物品!"));
            return;
        }

        // Consume items
        for (int slot : IN_SLOTS_EXCLUDING_MID) {
            inv.consumeItem(slot, 1);
        }

        // Pre-craft effects
        b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, 1, 1);
        b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_POWER_SELECT, 1.5F, 1);
        b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.5, 0.5, 0.5), 2, 0.1, 0.1, 0.1);

        Bukkit.getScheduler().runTaskLater(AlchimiaVitae.i(), () -> {
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_CONDUIT_ATTACK_TARGET, 0.5F, 1);
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1, 1);
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ITEM_TOTEM_USE, 0.1F, 1);
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_POWER_SELECT, 0.3F, 1);
            b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_LODESTONE_PLACE, 1.5F, 1);
            b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.5, 0.5, 0.5), 2, 0.1, 0.1, 0.1);

            Bukkit.getScheduler().runTaskLater(AlchimiaVitae.i(), () -> {
                b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
                b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_CONDUIT_ATTACK_TARGET, 1.5F, 1);
                b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ITEM_LODESTONE_COMPASS_LOCK, 1.5F, 1);
                b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_POWER_SELECT, 0.3F, 1);
                b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ITEM_TOTEM_USE, 0.3F, 1);
                b.getWorld().spawnParticle(Particle.FLASH, b.getLocation().add(0.5, 0.5, 0.5), 2, 0.1, 0.1, 0.1);

                Bukkit.getScheduler().runTaskLater(AlchimiaVitae.i(), () -> {
                    // Post-craft effects
                    b.getWorld().strikeLightningEffect(b.getLocation().add(0.5, 1, 0.5));
                    b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ITEM_TRIDENT_THUNDER, 0.5F, 1);
                    b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 1, 1);
                    b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
                    b.getWorld().playSound(b.getLocation().add(0.5, 0.5, 0.5), Sound.ITEM_TOTEM_USE, 0.5F, 1);
                    b.getWorld().spawnParticle(Particle.END_ROD, b.getLocation().add(0.5, 0.5, 0.5), 5, 0, 8, 0);
                    b.getWorld().spawnParticle(Particle.PORTAL, b.getLocation().add(0.5, 0.5, 0.5), 300, 2, 2, 2);

                    // Send message
                    p.sendMessage(Utils.legacySerialize("<gradient:#50fa75:#3dd2ff>你的物品已被注入!</gradient>"));
                }, 30);
            }, 30);
        }, 30);
    }

    private boolean canBeInfused(@NotNull ItemStack tool, @NotNull NamespacedKey infusion) {
        // Get the material
        Material mat = tool.getType();

        // Check if the Infusion can be applied to the tool
        if (mat.isItem()) {
            if (VALID_AXE.contains(mat) &&
                    (infusion.equals(DESTRUCTIVE_CRITS) ||
                            infusion.equals(PHANTOM_CRITS))) {
                return true;
            } else if (VALID_BOW.contains(mat) &&
                    (infusion.equals(TRUE_AIM) ||
                            infusion.equals(FORCEFUL) ||
                            infusion.equals(VOLATILE) ||
                            infusion.equals(HEALING))) {
                return true;
            } else if (VALID_HOE.contains(mat) &&
                    infusion.equals(REPLANT)) {
                return true;
            } else if (VALID_CHESTPLATE.contains(mat) &&
                    infusion.equals(TOTEM_STORAGE)) {
                return true;
            } else if (VALID_FISHING_ROD.contains(mat) &&
                    infusion.equals(KNOCKBACK)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}