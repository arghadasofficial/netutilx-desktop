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

import java.util.List;

/**
 *
 * @author argha
 */
public class DnsTypesApiResponse {
    private boolean success;
    private String message;
    private List<DnsType> data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<DnsType> getData() {
        return data;
    }
}
