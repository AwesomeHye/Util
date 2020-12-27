package kr.co.saramin.lab.recruitassigntaskrepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Disabled
public class TestParent {
    @MockBean
    MainApplication mainApplication;

    private long start, end;

    @BeforeEach
    public void setStartTime(){
        start = System.currentTimeMillis();
    }

    @AfterEach
    public void printElapsedTime(){
        end = System.currentTimeMillis();
        log.info("Elapsed time: {}s", (end-start)/1000.0);
    }
}
