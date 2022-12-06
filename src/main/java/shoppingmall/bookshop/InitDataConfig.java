package shoppingmall.bookshop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import shoppingmall.bookshop.entity.User;
import shoppingmall.bookshop.repository.UserRepository;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitDataConfig implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        List<User> sampleUsers = getInitSamplesFromFile();
        if (sampleUsers == null || sampleUsers.size() == 0) {
            throw new IllegalArgumentException("no data in init data file.");
        }

        userRepository.saveAll(sampleUsers);
    }

    private List<User> getInitSamplesFromFile() throws IOException {
        List<User> usersList;
        String FILE_INIT_SAMPLE = "SampleData.json";
        try (InputStream inputStream = getStreamFromResource(FILE_INIT_SAMPLE)) {
            JsonNode sampleNode = getSampleNode(inputStream);
            usersList = getSampleListFromNode(sampleNode);
        }
        return usersList;
    }

    private InputStream getStreamFromResource(String fileLocation) {
        ClassLoader classLoader = InitDataConfig.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileLocation);

        if (inputStream == null) {
            throw new IllegalArgumentException("init data file \"" + fileLocation + "\" not found.");
        } else {
            return inputStream;
        }
    }

    private JsonNode getSampleNode(InputStream inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode sampleNode = null;
        try (DataInputStream dis = new DataInputStream(inputStream)) {
            sampleNode = objectMapper.readTree(dis).path("sampleUser");
        } catch (IOException io) {
            io.printStackTrace();
        }
        return sampleNode;
    }

    private List<User> getSampleListFromNode(JsonNode sampleNode) {
        ObjectMapper objectMapper = new ObjectMapper();
        return new ArrayList<>(objectMapper.convertValue(sampleNode, new TypeReference<List<User>>() {
        }));
    }

}

