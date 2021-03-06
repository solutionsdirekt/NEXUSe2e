/**
 *  NEXUSe2e Business Messaging Open Source
 *  Copyright 2000-2021, direkt gruppe GmbH
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation version 3 of
 *  the License.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.nexuse2e.test.http;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * A command-line tool that allows to initiate one or more HTTP requests
 * to the Nexus server.
 * 
 * @author jonas.reese
 */
public class HttpTestClient {

    private static final int SOCKET_TIMEOUT = 120000;

    private static class RunnableImpl implements Runnable {

        long    timeAcc = 0;
        long    maxTime = Long.MIN_VALUE;
        long    minTime = Long.MAX_VALUE;
        int     threadNum;
        int     repeat;
        boolean plain;
        URL     url;
        String  choreographyId;
        String  actionId;
        String  participantId;
        String  senderId;
        String  content;
        String  user;
        String  password;

        RunnableImpl( int threadNum, int repeat, boolean plain, URL url, String choreographyId, String actionId,
                String participantId, String senderId, String content, String user, String password ) {

            this.threadNum = threadNum;
            this.repeat = repeat;
            this.plain = plain;
            this.url = url;
            this.choreographyId = choreographyId;
            this.actionId = actionId;
            this.participantId = participantId;
            this.senderId = senderId;
            this.content = content;
            this.user = user;
            this.password = password;
        }

        public void run() {

            DateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
            try {
                HttpClient client = new HttpClient();
                for ( int i = 0; i < repeat; i++ ) {
                    long delay = (long) ( Math.random() * 100.0 );
                    Thread.sleep( delay );
                    
                    String urlParams = "";

                    UUID isoUuid = UUID.randomUUID();
                    String conversationId = isoUuid.toString();
                    isoUuid = UUID.randomUUID();
                    String messageId = isoUuid.toString();

                    if ( plain ) {
                        StringBuffer buffer = new StringBuffer( "?" );
                        buffer.append( "ChoreographyID=" + choreographyId );
                        buffer.append( "&ActionID=" + actionId );
                        buffer.append( "&ParticipantID=" + senderId );
                        buffer.append( "&ConversationID="+ conversationId );
                        buffer.append( "&MessageID="+ messageId );
                        urlParams = buffer.toString();
                    }
                    HostConfiguration configuration = new HostConfiguration();
                    configuration.setHost( new HttpHost( url.getHost(), url.getPort(), Protocol.getProtocol( url
                            .getProtocol() ) ) );
                    client.setHostConfiguration( configuration );
                    PostMethod method = new PostMethod( url.toExternalForm() + urlParams );
                    method.getParams().setSoTimeout( SOCKET_TIMEOUT );
                    if ( plain ) {
                        method.setParameter( "ChoreographyID", choreographyId );
                        method.setParameter( "ActionID", actionId );
                        method.setParameter( "ParticipantID", participantId );
                        method.setParameter( "ConversationID", conversationId );
                        method.setParameter( "MessageID", messageId );
                        //out( "Request: " + content );
                        RequestEntity requestEntity = new StringRequestEntity( content, "text/xml", "UTF-8" );
                        method.setRequestEntity( requestEntity );
                    } else {
                        String timestamp = df.format( new Date() );
                        // replace variables in template file
                        String replaced = StringUtils.replace( content, "${ChoreographyID}", choreographyId );
                        replaced = StringUtils.replace( replaced, "${ActionID}", actionId );
                        replaced = StringUtils.replace( replaced, "${ParticipantID}", participantId );
                        replaced = StringUtils.replace( replaced, "${SenderID}", senderId );
                        replaced = StringUtils.replace( replaced, "${ConversationID}", conversationId );
                        replaced = StringUtils.replace( replaced, "${MessageID}", messageId );
                        replaced = StringUtils.replace( replaced, "${Timestamp}", timestamp );
                        RequestEntity requestEntity = new StringRequestEntity( replaced, "multipart/related", "UTF-8" );
                        //out( "Request: " + replaced );
                        method.setRequestEntity( requestEntity );
                    }
                    if (!StringUtils.isBlank( user ) && !StringUtils.isBlank( password )) {
                        Credentials credentials = new UsernamePasswordCredentials( user, password );
                        client.getState().setCredentials( AuthScope.ANY, credentials );
                        method.setDoAuthentication( true );
                    }
                    long time = System.currentTimeMillis();
                    int result = client.executeMethod( method );
                    time = System.currentTimeMillis() - time;
                    if ( maxTime < time ) {
                        maxTime = time;
                    }
                    if ( minTime > time ) {
                        minTime = time;
                    }
                    timeAcc += time;
                    out( "Request in thread " + threadNum + " took " + time + " ms, HTTP result is " + result + "("
                            + method.getResponseBodyAsString() + ")" );
                }
            } catch ( HttpException e ) {
                e.printStackTrace();
            } catch ( IOException e ) {
                System.err.println( e.getMessage() );
            } catch ( InterruptedException ignored ) {
            }
        }
    }

    private static synchronized void out( String s ) {

        System.out.println( s );
    }

    private static void request( URL url, File file, boolean plain, int repeat, int threads, String choreographyId,
            String actionId, String participantId, String senderId, String user, String password ) throws IOException {

        String content = FileUtils.readFileToString( file, "UTF-8" );
        ExecutorService threadPool = Executors.newFixedThreadPool( threads );
        List<RunnableImpl> runnables = new ArrayList<RunnableImpl>();
        for ( int i = 0; i < threads; i++ ) {
            RunnableImpl runnable = new RunnableImpl( i, repeat, plain, url, choreographyId, actionId, participantId,
                    senderId, content, user, password );
            runnables.add( runnable );
            threadPool.execute( runnable );
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination( Long.MAX_VALUE, TimeUnit.NANOSECONDS );
        } catch ( InterruptedException ignored ) {
            System.out.println( "interrupted" );
        }

        out( "" );
        out( "Statistics" );
        out( "==========" );
        out( "" );
        int i = 0;
        for ( RunnableImpl runnable : runnables ) {
            out( "Thread " + ( i++ ) + " avg. " + ( runnable.timeAcc / runnable.repeat ) + " ms, min. "
                    + runnable.minTime + " ms, max. " + runnable.maxTime + " ms" );
        }
    }

    private static void printHelp( ParseException e, Options options ) {

        if ( e != null && e.getMessage() != null ) {
            if ( e instanceof MissingOptionException ) {
                System.err.println( "Missing option(s): " + e.getMessage() );
            } else if ( e instanceof MissingArgumentException ) {
                System.err.println( "Missing argument(s): " + e.getMessage() );
            } else {
                System.err.println( e.getMessage() );
            }
        }
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java " + HttpTestClient.class.getCanonicalName() + " [options] <file> <url>", options );
    }

    public static void main( String[] args ) {

        CommandLineParser parser = new GnuParser();
        Options options = new Options();
        options.addOption( "?", "help", false, "show command-line options" );
        options.addOption( "p", "plain", false, "use 'plain' request (instead of EBXML header)" );
        Option option = new Option( "r", "repeat", true, "repeat request n times" );
        option.setArgName( "n" );
        options.addOption( option );
        option = new Option( "t", "threads", true, "start n threads (default is 1)" );
        option.setArgName( "n" );
        options.addOption( option );
        option = new Option( "cid", "choreographyid", true, "the choreography ID" );
        option.setArgName( "id" );
        option.setRequired( true );
        options.addOption( option );
        option = new Option( "aid", "actionid", true, "the action ID" );
        option.setArgName( "id" );
        option.setRequired( true );
        options.addOption( option );
        option = new Option( "pid", "participantid", true, "the participant ID" );
        option.setArgName( "id" );
        option.setRequired( true );
        options.addOption( option );
        option = new Option( "sid", "senderid", true, "the sender ID" );
        option.setArgName( "id" );
        option.setRequired( true );
        options.addOption( option );
        option = new Option( "u", "user", true, "the HTTP basic auth user name" );
        option.setArgName( "id" );
        options.addOption( option );
        option = new Option( "pass", "password", true, "the HTTP basic auth password" );
        option.setArgName( "id" );
        options.addOption( option );
        try {
            CommandLine commandLine = parser.parse( options, args );
            if ( commandLine.hasOption( "?" ) ) {
                printHelp( null, options );
                System.exit( 0 );
            }
            boolean plain = commandLine.hasOption( "p" );
            List<?> l = commandLine.getArgList();
            if ( l.size() < 1 ) {
                throw new MissingArgumentException( "file" );
            }
            if ( l.size() < 2 ) {
                throw new MissingArgumentException( "request URL" );
            }
            String file = l.get( 0 ).toString();
            URL url;
            String urlString = l.get( 1 ).toString();
            try {
                url = new URL( urlString );
            } catch ( MalformedURLException mfuex ) {
                throw new ParseException( "Malformed URL: " + urlString );
            }

            int repeat = 1;
            try {
                repeat = Integer.parseInt( commandLine.getOptionValue( "r", "1" ) );
            } catch ( NumberFormatException nfex ) {
                throw new ParseException( "Repeat option must be followed by integer" );
            }

            int threads = 1;
            try {
                threads = Integer.parseInt( commandLine.getOptionValue( "t", "1" ) );
            } catch ( NumberFormatException nfex ) {
                throw new ParseException( "Thread option must be followed by integer" );
            }
            
            String user = null;
            if (commandLine.hasOption( "u" )) {
                user = commandLine.getOptionValue( "u" );
            }

            String password = null;
            if (commandLine.hasOption( "pass" )) {
                password = commandLine.getOptionValue( "pass" );
            }

            String choreographyId = commandLine.getOptionValue( "cid" );
            String actionId = commandLine.getOptionValue( "aid" );
            String participantId = commandLine.getOptionValue( "pid" );
            String senderId = commandLine.getOptionValue( "sid" );

            System.out.println( "requesting " + url + ( plain ? " using plain HTTP" : "" ) + ", posting file " + file );

            request( url, new File( file ), plain, repeat, threads, choreographyId, actionId, participantId, senderId, user, password );
        } catch ( ParseException e ) {
            printHelp( e, options );
            System.exit( -1 );
        } catch ( IOException ioex ) {
            System.err.println( ioex.getMessage() );
        }
    }
}
