package hyein.app.riditwebproject.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VariousUtils {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

}
