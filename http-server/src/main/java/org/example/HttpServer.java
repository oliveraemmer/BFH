package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HttpServer {

	private static final int SERVER_PORT = 8080;
	private static final String DOCUMENT_ROOT = "docroot";
	private static final String INDEX_FILE = "index.html";

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
		System.out.println("Server ready on port " + SERVER_PORT);
		while (true) {
			Socket socket = serverSocket.accept();
			try (InputStream inputStream = socket.getInputStream();
				 OutputStream outputStream = socket.getOutputStream()) {
				Scanner scanner = new Scanner(inputStream);
				PrintWriter writer = new PrintWriter(outputStream, true);

				String requestLine = scanner.nextLine();
				System.out.println("Request: " + requestLine + "\n");
				String[] parts = requestLine.split(" ");
				String method = parts[0];
				String path = parts[1];

				if (!method.equals("GET")) {
					writer.println("HTTP/1.0 405 Method Not Allowed");
				} else {
					if (path.endsWith("/")) {
						path += INDEX_FILE;
					}
					Path filePath = Paths.get(DOCUMENT_ROOT, path);
					if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
						writer.println("HTTP/1.0 404 Not Found");
					} else {
						System.out.println("Sending\n");
						System.out.println("HTTP/1.0 200 OK");
						writer.println("HTTP/1.0 200 OK");
						System.out.println("Content-Type: " + Files.probeContentType(filePath));
						writer.println("Content-Type: " + Files.probeContentType(filePath));
						System.out.println("Content-Length: " + Files.size(filePath) + "\n\n");
						writer.println("Content-Length: " + Files.size(filePath));
						writer.println();
						outputStream.write(Files.readAllBytes(filePath));
					}
				}
			} finally {
				socket.close();
			}
		}
	}
}
