package ch.bfh.ti.academia;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

public class ServerUtil {

	private static Server server;

	public static void start() throws Exception {
		server = new Server(8080);
		WebAppContext context = new WebAppContext();
		context.setContextPath("/");
		context.setConfigurations(new Configuration[] {new AnnotationConfiguration(), new MetaInfConfiguration()});
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/classes/.*");
		server.setHandler(context);
		server.start();
	}

	public static void stop() throws Exception {
		server.stop();
	}
}
