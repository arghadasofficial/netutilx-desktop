/*
 * Copyright (C) 2025 argha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package argha.netutilx.util;

import argha.netutilx.model.DnsRecord;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

/**
 *
 * @author argha
 */
public class DnsRecordDeserializer implements JsonDeserializer<DnsRecord> {

    @Override
    public DnsRecord deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DnsRecord record = new DnsRecord();

        // Deserialize common fields
        record.setName(jsonObject.get("name").getAsString());
        record.setTtl(jsonObject.get("ttl").getAsInt());
        record.setType(jsonObject.get("type").getAsString());

        // Intelligently deserialize the 'data' field
        JsonElement dataElement = jsonObject.get("data");
        if (dataElement.isJsonObject()) {
            // If it's an object (SOA record), parse it into the SoaData class
            DnsRecord.SoaData soaData = context.deserialize(dataElement, DnsRecord.SoaData.class);
            record.setData(soaData);
        } else {
            // Otherwise, it's a primitive (String), so just get its value
            record.setData(dataElement.getAsString());
        }

        return record;
    }
}
