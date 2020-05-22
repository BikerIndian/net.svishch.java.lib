package net.svishch.java.lib.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Date: 06.05.2019
 *
 * @author Vladimir Svishch (IndianBiker)  mail:5693031@gmail.com
 * @version 1.0.0
 *
 */
public class Lan {

    /**
     *
     * @return возвращает список локальных Ip адресов
     */
    public List getIp() {

        List ipAddress = new ArrayList();

        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();  // gets All networkInterfaces of your device
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface inet = (NetworkInterface) networkInterfaces.nextElement();
                Enumeration address = inet.getInetAddresses();
                while (address.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) address.nextElement();
                    if (inetAddress.isSiteLocalAddress()) {
                        ipAddress.add(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (Exception e) {

        }

        return ipAddress;

    }
}
