package kr.datasolution.practice.hyein;

import kr.datasolution.practice.hyein.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
@Slf4j
public class ListUtilsTest {

    @Test
    public void integerListTest(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        log.info(list+"");

        log.info(ListUtils.subListByNumber(list, 1)+"");
        assertEquals("[[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]]", ListUtils.subListByNumber(list, 1)+"");

        log.info(ListUtils.subListByNumber(list, 5)+"");
        assertEquals("[[1, 2], [3, 4], [5, 6], [7, 8], [9, 10]]", ListUtils.subListByNumber(list, 5)+"");

        log.info(ListUtils.subListByNumber(list, 3)+"");
        assertEquals("[[1, 2, 3, 4], [5, 6, 7], [8, 9, 10]]", ListUtils.subListByNumber(list, 3)+"");


        log.info(ListUtils.subListByNumber(list, 10)+"");
        assertEquals("[[1], [2], [3], [4], [5], [6], [7], [8], [9], [10]]", ListUtils.subListByNumber(list, 10)+"");
    }

    @Test
    public void countZeroTest(){
        List<Integer> list = new ArrayList<>(Arrays.asList(6, 1232323, 6767,343432,132,7574,54536,1321,878,34,213));
        log.info(ListUtils.subListByNumber(list, 0)+"");
    }

    @Test
    public void exceedCountTest(){
        List<Integer> list = new ArrayList<>(Arrays.asList(6, 1232323, 6767,343432,132,7574,54536,1321,878,34,213));

        log.info(ListUtils.subListByNumber(list, 12)+"");
    }
    @Test
    public void stringListTest(){
        List<String> list = new ArrayList<>(Arrays.asList("6", "1232323", "6767","343432","132","7574","54536","1321","878","34","213"));

        log.info(ListUtils.subListByNumber(list, 2)+"");
        assertEquals("[[6, 1232323, 6767, 343432, 132, 7574], [54536, 1321, 878, 34, 213]]", ListUtils.subListByNumber(list, 2)+"");
    }

    @Test
    public void filePathTest() {
        File conceptDir = FileUtils.getFile("D:\\TestSpace\\BIGstation\\rule_upload\\concept");
        log.info(conceptDir+"");

        if(conceptDir.isDirectory()){
            String[] files = conceptDir.list();
            log.info(ListUtils.subListByNumber(Arrays.asList(files), 8)+"");
            assertEquals(8, ListUtils.subListByNumber(Arrays.asList(files), 8).size());
        }

    }

}
