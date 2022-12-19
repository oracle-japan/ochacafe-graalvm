package com.example.fn;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fnproject.fn.api.OutputEvent.Status;
import com.fnproject.fn.testing.FnResult;
import com.fnproject.fn.testing.FnTestingRule;

public class HelloFunctionTest {

    private static JsonNode config;

    static{
        try {
            final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            final JsonNode funcYaml = mapper.readValue(new File("func.yaml"), JsonNode.class);
            config = funcYaml.get("config");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Rule
    public final FnTestingRule testing = FnTestingRule.createDefault();

    @Test
    public void test(){

        String[] configs = {
            "OCI_REGION", "JDBC_DRIVER", "JDBC_URL", "JDBC_USER", "JDBC_PASSWORD",
            "JDBC_PASSWORD_SECRET_ID", "JDBC_PASSWORD_SECRET","JDBC_PASSWORD_SECRET","ADB_ID","ADB_WALLET_DIR"
        };

        Arrays.stream(configs).forEach(c -> {
            JsonNode val = config.get(c);
            if(Objects.nonNull(val)) 
                testing.setConfig(c, val.asText());
        });

        testing.givenEvent().withBody("1").enqueue();
        testing.givenEvent().withBody("81").enqueue();
        testing.givenEvent().withBody("999").enqueue();

        testing.thenRun(HelloFunction.class, "handleRequest");

        List<FnResult> results = testing.getResults();
        assertEquals(results.size(), 3);
        FnResult theResult = results.get(0);
        assertEquals("USA", theResult.getBodyAsString());
        theResult = results.get(1);
        assertEquals("Japan", theResult.getBodyAsString());
        theResult = results.get(2);
        assertEquals(Status.FunctionError, theResult.getStatus());
    }


}