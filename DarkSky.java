package com.example.weather;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class DarkSky {
    public static Weather getWeather(String url) throws Exception
    {
        Weather weather = new Weather();

        // get request
        HttpClient obj = new HttpClient();
        String json_str = null;

        try {
            json_str = obj.sendGet(url);
        } finally {
            obj.close();
            if(json_str == null){
                System.out.println("Could not retrieve weather");
                return weather;
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        weather = mapper.readValue(json_str, new TypeReference<Weather>(){});
        return weather;
    }
}
