package com.example.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class SocketUtil {
    //打印输出流
    public static void write2Stream(String data, PrintWriter printWriter) {
        if (data == null) {
            return;
        }
        if (printWriter != null) {
            printWriter.println(data);
        }
    }
   //关闭输出流
    public static void closePrintWriter(PrintWriter pw) {
        if (pw != null) {
            pw.close();
        }
    }
    //关闭输入流
    public static void closeBufferedReader(BufferedReader br) {
        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //等待线程
    public static void toWait(Object o, long millis) {
        synchronized (o) {
            try {
                o.wait(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

