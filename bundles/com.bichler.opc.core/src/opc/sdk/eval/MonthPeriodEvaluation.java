package opc.sdk.eval;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.application.AbstractApplication;

public class MonthPeriodEvaluation {
	private static final String PROP_START = "START";
	private static final String PROP_DURATION = "DURATION";
	private AbstractApplication application;
	private Timer timer;

	public MonthPeriodEvaluation(AbstractApplication application) {
		this.application = application;
		this.timer = new Timer("Evaluation", true);
	}

	public void start() {
		TimerTask evalTask = new TimerTask() {
			@Override
			public void run() {
				eval();
			}
		};
		this.timer.schedule(evalTask, 1000, 10000);
	}

	private void eval() {
		File f = new File("evalproperties.evl");
		FileInputStream fis = null;
		// read
		Properties property = new Properties();
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			property.load(fis);
			String start = property.getProperty(PROP_START);
			String dur = property.getProperty(PROP_DURATION);
			// init eval
			if (start == null || dur == null) {
				long now = System.currentTimeMillis();
				start = dur = "" + now;
				property.setProperty(PROP_START, "" + start);
				property.setProperty(PROP_DURATION, "" + start);
			} else {
				long last = Long.parseLong(dur);
				long now = System.currentTimeMillis();
				// check expired
				long first = Long.parseLong(start);
				long month = 1000l * 60l * 60l * 24l * 30l;
				if (now > (first + month)) {
					// Exit
					this.application.close();
					this.timer.cancel();
					return;
				}
				// check time last is wrongb
				if (now < last) {
					// Exit
					this.application.close();
					this.timer.cancel();
					return;
				}
				property.setProperty(PROP_DURATION, "" + now);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		// write
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f);
			property.store(fos, "Evalproperties");
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}
}
