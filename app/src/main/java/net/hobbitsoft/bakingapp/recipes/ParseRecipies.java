package net.hobbitsoft.bakingapp.recipes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseRecipies {

    private static final String TAG = ParseRecipies.class.getSimpleName();

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String STEPS = "steps";
    private static final String SERVINGS = "servings";
    private static final String IMAGE = "image";

    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGREDIENT = "ingredient";

    private static final String SHORT_DESCRIPTION = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_URL = "videoURL";
    private static final String THUMBNAIL_URL = "thumbnailURL";

    public static ArrayList<Recipe> parseRecipies(String recipiesJSON) {
        ArrayList<Recipe> parsedRecipies = new ArrayList<>();

        try {
            JSONArray recipeArray = new JSONArray(recipiesJSON);
            for (int i = 0; i < recipeArray.length(); i++) {
                Recipe recipe = new Recipe();
                String recipeString = recipeArray.getString(i);
                JSONObject recipeJSON = new JSONObject(recipeString);
                recipe.setId(recipeJSON.optInt(ID));
                recipe.setName(recipeJSON.optString(NAME));
                JSONArray ingredientsJSONArray = recipeJSON.optJSONArray(INGREDIENTS);
                ArrayList<Ingredient> ingredientsArrayList = new ArrayList<>();
                for (int j = 0; j < ingredientsJSONArray.length(); j++) {
                    ingredientsArrayList.add(parseIngredient(ingredientsJSONArray.optString(j)));
                }
                recipe.setIngredients(ingredientsArrayList);
                JSONArray stepJSONArray = recipeJSON.optJSONArray(STEPS);
                ArrayList<Step> stepArrayList = new ArrayList<>();
                for (int k = 0; k < stepJSONArray.length(); k++) {
                    stepArrayList.add(parseStep(stepJSONArray.optString(k)));
                }
                recipe.setSteps(stepArrayList);
                recipe.setServings(recipeJSON.optInt(SERVINGS));
                recipe.setImage(recipeJSON.optString(IMAGE));
                parsedRecipies.add(recipe);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }

        return parsedRecipies;

    }

    private static Ingredient parseIngredient(String ingredientJSON) {
        Ingredient ingredient = new Ingredient();

        try {
            JSONObject jsonObject = new JSONObject(ingredientJSON);
            ingredient.setQuantity(jsonObject.optString(QUANTITY));
            ingredient.setMeasure(jsonObject.getString(MEASURE));
            ingredient.setIngredient(jsonObject.getString(INGREDIENT));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }

        return ingredient;

    }

    private static Step parseStep(String stepJSON) {
        Step step = new Step();

        try {
            JSONObject jsonObject = new JSONObject(stepJSON);
            step.setId(jsonObject.optInt(ID));
            step.setShortDescription(jsonObject.optString(SHORT_DESCRIPTION));
            step.setDescription(jsonObject.optString(DESCRIPTION));
            step.setVideoURL(jsonObject.optString(VIDEO_URL));
            step.setThumbnailURIL(jsonObject.optString(THUMBNAIL_URL));
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }

        return step;
    }
}
