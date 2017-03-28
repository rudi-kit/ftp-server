package rk.core;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Connection {
    final static Logger log = Logger.getLogger(Connection.class);
    public static int DEFAULT_BUFFER_SIZE = 8192;
    private final BufferedInputStream bis;
    private final BufferedOutputStream bos;
    private Socket socket;
    private String charset = StandardCharsets.UTF_8.toString();

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        bis = new BufferedInputStream(socket.getInputStream());
        bos = new BufferedOutputStream(socket.getOutputStream());
    }

    public void write(String message) throws IOException {
        bos.write(message.getBytes(StandardCharsets.UTF_8));
        log.trace(message);
        bos.flush();
    }

    public void writeSequence(String message) throws IOException {
        log.trace(message);
        bos.write(message.getBytes(StandardCharsets.UTF_8));
    }

    public void flush() throws IOException {
        bos.flush();
    }

    public String readLine() throws IOException {
        Scanner scanner = new Scanner(bis, charset);
        scanner.useDelimiter("\n");
        String message = scanner.nextLine();

        log.trace(String.format("READ LINE [%s]\n", message));
        return message;
    }

    public void close() throws IOException {
        log.trace("CLOSE CONNECTION");
        socket.close();
    }

    public void writeFrom(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
        for (int readen; (readen = inputStream.read(bytes)) != -1; ) {
            bos.write(bytes, 0, readen);
        }
        bos.flush();
    }

    public void readTo(OutputStream os) throws IOException {
        byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
        for (int readen; (readen = bis.read(bytes)) != -1; ) {
            os.write(bytes, 0, readen);
        }
        os.flush();
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public boolean isClosed() {
        return socket.isClosed();
    }
}
