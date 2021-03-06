package com.cosmicpush.engine.grizzly;

import com.cosmicpush.app.system.CpApplication;
import org.crazyyak.dev.common.StringUtils;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class GrizzlyPushServer {

  public String serverName = "www.localhost";
  private boolean shutDown = false;
  private int port = 8080;
  private int shutdownPort = 8005;
  private String context = "push-server";
  private boolean openBrowser;

  public URI baseUri;

  private HttpServer httpServer;
  private ServerSocket socket;
  private Thread acceptThread;
  /** handlerLock is used to synchronize access to socket, acceptThread and callExecutor. */
  private final ReentrantLock handlerLock = new ReentrantLock();
  private static final int socketAcceptTimeoutMilli = 5000;

  public GrizzlyPushServer() {
  }

  /**
   * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
   * @param args varius command line arguments for the server.
   * @return Grizzly HTTP server.
   * @throws Exception for any reason
   */
  public HttpServer startServer(String...args) throws Exception {
    if (args.length % 2 != 0) {
      throw new IllegalArgumentException("Expected an even number of arguments: " + Arrays.asList(args));
    }

    for (int i = 0; i < args.length; i += 2) {
      String key = args[i];
      String value = args[i+1];
      if ("serverName".equals(key)) {
        serverName = value;
      } else if ("port".equals(key)) {
        port = Integer.valueOf(value);
      } else if ("shutdown".equals(key)) {
        shutdownPort = Integer.valueOf(value);
      } else if ("context".equals(key)) {
        context = value;
      } else if ("open".equals(key)) {
        openBrowser = Boolean.valueOf(value);
      } else if ("action".equals(key) && "stop".equals(value)) {
        shutDown = true;
      }
    }

    this.baseUri = URI.create("http://"+serverName+":"+ port+"/"+context+"/");

    shutdownExisting();

    if (shutDown) {
      System.exit(0);
      return null;
    }

    CpApplication application = new CpApplication();
    CpResourceConfig rc = new CpResourceConfig(application);
    httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, rc);


    // Lock the handler, IllegalStateException thrown if we fail.
    lockHandler();
    try {
      if (acceptThread != null) {
        throw new IllegalStateException("Socket handler thread is already running.");
      }

      try {
        // Set the accept timeout so we won't block indefinitely.
        socket = new ServerSocket(shutdownPort);
        socket.setSoTimeout(socketAcceptTimeoutMilli);

        String msg = String.format("%s is accepting connections on port %s from %s.", getClass().getSimpleName(), shutdownPort, socket.getInetAddress().getHostAddress());
        System.out.println(msg);

      } catch(IOException ex) {
        String msg = String.format("IOException starting server socket, maybe port %s was not available.", shutdownPort);
        System.err.println(msg);
        ex.printStackTrace();
      }

      Thread shutdownThread = new Thread(httpServer::shutdown, "shutdownHook");
      Runtime.getRuntime().addShutdownHook(shutdownThread);

      Runnable acceptRun = GrizzlyPushServer.this::socketAcceptLoop;
      acceptThread = new Thread(acceptRun);
      acceptThread.start();

    } finally {
      // Be sure to always give up the lock.
      unlockHandler();
    }

    return httpServer;
  }

  private void shutdownExisting() throws IOException {
    try(Socket localSocket = new Socket(serverName, shutdownPort)) {
      try(OutputStream outStream = localSocket.getOutputStream()) {
        outStream.write("SHUTDOWN".getBytes());
        outStream.flush();
      }
    } catch (ConnectException ignored) {
    }
  }

  private void lockHandler() throws TimeoutException, InterruptedException {
    int timeout = 5;
    TimeUnit timeUnit = TimeUnit.SECONDS;

    if (!handlerLock.tryLock(timeout, timeUnit)) {
      String msg = String.format("Failed to obtain lock within %s %s", timeout, timeUnit);
      throw new TimeoutException(msg);
    }
  }

  /**
   * Really just used to improve readability and so we limit when we directly access handlerLock.
   */
  private void unlockHandler() {
    handlerLock.unlock();
  }

  public URI getBaseUri() {
    return baseUri;
  }

  private void socketAcceptLoop() {

    // Socket accept loop.
    while (!Thread.interrupted()) {
      try {

        // REVIEW - Sleep to allow another thread to lock the handler (never seems to happen without this). Could allow acceptThread to be interrupted in stop without the lock.
        Thread.sleep(5);

        // Lock the handler so we don't accept a new connection while stopping.
        lockHandler();
        Socket client;

        // Ensure we have not stopped or been interrupted.
        if (acceptThread == null || Thread.interrupted()) {
          System.out.println("Looks like ServordSocketHandler has been stopped, terminate our acceptLoop.");
          return;
        }

        // We have are not stopped, so accept another connection.
        client = socket.accept();

        int val;
        StringBuilder builder = new StringBuilder();
        InputStream is = client.getInputStream();

        while ((val = is.read()) != -1) {
          builder.append((char)val);
          if ("SHUTDOWN".equals(builder.toString())) {
            System.out.println("Shutdown command received.");
            httpServer.shutdownNow();
            System.exit(0);
          }
        }

      } catch (SocketTimeoutException | TimeoutException ex) {
        // Accept timed out, which is excepted, try again.

      } catch (Throwable ex) {
        System.out.println("Unexpected exception");
        ex.printStackTrace();
        return;

      } finally {
        unlockHandler();
      }
    }
  }

  /**
   * Main method.
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      GrizzlyPushServer pushServer = new GrizzlyPushServer();
      HttpServer server = pushServer.startServer(args);

      if (server == null) {
        System.out.println("Application stopped.");

      } else {
        System.out.printf("Application started with WADL available at %sapplication.wadl%n", pushServer.getBaseUri());

        if (pushServer.openBrowser) {
          String path = String.format("%s?username=%s&password=%s", pushServer.getBaseUri(), StringUtils.encodeUrl("test@example.com"), "test");
          URI uri = URI.create(path);
          java.awt.Desktop.getDesktop().browse(uri);
        }

        Thread.currentThread().join();
      }

    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
