package com.twd.bff.app.common.util.masking;

public enum MaskingTypeEnum {
   MaskAll("MASK_ALL", "전체마스킹"),
   MaskName("MASK_NAME", "성명"),
   MaskNameTwoLength("MASK_NAME1", "2자이름"),
   MaskNameThreeLength("MASK_NAME2", "3자이름"),
   MaskNameFourLength("MASK_NAME3", "4자이름"),
   MaskNameFiveMoreLength("MASK_NAME4", "4자초과이름"),
   MaskPsptName("MASK_PSPT_NAME",""),
   MaskBirth("MASK_BIRTH", ""),
   MaskTel("MASK_TEL", ""),
   MaskTelOne("MASK_TEL_ONE", ""),
   MaskCell("MASK_CELL", ""),
   MaskCellOne("MASK_CELL_ONE", ""),
   MaskAddr("MASK_ADDR", ""),
   MaskIp("MASK_IP", ""),
   MaskEmail("MASK_EMAIL", ""),
   MaskId("MASK_ID", ""),
   MaskPsportno("MASK_PSPORTNO", ""),
   MaskCard("MASK_CARD", ""),
   MaskAccount("MASK_ACCOUNT", ""),
   MaskPsportdt("MASK_PSPORTDT", "");

   private String desc;
   private String code;

   MaskingTypeEnum(String code, String desc) {
      this.desc = desc;
      this.code = code;
   }

   public String getDesc() {
      return desc;
   }

   public String getCode() {
      return code;
   }

   public static MaskingTypeEnum valueOfCode(String code) {
      for (MaskingTypeEnum st : MaskingTypeEnum.values()) {
         if (st.getCode().equals(code)) {
            return st;
         }
      }
      return null;
   }
}