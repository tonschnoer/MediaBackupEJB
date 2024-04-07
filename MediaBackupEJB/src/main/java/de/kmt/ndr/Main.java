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
		System.out.println("Read ini file");
		Config.readini();
		
	}

	// ***********************************************************************************
	// triggers every time when timer fires
	// ***********************************************************************************
	@Timeout
	public void scheduler(Timer timer) {
		try {
			System.out.println("Timer fired.");
			TCPClient tc = new TCPClient();
			tc.run();
			
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
	}

	// ***********************************************************************************
	// OnStart; configures timer
	// ***********************************************************************************
	@PostConstruct
	public void initialize() {
		try {
			_setUP();
			System.out.println("initialize application");
			ScheduleExpression se = new ScheduleExpression();
			
			if (!Config.hours.equals("")) se = se.hour(Config.hours);
			if (!Config.minutes.equals("")) se = se.minute(Config.minutes);
			if (!Config.seconds.equals("")) se = se.second(Config.seconds);
			
			timerService.createCalendarTimer(se, new TimerConfig("Next Backup Request scheduled at ", false));
		} catch (Exception _ex) {

			_ex.printStackTrace();
		}
	}

	// ***********************************************************************************
	// OnStop; destroys timer
	// ***********************************************************************************
	@PreDestroy
	public void stop() {

		try {
			for (Timer timer : timerService.getTimers()) {
				timer.cancel();
			}
		} catch (Exception _ex) {
			_ex.printStackTrace();
		}
	}

}
