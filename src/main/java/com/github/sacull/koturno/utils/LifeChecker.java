package com.github.sacull.koturno.utils;

import com.github.sacull.koturno.entities.Host;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@Component
public class LifeChecker {

    public boolean isReachable(Host host) {
        for (int i = 50; i <= 150; i += 50) {
            if (this.isReachable(host, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReachableByTcp(Host host) {
        for (int i = 50; i <= 150; i += 50) {
            if (this.isReachableByTcp(host, 7, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReachableByTcp(Host host, int port) {
        for (int i = 50; i <= 150; i += 50) {
            if (this.isReachableByTcp(host, port, i)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReachableByPing(Host host) {
        for (int i = 1; i <= 3; i++) {
            if (this.isReachableByExternalPing(host)) {
                return true;
            }
        }
        return false;
    }

    private boolean isReachable(Host host, int timeout) {
        try {
            if (InetAddress.getByName(host.getAddress()).isReachable(timeout)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private boolean isReachableByTcp(Host host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(host.getAddress()), port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isReachableByExternalPing(Host host) {
        try {
            String cmd;

            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "cmd /C ping -n 1 " + host.getAddress() + " | find \"TTL\"";
            } else {
                cmd = "ping -c 1 " + host.getAddress();
            }

            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            return myProcess.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
}
