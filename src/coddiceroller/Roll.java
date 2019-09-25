/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coddiceroller;

import java.util.ArrayList;

/**
 *
 * @author Dylan Veraart
 */
public class Roll {

  int successes, ones, bonuses, bonusOnes, bonusSuccesses;
  ArrayList<Integer> rolls;
  ArrayList<Integer> bonusRolls;

  public Roll(int dice, int again) {
    successes = 0;
    ones = 0;
    rolls = new ArrayList<>(dice);
    bonusRolls = new ArrayList<>();
    for (int i = 0; i < dice; i++) {
      int die = (int) (Math.random() * 10) + 1;
      if (die > 7) {
        successes++;
      } else if (die == 1) {
        ones++;
      }
      if (die >= again) {
        bonus(again);
      }
      rolls.add(die);
    }
  }

  private void bonus(int again) {
    bonuses++;
    int die;
    if (bonuses > bonusRolls.size()) {
      die = (int) (Math.random() * 10) + 1;
      if (die > 7) {
        bonusSuccesses++;
      } else if (die == 1) {
        bonusOnes++;
      }
      bonusRolls.add(die);
    } else {
      die = bonusRolls.get(bonuses - 1);
      if (die > 7) {
        bonusSuccesses++;
      } else if (die == 1) {
        bonusOnes++;
      }
    }
    if (die >= again) {
      bonus(again);
    }
  }

  public void again(int again) {
    bonuses = 0;
    bonusSuccesses = 0;
    bonusOnes = 0;
    for (int i = 0; i < rolls.size(); i++) {
      if (rolls.get(i) >= again) {
        bonus(again);
      }
    }
  }

  public String toString() {
    StringBuilder retString = new StringBuilder();
    for (int i = 0; i < rolls.size(); i++) {
      retString.append(rolls.get(i).toString());
      retString.append(", ");
    }
    for (int i = 0; i < bonuses; i++) {
      retString.append(bonusRolls.get(i).toString());
      retString.append(", ");
    }
    retString.delete(retString.length() - 2, retString.length() - 1);
    return retString.toString();
  }

  public String info(boolean houseRule) {
    int successes = this.successes + this.bonusSuccesses;
    int ones = this.ones + this.bonusOnes;
    StringBuilder retString = new StringBuilder();
    retString.append("<html><center>");
    if (successes == 0) {
      retString.append("No successes.<br>");
    } else {
      retString.append(successes).append(" success");
      if (successes != 1) {
        retString.append("es");
      }
      retString.append(".<br>");
    }
    if (houseRule && ones >= ((rolls.size() + bonuses) / 2.0)) {
      if (successes == 0) {
        retString.append("Dramatic failure.");
      } else if (successes >= 5) {
        retString.append("Dramatic failure and exceptional success.");
      } else {
        retString.append("Dramatic failure and success.");
      }
    } else {
      if (successes == 0) {
        retString.append("Failure.");
      } else if (successes >= 5) {
        retString.append("Exceptional success.");
      } else {
        retString.append("Success.");
      }
    }
    retString.append("</center></html>");
    return retString.toString();
  }
}
