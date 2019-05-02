package com.datasolution.ridit.datamigration.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 자바 직렬화 유틸
 */
public class SerializeUtils {

    /**
     * 객체를 바이트 파일에 쓰기
     *
     * @String 파일경로
     * @Object 객체
     * @int 파일사이즈
     */
    public static int writeSerializedByteFile(String pathAndFileNameString, Object writeObject){
        int fileSize = 0;

        try {
            //Object를 byte로 바꾸기
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
            outputStream.writeObject(writeObject);
            byte[] writeByte = byteArrayOutputStream.toByteArray();
            //파일에 쓰기
            Files.write(Paths.get(pathAndFileNameString), writeByte);
            fileSize = writeByte.length;

            outputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileSize;
    }

    /**
     * 바이너리 파일을 읽어 자바 객체로 변환
     *
     * @String pathAndFileNameString 파일경로
     * @return Object
     */
    public static Object readDeserializedByteFile(String pathAndFileNameString){
        Object object = null;
        try {
            //바이트 읽어서 Object 객체에 저장
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(pathAndFileNameString)));
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);
            object = inputStream.readObject();

            inputStream.close();
            byteArrayInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }
}
