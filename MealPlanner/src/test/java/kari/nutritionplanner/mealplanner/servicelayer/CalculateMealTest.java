
package kari.nutritionplanner.mealplanner.servicelayer;

import java.util.List;
import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class CalculateMealTest {

    private CalculateMeal cm;
    private MealCalcHelper helper;
    private final double delta = 0.0001;
    private final double bigDelta = 9.5;
    private final double hugeDelta = 9.5;
    private final double proteinDelta = 11.5;

    public CalculateMealTest() {
    }

    @Before
    public void setUp() {
        ProcessIngredients pi = new ProcessIngredients(true);
        this.cm = new CalculateMeal(pi);
        this.helper = new MealCalcHelper(pi);
    }

    @Test
    public void testGetIngredientsCreated() {
        Map<String, Map<Integer, Ingredient>> ings = cm.getIngredients();
        assertTrue(ings.size() > 0);
    }

    @Test
    public void testGetIngredientsNotEmpty() {
        Map<String, Map<Integer, Ingredient>> ings = cm.getIngredients();
        Map<Integer, Ingredient> mains = ings.get("mains");
        assertTrue(mains.size() > 0);
        assertEquals("Kana", mains.get(750).getName());
    }

//    @Test
//    public void testMainIngSetter() {
//        boolean success = cm.setMainIngredient(805, 25, 15);
//        assertTrue(success);
//        double protein = cm.getMeal().getProtein();
//        assertEquals(25, protein, delta);
//        assertEquals(1.339907814, cm.getMeal().getMainIngredientAmount(), delta);
//        assertEquals(0.58018, cm.getMeal().getFat(), delta);
//    }
//    @Test
//    public void testSauceSetter() {
//        cm.setMainIngredient(805, 25, 15);
//        boolean success = cm.setSauce(30);
//        assertTrue(success);
//        assertEquals(30, cm.getMeal().getFat(), delta);
//        assertEquals(0.693896408, cm.getMeal().getSauceAmount(), delta);
//    }
//    @Test
//    public void testMiscSetter() {
//        cm.setMisc();
//        double calories = cm.getMeal().getCalories();
//        assertEquals(22.474904398, calories, delta);
//    }
//    @Test
//    public void testSideSetterOnEmptyMeal() {
//        cm.setSideIngredient(400);
//        assertEquals(400, cm.getMeal().getCalories(), delta);
//    }
//    @Test
//    public void testSideSetter() {
//        cm.setMainIngredient(805, 25, 15);
//        cm.setSauce(30);
//        cm.setMisc();
//        cm.setSideIngredient(800);
//        assertEquals(800, cm.getMeal().getCalories(), delta);
//    }
    @Test
    public void testGetMainIngId() {
        int id = helper.getIdForMainIng("Kuha");
        assertEquals(805, id);
        id = helper.getIdForMainIng("Tofu, soijavalmiste, soijapapujuusto");
        assertEquals(33501, id);
        id = helper.getIdForMainIng("Nyhtökaura, nude");
        assertEquals(34307, id);
        id = helper.getIdForMainIng("testi");
        assertEquals(0, id);
    }

    @Test
    public void testGetMaingIngs() {
        List<Ingredient> ings = helper.getMainIngredients();
        assertEquals(5, ings.size());
    }

//    @Test
//    public void testSetWholeMeal() {
//        for (int i = 0; i < 100; i++) {
//            cm = new CalculateMeal(true);
//            Ingredient ing = cm.getIngredients().get("mains").get(getRandomMain());
//            int calories = new Random().nextInt(300) + 300;
//            double protein = calories / 20;
//            double fat = calories / 30;
//            cm.getMeal().setSauceAmount(-5.5);
//            assertTrue(cm.calculateAllMeal(ing.getId(), 99999, calories, protein, fat));
//            assertEquals(calories, cm.getMeal().getCalories(), hugeDelta);
//            assertEquals(protein, cm.getMeal().getProtein(), proteinDelta);
//            assertEquals(fat, cm.getMeal().getFat(), bigDelta);
//            assertEquals("juurekset, uunissa", cm.getMeal().getMisc().getName());
//            assertTrue(cm.getMeal().getSauceAmount() >= 0);
//            if (cm.getMeal().getSideIngredient().getProtein() >= 3 || cm.getMeal().getMainIngredient().getProtein() >= 20) {
//                assertEquals(1, cm.getMeal().getMiscAmount(), delta);
//            } else {
//                assertEquals(0.5, cm.getMeal().getMiscAmount(), delta);
//            }
//        }
//
//    }

    private int getRandomMain() {
        int seed = new Random().nextInt(helper.getMainIngredients().size());
        return helper.getMainIngredients().get(seed).getId();
    }

    @Test
    public void testLowFatMeal() {
        assertFalse(cm.calculateAllMeal(750, 99999, 300, 50, 5));
        assertFalse(cm.calculateAllMeal(750, 99999, 300, 10, 5));
        assertTrue(cm.calculateAllMeal(805, 99999, 500, 50, 10));
        assertEquals(10, cm.getMeal().getFat(), bigDelta);
        assertEquals(0, cm.getMeal().getSauceAmount(), bigDelta);
    }

    @Test
    public void testSetMainIng() {
        assertFalse(cm.calculateAllMeal(805, 99999, 50, 10, 4));
        assertFalse(cm.calculateAllMeal(805, 99999, 50, 9, 5));
        assertFalse(cm.calculateAllMeal(805, 99999, 50, 9, 4));
        assertTrue(cm.calculateAllMeal(805, 99999, 50, 10, 5));
        assertFalse(cm.calculateAllMeal(805, 99999, 49, 10, 5));
        assertFalse(cm.calculateAllMeal(805, 99999, 49, 9, 4));
        assertFalse(cm.calculateAllMeal(805, 99999, 49, 10, 4));
    }

    @Test
    public void testZeroCaloriesMeal() {
        assertFalse(cm.calculateAllMeal(805, 99999, 0, 30, 30));
    }

    @Test
    public void testZeroProteinMeal() {
        assertFalse(cm.calculateAllMeal(805, 99999, 500, 0, 20));
    }

    @Test
    public void testZeroFatMeal() {
        assertFalse(cm.calculateAllMeal(805, 99999, 500, 50, 0));
    }

    @Test
    public void testSetWholeMealFalse() {
        assertFalse(cm.calculateAllMeal(805, 99999, 1000, 30, 0));
    }

    @Test
    public void testSetWholeMealFat() {
        int calories = new Random().nextInt(300) + 400;
        int protein = calories / 20;
        int fat = calories / 30;
        assertTrue(cm.calculateAllMeal(805, 99999, calories, protein, fat));
        assertEquals(fat, cm.getMeal().getFat(), bigDelta);
    }

    @Test
    public void testSetWholeMealProtein() {
        int calories = new Random().nextInt(300) + 400;
        int protein = calories / 15;
        int fat = calories / 30;
        assertTrue(cm.calculateAllMeal(805, 99999, calories, protein, fat));
        assertEquals(protein, cm.getMeal().getProtein(), proteinDelta);
    }

//    @Test
//    public void testSetWholeMealCalories() {
//        int calories = new Random().nextInt(300) + 300;
//        int protein = calories / 20;
//        int fat = calories / 30;
//        assertTrue(cm.calculateAllMeal(805, 99999, calories, protein, fat));
//        assertEquals(calories, cm.getMeal().getCalories(), hugeDelta);
//    }

}
