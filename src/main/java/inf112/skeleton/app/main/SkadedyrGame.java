package inf112.skeleton.app.main;

import com.badlogic.gdx.ApplicationListener;


import inf112.skeleton.app.controller.SkadedyrController;
import inf112.skeleton.app.model.SkadedyrModel;
import inf112.skeleton.app.view.SkadedyrView;

public class SkadedyrGame implements ApplicationListener{

    private final SkadedyrModel model;
    private final SkadedyrView view;
    private final SkadedyrController controller;

    public SkadedyrGame(){
        this.model  = new SkadedyrModel();
        this.controller = new SkadedyrController(model);
        this.view = new SkadedyrView(model);
       

    }

    @Override
    public void create() {
        controller.startTimer();
        view.create();
        model.initCatMenu();
    }

    @Override
    public void resize(int width, int height) {
        view.resize(width, height);
    }

    @Override
    public void render() {
        view.render();
       
    }

    @Override
    public void pause() {
        model.setPause();
    }

    @Override
    public void resume() {
        model.setPause();

    }

    @Override
    public void dispose() {
        view.dispose();
    }
}
