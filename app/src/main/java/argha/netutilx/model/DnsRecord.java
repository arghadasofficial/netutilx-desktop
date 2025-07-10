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
package argha.netutilx.model;

/**
 *
 * @author argha
 */
public class DnsRecord {

    private String name;
    private int ttl;
    private String type;
    private Object data;

    // Use a nested static class for the complex SOA data
    public static class SoaData {

        public String mname;
        public String rname;
        public long serial;
        public int refresh;
        public int retry;
        public int expire;
        public int minimum;

        @Override
        public String toString() {
            return String.format(
                    "MNAME: %s\n"
                    + "RNAME: %s\n"
                    + "Serial: %d\n"
                    + "Refresh: %d, Retry: %d, Expire: %d, Min TTL: %d",
                    mname, rname, serial, refresh, retry, expire, minimum);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getTtl() {
        return ttl;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    // Setters - needed by the deserializer
    public void setName(String name) {
        this.name = name;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("DnsRecord[name=%s, type=%s, data=%s]", name, type, data.toString());
    }
}
