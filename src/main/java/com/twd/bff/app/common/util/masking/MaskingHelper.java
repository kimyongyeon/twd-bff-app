package com.twd.bff.app.common.util.masking;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MaskingHelper {
	
   private static Properties properties = new Properties();
   
   public static void init(String lang) {
      try {

         String language = lang.toLowerCase();

         properties.load(
            Thread.currentThread().getContextClassLoader()
               .getResourceAsStream("encryption/masking_" + language + ".properties"));
      } catch (Exception e) {
         log.debug("MaskingHelper init lang : ", lang);
         log.debug("MaskingHelper init Exception : ", e);
      }
   }

   public static void initFilename(String filename) {
      try {
         properties.load(
            Thread.currentThread().getContextClassLoader()
               .getResourceAsStream(filename));
      } catch (Exception e) {
         log.debug("MaskingHelper initFilename Exception : ", e);
      }
   }

   public static String maskString(String type, String text) throws Exception {

      switch (MaskingTypeEnum.valueOfCode(type)) {
         case MaskName: //항목 : 성명
            text = nameMasking(text);
            break;
         case MaskPsptName: //항목 : 여권명
            text = psptNameMasking(text);
            break;
         case MaskTel: //전화번호(컬럼분리와 컬럼이 분리 되지 않은경우)
         case MaskTelOne:
            text = telMasking(text);
            break;
         case MaskCell: //전화번호(컬럼분리와 컬럼이 분리 되지 않은경우)
         case MaskCellOne:
            text = cellMasking(text);
            break;
         case MaskEmail: //이메일 마스킹
            text = emailMasking(text);
            break;
         case MaskId: //id 마스킹
            text = idMasking(text);
            break;
         case MaskCard: //카드번호 앞 6자리, 뒤 4자리 빼고 마스킹  (ex : 9430-20**-****-2399), 해외 카드는 '-' 없이 자릿수가 14~16 자리임 앞의 6자리 빼고 마스킹 처리
            text = cardMasking(text);
         case MaskBirth: //생일 전부 마스킹
         case MaskAddr: //주소마스킹
         case MaskIp: //IP 마스킹 (ex : 123.123.***.123)
         case MaskPsportno: //여권번호 뒤 3자리 마스킹
         case MaskAccount: //계좌번호 풀 마스킹
         case MaskPsportdt: //여권만료일 풀 마스킹
            text = masking(type, text);
            break;
         default:
            break;
      }
      return text;
   }

   /**
    * 성명 마스킹 2자인 경우 뒷자리 마스킹 3~4자인 경우 가운데 마스킹 5자 이상인 경우 5번째 자리부터 이후 마스킹 (띄어쓰기 포함)
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String nameMasking(String text) throws Exception {

      String typeName = "";

      if (text.length() == 1) { //이름이 1자인 경우-> 그대로 return
         typeName = text;
      } else if (text.length() == 2) { //이름이 2자인경우
         typeName = MaskingTypeEnum.MaskNameTwoLength.getCode();
      } else if (text.length() == 3) { //이름이 3자인경우
         typeName = MaskingTypeEnum.MaskNameThreeLength.getCode();
      } else if (text.length() == 4) { //이름이 4자인경우
         typeName = MaskingTypeEnum.MaskNameFourLength.getCode();
      } else if (text.length() > 4) { //이름이  4자 이상인 경우
         typeName = MaskingTypeEnum.MaskNameFiveMoreLength.getCode();
      }

      return masking(typeName, text);
   }

   /**
    * 여권영문명 마스킹 Last Name 마스킹 적용 First Name 마스킹 미적용 (Ex : Hong Gildong > Hong *******)
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String psptNameMasking(String text) throws Exception {

      String typeName = MaskingTypeEnum.MaskPsptName.getCode();

      if (text.indexOf(" ") > -1) {
         int spaceIdx = 0; // space
         spaceIdx = text.indexOf(" ");
         String firstName = text.substring(0, spaceIdx);
         String lastName = text.substring(spaceIdx).trim();
         lastName = masking(typeName, lastName);
         text = firstName + " " + lastName;
      }
      return text;
   }

   private static String cardMasking(String text) throws Exception {
      String typeName = MaskingTypeEnum.MaskCard.getCode();

      if (text.indexOf("-") > -1) {
         text = masking(typeName, text);
      } else {
         String headerText = text.substring(0, 6);
         String subText = text.substring(6);
         subText = masking(MaskingTypeEnum.MaskAll.getCode(), subText);

         text = headerText + subText;
      }

      return text;
   }

   /**
    * 전화번호 마스킹 국가번호+번호1+번호2+번호3 인 경우 -> 번호2 마스킹 (국번) 전화번호 단일 컬럼인 경우 -> 데이터 마지막문자부터 역방향으로 5~8번째 영역 마스킹('-' 없이 저장)
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String telMasking(String text) throws Exception {

      String typeName = MaskingTypeEnum.MaskTel.getCode();

      if (!("").equals(text) && text.length() < 5) {
         text = masking(MaskingTypeEnum.MaskAll.getCode(), text);
      } else {
         if (text.indexOf("-") > -1) {
            text = masking(typeName, text);
         } else {
            String subText = text.substring(text.length() - 8); //뒤에서 8자리를 잘라냄
            String headerText = text.substring(0, text.length() - 8);

            typeName = MaskingTypeEnum.MaskTelOne.getCode();
            subText = masking(typeName, subText); //뒤에서 8자리를 잘라낸 전번 (?=.{5}). 앞 4자리를 마스킹 처리함

            text = headerText + subText;
         }
      }
      return text;
   }

   /**
    * 핸드폰 마스킹 국가번호+번호1+번호2+번호3 인 경우 -> 번호2 마스킹 (국번) 전화번호 단일 컬럼인 경우 -> 데이터 마지막문자부터 역방향으로 5~8번째 영역 마스킹('-' 없이 저장)
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String cellMasking(String text) throws Exception {

      String typeName = MaskingTypeEnum.MaskCell.getCode();

      if (!("").equals(text) && text.length() < 5) {
         text = masking(MaskingTypeEnum.MaskAll.getCode(), text);
      } else {
         if (text.indexOf("-") > -1) {
            text = masking(typeName, text);
         } else {
            String subText = text.substring(text.length() - 8); //뒤에서 8자리를 잘라냄
            String headerText = text.substring(0, text.length() - 8);

            typeName = MaskingTypeEnum.MaskCellOne.getCode(); //뒤에서 8자리를 잘라낸 전번 (?=.{5}). 앞 4자리를 마스킹 처리함
            subText = masking(typeName, subText);

            text = headerText + subText;
         }
      }
      return text;
   }

   /**
    * 1) @ 값 이전     2자리 : 2번째 문자,      3자리 이상 : 3번째 자리부터 이후 2) @ 값 이후      첫번째 '.' 값 전까지 ex :
    * a@*****.com, a*@*****.com, ab***@*****.com
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String emailMasking(String text) throws Exception {

      String typeName = MaskingTypeEnum.MaskEmail.getCode();

      if (text.indexOf("@") > -1) {
         int spaceIdx = 0; // space
         spaceIdx = text.indexOf("@");

         String firstName = text.substring(0, spaceIdx);
         String lastName = text.substring(spaceIdx + 1);

         if (firstName.length() == 2) {
            typeName = MaskingTypeEnum.MaskNameTwoLength.getCode();
            firstName = masking(typeName, firstName);
         } else {
            typeName = MaskingTypeEnum.MaskEmail.getCode();
            firstName = masking(typeName, firstName);
         }

         if (text.indexOf(".") > -1) {
            String tmp1 = lastName.substring(0, lastName.indexOf("."));
            String tmp2 = lastName.substring(lastName.indexOf("."));

            typeName = MaskingTypeEnum.MaskAll.getCode();
            tmp1 = masking(typeName, tmp1);
            lastName = tmp1 + tmp2;
         }
         text = firstName + "@" + lastName;
      }
      return text;
   }

   /**
    * id 마스킹     * 2자리 : 2번째 문자,       * 3자리 이상 : 3번째 자리부터 이후 ex : ghg********
    *
    * @param text
    * @return text
    * @throws Exception
    */
   private static String idMasking(String text) throws Exception {

      String typeName = MaskingTypeEnum.MaskId.getCode();

      if (text.length() == 2) {
         typeName = MaskingTypeEnum.MaskNameTwoLength.getCode();
         text = masking(typeName, text);
      } else {
         text = masking(typeName, text);
      }
      return text;
   }

   /**
    * properties 에서 패턴과 REGEX 를 읽어와 마스킹 처리 후리 턴
    *
    * @param typeName,
    *            text
    * @return text
    * @throws Exception
    */
   private static String masking(String typeName, String text) throws Exception {
      StringBuilder sb = new StringBuilder();
      String patternKey = sb.append(typeName).append(".PATTERN").toString();
      sb = new StringBuilder();
      String regexKey = sb.append(typeName).append(".REGEX").toString();

      if (properties.containsKey(patternKey) && properties.containsKey(regexKey)) {
         String pattern = properties.getProperty(patternKey);
         String regex = properties.getProperty(regexKey);
         String result = text.replaceAll(pattern, regex);

         return result;
      } else {
         throw new Exception("no such pattern and regex are registered in the property file: " + typeName);
      }
   }

}
