/*
 * Copyright (c) 2019 Alexey Zinchenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.github.prominence.randomkitties.bot.util;

import com.github.prominence.randomkitties.bot.model.Kitty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CatUtils {

    public static final Logger log = LogManager.getLogger(CatUtils.class);

    private static final String API_URL = "https://api.thecatapi.com/v1/images/search?api_key=<YOUR API KEY FROM THECATAPI.COM>";

    public static List<Kitty> getRandomCatsList() {
        return getRandomCatsList(1);
    }

    public static List<Kitty> getRandomCatsList(int amount) {
        try {
            final String spec = API_URL + "&limit=" + amount;

            log.debug("Making request: " + spec);

            HttpURLConnection connection = (HttpURLConnection) new URL(spec).openConnection();
            connection.setRequestMethod("GET");
            final int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Bad response.");
            }

            final InputStream inputStream = connection.getInputStream();
            return JSONUtils.parseJSON(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
