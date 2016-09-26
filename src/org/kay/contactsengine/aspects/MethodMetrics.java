package org.kay.contactsengine.aspects;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MethodMetrics {
	private long time;
	Instant start;
	
	@Pointcut("execution(* org.kay.contactsengine.actions.*.execute(..)) ")
	public void executors() {
	
	}
	
	@Before("executors()")
	public void timerStart() {
		start = Instant.now();
	}
	
	@After("executors()")
	public void timerStop() {
		Instant stop = Instant.now();
		Duration timeElapsed = Duration.between(start,stop);
		System.out.println("Time taken: " + timeElapsed.toMillis());
	}
}
