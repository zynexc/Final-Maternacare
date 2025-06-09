package com.maternacare.service;

import com.maternacare.model.MaternalRecord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonToken;
import javafx.beans.property.*;
import com.google.gson.JsonSyntaxException;
import java.util.stream.Collectors;

public class MaternalRecordService {
    private static final String RECORDS_FILE = "maternal_records.json";
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
            .registerTypeAdapter(ObjectProperty.class, new ObjectPropertyAdapter())
            .registerTypeAdapter(IntegerProperty.class, new IntegerPropertyAdapter())
            .registerTypeAdapter(DoubleProperty.class, new DoublePropertyAdapter())
            .setPrettyPrinting()
            .create();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    // Custom Gson TypeAdapter for LocalDate
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            } else {
                return LocalDate.parse(in.nextString());
            }
        }
    }

    // Custom Gson TypeAdapter for StringProperty
    private static class StringPropertyAdapter extends TypeAdapter<StringProperty> {
        @Override
        public void write(JsonWriter out, StringProperty value) throws IOException {
            if (value == null || value.get() == null) {
                out.nullValue();
            } else {
                out.value(value.get());
            }
        }

        @Override
        public StringProperty read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return new SimpleStringProperty(null);
            } else {
                return new SimpleStringProperty(in.nextString());
            }
        }
    }

    // Custom Gson TypeAdapter for ObjectProperty (handles LocalDate specifically as
    // it's used in the model)
    private static class ObjectPropertyAdapter extends TypeAdapter<ObjectProperty> {
        private final LocalDateAdapter localDateAdapter = new LocalDateAdapter();

        @Override
        public void write(JsonWriter out, ObjectProperty value) throws IOException {
            if (value == null || value.get() == null) {
                out.nullValue();
            } else if (value.get() instanceof LocalDate) {
                localDateAdapter.write(out, (LocalDate) value.get());
            } else {
                // Fallback for other object types if needed, or throw an error
                throw new IOException("Unsupported object type in ObjectProperty: " + value.get().getClass());
            }
        }

        @Override
        public ObjectProperty read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return new SimpleObjectProperty<>(null);
            } else {
                // Assuming only LocalDate is stored in ObjectProperty for this model
                return new SimpleObjectProperty<>(localDateAdapter.read(in));
            }
        }
    }

    // Custom Gson TypeAdapter for IntegerProperty
    private static class IntegerPropertyAdapter extends TypeAdapter<IntegerProperty> {
        @Override
        public void write(JsonWriter out, IntegerProperty value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.get());
            }
        }

        @Override
        public IntegerProperty read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return new SimpleIntegerProperty(0);
            } else {
                return new SimpleIntegerProperty(in.nextInt());
            }
        }
    }

    // Custom Gson TypeAdapter for DoubleProperty
    private static class DoublePropertyAdapter extends TypeAdapter<DoubleProperty> {
        @Override
        public void write(JsonWriter out, DoubleProperty value) throws IOException {
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.get());
            }
        }

        @Override
        public DoubleProperty read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return new SimpleDoubleProperty(0.0);
            } else {
                return new SimpleDoubleProperty(in.nextDouble());
            }
        }
    }

    public void saveRecords(List<MaternalRecord> newRecords) {
        try (Writer writer = new FileWriter(RECORDS_FILE)) {
            // Update IDs for new records
            for (MaternalRecord record : newRecords) {
                if (record.getId() == 0) {
                    record.setId(nextId.getAndIncrement());
                }
            }
            // Convert to DTOs for serialization
            List<MaternalRecord.MaternalRecordDTO> dtos = newRecords.stream().map(MaternalRecord::toDTO)
                    .collect(Collectors.toList());
            gson.toJson(dtos, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MaternalRecord> loadRecords() {
        File file = new File(RECORDS_FILE);
        if (!file.exists() || file.length() == 0) {
            // Initialize file with empty array if it doesn't exist or is empty
            try (Writer writer = new FileWriter(RECORDS_FILE)) {
                writer.write("[]");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(RECORDS_FILE)) {
            List<MaternalRecord.MaternalRecordDTO> dtos = gson.fromJson(reader,
                    new com.google.gson.reflect.TypeToken<List<MaternalRecord.MaternalRecordDTO>>() {
                    }.getType());
            if (dtos == null) {
                // If parsing failed, initialize with empty array
                try (Writer writer = new FileWriter(RECORDS_FILE)) {
                    writer.write("[]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }
            List<MaternalRecord> records = dtos.stream().map(MaternalRecord::fromDTO).collect(Collectors.toList());
            // Update nextId based on loaded records
            int maxId = records.stream()
                    .mapToInt(MaternalRecord::getId)
                    .max()
                    .orElse(0);
            nextId.set(maxId + 1);
            return records;
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            // If there's any error reading or parsing, initialize with empty array
            try (Writer writer = new FileWriter(RECORDS_FILE)) {
                writer.write("[]");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return new ArrayList<>();
        }
    }
}