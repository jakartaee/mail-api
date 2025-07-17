/*
 * Copyright (c) 2025 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.mail.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FakeSMTPServer {

    private volatile boolean running = true;

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Fake SMTP server is running on port " + port);

            while (running) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // 获取客户端输入流，用于接收 SMTP 请求
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // 发送欢迎消息，模拟服务器响应
                writer.println("220 Fake SMTP Server Ready");

                // 读取客户端的输入并打印
                String line;
                while ((line = reader.readLine()) != null) {
                    // 打印收到的每一行 SMTP 报文
                    System.out.println("Received: " + line);

                    // 模拟 SMTP 交互
                    if (line.startsWith("HELO") || line.startsWith("EHLO")) {
                        writer.println("250 Hello " + clientSocket.getInetAddress().getHostName());
                    } else if (line.startsWith("MAIL FROM")) {
                        writer.println("250 OK");
                    } else if (line.startsWith("RCPT TO")) {
                        writer.println("250 OK");
                    } else if (line.equals("DATA")) {
                        writer.println("354 Start mail input; end with <CRLF>.<CRLF>");
                    } else if (line.equals(".")) {
                        writer.println("250 OK: Message received");
                    } else if (line.equals("QUIT")) {
                        writer.println("221 Bye");
                        break;
                    }

                    // 如果客户端发送了 "QUIT"，则退出当前会话
                    if (line.equals("QUIT")) {
                        break;
                    }
                }

                // 关闭当前客户端连接
                clientSocket.close();
                System.out.println("Client disconnected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        running = false;
        System.out.println("Fake SMTP server is stopping...");
    }
}