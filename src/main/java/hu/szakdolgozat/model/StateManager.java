package hu.szakdolgozat.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.szakdolgozat.model.AppState;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class StateManager {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static void saveState(AppState state, String filePath) throws IOException {
        objectMapper.writeValue(new File(filePath), state);
    }
    public static AppState loadState(String filePath) throws IOException {
        return objectMapper.readValue(new File(filePath),AppState.class);
    }
}
