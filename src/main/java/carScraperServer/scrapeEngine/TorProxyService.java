package carScraperServer.scrapeEngine;

import org.silvertunnel_ng.netlib.api.NetFactory;
import org.silvertunnel_ng.netlib.tool.CustomNetlibProxy;

public enum TorProxyService {
    instance;

    private final String localProxyAddress = "127.0.0.1:1080";

    private final Object lock = new Object();
    private volatile boolean isFinished;
    private volatile boolean portOpened;

    public void establishTor() {

        try {
            if (CustomNetlibProxy.isStarted()) {
                CustomNetlibProxy.stop();
                synchronized (lock) {
                    while (!isFinished) {
                        lock.wait();
                    }
                }
            }

            //remove previous Tor net layer
            NetFactory.getInstance().clearRegisteredNetLayers();

            Thread thread = new Thread(() -> {
                isFinished = false;
                portOpened = false;
                String[] arguments = new String[]{localProxyAddress, "socks_over_tor_over_tls_over_tcpip"};
                CustomNetlibProxy.start(arguments, () -> {
                    synchronized (lock) {
                        portOpened = true;
                        lock.notifyAll();
                    }
                });
                synchronized (lock) {
                    isFinished = true;
                    lock.notifyAll();
                }
            });

            thread.start();
            synchronized (lock) {
                while (!portOpened) {
                    lock.wait();
                }
            }
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopTor() {
        if (CustomNetlibProxy.isStarted()) {
            CustomNetlibProxy.stop();
        }
    }

    public String getProxyIp() {
        return localProxyAddress.split(":")[0];
    }

    public int getProxyPort() {
        return Integer.parseInt(localProxyAddress.split(":")[1]);
    }
}
