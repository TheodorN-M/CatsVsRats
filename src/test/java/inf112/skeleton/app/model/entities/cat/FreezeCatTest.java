package inf112.skeleton.app.model.entities.cat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import inf112.skeleton.app.model.entities.rat.IRat;
import inf112.skeleton.app.model.entities.rat.Rat;
import inf112.skeleton.app.view.GameResourceFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class FreezeCatTest {
      private FreezeCat freezeCat;

    @Mock
    private Texture aliveTextureMock;
    @Mock
    private Texture frozenTextureMock;
    @Mock
    private Texture deadTextureMock;
    @Mock
    private Sound mockSound; 

    private LinkedList<IRat> rats;

    @BeforeEach
    void setup() {
    GameResourceFactory mockFactory = mock(GameResourceFactory.class);
    Texture mockTexture = mock(Texture.class);

    lenient().when(mockFactory.getSound("sound/ice.mp3")).thenReturn(this.mockSound);
    lenient().when(mockFactory.getTexture(anyString())).thenReturn(mockTexture);

    freezeCat = new FreezeCat(mockFactory);
        rats = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            rats.add(new Rat(100, 10, aliveTextureMock, 50, 20, frozenTextureMock, 25, deadTextureMock));
        }
    }

    @Test
    void testAttack() {
        ArrayList<Integer> healthList = new ArrayList<>();
        for (IRat rat : rats) {
           Integer initialHealth = rat.getHealth();
              healthList.add(initialHealth);
        }
        if (freezeCat.canAttack()) {
            freezeCat.attack(rats);
        }

        for (int j = 0; j < rats.size(); j++) {
            assertEquals(healthList.get(j) - freezeCat.getStrength(), rats.get(j).getHealth());  
            
        }
    }

    @Test
    void upgradeDamageTest() {
        int initialStrength = freezeCat.getStrength();
        freezeCat.upgradeDamage();
        assertEquals((int)(initialStrength * 1.25), freezeCat.getStrength());
    }

    @Test
    void testUpgradeRange() {
        int initialRange = freezeCat.getRange();
        freezeCat.upgradeRange();
        assertEquals((int)(initialRange * 1.25), freezeCat.getRange());
    }

    @Test
    void testUpgradeFireRate() {
        float initialFireRate = freezeCat.getFireRate();
        freezeCat.upgradeFireRate();
        assertEquals(initialFireRate * 0.75, freezeCat.getFireRate(), 0.01);
    }

    @Test
    void testPlayAttackSound() {
        if (freezeCat.canAttack()) {
            freezeCat.attack(rats);
            verify(mockSound, times(1)).play(0.6f);
        }
    }
}
