package mods.eln.api.recipe;

import mods.eln.Eln;
import mods.eln.api.Misc;
import mods.eln.api.Utilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
//import mods.eln.electricalfurnace.ElectricalFurnaceProcess;

public class RecipesList {

    public static final ArrayList<RecipesList> listOfList = new ArrayList<RecipesList>();

    private ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    private ArrayList<ItemStack> machineList = new ArrayList<ItemStack>();

    public RecipesList() {
        listOfList.add(this);
    }

    public static ArrayList<Recipe> getGlobalRecipeWithOutput(ItemStack output) {
        output = output.copy();
        output.stackSize = 1;
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        for (RecipesList recipesList : listOfList) {
            list.addAll(recipesList.getRecipeFromOutput(output));
        }

        FurnaceRecipes furnaceRecipes = FurnaceRecipes.smelting();

        {
            Iterator it = furnaceRecipes.getSmeltingList().entrySet().iterator();
            while (it.hasNext()) {
                try {
                    Map.Entry pairs = (Map.Entry) it.next();
                    Recipe recipe; // List<Integer>, ItemStack
                    ItemStack stack = (ItemStack) pairs.getValue();
                    ItemStack li = (ItemStack) pairs.getKey();
                    if (Utilities.areSame(output, stack)) {
                        list.add(recipe = new Recipe(li.copy(), output, Misc.getElectricalFurnaceProcessEnergyNeededPerSmelt()));
                        recipe.setMachineList(Eln.instance.furnaceList);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        return list;
    }

    public static ArrayList<Recipe> getGlobalRecipeWithInput(ItemStack input) {
        input = input.copy();
        input.stackSize = 64;
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        for (RecipesList recipesList : listOfList) {
            Recipe r = recipesList.getRecipe(input);
            if (r != null)
                list.add(r);
        }

        FurnaceRecipes furnaceRecipes = FurnaceRecipes.smelting();
        ItemStack smeltResult = furnaceRecipes.getSmeltingResult(input);
        Recipe smeltRecipe;
        if (smeltResult != null) {
            try {
                ItemStack input1 = input.copy();
                input1.stackSize = 1;
                list.add(smeltRecipe = new Recipe(input1, smeltResult, Misc.getElectricalFurnaceProcessEnergyNeededPerSmelt()));
                smeltRecipe.machineList.addAll(Eln.instance.furnaceList);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return list;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipeList;
    }

    public ArrayList<ItemStack> getMachines() {
        return machineList;
    }

    public void addRecipe(Recipe recipe) {
        recipeList.add(recipe);
        recipe.setMachineList(machineList);
    }

    public void addMachine(ItemStack machine) {
        machineList.add(machine);
    }

    public ArrayList<Recipe> getRecipeFromOutput(ItemStack output) {
        ArrayList<Recipe> list = new ArrayList<Recipe>();
        for (Recipe r : recipeList) {
            for (ItemStack stack : r.getOutputCopy()) {
                if (Utilities.areSame(stack, output)) {
                    list.add(r);
                    break;
                }
            }
        }
        return list;
    }

    public Recipe getRecipe(ItemStack input) {
        for (Recipe r : recipeList) {
            if (r.canBeCraftedBy(input)) return r;
        }
        return null;
    }
}
/*		FurnaceRecipes.smelting().addSmelting(in.itemID, in.getItemDamage(),
                findItemStack("Copper ingot"), 0);*/