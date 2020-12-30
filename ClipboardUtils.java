package dev.hyein.seleniumsample.util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

public class ClipboardUtils {

    /**
     * 클립보드로 복사
     *
     * @param content 복사할 텍스트
     */
    public static void copy(String content){
        System.setProperty("java.awt.headless", "false");

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(content);
        clipboard.setContents(stringSelection, null);
    }

    /**
     * 클립보드 텍스트 반환
     * @return 복사한 텍스트
     */
    public static String paste() {
        System.setProperty("java.awt.headless", "false");

        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable transferable = clipboard.getContents(ClipboardUtils.class); 
            return transferable.getTransferData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
