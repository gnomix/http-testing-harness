package org.sonatype.tests.http.server.jetty.behaviour;

/*
 * Copyright (c) 2010-2011 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0, 
 * and you may not use this file except in compliance with the Apache License Version 2.0. 
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the Apache License Version 2.0 is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import com.google.common.io.Files;
import org.sonatype.tests.http.server.api.Behaviour;

/**
 * @author Benjamin Hanzelmann
 */
public class Content
    implements Behaviour
{

    private String content;

    private String type;

    private File file;

    public Content( final File content )
    {
        this( content, "application/octet-stream" );
    }

    public Content( final File content, final String type )
    {
        this.file = content;
        this.type = type;
    }

    public static Content content( File content )
    {
        return new Content( content );
    }

    public static Content content( File content, String type )
    {
        return new Content( content, type );
    }

    public static Content content( String content )
    {
        return new Content( content );
    }

    public static Content content( String content, String type )
    {
        return new Content( content, type );
    }

    public Content()
    {
        this.type = "text/plain";
    }

    public Content( String content )
    {
        this( content, "text/plain" );
    }

    public Content( String content, String type )
    {
        this.content = content;
        this.type = type;
    }

    public boolean execute( HttpServletRequest request, HttpServletResponse response, Map<Object, Object> ctx )
        throws Exception
    {
        if ( "GET".equals( request.getMethod() ) )
        {
            if ( file != null )
            {
                deliverFile( request, response );
            }
            else
            {
                deliverString( request, response, ctx );
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    private void deliverFile( final HttpServletRequest request, final HttpServletResponse response )
        throws IOException
    {
        response.setContentType( type );
        response.setContentLength( (int) file.length() );
        response.getOutputStream().write( Files.toByteArray( file ) );
    }

    private void deliverString( final HttpServletRequest request, final HttpServletResponse response,
                                final Map<Object, Object> ctx )
        throws IOException
    {
        String content = this.content;
        response.setContentType( type );

        if ( content == null )
        {
            String pathInfo = request.getPathInfo();
            content = pathInfo == null ? "" : pathInfo.substring( 1 );
        }

        if ( ctx.containsKey( Keys.CONTENT ) )
        {
            content = ctx.get( Keys.CONTENT ).toString();
        }

        response.setContentLength( content.getBytes( "UTF-8" ).length );
        try
        {
            response.getOutputStream().write( content.getBytes( "UTF-8" ) );
        }
        catch ( IllegalStateException e )
        {
            response.getWriter().write( content );
        }
    }

}
