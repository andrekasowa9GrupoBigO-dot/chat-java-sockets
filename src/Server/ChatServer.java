package Server;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static final int PORT = 5000;

    private static ConcurrentHashMap<String, PrintWriter> users = new ConcurrentHashMap<>();

    public static void main(String[] args) {

        System.out.println("Servidor iniciado na porta " + PORT);

        try (ServerSocket server = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = server.accept();
                new Thread(new ClientHandler(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class gClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {

            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                username = in.readLine();

                if (username == null || username.trim().isEmpty()) {
                    return;
                }

                users.put(username, out);

                System.out.println("..." + username + " entrou");

                broadcast("ONLINE|" + username);
                sendUsers();

                String msg;

                while ((msg = in.readLine()) != null) {

                    System.out.println(":" + username + ": " + msg);

                    if (msg.startsWith("TYPING|")) {

                        broadcast(msg);

                    } else if (msg.startsWith("DM|")) {

                        handleDM(msg);

                    } else if (msg.startsWith("IMG|")) {

                        broadcast(msg);

                    } else {

                        broadcast("MSG|" + username + "|" + msg);
                    }
                }

            } catch (Exception e) {
                System.out.println("⚠️ Erro com " + username);
            } finally {
                logout();
            }
        }

        private void handleDM(String msg) {

            String[] parts = msg.split("\\|", 3);

            if (parts.length < 3) return;

            String to = parts[1];

            PrintWriter receiver = users.get(to);

            if (receiver != null) {

                receiver.println(msg); // envia para destinatário
                out.println(msg);      // envia para quem enviou

                System.out.println(": DM de " + username + " para " + to);

            } else {
                out.println("MSG|SERVER|Usuário offline");
            }
        }

        private void broadcast(String msg) {

            for (PrintWriter w : users.values()) {
                w.println(msg);
            }
        }

        private void sendUsers() {

            StringBuilder sb = new StringBuilder("USERS|");

            for (String u : users.keySet()) {
                sb.append(u).append(",");
            }

            broadcast(sb.toString());

            System.out.println("👥 Online: " + users.keySet());
        }

        private void logout() {

            if (username != null) {

                users.remove(username);

                System.out.println("." + username + " saiu");

                broadcast("OFFLINE|" + username);
                sendUsers();
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}