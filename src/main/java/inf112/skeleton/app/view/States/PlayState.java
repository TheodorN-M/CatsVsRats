package inf112.skeleton.app.view.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import inf112.skeleton.app.controller.buttons.menu.MenuButtons;
import inf112.skeleton.app.controller.buttons.upgrade.UpgradeButtons;
import inf112.skeleton.app.model.SkadedyrModel;
import inf112.skeleton.app.model.catmenu.CatMenu;
import inf112.skeleton.app.model.entities.cat.ICat;
import inf112.skeleton.app.model.entities.rat.IRat;

public class PlayState extends State {
    private ShapeRenderer shapeRenderer;
    private SkadedyrModel model;
    private BitmapFont font;
    private Stage stage;
    private Stage upgradeStage;
    private CatMenu catMenu;
   // private Button pauseButton;
    private Texture mapTexture;
    private MenuButtons menu;
    private UpgradeButtons upgradeButtons;
    private float alpha = 0f;

    public PlayState(GameStateManager gsm, SkadedyrModel model) {
        super(gsm);
        this.model = model;
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        font.setColor(Color.BLACK);
        this.catMenu = model.getCatMenu();
        this.stage = new Stage();
        this.upgradeStage = new Stage();
        this.mapTexture = new Texture("map/Spill_Plattform.jpg");
        upgradeButtons = new UpgradeButtons(model);
        menu = new MenuButtons(model);
        addUpgradeButtonsToStage();
        addMenuButtonsToStage();
        Gdx.input.setInputProcessor(stage);
    }

    public void addUpgradeButtonsToStage() {
        upgradeStage.addActor(upgradeButtons.upgradeDamageButton());
        upgradeStage.addActor(upgradeButtons.upgradeFireRateButton());
        upgradeStage.addActor(upgradeButtons.upgradeRangeButton());
    }

    private void addMenuButtonsToStage() {
        stage.addActor(menu.exitButton());
        stage.addActor(menu.pauseButton());
        stage.addActor(menu.speedButton());
        stage.addActor(menu.helpButton());
        stage.addActor(menu.restartButton());
    }

    public void updateUpgradeButtons() {
        upgradeButtons.updateButtonAppearance();
    }

    public void updateMenuButtons() {
        menu.updateButtonAppearance();

    }

    @Override
    public void render(SpriteBatch batch) {
        ICat selectedCat = model.getSelectedCat();
        ScreenUtils.clear(Color.DARK_GRAY);

        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }

        if (alpha < 1f) {
            alpha += Gdx.graphics.getDeltaTime() / 2; // Increase alpha over time
        }

        batch.begin();

        batch.setColor(1, 1, 1, alpha);
        batch.draw(mapTexture, 0, 200, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 300);

        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawRangeCircleFor(selectedCat);
        shapeRenderer.end();

        drawCatMenu(batch);

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        drawCats(batch);
        drawRats(batch);
        // batch.end();

        // batch.begin();
        drawGameStatus(batch);
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        
        drawUpgradeButtons(selectedCat);

        changeState();

    }


    private void changeState(){
        if (model.isGameWon()) {
            gsm.set(new WinState(gsm, model));

        }
        if (model.isGameOver()) {
            gsm.set(new GameOverState(gsm, model));
        }
        if (model.getHelp()) {
            gsm.set(new HelpState(gsm, model));
        }
    }

    private void drawRangeCircleFor(Cat selectedCat){
        if (selectedCat != null) {

            Circle range = selectedCat.getRangeCircle();
            shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.5f);
            shapeRenderer.circle(range.x, range.y, range.radius);
        }
    }

    private void drawUpgradeButtons(Cat selectedCat){
        if (selectedCat != null) {
            Gdx.input.setInputProcessor(upgradeStage);
            upgradeStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
            upgradeStage.draw();

        } else {
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void drawGameStatus(SpriteBatch batch) {
        font.draw(batch, "Dine liv: " + model.getLives(), 1100, 800);
        font.draw(batch, "Dine penger: " + model.getMoney(), 925, 800);
        font.draw(batch, "Din Score: " + model.getPoints(), 800, 800);
        font.draw(batch, "Level: " + model.getLevel(), 700, 800);
        font.draw(batch, model.nextWaveText(), 500, 150);
        font.draw(batch, model.uppgradeErrorText(), 900, 190);
        font.setColor(Color.WHITE);
    }

    private void drawCatMenu(SpriteBatch batch) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        catMenu.draw(shapeRenderer);
        shapeRenderer.end();

        int playerMoney = model.getMoney();
        batch.begin();
        catMenu.draw(batch, playerMoney);
        batch.end();
    }

    private void drawCats(SpriteBatch batch) {
        for (ICat cat : model.getCats()) {
            Sprite catSprite = cat.getSprite();
            catSprite.draw(batch);
        }
    }

    private void drawRats(SpriteBatch batch) {
        for (IRat rat : model.getRats()) {
            Sprite catSprite = rat.getSprite();
            catSprite.draw(batch);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        upgradeStage.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }

}
