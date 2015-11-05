/*
 * silvertunnel.org Netlib - Java library to easily access anonymity networks
 * Copyright (c) 2009-2012 silvertunnel.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.silvertunnel_ng.netlib.layer.mock;

import org.silvertunnel_ng.netlib.api.*;
import org.silvertunnel_ng.netlib.nameservice.mock.NopNetAddressNameService;

import java.io.IOException;
import java.util.Map;

/**
 * Simple NetLayer that always fails to create a Net(Server)Socket.
 *
 * @author hapke
 */
public class NopNetLayer implements NetLayer {
    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#createNetSocket(java.util.Map, org.silvertunnel_ng.netlib.api.NetAddress, org.silvertunnel_ng.netlib.api.NetAddress)
     */
    @Override
    public synchronized NetSocket createNetSocket(
            Map<String, Object> localProperties, NetAddress localAddress,
            NetAddress remoteAddress) throws IOException {
        throw new IOException(
                "NopNetLayer.createNetSocket() always throws this IOException");
    }

    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#createNetServerSocket(java.util.Map, org.silvertunnel_ng.netlib.api.NetAddress)
     */
    @Override
    public NetServerSocket createNetServerSocket(
            Map<String, Object> properties, NetAddress localListenAddress)
            throws IOException {
        throw new IOException(
                "NopNetLayer.createNetServerSocket() always throws this IOException");
    }

    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#getStatus()
     */
    @Override
    public NetLayerStatus getStatus() {
        return NetLayerStatus.READY;
    }

    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#waitUntilReady()
     */
    @Override
    public void waitUntilReady() {
        // nothing to do
    }

    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#clear()
     */
    @Override
    public void clear() throws IOException {
        // nothing to do
    }

    /**
     * @see org.silvertunnel_ng.netlib.api.NetLayer#getNetAddressNameService
     */
    @Override
    public NetAddressNameService getNetAddressNameService() {
        return NopNetAddressNameService.getInstance();
    }

    @Override
    public void close() {
        // nothing to do
    }
}
