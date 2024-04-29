package inf112.skeleton.app.model.entities.cat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.model.entities.rat.Rat;
import inf112.skeleton.app.view.GameResourceFactory;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BasicCatTest {
    private BasicCat basicCat;

    @Mock
    private Texture aliveTextureMock;
    @Mock
    private Texture frozenTextureMock;
    @Mock
    private Texture deadTextureMock;
   
    @Mock
    private Sound mockSound; 

    private LinkedList<Rat> rats;


    @BeforeEach
    void setup() {
        GameResourceFactory mockFactory = mock(GameResourceFactory.class);
        lenient().when(mockFactory.getSound("sound/fart.mp3")).thenReturn(this.mockSound);
        lenient().when(mockFactory.getTexture(anyString())).thenReturn(mock(Texture.class));
    
        basicCat = new BasicCat(mockFactory);
        rats = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            rats.add(new Rat(100, 10, aliveTextureMock, 50, 20, frozenTextureMock, 25, deadTextureMock));
        }
    }
    

    @Test
    void testAttack() {
        if (basicCat.canAttack()) {
            int initialHealth = rats.getFirst().getHealth();
            basicCat.attack(rats);
            assertEquals(initialHealth - basicCat.getStrength(), rats.getFirst().getHealth());
        }
    }

    @Test
    void upgradeDamageTest() {
        int initialStrength = basicCat.getStrength();
        basicCat.upgradeDamage();
        assertEquals((int)(initialStrength * 1.25), basicCat.getStrength());
    }

    @Test
    void testUpgradeRange() {
        int initialRange = basicCat.getRange();
        basicCat.upgradeRange();
        assertEquals((int)(initialRange * 1.25), basicCat.getRange());
    }

    @Test
    void testUpgradeFireRate() {
        float initialFireRate = basicCat.getFireRate();
        basicCat.upgradeFireRate();
        assertEquals(initialFireRate * 0.75, basicCat.getFireRate(), 0.01);
    }
    
    @Test
    void testPlayAttackSound() {
        if (basicCat.canAttack()) {
            basicCat.attack(rats);
            verify(mockSound, times(1)).play(0.6f);
        }
    }

    
}
