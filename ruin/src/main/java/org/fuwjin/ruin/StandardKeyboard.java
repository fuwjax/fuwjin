package org.fuwjin.ruin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardKeyboard implements Keyboard{
   private final Map<Character, RobotAction> map = new HashMap<Character, RobotAction>();
   {
      for(final char ch: "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()){
         map.put(Character.toLowerCase(ch), new KeyAction(ch));
         map.put(ch, ActionModifier.SHIFT.modified(new KeyAction(ch)));
      }
   }

   @Override
   public RobotAction toAction(final String text){
      final List<RobotAction> actions = new ArrayList<RobotAction>(text.length());
      for(final char ch: text.toCharArray()){
         actions.add(map.get(ch));
      }
      return CompositeAction.newComposite(actions);
   }
}
