package kr.co.saramin.lab.recruitassigntaskrepository.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

    /**
     * 정규식에서 그룹에 해당되는 애 하나만 리턴 없으면 null return
     * @param content
     * @param regex
     * @param groupName
     * @return
     */
    public static String extractSingleGroup(String content, String regex, String groupName){
        String matchedContent = null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if(matcher.find()) {
            matchedContent = matcher.group(groupName);
        }

        return matchedContent;
    }


    /**
     * 다양한 그룹 활용 가능하도록 Matcher 반환
     * @param content
     * @param regex
     * @return
     */
    public static Matcher extractMatcher(String content, String regex){

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        return matcher;
    }
}
