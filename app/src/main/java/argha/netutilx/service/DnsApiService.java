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
package argha.netutilx.service;

import argha.netutilx.model.DnsInfoApiResponse;
import argha.netutilx.model.DnsRecord;
import argha.netutilx.model.DnsServer;
import argha.netutilx.model.DnsServerApiResponse;
import argha.netutilx.model.DnsType;
import argha.netutilx.model.DnsTypesApiResponse;
import argha.netutilx.util.DnsRecordDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author argha
 */
public class DnsApiService {

    private static final String API_BASE_URL = "https://netutilx.grow10x.business/";
    private final HttpClient httpClient;
    private final Gson gson;

    public DnsApiService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new GsonBuilder().registerTypeAdapter(DnsRecord.class, new DnsRecordDeserializer()).create();
    }

    public List<DnsServer> getDnsServers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_BASE_URL + "get_dns_servers.php")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        DnsServerApiResponse apiResponse = gson.fromJson(response.body(), DnsServerApiResponse.class);
        return (apiResponse != null && apiResponse.isSuccess()) ? apiResponse.getData() : Collections.emptyList();
    }

    public List<DnsType> getDnsTypes() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_BASE_URL + "get_dns_types.php")).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        DnsTypesApiResponse apiResponse = gson.fromJson(response.body(), DnsTypesApiResponse.class);
        return (apiResponse != null && apiResponse.isSuccess()) ? apiResponse.getData() : Collections.emptyList();
    }
    
     public List<DnsRecord> getDnsInfo(String query, String serverId, String typeId) throws IOException, InterruptedException {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format("%sget_dns_info.php?query=%s&serverId=%s&typeId=%s",
                API_BASE_URL, encodedQuery, serverId, typeId);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        DnsInfoApiResponse apiResponse = gson.fromJson(response.body(), DnsInfoApiResponse.class);
        
        return (apiResponse != null && apiResponse.isSuccess()) ? apiResponse.getData() : Collections.emptyList();
    }
}
