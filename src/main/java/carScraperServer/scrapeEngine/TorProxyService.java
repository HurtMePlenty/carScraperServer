package carScraperServer.scrapeEngine;

import org.silvertunnel_ng.netlib.tool.NetlibProxy;

public enum TorProxyService {
    instance;

    private final String localProxyAddress = "127.0.0.1:1080";

    private final Object lock = new Object();
    private volatile boolean isFinished;

    public void establishTor() {

        try {
            if (NetlibProxy.isStarted()) {
                NetlibProxy.stop();
                synchronized (lock) {
                    while (!isFinished) {
                        lock.wait();
                    }
                }
            }


            Thread thread = new Thread(() -> {
                isFinished = false;
                String[] arguments = new String[]{localProxyAddress, "socks_over_tor_over_tls_over_tcpip"};
                NetlibProxy.start(arguments);
                synchronized (lock) {
                    isFinished = true;
                    lock.notifyAll();
                }
            });

            thread.start();
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopTor() {
        if (NetlibProxy.isStarted()) {
            NetlibProxy.stop();
        }
    }

    /*private void establishTorConnection(String ip, String id) {
        try {
            final String listenAddressPortArg = ip;
            final TcpipNetAddress localListenAddress = new TcpipNetAddress(
                    listenAddressPortArg);

            // open server port
            netServerSocket = NetFactory.getInstance()
                    .getNetLayerById(NetLayerIDs.TCPIP)
                    .createNetServerSocket(null, localListenAddress);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }*/

    public String getProxyIp() {
        return localProxyAddress.split(":")[0];
    }

    public int getProxyPort() {
        return Integer.parseInt(localProxyAddress.split(":")[1]);
    }
}
