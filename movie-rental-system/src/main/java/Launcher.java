import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Launcher {
    private static final int PORT = 8000;

    public static void main(String[] args) throws Exception {
        // Create Tomcat instance
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);
        tomcat.getConnector();

        // Get the web application directory
        String webappDirLocation = "src/main/webapp/";
        StandardContext ctx = (StandardContext) tomcat.addWebapp("", 
            new File(webappDirLocation).getAbsolutePath());

        // Add compiled classes to the context
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
            additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        try {
            // Start the server
            tomcat.start();
            System.out.println("Server started on port " + PORT);
            System.out.println("Open your browser and navigate to http://localhost:" + PORT);
            // Keep the server running
            tomcat.getServer().await();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
