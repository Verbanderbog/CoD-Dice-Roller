/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coddiceroller;

import java.util.*;

/**
 *
 * @author Dylan Veraart
 */
public class HealthState {

  ArrayList<Integer> health;
  int tempHealth;

  public HealthState(int maxHealth, int tempHealth) {
    health = new ArrayList<>(maxHealth);
    this.tempHealth = tempHealth;
    for (int i = 0; i < maxHealth; i++) {
      health.add(0);
    }
  }

  private HealthState(HealthState previous) {
    this.health = new ArrayList<>(previous.health);
    this.tempHealth = previous.tempHealth;
  }

  public HealthState newMaxHealth(int maxHealth) {
    HealthState retHealth = new HealthState(this);
    int difference = maxHealth - retHealth.health.size();
    if (difference > 0) {
      for (int i = 0; i < difference; i++) {
        retHealth.health.add(0);
      }
    } else if (difference < 0) {
      int damages[] = {0, 0, 0};
      for (int i = 0; i < (difference * -1); i++) {
        int damage = retHealth.health.remove(retHealth.health.size() - 1);
        if (damage > 0) {
          damages[damage - 1]++;
        }
      }
      retHealth.damage(false, damages);
    }
    return retHealth;
  }

  public HealthState newTempHealth(int tempHealth) {
    HealthState retHealth = new HealthState(this);
    retHealth.tempHealth = tempHealth;
    return retHealth;
  }

  public HealthState clear() {
    HealthState retHealth = new HealthState(this);
    for (int i = 0; i < health.size(); i++) {
      retHealth.health.set(i, 0);
    }
    return retHealth;
  }

  public HealthState damage(int damages[]) {
    HealthState retHealth = new HealthState(this);
    retHealth.damage(true, damages);
    return retHealth;
  }

  private void damage(boolean affectTempHealth, int damages[]) {
    boolean hasDamage = false;
    for (int damageType = 2; damageType >= 0; damageType--) {
      for (int i = 0; i < damages[damageType]; i++) {
        hasDamage = true;
        this.damage(affectTempHealth, damageType + 1);
      }
    }
    if (affectTempHealth && tempHealth > 0 && hasDamage) {
      this.tempHealth = 0;
    }
  }

  private void damage(boolean affectTempHealth, int damage) {
    if (affectTempHealth && tempHealth > 0) {
      tempHealth--;
    } else {
      int last = health.get(health.size() - 1);
      switch (last) {
        case 0:
          health.set((health.size() - 1), damage);
          Collections.sort(health);
          Collections.reverse(health);
          break;
        case 1:
          if (damage < 3) {
            health.set((health.size() - 1), 2);
            Collections.sort(health);
            Collections.reverse(health);
          } else {
            health.set((health.size() - 1), 3);
            Collections.sort(health);
            Collections.reverse(health);
            this.damage(false, 1);
          }
          break;
        case 2:
          health.set((health.size() - 1), 3);
          Collections.sort(health);
          Collections.reverse(health);
          break;
      }
    }
  }

  public HealthState heal(int boxes) {
    HealthState retHealth = new HealthState(this);
    for (int i = 0; i < boxes; i++) {
      int last = retHealth.indexOfLastDamage();
      if (last < 0) {
        break;
      }
      retHealth.health.set(last, 0);
    }
    return retHealth;
  }

  public String healInfo() {
    if (health.contains(1)) {
      return "Next box heals in 15 minutes";
    }
    if (health.contains(2)) {
      return "Next box heals in 2 days";
    }
    if (health.contains(3)) {
      return "Next box heals in a week";
    }
    return "";
  }

  public String healthInfo() {
    switch (health.get(health.size()-1)){
      case 3:
        return "You're dead! YAAAAAAY!";
      case 2:
        return "Take damage every 20 turns. Keep making Stamina rolls.";
      case 1:
        return "Make a Stamina roll each turn to remain concious.";
    }
    return "";
  }

  public HealthState downgrade() {
    HealthState retHealth = new HealthState(this);
    retHealth.health.set(0, (health.get(0) - 1));
    Collections.sort(retHealth.health);
    Collections.reverse(retHealth.health);
    return retHealth;
  }

  public int indexOfLastDamage() {
    int last = health.indexOf(0);
    if (last == -1) {
      last = health.size();
    }
    last--;
    return last;
  }

}
