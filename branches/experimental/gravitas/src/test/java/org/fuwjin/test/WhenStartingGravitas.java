package org.fuwjin.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.fuwjin.gravitas.Gravitas;
import org.fuwjin.gravitas.util.SystemUtils;
import org.junit.Test;

public class WhenStartingGravitas {
	@Test
	public void shouldLoadServices() throws Throwable{
		SystemUtils.buffer();
		try{
			Gravitas.main();
			SystemUtils.in.println("hello world");
			assertThat(SystemUtils.out.readLine(), is("world says hi to you!"));
			SystemUtils.in.println("hello bob");
			assertThat(SystemUtils.out.readLine(), is("bob says hi to you!"));
		}finally{
			SystemUtils.release();
		}
	}
}
