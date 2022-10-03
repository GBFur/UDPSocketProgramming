package no.gbf;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {

  public static void main(String[] args) {
    Thread udpThread = new Thread(() -> {
      try {
        UDPClient client = new UDPClient("129.241.152.12", 1234);
        client.message("task");

      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    udpThread.start();
  }

  InetAddress address;
  DatagramSocket socket;
  String hostname;
  int port;

  public UDPClient(String hostname, int port) throws UnknownHostException, SocketException {
    this.hostname = hostname;
    this.port = port;
    this.address = InetAddress.getByName(hostname);
    this.socket = new DatagramSocket();
  }

  public void message(String string) throws IOException {
    byte[] receivingDataBuffer = new byte[1024];
    String type = "";

    DatagramPacket sendingPacket = new DatagramPacket(string.getBytes(),
        string.getBytes().length, this.address, this.port);
    this.socket.send(sendingPacket);

    DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,
        receivingDataBuffer.length);
    this.socket.receive(receivingPacket);

    String receivedString = new String(receivingPacket.getData());
    //System.out.println(receivedString);

    if (receivedString.endsWith(".")) {
      type = "statement";
    } else {
      type = "question";
    }

    int wordCount = 0;
    String receivedStringSpecialCharactersRemoved = receivedString.replaceAll("[^A-Za-z0-9' ']", "");
    if(receivedStringSpecialCharactersRemoved.isEmpty()) {
      wordCount = 0;
    } else {
      wordCount = receivedStringSpecialCharactersRemoved.split("\\s+").length;
    }

    System.out.println(receivedString + " " + type + " " + wordCount);

    this.socket.close();
  }



}
