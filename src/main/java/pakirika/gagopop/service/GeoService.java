package pakirika.gagopop.service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;

@Service
public class GeoService {

    @Value( "${kakao.api.key}" )
    String apiKey;
    @Value( "${kakao.api.url}" )
    String apiUrl;

    //주소 -> 위도 경도
    public String getKakaoApiFromAddress(String address){


        String jsonString = null;

        try{
            address =URLEncoder.encode( address, "UTF-8" );
            String addr = apiUrl + "?query="+ address;
            URL url = new URL(addr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);
            BufferedReader json = null;
            json = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();
            String line;
            while ((line = json.readLine()) != null) {
                docJson.append(line);
            }
            jsonString = docJson.toString();
            json.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
    public ArrayList changeToJSON(String jsonString) throws ParseException, net.minidev.json.parser.ParseException {
        ArrayList<Float> array = new ArrayList<Float>();
        JSONParser parser = new JSONParser();
        JSONObject document = (JSONObject)parser.parse(jsonString);
        JSONArray jsonArray = (JSONArray) document.get("documents");

        if (jsonArray.isEmpty()) {
            throw new IllegalArgumentException("주소를 찾을 수 없습니다. 다시 확인해주세요.");
        }

        JSONObject position = (JSONObject)jsonArray.get(0);
        float lon = Float.parseFloat((String) position.get("x"));
        float lat = Float.parseFloat((String) position.get("y"));
        array.add(lon);
        array.add(lat);
        return array;
    }
}
