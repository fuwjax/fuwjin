package org.fuwjin.ruin;

import static java.util.Collections.singletonMap;
import static org.fuwjin.chessur.Catalog.loadCat;
import static org.fuwjin.chessur.InStream.NONE;
import static org.fuwjin.chessur.InStream.stream;
import static org.fuwjin.util.StreamUtils.reader;
import static org.fuwjin.util.StringUtils.readAll;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.HashMap;
import java.util.Map;
import org.fuwjin.chessur.Catalog;

public class Ruin {
   private static Catalog ruinParser;
   private final Robot robot;
   private final Keyboard keyboard;
   private final Map<String, RobotAction> targets;

   public Ruin(final Keyboard keyboard) {
      try {
         robot = new Robot();
      } catch(final AWTException e) {
         throw new RuntimeException("Cannot create RUIn Robot", e);
      }
      this.keyboard = keyboard;
      targets = new HashMap<String, RobotAction>();
   }

   public void addTarget(final String id, final Target target) {
      targets.put(id, new MouseMoveAction(target));
   }

   public RobotAction key(final String text) {
      return keyboard.toAction(text);
   }

   public RobotAction move(final String target) {
      return targets.get(target);
   }

   public void open(final String path) throws Exception {
      final Catalog layout = loadCat(readAll(reader(path, "UTF-8")));
      final Target target = (Target)layout.transform(NONE, singletonMap("ruin", this));
      target.await(1000);
   }

   public void run(final String path) throws Exception {
      if(ruinParser == null) {
         ruinParser = loadCat(readAll(reader("ruin.cat", "UTF-8")));
      }
      final RobotAction action = (RobotAction)ruinParser.transform(stream(reader(path, "UTF-8")),
            singletonMap("ruin", this));
      action.perform(robot);
   }
}
