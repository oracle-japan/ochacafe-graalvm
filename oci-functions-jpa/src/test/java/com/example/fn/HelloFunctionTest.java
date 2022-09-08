package com.example.fn;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    public final FnTestingRule testing = FnTestingRule.createDefault()
    .setConfig("JDBC_DRIVER", config.get("JDBC_DRIVER").asText())
    .setConfig("JDBC_URL", config.get("JDBC_URL").asText())
    .setConfig("JDBC_USER", config.get("JDBC_USER").asText())
    .setConfig("JDBC_PASSWORD_SECRET_ID", config.get("JDBC_PASSWORD_SECRET_ID").asText())
    .setConfig("OCI_REGION", config.get("OCI_REGION").asText());

    @Test
    public void test(){

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