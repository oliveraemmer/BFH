package ch.bfh.ti.academia;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

	public static void setLevel(Level level) {
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(level);
		Arrays.stream(rootLogger.getHandlers()).forEach(handler -> handler.setLevel(level));
	}
}
