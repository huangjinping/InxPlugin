package com.inxpl.utils.xml;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.hash.LinkedHashMap;
import org.apache.http.util.TextUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Set;

/**
 * Created by wangyan-pd on 2016/8/22.
 */
public class HardDimensReplaceFromatter {
    private static HardDimensReplaceFromatter instance;
    private File xmlFILE;

    public static HardDimensReplaceFromatter getInstance() {
        if(instance == null)
            instance = new HardDimensReplaceFromatter();
        return instance;
    }
    private static LinkedHashMap<String,String> dimensKeyValuesMap = new LinkedHashMap<String,String>();
//    public HashMap<String,String>  getStringKeyValuesMap(){
//        return dimensKeyValuesMap;
//    }
    public void initKeyValuesMap(VirtualFile file) {
        dimensKeyValuesMap.clear();
        index = 0;
        String xmlFile = "";
        if(file.getPath().endsWith("/layout")){
            xmlFile = file.getPath().replace("/layout","");
        }else if(file.getPath().endsWith(".xml") && file.getPath().contains("/layout")){
            int index = file.getPath().lastIndexOf("/layout");
            xmlFile =  file.getPath().substring(0,index);
        }
        String Values = xmlFile +File.separator +"values";
        String Strings = "dimens.xml";
        xmlFILE = new File(Values,Strings);
        if(!new File(Values).exists())
            new File(Values).mkdirs();
        if(!xmlFILE.exists()){
            try {
                xmlFILE.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return;
        }
        try {
            initMapUseSax(xmlFILE);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMapUseSax(File xmlFILE) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 2.???????????????
        SAXParser parser = factory.newSAXParser();
        // 3.?????????????????????????????????????????????,??????????????????
        SaxHandler dh = new SaxHandler();
        parser.parse(xmlFILE, dh);
    }

    private  static int index = 0;

    public String obtainStringKeyByValue(String value) {
        if(dimensKeyValuesMap.values().contains(value))
            return getKeyByValue(value);
        //??????????????????????????????key?????????map??????
        try {
            if(Integer.parseInt(getValueExceptDps(value)) < 0)
                value = "_"+Math.abs(Integer.parseInt(getValueExceptDps(value)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String currentIndex = "dimen_"+value;
        while (dimensKeyValuesMap.containsKey(currentIndex)){
            index++;
            currentIndex = "dimen_"+value+"_"+index;
        }
        dimensKeyValuesMap.put(currentIndex,value);
        return currentIndex;

    }

    private String getValueExceptDps(String value) {
        //value.endsWith("dp") || value.endsWith("in")|| value.endsWith("mm")|| value.endsWith("pt")|| value.endsWith("px")|| value.endsWith("sp")
        if(TextUtils.isEmpty(value))
            return "";
        return value.replace("dp","").replace("in","").replace("mm","").replace("pt","").replace("px","").replace("sp","");
    }

    private String getKeyByValue(String value) {
        Set<String> keySet = dimensKeyValuesMap.keySet();
        for(String key :keySet){
            if(dimensKeyValuesMap.get(key).equals(value))
                return key;
        }
        return "";
    }

    public void saveMap2ValuesStrings() {
        if(dimensKeyValuesMap.size() == 0)
            return;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(xmlFILE,false);//????????????
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8");
            String strings = covertStringKeyValuesMap2Xml(dimensKeyValuesMap);
            outputStreamWriter.write(strings);
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String covertStringKeyValuesMap2Xml(LinkedHashMap<String, String> stringKeyValuesMap) {
        StringBuffer xml =new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n");
        for(String key :stringKeyValuesMap.keySet()){
            xml.append("\t<dimen name=\"").append(key).append("\">").append(stringKeyValuesMap.get(key)).append("</dimen>\n");
        }
        xml.append("</resources>");
        return xml.toString();
    }

    public boolean isDimenValue(String value) {
        if(TextUtils.isEmpty(value))
            return false;
        return value.endsWith("dp") || value.endsWith("in")|| value.endsWith("mm")|| value.endsWith("pt")|| value.endsWith("px")|| value.endsWith("sp");
    }

    static class SaxHandler extends DefaultHandler {

        private String currentKey;

        /* ????????????????????????
                   arg0???????????????????????????????????????????????????
                   arg1???arg2????????????????????????????????????????????? */
        @Override
        public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
            String content = new String(arg0, arg1, arg2);
            System.out.println(content);
            super.characters(arg0, arg1, arg2);
            if(!TextUtils.isEmpty(currentKey))
                dimensKeyValuesMap.put(currentKey,content);
            currentKey = "";
        }

        @Override
        public void endDocument() throws SAXException {
            System.out.println("\n??????????????????????????????????????????");
            super.endDocument();
        }

        /* arg0???????????????
           arg1?????????????????????????????????????????????????????????????????????
           arg2????????????????????????????????? */
        @Override
        public void endElement(String arg0, String arg1, String arg2)
                throws SAXException {
            System.out.println("??????????????????  " + arg2);
            super.endElement(arg0, arg1, arg2);
        }

        @Override
        public void startDocument() throws SAXException {
            System.out.println("??????????????????????????????????????????\n");
            super.startDocument();
        }

        /*arg0???????????????
          arg1?????????????????????????????????????????????????????????????????????
          arg2?????????????????????????????????
          arg3??????????????????????????? */
        @Override
        public void startElement(String arg0, String arg1, String arg2,
                                 Attributes arg3) throws SAXException {
            System.out.println("?????????????????? " + arg2);
            if (arg3 != null) {
                for (int i = 0; i < arg3.getLength(); i++) {
                    // getQName()????????????????????????
                    System.out.print(arg3.getQName(i) + "=\"" + arg3.getValue(i) + "\"");
                    if(arg3.getQName(i).equals("name"))
                        currentKey = arg3.getValue(i);
                }
            }
            System.out.print(arg2 + ":");
            super.startElement(arg0, arg1, arg2, arg3);
        }
    }
}
