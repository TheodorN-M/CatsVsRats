package inf112.skeleton.app.model.entities.rat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import inf112.skeleton.app.model.entities.Projectile;
import java.util.ArrayList;
import java.util.EnumMap;

public class Rat implements IRat {
    private int speed;
    private Vector2 pos;
    private int health;
    private Rectangle spriteRect;
    private float secs;
    private Integer bounty;
    private Integer points;
    private boolean rewardClaimed = false;
    private boolean exited = false;
    public float coinVisibleTime = 0f;
    private Sprite sprite;
    private boolean isFrozen;
    public ImageSwapper currentState = ImageSwapper.ALIVE;
    private EnumMap<ImageSwapper, Texture> textures = new EnumMap<>(ImageSwapper.class);
    int halfsize = 25;
    private CatmullRomSpline<Vector2> path;
    private float progress;
    private Vector2[] controlPoints;
    private Direction direction = Direction.RIGHT;
    

    public Rat(int health, int speed, Texture texture, Integer bounty, Integer points, Texture frozenTexture, int halfsize, Texture deadTexture) {
        this.health = health;
        this.speed = speed;
        this.points = points;
        this.bounty = bounty;
        this.pos = new Vector2(-10, 430);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(halfsize * 2, halfsize * 2);
        this.sprite.setPosition(pos.x - halfsize, pos.y - halfsize);
        this.secs = 0;
        textures.put(ImageSwapper.ALIVE, texture);
        textures.put(ImageSwapper.FROZEN, frozenTexture);
        textures.put(ImageSwapper.DEAD, deadTexture);
        this.spriteRect = new Rectangle(pos.x - halfsize, pos.y - halfsize, halfsize * 2, halfsize * 2);
        createPath();
    }

    public void moveAlongPath(float delta) {
        if (progress < 1) {
            progress += delta * 0.01;
        }
    
        Vector2 currentPosition = new Vector2();
        path.valueAt(currentPosition, progress); 
        pos.set(currentPosition); 
        sprite.setPosition(pos.x - halfsize, pos.y - halfsize); 
        spriteRect.setPosition(pos.x - halfsize, pos.y - halfsize); 
        for (int i = 0; i < controlPoints.length - 1; i++) {
            if (pos.epsilonEquals(controlPoints[i], 1.0f)) {
                updateDirection(controlPoints[i], controlPoints[i + 1]);
                rotateImage();
                break;
            }
        }
    }
    

    public void createPath() {
        controlPoints = new Vector2[] {
                new Vector2(10.0f,155.0f),
                new Vector2(133.0f,155.0f),
                new Vector2(137.0f,360.0f),
                new Vector2(64.0f,359.0f),
                new Vector2(64.0f,359.0f),
                new Vector2(69.0f,671.0f),
                new Vector2(291.0f,671.0f),
                new Vector2(304.0f,159.0f),
                new Vector2(448.0f,147.0f),
                new Vector2(453.0f,255.0f),
                new Vector2(584.0f,272.0f),
                new Vector2(582.0f,479.0f),
                new Vector2(449.0f,486.0f),
                new Vector2(447.0f,654.0f),
                new Vector2(724.0f,664.0f),
                new Vector2(724.0f,664.0f),
                new Vector2(739.0f,182.0f),
                new Vector2(800,182.0f),
        };
        this.path = new CatmullRomSpline<>(controlPoints, false);
    }

    private void updateDirection(Vector2 current, Vector2 next) {
        if (next.x > current.x) {
            direction = Direction.RIGHT;
        } else if (next.x < current.x) {
            direction = Direction.LEFT;
        } else if (next.y > current.y) {
            direction = Direction.UP;
        } else if (next.y < current.y) {
            direction = Direction.DOWN;
        }
        else{ 
            direction = Direction.OUT;
        }
    }

    public Direction getDirection(){
        return direction;
    }


    @Override
    public boolean isrewardClaimed() {
        return rewardClaimed;
    }

    @Override
    public boolean isExited() {
        return exited;
    }

    @Override
    public void rewardClaimed() {
        this.rewardClaimed = true;
    }

    @Override
    public void exit() {
        this.exited = true;
    }

    private enum ImageSwapper {
        ALIVE,
        FROZEN,
        DEAD;
    }

    @Override
    public boolean isHitByProjectile(ArrayList<Projectile> projectiles) {
        for (Projectile rect : projectiles) {
            if (rect.getRectangle().overlaps(spriteRect)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Projectile getHitByProjectile(ArrayList<Projectile> projectiles) {
        for (Projectile projectile : projectiles) {
            if (projectile.getRectangle().overlaps(spriteRect)) {
                return projectile;
            }
        }
        return null;
    }

    @Override
    public int getBounty() {
        return bounty;
    }

    @Override
    public int getPoints() {
        return points;
    }

    private void swapImage(ImageSwapper image) {
        currentState = image;
    }

    @Override
    public Texture getTexture() {
        return textures.get(currentState);
    }

    @Override
    public Rectangle getRectangle() {
        return spriteRect;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    // fix this using getDirection
    public enum Direction {
        UP,
        DOWN,
        RIGHT,
        LEFT,
        OUT;
    }

    @Override
    public void addTime() {
        this.secs += 0.05;

    }

    private float getRotationAngle() {
        Direction dir = getDirection();
        switch (dir) {
            case UP:
                return 0;
            case DOWN:
                return 180;
            case LEFT:
                return 90;
            case RIGHT:
                return -90;
            case OUT:
                return 0;
            default:
                throw new Error("Unexpected Direction: " + dir);
        }
    }

    @Override
    public void rotateImage() {
        float angle = getRotationAngle();
        this.sprite.setOriginCenter();
        this.sprite.setRotation(angle);
    }

   
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public void render(SpriteBatch batch) {
    }

    public void killedAnimation() {
        swapImage(ImageSwapper.DEAD);
        health = 0;
        speed = 0;
        this.sprite.setTexture(getTexture());
    }

    public void updateCoinVisibility(float deltaTime) {
        if (isKilled()) {
            coinVisibleTime += deltaTime;
        }
    }

    @Override
    public boolean isKilled() {
        return health <= 0;
    }

    @Override
    public boolean isOut() {
        if (this.getDirection() == Direction.OUT) {
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(Vector2 pos) {
        this.pos = pos;
        rectangleUpdater();
    }

    public void rectangleUpdater(){
        spriteRect.x = pos.x - halfsize;
        spriteRect.y = pos.y - halfsize;
        
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public Vector2 getPosition() {
        return pos;
    }

    @Override
    public void freeze() {
        isFrozen = true;
        swapImage(ImageSwapper.FROZEN);
        this.sprite.setTexture(getTexture());
    }

    @Override
    public void unfreeze() {
        isFrozen = false;
        swapImage(ImageSwapper.ALIVE);
        this.sprite.setTexture(getTexture());
    }

    @Override
    public boolean isFrozen() {
        return isFrozen;
    }
}
