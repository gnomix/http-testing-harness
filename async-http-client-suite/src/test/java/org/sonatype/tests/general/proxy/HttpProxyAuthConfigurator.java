package org.sonatype.tests.general.proxy;

import org.sonatype.tests.jetty.server.configurations.DefaultSuiteConfigurator;
import org.sonatype.tests.jetty.server.impl.JettyProxyProvider;
import org.sonatype.tests.jetty.server.impl.JettyServerProvider;
import org.sonatype.tests.server.api.ServerProvider;

/**
 * @author Benjamin Hanzelmann
 */
public class HttpProxyAuthConfigurator
    extends DefaultSuiteConfigurator
{

    private JettyProxyProvider provider;

    public HttpProxyAuthConfigurator()
    {
        try
        {
            ServerProvider realServer = new JettyServerProvider();
            realServer.setPort( 8887 );
            provider = new JettyProxyProvider( realServer, "puser", "password" );
        }
        catch ( Exception e )
        {
            throw new IllegalStateException( e );
        }
    }

    @Override
    public ServerProvider provider()
    {
        return provider;
    }

}