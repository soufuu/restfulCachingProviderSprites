package com.enterpriseandroid.restfulsprites.svc;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.enterpriseandroid.restfulsprites.data.SpritesContract;

/**
 *
 */
public class MessageHandler {
    private static final String TAG = "JSON";
    // sprite columns
    public static final String TAG_ID = "id";
    public static final String TAG_COLOR = "color";
    public static final String TAG_DX = "dx";
    public static final String TAG_DY = "dy";
    public static final String TAG_PANEL_HEIGHT = "panelheight";
    public static final String TAG_PANEL_WIDTH = "panelwidth";
    public static final String TAG_X = "x";
    public static final String TAG_Y = "y";
    // end sprite columns
    public static final String TAG_LOCATION = "location";
    private static final Map<String, String> UNMARSHAL_TAB;
    private static final Map<String, String> MARSHAL_TAB;


    static {
        Map<String, String> m = new HashMap<String, String>();
        // sprite columns
        m.put(RESTService.ID, TAG_ID);
        m.put(RESTService.COLOR, TAG_COLOR);
        m.put(RESTService.DX, TAG_DX);
        m.put(RESTService.DY, TAG_DY);
        m.put(RESTService.PANEL_HEIGHT, TAG_PANEL_HEIGHT);
        m.put(RESTService.PANEL_WIDTH, TAG_PANEL_WIDTH);
        m.put(RESTService.X, TAG_X);
        m.put(RESTService.Y, TAG_Y);
        // end sprite columns
        MARSHAL_TAB = m;
    }

    static {
        Map<String, String> m = new HashMap<String, String>();
        // sprite columns
        m.put(TAG_ID, SpritesContract.Columns.ID);
        m.put(TAG_COLOR, SpritesContract.Columns.COLOR);
        m.put(TAG_DX, SpritesContract.Columns.DX);
        m.put(TAG_DY, SpritesContract.Columns.DY);
        m.put(TAG_PANEL_HEIGHT, SpritesContract.Columns.PANEL_HEIGHT);
        m.put(TAG_PANEL_WIDTH, SpritesContract.Columns.PANEL_WIDTH);
        m.put(TAG_X, SpritesContract.Columns.X);
        m.put(TAG_Y, SpritesContract.Columns.Y);
        m.put(TAG_LOCATION, SpritesContract.Columns.REMOTE_ID);
        // end sprite columns
        UNMARSHAL_TAB = m;
    }

    public String marshal(Bundle args) throws JSONException {
        JSONObject payload = new JSONObject();
        // sprite columns
        if (args.containsKey(RESTService.ID)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.ID),
                    args.getString(RESTService.ID));
        }
        if (args.containsKey(RESTService.COLOR)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.COLOR),
                    args.getString(RESTService.COLOR));
        }
        if (args.containsKey(RESTService.DX)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.DX),
                    args.getString(RESTService.DX));
        }
        if (args.containsKey(RESTService.DY)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.DY),
                    args.getString(RESTService.DY));
        }
        if (args.containsKey(RESTService.PANEL_HEIGHT)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.PANEL_HEIGHT),
                    args.getString(RESTService.PANEL_HEIGHT));
        }
        if (args.containsKey(RESTService.PANEL_WIDTH)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.PANEL_WIDTH),
                    args.getString(RESTService.PANEL_WIDTH));
        }
        if (args.containsKey(RESTService.X)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.X),
                    args.getString(RESTService.X));
        }
        if (args.containsKey(RESTService.Y)) {
            payload.put(
                    MARSHAL_TAB.get(RESTService.Y),
                    args.getString(RESTService.Y));
        }
        // end sprite columns
        return payload.toString();
    }

    public ContentValues unmarshal(Reader in, ContentValues vals)
            throws IOException
    {
        JsonReader reader = null;
        try {
            reader = new JsonReader(in);
            unmarshalSprite(reader, vals);
        }
        finally {
            if (null != reader) {
                try { reader.close(); } catch (Exception e) {}
            }
        }
        return vals;
    }

    public void unmarshalSprite(JsonReader in, ContentValues vals)
            throws IOException
    {
        in.beginObject();
        while (in.hasNext()) {
            String key = in.nextName();
            String val = in.nextString();
            String col = UNMARSHAL_TAB.get(key);
            if (null == col) {
                Log.w(TAG, "Ignoring unexpected JSON tag: " + key + "=" + val);
                continue;
            }

            if (TAG_LOCATION.equals(key)) { val = parseLocation(val); }

            vals.put(col, val);
        }
        in.endObject();
    }

    private String parseLocation(String val) {
        return Uri.parse(val).getLastPathSegment();
    }
}