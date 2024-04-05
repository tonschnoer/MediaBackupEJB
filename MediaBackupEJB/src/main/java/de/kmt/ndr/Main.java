package de.kmt.ndr;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.ScheduleExpression;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerConfig;
import jakarta.ejb.TimerService;


@Singleton
@Startup

public class Main {
	
    @Resource
    private TimerService timerService;
    
    private void _setUP() {
  	
    }
    
    @Timeout
    public void scheduler(Timer timer) {
    	try {
    		_setUP();
    		System.out.println("Timer fired.");
    	}
    	catch(Exception _ex) {
    		
    	}
    }

    @PostConstruct
    public void initialize() {
    	try {
    		_setUP();
            ScheduleExpression se = new ScheduleExpression();
            se.hour("*").minute("0/1").second("0/1");
            
            timerService.createCalendarTimer(se, new TimerConfig("ARD-ZDF-Box Cleanup Service scheduled at ", false));
    	}
    	catch(Exception _ex) {
    		
    	}
    }

    @PreDestroy
    public void stop() {
    	
    	try {
    		_setUP();
	        for (Timer timer : timerService.getTimers()) {
        	
	            timer.cancel();
	        }
    	}
    	catch(Exception _ex) {
    		
    	}
    }

}

