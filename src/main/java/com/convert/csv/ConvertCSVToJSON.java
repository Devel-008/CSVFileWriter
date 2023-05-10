package com.convert.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.IOException;

public class ConvertCSVToJSON {
    public static void main(String[] args) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(new File("src/main/resources/dataJson.json"));
        System.out.println(jsonNode.toPrettyString());
        CsvSchema.Builder builder = CsvSchema.builder()
                .addColumn("name")
                .addColumn("code")
                .addColumn("date")
                .addColumn("gotMarried");

        CsvSchema csvSchema = builder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File("src/main/resources/dataCSV.csv"), jsonNode);
    }
}
