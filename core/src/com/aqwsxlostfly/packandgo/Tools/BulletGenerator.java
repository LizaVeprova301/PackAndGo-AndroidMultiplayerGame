package com.aqwsxlostfly.packandgo.Tools;

import com.aqwsxlostfly.packandgo.Heroes.Bullet;
import com.aqwsxlostfly.packandgo.Main;
import com.aqwsxlostfly.packandgo.Screens.GameSc;
import com.aqwsxlostfly.packandgo.Screens.WaitingSc;

public class BulletGenerator {
    boolean isFire;

    public void update(Joystick joy) {
        isFire = joy.getDir().getX() != 0 || joy.getDir().getY() != 0;
        if (isFire) WaitingSc.bullets.add(new Bullet(Main.bullet, Main.players.get(Main.meId).position,
                25, Main.players.get(Main.meId).radius / 8, joy.getDir()));


    }
}
