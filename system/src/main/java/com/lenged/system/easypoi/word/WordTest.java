package com.lenged.system.easypoi.word;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @title: WordTest
 * @description: TODO
 * @auther: zhangjianyun
 * @date: 2022/8/31 10:59
 */
public class WordTest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");


    /**
     * 导出带图片的word
     * 图片列表
     * @throws Exception
     */
    @Test
    public void imageWordExport() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Easypoi");
        map.put("time", format.format(new Date()));
        Person person = new Person();
        person.setName("JueYue");
        map.put("p", person);
        ImageEntity image = new ImageEntity();
        image.setHeight(200);
        image.setWidth(500);
        image.setUrl("template/img/mainbg.jpg");
        image.setType(ImageEntity.URL);
        image.setLocationType(ImageEntity.ABOVE);
        map.put("testCode", image);

        List<ImageEntity> listImage = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            image = new ImageEntity();
            image.setHeight(40);
            image.setWidth(100);
            image.setUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg0.pconline.com.cn%2Fpconline%2F1312%2F13%2F3997518_14-014632_349_thumb.jpg&refer=http%3A%2F%2Fimg0.pconline.com.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1664517779&t=a29c97e8ea55c41cf19fb0e9e0c54ac2");
            image.setType(ImageEntity.URL);
            listImage.add(image);
        }
        map.put("imglist",listImage);

        XWPFDocument doc = WordExportUtil.exportWord07(
                "./template/word/imgfor.docx", map);
        FileOutputStream fos = new FileOutputStream("./target/imgfor.docx");
        doc.write(fos);
        fos.close();
    }


    @Test
    public void SimpleWordExport() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Jee\r\ncg");
        map.put("person", "Jue\r\nYue");
        map.put("auditPerson", "JueYue");
        map.put("time", format.format(new Date()));
        map.put("date", new Date());
        List<Person> list = new ArrayList<Person>();
        Person p = new Person();
        p.setName("小明");
        p.setTel("18711111111");
        p.setEmail("18711111111@\\r\\n139.com");
        list.add(p);
        p = new Person();
        p.setName("小红");
        p.setTel("18711111112");
        p.setEmail("18711111112@\r\n139.com");
        list.add(p);
        map.put("pList", list);
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(
                    "./template/word/SimpleExcel.docx", map);
            FileOutputStream fos = new FileOutputStream("./target/wrapExcel.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
