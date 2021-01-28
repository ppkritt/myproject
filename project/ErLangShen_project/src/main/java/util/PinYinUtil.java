package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PinYinUtil {

    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    /**
     * 汉语拼音格式化类
     */
    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();

    static {
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE); //小写拼音
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);  //不带声调
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);  //带v

    }

    /**
     * 字符串中是否包含中文
     * @param name
     * @return
     */
    public static boolean containsChinese(String name) {
        //正则表达式
        return name.matches(".*" + CHINESE_PATTERN + ".*");
    }

    /**
     * 通过文件名获取全拼+拼音首字母
     * 中华人民共和国--->zhongghuarenmingongheguo/zhrmghg(不考虑多音字)
     * @param name 文件名
     * @return    全拼+拼音首字母  数组
     */
    public static String[] get(String name){
        String[] result = new String[2];
        StringBuilder all = new StringBuilder();//全拼
        StringBuilder first = new StringBuilder();//首字母

        for (char c:name.toCharArray()
             ) {
            try{
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c,FORMAT);//多音字
                if(pinyins == null || pinyins.length == 0){
                    all.append(c);
                    first.append(c);
                }else {
                    all.append(pinyins[0]);
                    first.append(pinyins[0].charAt(0));
                }
            }catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                all.append(c);
                first.append(c);
            }
        }
        result[0] = all.toString();
        result[1] = first.toString();
        return result;
    }

    /**
     * 多音字版获取名称
     * @param name 文件名
     * @param fullSpell true为全拼,false为首字母
     * @return 文件名多音字数组
     */
    public static String[][] get(String name,boolean fullSpell){
        char[] chars = name.toCharArray();
        String[][] result = new String[chars.length][];
        for (int i = 0; i < chars.length; i++) {
            try{
                //去除音调后,会有重复,"只":[zhi,zhi...]---------quchong
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(chars[i],FORMAT);//多音字

                if(pinyins == null || pinyins.length == 0){
                   result[i] = new String[]{String.valueOf(chars[i])};
                }else{
                    result[i] = unique(pinyins,fullSpell);
                }
            }catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                result[i] = new String[]{String.valueOf(chars[i])};
            }
        }
        return result;
    }

    /**
     * 数组去重操作
     * @param array
     * @param fullSpell
     * @return
     */
    public static String[] unique(String[] array,boolean fullSpell){
        Set<String> set = new HashSet<>();
        for (String s:array
             ) {
            if(fullSpell){
                set.add(s);
            }else {
                set.add(String.valueOf(s.charAt(0)));
            }
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * 每个中文字符返回的是字符串数组,每两个字符串数组合并为一个字符串数组,之后以此类推
     * @param pinyinArray 文件名多音字数组
     * @return 返回文件名的排列组合
     */
    public static String[] compose(String[][] pinyinArray){
        if (pinyinArray == null || pinyinArray.length == 0){
            return null;
        }else if (pinyinArray.length == 1){
            return pinyinArray[0];
        }else {
            String[] result = pinyinArray[0];
            for (int i = 1; i < pinyinArray.length; i++) {
                result = compose(result,pinyinArray[i]);
            }
            return result;
        }
    }

    /**
     * 拼音数组两两组合
     * @param pinyin1
     * @param pinyin2
     * @return 返回组合
     */
    public static String[] compose(String[] pinyin1,String[] pinyin2){
        String[] result = new String[pinyin1.length*pinyin2.length];
        int k = 0;
        for (int i = 0; i < pinyin1.length; i++) {
            for (int j = 0; j < pinyin2.length; j++) {
                result[k] = pinyin1[i] + pinyin2[j];
                k++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(compose(get("中华人民共和国",true))));
        System.out.println(Arrays.toString(compose(get("中华人民共和国",false))));
    }
}
