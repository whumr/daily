package test.continuation;

import java.lang.management.ManagementFactory;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.webapp.WebAppContext;

public class T {

	public static void main(String[] args) throws Exception {
		Server s = new Server();
		
//		// Setup JMX
//		MBeanContainer mbContainer = new MBeanContainer(ManagementFactory.getPlatformMBeanServer());
//		s.getContainer().addEventListener(mbContainer);
//		s.addBean(mbContainer);
//
//		// Register loggers as MBeans
//		mbContainer.addBean(Log.getLogger(T.class));
		
		SelectChannelConnector nioConnector = new SelectChannelConnector();
        nioConnector.setUseDirectBuffers(false);
        nioConnector.setPort(80);
        s.addConnector(nioConnector);
        
        
        HandlerCollection handlers = new HandlerCollection();
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        
        
        WebAppContext app = new WebAppContext("web/", "/");
        
        contexts.addHandler(app);
        
        
//        contexts.addContext("/", "/t/index.html");
        
        handlers.setHandlers(new Handler[]{contexts, new DefaultHandler()});
        s.setHandler(handlers);
        
        
		s.start();
		
		app.start();
	}
}