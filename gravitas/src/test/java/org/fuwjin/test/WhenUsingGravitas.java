package org.fuwjin.test;

import static org.fuwjin.gravitas.Gravitas.main;
import static org.fuwjin.util.SystemUtils.buffer;
import static org.fuwjin.util.SystemUtils.in;
import static org.fuwjin.util.SystemUtils.out;
import static org.fuwjin.util.SystemUtils.release;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fuwjin.util.matcher.PatternMatcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WhenUsingGravitas{
   @BeforeClass
   public static void init() throws Exception{
      buffer();
      main();
   }

   @AfterClass
   public static void close() throws Exception{
      input("quit");
      expect("Shutting down now");
      release();
   }
   
   @After
   public void teardown() throws Exception{
      assertThat(clearSystemOut(),is(0));
   }

   private static Integer clearSystemOut() throws IOException{
      int count = 0;
      while(out.ready()){
         out.readLine();
         count++;
      }
      return count;
   }

   @Test
   public void shouldDisplayHelp() throws Throwable{
      input("help");
      expect("jobs, status - Displays the current status of all known jobs");
      expect("jobs $jobId, status $jobId - Displays the current status of a single job");
      clearSystemOut();
      input("help jobs");
      expect("jobs - Displays the current status of all known jobs");
      expect("   Aliases: status");
      expect("");
      expect("jobs $jobId - Displays the current status of a single job");
      expect("   jobId: the id of the job to display");
      expect("   Aliases: status $jobId");
      clearSystemOut();
   }

   @Test
   public void shouldManageJobs() throws Throwable{
      input("clear jobs");
      matches("Removed \\d+ jobs");
      input("clear jobs");
      expect("Removed 1 job");
      input("jobs");
      matches("1\\) \\[(Pending|Executing)] \\*bootstrap\\*");
      matches("\\d+\\) \\[Finished] clear jobs");
      matches("\\d+\\) \\[Executing] jobs");
      input("quit 30");
      expect("Shutting down in 30 seconds");
      input("last job");
      final int id = Integer.valueOf(matchesPattern("(\\d+)\\) \\[Pending] \\*delayed quit\\*"));
      input("kill " + id);
      expect("Job " + id + " has been cancelled");
      input("jobs " + id);
      matches("\\d+\\) \\[Interrupted] \\*delayed quit\\*");
      input("kill " + id);
      expect("Could not cancel job " + id);
      input("clear jobs");
      expect("Removed 8 jobs");
      input("jobs " + id);
      expect("There is no job " + id);
      input("kill " + id);
      expect("There is no job " + id);
   }

   @Test
   public void shouldManageQueue() throws Throwable{
      input("queue");
      expect("The queue is empty");
      input("silly command");
      input("queue");
      expect("1) [console] silly command");
      input("clear queue");
      expect("Removed 1 event");
      input("clear queue");
      expect("The queue is empty");
      input("wrong 1");
      input("wrong 2");
      input("clear queue");
      expect("Removed 2 events");
   }

   private static void expect(final String expectation) throws IOException{
      assertThat(out.readLine(), is(expectation));
   }

   private static void input(final String command){
      in.println(command);
   }
   
   private static void matches(final String expectation) throws IOException{
      assertThat(out.readLine(), PatternMatcher.matches(expectation));
   }

   private static String matchesPattern(final String expectation) throws IOException{
      final String response = out.readLine();
      Matcher matcher = Pattern.compile(expectation).matcher(response);
      assertTrue("Expected: is "+response+" got: "+expectation,matcher.matches());
      return matcher.group(1);
   }
}
