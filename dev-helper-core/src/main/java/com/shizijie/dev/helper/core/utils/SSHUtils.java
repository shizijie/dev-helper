package com.shizijie.dev.helper.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author shizijie
 * @version 2020-08-01 下午3:45
 */
public class SSHUtils {
    private static String DEFAULT_CHAR_SET = "UTF-8";

    /**
     * 登录主机
     * @return
     *      登录成功返回true，否则返回false
     */
    public static Connection login(String ip, String userName, String password) throws IOException {
        boolean isAuthenticated = false;
        Connection conn;
        if(ip.indexOf(":")!=-1){
            conn = new Connection(ip.substring(0,ip.indexOf(":")),Integer.parseInt(ip.substring(ip.indexOf(":")+1)));
        }else{
            conn = new Connection(ip);
        }
        conn.connect(); // 连接主机
        isAuthenticated = conn.authenticateWithPassword(userName, password); // 认证
        if(!isAuthenticated){
            return null;
        }
        return conn;
    }

    /**
     * 远程执行shell脚本或者命令
     * @param cmd
     *      即将执行的命令
     * @return
     *      命令执行完后返回的结果值
     */
    public static String execute(Connection conn, String cmd) throws IOException {
        String result = "";
        Session session = null;
        try {
            if(conn != null){
                session = conn.openSession();  // 打开一个会话
                session.execCommand(cmd);      // 执行命令
                result = processStdout(session.getStdout(), DEFAULT_CHAR_SET);
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    /**
     * 解析脚本执行返回的结果集
     * @param in 输入流对象
     * @param charset 编码
     * @return
     *       以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset) throws IOException {
        InputStream stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
        String line = null;
        while((line = br.readLine()) != null){
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }
}
