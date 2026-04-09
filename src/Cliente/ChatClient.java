package Cliente;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private BufferedReader in;
    private PrintWriter out;

    public ChatClient(String host, int port, String username) throws IOException {

        Socket socket = new Socket(host, port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println(username);
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public BufferedReader getReader() {
        return in;
    }
}