package resources;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.InputStream;

public class Config {
    public static TestConfig getConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = new FileInputStream("/Users/mattarcher/IdeaProjects/iChiveTests/src/test/java/resources/test-config.json");
        return mapper.readValue(inputStream, TestConfig.class);
    }

}
