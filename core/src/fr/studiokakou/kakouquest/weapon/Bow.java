package fr.studiokakou.kakouquest.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import fr.studiokakou.kakouquest.map.Point;
import fr.studiokakou.kakouquest.player.Camera;
import fr.studiokakou.kakouquest.player.Player;
import fr.studiokakou.kakouquest.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Bow {
    Texture loading;
    Texture loaded;

    Sprite sprite;

    public int arrowCount;

    public boolean isLoaded;
    public boolean isLoading;

    public LocalDateTime loadTime;

    Player player;

    public Point attackDirection;
    public Point attackStartPoint;
    public float rotation;

    public static final int BOW_DAMAGE = 20;

    public ArrayList<Arrow> arrows = new ArrayList<>();

    public Bow(Player player) {
        loading = new Texture("assets/weapon/weapon_bow.png");
        loaded = new Texture("assets/weapon/weapon_bow_2.png");
        Arrow.initTexture();

        arrowCount = 20;
        isLoaded = false;
        isLoading = false;

        this.player = player;

        this.sprite = new Sprite(loaded);
    }

    public void startAttack(){
        if (arrowCount == 0) {
            return;
        }

        if (!isLoaded) {
            isLoading = true;
            loadTime = LocalDateTime.now();
        }

    }

    public void attack(){
        if (!isLoaded) {
            return;
        }

        arrowCount--;
        isLoaded = false;
        isLoading = false;

        arrows.add(new Arrow(this.attackStartPoint, this.rotation));
    }

    public void updateArrows(SpriteBatch batch){
        for (Arrow arrow : arrows) {
            arrow.update(Gdx.graphics.getDeltaTime(), batch);
        }
    }

    public void draw(SpriteBatch batch) {

        updateArrows(batch);

        if (!isLoaded && !isLoading) {
            return;
        }

        if (arrowCount == 0) {
            isLoaded = false;
            isLoading = false;
            return;
        }

        if (isLoaded){
            sprite.setTexture(loaded);
        } else {
            sprite.setTexture(loading);
        }

        if (isLoading) {
            if (LocalDateTime.now().isAfter(loadTime.plusSeconds(1))) {
                isLoaded = true;
                isLoading = false;
            }
        }

        this.attackDirection = Utils.mousePosUnproject(Camera.camera);
        this.rotation = Utils.getAngleWithPoint(player.center(), this.attackDirection);

        sprite.setRotation(rotation);
        Point attackPos = Point.getPosWithAngle(player.center(), Player.PLAYER_MELEE_WEAPON_DISTANCE, rotation);
        sprite.setPosition(attackPos.x- (float) loading.getWidth() /2, attackPos.y - (float) loaded.getHeight() / 2);
        this.attackStartPoint = new Point(attackPos.x- (float) loading.getWidth() /2, attackPos.y - (float) loaded.getHeight() / 2);
        sprite.draw(batch);
    }
}
