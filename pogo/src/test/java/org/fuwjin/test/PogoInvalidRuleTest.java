package org.fuwjin.test;

import static org.fuwjin.pogo.PogoUtils._new;
import static org.fuwjin.pogo.PogoUtils.append;
import static org.fuwjin.pogo.PogoUtils.ignore;
import static org.fuwjin.pogo.PogoUtils.ref;
import static org.fuwjin.pogo.PogoUtils.result;
import static org.fuwjin.pogo.PogoUtils.rule;
import static org.fuwjin.pogo.PogoUtils.type;
import static org.junit.Assert.fail;

import org.fuwjin.pogo.Grammar;
import org.junit.Test;

/**
 * Tests invalid rules for pogo.
 */
public class PogoInvalidRuleTest {
   /**
    * Tests behavior for an unknown rule.
    */
   @Test
   public void testUnknownRule() {
      new Grammar() {
         {
            add(rule(
                  "Grammar", type(org.fuwjin.test.SampleBuilderPattern.class), _new(), result(), ref("Rule", ignore(), append("addChild")))); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
            try {
               resolve();
               fail("Should throw an exception"); //$NON-NLS-1$
            } catch(final RuntimeException e) {
               // pass
            }
         }
      };
   }
}
