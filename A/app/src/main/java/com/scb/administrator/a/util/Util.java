package com.scb.administrator.a.util;

/**
 * Created by Administrator on 2015/6/23 0023.
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Util {

    public List<Map<String, Object>> getInformation(String jonString)
            throws Exception {

        if(jonString==null) return null;
        else {
            JSONObject jsonObject = new JSONObject(jonString);
            JSONObject retData = jsonObject.getJSONObject("retData");
            List<Map<String, Object>> all = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            map.put("l_temp", retData.optString("l_tmp"));
            map.put("h_temp", retData.optString("h_tmp"));
            map.put("weather", retData.optString("weather"));
            all.add(map);

            return all;
        }
    }

    public static byte[] read(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1)
            baos.write(buffer, 0, len);
        baos.close();
        byte[] data = baos.toByteArray();
        return data;
    }

}
