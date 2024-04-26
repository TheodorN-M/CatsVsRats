package inf112.skeleton.app.controller.buttons.upgrade;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import inf112.skeleton.app.controller.buttons.ButtonFactory;
import inf112.skeleton.app.model.SkadedyrModel;
import inf112.skeleton.app.model.entities.cat.Cat;

public class UpgradeButtons {
    private SkadedyrModel model;
    private Stage stage;
    private Button upgradeDamageButton;
    private Button upgradeFireRateButton;
    private Button upgradeRangeButton;
    private int clicked = 0;
    private int cost = 0;
    private static final int KUKK = 70;

    private static final String damageTexture= "buttons_game/damage.png";
    private static final String fireRateTexture= "buttons_game/firerate.png";
    private static final String rangeTexture= "buttons_game/range.png";

    private static final String noMoneyTexture = "buttons_game/noMoney.png";
    private static final String usedUpTexture= "buttons/angryCat.png";
    private static final String clickTexture= "coin.png";



    public UpgradeButtons(SkadedyrModel model, Stage stage) {
        this.model = model;
        this.stage = stage;
    }

    public Button upgradeDamageButton() {
        cost = 500;
        upgradeDamageButton = ButtonFactory.createImageButton("buttons_game/damage.png", "buttons_game/damage.png");
        upgradeDamageButton.setSize(100, 100);
        upgradeDamageButton.setPosition(800, KUKK);
        upgradeDamageButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                if (model.getMoney() >= cost && clicked < 3) {
                    model.setMoney(model.getMoney() - cost); 
                    Cat cat = model.getSelectedCat();
                    if (cat != null)
                        cat.upgradeDamage();
                    clicked++;
                }
                updateButtonAppearance();
            }
            });
        return upgradeDamageButton;
    }

    public Button upgradeFireRateButton() {
            cost = 600;
            upgradeFireRateButton = ButtonFactory.createImageButton("buttons_game/firerate.png", "buttons_game/firerate.png");
            upgradeFireRateButton.setSize(100, 100);
            upgradeFireRateButton.setPosition(925, KUKK);
            upgradeFireRateButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    if (model.getMoney() >= cost && clicked < 3) {
                        model.setMoney(model.getMoney() - cost); 
                        Cat cat = model.getSelectedCat();
                        if (cat != null)
                            cat.upgradeFireRate(); 
                        clicked++;
                    }
                    updateButtonAppearance();
                }
            });
        return upgradeFireRateButton;
    }

    public Button upgradeRangeButton() {
            cost = 700;
            upgradeRangeButton = ButtonFactory.createImageButton("buttons_game/range.png", "buttons_game/range.png");
            upgradeRangeButton.setSize(100, 100);
            upgradeRangeButton.setPosition(1050, KUKK);
            upgradeRangeButton.addListener(new ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    if (model.getMoney() >= cost && clicked < 3) {
                        model.setMoney(model.getMoney() - cost); 
                        Cat cat = model.getSelectedCat();
                        if (cat != null)
                            cat.upgradeRange(); 
    
                        clicked++;
                    }
                    updateButtonAppearance();
                }
            });
        return upgradeRangeButton;
    }

    public void updateButtonAppearance() {
        updateDamageButtonAppearance();
        updateFireRateButtonAppearance();
        updateRangeButtonAppearance();
    }
    
    private void updateDamageButtonAppearance() {
        TextureRegionDrawable newDrawable;
        if (clicked >= 3) {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(usedUpTexture)));
        } else if (model.getMoney() < 500) { 
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(noMoneyTexture)));
        } else {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(damageTexture)));
        }
        upgradeDamageButton.getStyle().up = newDrawable;
        upgradeDamageButton.getStyle().down = new TextureRegionDrawable(new TextureRegion(new Texture(clickTexture)));
    }
    
    private void updateFireRateButtonAppearance() {
        TextureRegionDrawable newDrawable;
        if (clicked >= 3) {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(usedUpTexture)));
        } else if (model.getMoney() < 600) { 
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(noMoneyTexture)));
        } else {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(fireRateTexture)));
        }
        upgradeFireRateButton.getStyle().up = newDrawable;
        upgradeFireRateButton.getStyle().down = new TextureRegionDrawable(new TextureRegion(new Texture(clickTexture)));
    }
    
    private void updateRangeButtonAppearance() {
        TextureRegionDrawable newDrawable;
        if (clicked >= 3) {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(usedUpTexture)));
        } else if (model.getMoney() < 700) { 
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(noMoneyTexture)));
        } else {
            newDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(rangeTexture)));
        }
        upgradeRangeButton.getStyle().up = newDrawable;
        upgradeRangeButton.getStyle().down = new TextureRegionDrawable(new TextureRegion(new Texture(clickTexture))); 
    

    }
    
}
