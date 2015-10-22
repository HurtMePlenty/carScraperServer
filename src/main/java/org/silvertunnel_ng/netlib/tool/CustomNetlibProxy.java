package org.silvertunnel_ng.netlib.tool;

import org.silvertunnel_ng.netlib.api.NetFactory;
import org.silvertunnel_ng.netlib.api.NetLayerIDs;
import org.silvertunnel_ng.netlib.api.NetServerSocket;
import org.silvertunnel_ng.netlib.api.NetSocket;
import org.silvertunnel_ng.netlib.api.impl.SocketTimeoutInputStream;
import org.silvertunnel_ng.netlib.api.util.TcpipNetAddress;
import org.silvertunnel_ng.netlib.layer.socks.SocksServerNetLayer;
import org.silvertunnel_ng.netlib.layer.tor.TorNetLayer;
import org.silvertunnel_ng.netlib.layer.tor.clientimpl.Tor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.DatagramSocket;
import java.net.ServerSocket;


public class CustomNetlibProxy {


    /** */
    private static final Logger LOG = LoggerFactory.getLogger(CustomNetlibProxy.class);

    private static boolean startedFromCommandLine = true;
    private static volatile boolean started = false;
    private static volatile boolean stopped = false;
    private static NetServerSocket netServerSocket;
    private static NetSocket upperLayerNetSocket;
    private static NetLayerIDs lowerLayerNetLayerId;
    private static TorNetLayer torNetLayer;


    /**
     * Start the program from command line.
     *
     * @param argv
     */
    public static void start(final String[] argv, Runnable portOpenedCallback) {
        startedFromCommandLine = false;
        stopped = false;
        started = false;
        if (argv.length < 1) {
            LOG.error("NetProxy: insufficient number of command line arguments: you must specify [listen_port] and [net_layer_id] at least");
            System.exit(1);
            return;
        }

        int port = Integer.parseInt(argv[0].split(":")[1]);
        while (!isPortAvailable(port)) {
            try {
                Thread.sleep(2000);
                stop();
            } catch (Exception e) {
                LOG.warn(String.format("Error when was waiting for %d port", port));
            }
        }

        // open server port
        try {
            // parse listen address and port
            final String listenAddressPortArg = argv[0];
            final TcpipNetAddress localListenAddress = new TcpipNetAddress(
                    listenAddressPortArg);

            // open server port
            netServerSocket = NetFactory.getInstance()
                    .getNetLayerById(NetLayerIDs.TCPIP)
                    .createNetServerSocket(null, localListenAddress);

        } catch (final Exception e) {
            LOG.error("NetlibProxy: could not open server port", e);
            if (startedFromCommandLine) {
                LOG.error("System.exit(2)");
                System.exit(2);
            }
            return;
        }
        started = true;

        portOpenedCallback.run();

        // parse the netLayerId
        lowerLayerNetLayerId = NetLayerIDs.getByValue(argv[1]);

        // handle incoming connections (listen endless)
        try {
            while (!stopped) {
                upperLayerNetSocket = netServerSocket.accept();
                new NetProxySingleConnectionThread(upperLayerNetSocket,
                        lowerLayerNetLayerId).start();
            }
        } catch (final Exception e) {
            started = false;
            final String msg = "NetlibProxy: server-wide problem while running";
            if (stopped) {
                LOG.info(msg);
            } else {
                LOG.error(msg, e);
            }
            if (startedFromCommandLine) {
                LOG.error("System.exit(3)");
                System.exit(3);
            }
            return;
        }
    }

    /**
     * (Try to) close/exit the program.
     */
    public static void stop() {
        LOG.info("NetlibProxy: will be stopped now");
        stopped = true;
        started = false;

        // close server socket
        try {
            netServerSocket.close();
            //close NetSocket as well
            upperLayerNetSocket.close();
            //stop TOR
            SocksServerNetLayer socksServerNetLayer = (SocksServerNetLayer) NetFactory.getInstance().getNetLayerById(lowerLayerNetLayerId);
            Field lowerNetLayerField = SocksServerNetLayer.class.getDeclaredField("lowerNetLayer");
            lowerNetLayerField.setAccessible(true);
            torNetLayer = (TorNetLayer) lowerNetLayerField.get(socksServerNetLayer);
            Field torField = TorNetLayer.class.getDeclaredField("tor");
            torField.setAccessible(true);

            Tor tor = (Tor) torField.get(torNetLayer);
            /*for (Circuit circuit : tor.getCurrentCircuits()) {
                circuit.close(true);
            }*/

            tor.close(true);
            tor.clear();

            Thread.sleep(5000);

            stopLeaking();


        } catch (final IOException e) {
            LOG.warn("Exception while closing the server socket",
                    e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopLeaking() {
        try {
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread.getName().contains("Circuit") || thread.getName().contains("TLSDispatcher")) {
                    Class clazz = thread.getClass();
                    if (clazz.getSimpleName().equals("SocketTimeoutInputStreamThread")) {
                        for (Field field : clazz.getDeclaredFields()) {
                            if (field.getName().equals("stis")) {
                                field.setAccessible(true);
                                SocketTimeoutInputStream inputStream = (SocketTimeoutInputStream) field.get(thread);
                                try {
                                    inputStream.close();
                                } catch (Exception e) {
                                    //suppress
                                }
                                break;
                            }
                        }
                    } else if (clazz.getSimpleName().equals("TLSDispatcherThread")) {
                        for (Field field : clazz.getDeclaredFields()) {
                            if (field.getName().equals("sin")) {
                                field.setAccessible(true);
                                DataInputStream dataInputStream = (DataInputStream) field.get(thread);
                                try {
                                    dataInputStream.close();
                                } catch (Exception e) {
                                    //suppress
                                }
                                break;
                            }
                        }
                    }
                    thread.interrupt();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public static void changeIdentity() {
        try {
            SocksServerNetLayer socksServerNetLayer = (SocksServerNetLayer) NetFactory.getInstance().getNetLayerById(lowerLayerNetLayerId);
            Field field = SocksServerNetLayer.class.getDeclaredField("lowerNetLayer");
            field.setAccessible(true);
            torNetLayer = (TorNetLayer) field.get(socksServerNetLayer);

            torNetLayer.changeIdentity();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * Retrieve the current state.
     *
     * @return true=proxy server port is open
     */
    public static boolean isStarted() {
        return started;
    }

    // /////////////////////////////////////////////////////
    // test code
    // /////////////////////////////////////////////////////

    public static void testConnection() throws Exception {
        LOG.info("(client) connect client to server");
        final TcpipNetAddress remoteAddress = new TcpipNetAddress(
                "www.google.de", 80);
        final NetSocket client = NetFactory.getInstance()
                .getNetLayerById(NetLayerIDs.TCPIP)
                .createNetSocket(null, null, remoteAddress);

        LOG.info("(client) send data client->server");
        client.getOutputStream().write("GET /\n\n".getBytes());
        client.getOutputStream().flush();

        LOG.info("(client) read data from server");
        final byte[] dataReceivedByClient = readDataFromInputStream(100,
                client.getInputStream());

        LOG.info("(client) finish connection");
        client.close();
    }

    public static byte[] readDataFromInputStream(int maxResultSize,
                                                 InputStream is) throws IOException {
        final byte[] tempResultBuffer = new byte[maxResultSize];

        int len = 0;
        do {
            if (len >= tempResultBuffer.length) {
                // LOG.info("result buffer is full");
                break;
            }
            final int lastLen = is.read(tempResultBuffer, len,
                    tempResultBuffer.length - len);
            if (lastLen < 0) {
                // LOG.info("end of result stream");
                break;
            }
            len += lastLen;
        }
        while (true);

        // copy to result buffer
        final byte[] result = new byte[len];
        System.arraycopy(tempResultBuffer, 0, result, 0, len);

        return result;
    }

    public static boolean isPortAvailable(int port) {

        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                /* should not be thrown */
                }
            }
        }

        return false;
    }
}

