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
public class UndoQueue extends ArrayList<HealthState> {

  static final int MAX_SIZE = 40;
  int current;

  public UndoQueue() {
    super(MAX_SIZE);
    current = -1;
    this.add(new HealthState(0,0));
  }
  
  @Override
  
  public boolean add(HealthState h) {
    if (current < (MAX_SIZE)) {
      if (current < (this.size() - 1)) {
        removeRange((current + 1), (this.size()));
      }
      current=this.size();
      return super.add(h);
    } else {
      super.remove(0);
      return super.add(h);
    }
  }
  
  public HealthState undo(){
    if (current>0)
      current--;
    return super.get(current);
  }
  
  public HealthState redo(){
    if (current<(this.size() - 1))
      current++;
    return super.get(current);
  }
  
  public boolean isUndoable(){
    return (current>0);
  }
  
  public boolean isRedoable(){
    return (current<(this.size()-1));
  }
  
  public HealthState current(){
    return super.get(current);
  }
}
