package com.project.seabattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIController {

    private final Field field;

    Coordinate lastSuccessAttack;
    private final List<Coordinate> nextAttackList = new ArrayList<>();

    public AIController(Field field) {
        this.field = field;
    }

    public void startAttack() {
        if (lastSuccessAttack == null) {
            List<Coordinate> allowList = field.allowFireCells();

            Coordinate randomCoordinate = allowList.get(new Random().nextInt(allowList.size()));
            int x = randomCoordinate.getX();
            int y = randomCoordinate.getY();

            if (field.attackCell(x, y)) {
                if (!field.isKill(x, y)) {
                    lastSuccessAttack = new Coordinate(x, y);

                    if (field.isAllowFire(x + 1, y)) nextAttackList.add(new Coordinate(x + 1, y));
                    if (field.isAllowFire(x - 1, y)) nextAttackList.add(new Coordinate(x - 1, y));

                    if (continueAttack()) startAttack();
                }
                else startAttack();
            }

        }
        else {
            if (continueAttack()) startAttack();
        }
    }

    private boolean continueAttack() {
        if (nextAttackList.isEmpty()) {
            if (field.isAllowFire(lastSuccessAttack.getX(), lastSuccessAttack.getY() + 1))
                nextAttackList.add(new Coordinate(lastSuccessAttack.getX(), lastSuccessAttack.getY() + 1));
            if (field.isAllowFire(lastSuccessAttack.getX(), lastSuccessAttack.getY() - 1))
                nextAttackList.add(new Coordinate(lastSuccessAttack.getX(), lastSuccessAttack.getY() - 1));
        }

        int attackX = nextAttackList.get(0).getX();
        int attackY = nextAttackList.get(0).getY();
        if (field.attackCell(attackX, attackY)) {
            if (field.isKill(attackX, attackY)) {
                lastSuccessAttack = null;
                nextAttackList.clear();
                return true;
            }

            if (attackX > lastSuccessAttack.getX()) attackX++;
            if (attackX < lastSuccessAttack.getX()) attackX--;
            if (attackY > lastSuccessAttack.getY()) attackY++;
            if (attackY < lastSuccessAttack.getY()) attackY--;

            if (field.isAllowFire(attackX, attackY)) {
                Coordinate newCoordinate = new Coordinate(attackX, attackY);
                nextAttackList.add(newCoordinate);
            }

            nextAttackList.remove(0);
            continueAttack();
        }
        else nextAttackList.remove(0);
        return false;
    }
}
