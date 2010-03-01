package org.fuwjin.test;

import static org.fuwjin.gravitas.test.TestIntegration.newIntegration;

import java.util.Random;

import org.fuwjin.gravitas.test.TestIntegration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WhenUsingGravitas{
   private static TestIntegration test;

   @AfterClass
   public static void close() throws Exception{
      if(test != null){
         test.input("quit");
         test.expect("Shutting down now");
      }
   }

   @BeforeClass
   public static void init() throws Exception{
      test = newIntegration();
   }

   @Test
   public void shouldDisplayHelp() throws Throwable{
      test.input("help");
      test.expect("jobs, status - Displays the current status of all known jobs");
      test.expect("jobs $jobId, status $jobId - Displays the current status of a single job");
      test.expectLines(16);
      test.input("help jobs");
      test.expect("jobs - Displays the current status of all known jobs");
      test.expect("   Aliases: status");
      test.expect("");
      test.expect("jobs $jobId - Displays the current status of a single job");
      test.expect("   jobId: the id of the job to display");
      test.expect("   Aliases: status $jobId");
      test.expectLines(5);
   }

   @Test
   public void shouldManageJobs() throws Throwable{
      test.input("clear jobs");
      test.matches("Removed \\d+ jobs");
      test.input("clear jobs");
      test.expect("Removed 1 job");
      test.input("jobs");
      test.matches("\\d+\\) \\[Finished] clear jobs");
      test.matches("\\d+\\) \\[Executing] jobs");
      test.input("quit 30");
      test.expect("Scheduling quit in 30 seconds");
      test.input("last job");
      final String actual = test.matches("\\d+\\) \\[Pending] "+"quit");
      final int id = Integer.valueOf(actual.substring(0, actual.indexOf(')')));
      test.input("kill "+id);
      test.expect("Job " + id + " has been cancelled");
      test.input("jobs " + id);
      test.matches("\\d+\\) \\[Interrupted] quit");
      test.input("kill " + id);
      test.expect("Could not cancel job " + id);
      test.input("clear jobs");
      test.expect("Removed 8 jobs");
      test.input("jobs " + id);
      test.expect("There is no job " + id);
      test.input("kill " + id);
      test.expect("There is no job " + id);
   }

   @Test
   public void shouldManageQueue() throws Throwable{
      test.input("queue");
      test.expect("The queue is empty");
      test.input("silly command");
      test.input("queue");
      test.matches("\\d+\\) \\[test] silly command");
      test.input("clear queue");
      test.expect("Removed 1 event");
      test.input("clear queue");
      test.expect("The queue is empty");
      test.input("wrong 1");
      test.input("wrong 2");
      test.input("clear queue");
      test.expect("Removed 2 events");
   }

   @Test
   public void shouldManageScheduling() throws Throwable{
      test.input("in 1 millisecond echo hi");
      test.expect("Scheduling echo hi in 1 milliseconds");
      test.expect("hi");
   }

   @Test
   public void shouldDumpStackTrace() throws Throwable{
      test.input("threads");
      test.expectLines(10);
   }

   @Test
   public void shouldDropEvents() throws Throwable{
      test.input("this is not a valid event");
      test.input("queue");
      test.matches("\\d+\\) \\[test] this is not a valid event");
      test.input("every 1 millisecond repeat 2 times drop events");
      test.expect("Scheduling repeat 2 times drop events every 1 milliseconds");
      test.expect("Finished looping");
      test.input("queue");
      test.expect("The queue is empty");
   }

   @After
   public void assertIsClear() throws Exception{
      int num = new Random().nextInt();
      test.input("echo "+num);
      test.expect(Integer.toString(num));
   }
}
