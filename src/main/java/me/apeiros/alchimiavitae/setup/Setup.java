package me.apeiros.alchimiavitae.setup;

import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.apeiros.alchimiavitae.AlchimiaVitae;
import me.apeiros.alchimiavitae.listeners.MobDropListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionAxeListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionBowListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionFishingRodListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionHoeListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionSwordListener;
import me.apeiros.alchimiavitae.listeners.infusion.InfusionTotemListener;
import me.apeiros.alchimiavitae.setup.items.crafters.AltarOfInfusion;
import me.apeiros.alchimiavitae.setup.items.crafters.DivineAltar;
import me.apeiros.alchimiavitae.setup.items.crafters.OrnateCauldron;
import me.apeiros.alchimiavitae.setup.items.electric.EXPCrystallizer;
import me.apeiros.alchimiavitae.setup.items.electric.PlantInfusionChamber;
import me.apeiros.alchimiavitae.setup.items.general.CondensedSoul;
import me.apeiros.alchimiavitae.setup.items.general.Darksteel;
import me.apeiros.alchimiavitae.setup.items.general.EXPCrystal;
import me.apeiros.alchimiavitae.setup.items.general.Illumium;
import me.apeiros.alchimiavitae.setup.items.general.MoltenMysteryMetal;
import me.apeiros.alchimiavitae.setup.items.general.MysteryMetal;
import me.apeiros.alchimiavitae.setup.items.general.SoulCollector;
import me.apeiros.alchimiavitae.setup.items.plants.EvilEssence;
import me.apeiros.alchimiavitae.setup.items.plants.EvilMagicPlant;
import me.apeiros.alchimiavitae.setup.items.plants.GoodEssence;
import me.apeiros.alchimiavitae.setup.items.plants.GoodMagicPlant;
import me.apeiros.alchimiavitae.setup.items.potions.BenevolentBrew;
import me.apeiros.alchimiavitae.setup.items.potions.MalevolentConcoction;
import me.apeiros.alchimiavitae.setup.items.potions.PotionOfOsmosis;
import me.apeiros.alchimiavitae.utils.Categories;
import org.bukkit.NamespacedKey;

public class Setup {

    public static void setup(AlchimiaVitae p) {
        // Category
        Categories.MAIN.register(p);

        // Items
        new SoulCollector(Categories.GENERAL).register(p);
        new CondensedSoul(Categories.GENERAL).register(p);
        new PlantInfusionChamber(Categories.GENERAL).register(p);
        new GoodMagicPlant(Categories.GENERAL).register(p);
        new EvilMagicPlant(Categories.GENERAL).register(p);
        new GoodEssence(Categories.GENERAL).register(p);
        new EvilEssence(Categories.GENERAL).register(p);
        new EXPCrystallizer(Categories.GENERAL).register(p);
        new EXPCrystal(Categories.GENERAL).register(p);
        new Illumium(Categories.GENERAL).register(p);
        new Darksteel(Categories.GENERAL).register(p);
        new DivineAltar(Categories.GENERAL).register(p);
        new MoltenMysteryMetal(Categories.GENERAL).register(p);
        new MysteryMetal(Categories.GENERAL).register(p);
        new OrnateCauldron(Categories.GENERAL).register(p);
        new PotionOfOsmosis(Categories.GENERAL).register(p);
        new BenevolentBrew(Categories.GENERAL, p).register(p);
        new MalevolentConcoction(Categories.GENERAL, p).register(p);
        new AltarOfInfusion(Categories.INFUSIONS).register(p);

        // Listeners
        new MobDropListener(p);
        new InfusionAxeListener(p);
        new InfusionBowListener(p);
        new InfusionHoeListener(p);
        new InfusionTotemListener(p);
        new InfusionFishingRodListener(p);
        new InfusionSwordListener(p);

        // Researches
        setupResearches(p);
    }

    public static void setupResearches(AlchimiaVitae p) {
        new Research(new NamespacedKey(p, "soul"), 131072,
                "破壞生死輪迴", 25)
                .addItems(Items.CONDENSED_SOUL, Items.SOUL_COLLECTOR)
                .register();

        new Research(new NamespacedKey(p, "magic_plants"), 131073,
                "兩極對立", 30)
                .addItems(Items.PLANT_INFUSION_CHAMBER, Items.GOOD_MAGIC_PLANT, Items.EVIL_MAGIC_PLANT)
                .register();

        new Research(new NamespacedKey(p, "magic_essence"), 131074,
                "磨它下來", 10)
                .addItems(Items.GOOD_ESSENCE, Items.EVIL_ESSENCE)
                .register();

        new Research(new NamespacedKey(p, "exp_crystals"), 131075,
                "結晶化經驗", 21)
                .addItems(Items.EXP_CRYSTALLIZER, Items.EXP_CRYSTAL)
                .register();

        new Research(new NamespacedKey(p, "magic_steel"), 131076,
                "神秘金屬", 16)
                .addItems(Items.DARKSTEEL, Items.ILLUMIUM)
                .register();

        new Research(new NamespacedKey(p, "divine_altar"), 131077,
                "古代祭壇失散多年的兄弟", 45)
                .addItems(Items.DIVINE_ALTAR)
                .register();

        new Research(new NamespacedKey(p, "metal_amalgamation"), 131078,
                "一種金屬物質的混和物", 19)
                .addItems(Items.MOLTEN_MYSTERY_METAL, Items.MYSTERY_METAL)
                .register();

        new Research(new NamespacedKey(p, "ornate_cauldron"), 131079,
                "用於釀造高級藥水的裝置", 35)
                .addItems(Items.ORNATE_CAULDRON)
                .register();

        new Research(new NamespacedKey(p, "potion_of_osmosis"), 131080,
                "滲透與吸收", 30)
                .addItems(Items.POTION_OF_OSMOSIS)
                .register();

        new Research(new NamespacedKey(p, "benevolent_brew"), 131081,
                "蓋亞本人的祝福", 35)
                .addItems(Items.BENEVOLENT_BREW)
                .register();

        new Research(new NamespacedKey(p, "malevolent_concoction"), 131082,
                "一種帶有輕微腐壞色彩的物質", 35)
                .addItems(Items.MALEVOLENT_CONCOCTION)
                .register();

        new Research(new NamespacedKey(p, "altar_of_infusion"), 131083,
                "為你的物品注入活力的終極祭壇", 30)
                .addItems(Items.ALTAR_OF_INFUSION)
                .register();
    }

    private static void setupDivineAltar(AlchimiaVitae p) {

    }

    private static void setupOrnateCauldron() {

    }

    private static void setupInfusionAltar(AlchimiaVitae p) {

    }
}
