package org.fuwjin.test;

import static org.fuwjin.gravitas.Gravitas.main;
import static org.fuwjin.gravitas.util.SystemUtils.buffer;
import static org.fuwjin.gravitas.util.SystemUtils.clearOut;
import static org.fuwjin.gravitas.util.SystemUtils.in;
import static org.fuwjin.gravitas.util.SystemUtils.out;
import static org.fuwjin.gravitas.util.SystemUtils.release;
import static org.fuwjin.gravitas.util.matcher.RegExMatcher.matches;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
   public static void teardown() throws Exception{
      in.println("quit");
      assertThat(out.readLine(),is("Shutting down now"));
      release();
   }
   
   @Test
   public void shouldManageQueue() throws Throwable{
      in.println("queue");
      assertThat(out.readLine(),is("The queue is empty"));
      in.println("silly command");
      in.println("queue");
      assertThat(out.readLine(), is("1) [console] silly command"));
      in.println("clear queue");
      assertThat(out.readLine(), is("Removed 1 event"));
      in.println("clear queue");
      assertThat(out.readLine(), is("The queue is empty"));
      in.println("wrong 1");
      in.println("wrong 2");
      in.println("clear queue");
      assertThat(out.readLine(), is("Removed 2 events"));
   }
   
   @Test
   public void shouldManageJobs() throws Throwable{
      in.println("clear jobs");
      assertThat(out.readLine(),matches("Removed \\d+ jobs"));
      in.println("clear jobs");
      assertThat(out.readLine(),is("Removed 1 job"));
      in.println("jobs");
      assertThat(out.readLine(),is("1) [Pending] *bootstrap*"));
      assertThat(out.readLine(),matches("\\d+\\) \\[Finished] clear jobs"));
      assertThat(out.readLine(),matches("\\d+\\) \\[Executing] jobs"));
      in.println("quit 30");
      assertThat(out.readLine(),is("Shutting down in 30 seconds"));
      in.println("last job");
      String response = out.readLine();
      int id = Integer.valueOf(response.substring(0,response.indexOf(')')));
      assertThat(response,matches("\\d+\\) \\[Pending] \\*delayed quit\\*"));
      in.println("kill "+id);
      assertThat(out.readLine(),is("Job "+id+" has been cancelled"));
      in.println("jobs "+id);
      assertThat(out.readLine(),matches("\\d+\\) \\[Interrupted] \\*delayed quit\\*"));
      in.println("kill "+id);
      assertThat(out.readLine(),is("Could not cancel job "+id));
      in.println("clear jobs");
      assertThat(out.readLine(),is("Removed 8 jobs"));
      in.println("jobs "+id);
      assertThat(out.readLine(),matches("There is no job "+id));
      in.println("kill "+id);
      assertThat(out.readLine(),is("There is no job "+id));
   }
   
   @Test
   public void shouldDisplayHelp() throws Throwable{
      in.println("help");
      assertThat(out.readLine(),is("jobs, status - Displays the current status of all known jobs"));
      assertThat(out.readLine(),is("jobs $jobId, status $jobId - Displays the current status of a single job"));
      clearOut();
      in.println("help jobs");
      assertThat(out.readLine(),is("jobs - Displays the current status of all known jobs"));
      assertThat(out.readLine(),is("   Aliases: status"));
      assertThat(out.readLine(),is(""));
      assertThat(out.readLine(),is("jobs $jobId - Displays the current status of a single job"));
      assertThat(out.readLine(),is("   jobId: the id of the job to display"));
      assertThat(out.readLine(),is("   Aliases: status $jobId"));
      clearOut();
   }
}
