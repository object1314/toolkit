/*
 * Copyright (c) 2023-2023 XuYanhang.
 */
package org.xuyh.util;

/**
 * <pre>
 * 中华人民共和国国家标准 GB 11643-1999
 * 公民身份号码
 * Citizen identification number
 *
 * 1. 范围
 *  该标准规定了公民身份号码的编码对象、号码的结构和表现形式，使每个编码对象获得一个唯一的、不变的法定号码。
 * 2. 编码对象
 *  公民身份号码的编码对象是具有中华人民共和国国籍的公民。
 * 3. 结构
 *  公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 4. 地址码
 *  第1位为以前的大区制代码，全国共分为8个大区：华北（1）、东北（2）、华东（3）、华南（4）、西南（5）、西北（6）、台湾（7）和港澳（8）
 *  第2位是大区所在省市编码
 *      华北地区：北京市|11，天津市|12，河北省|13，山西省|14，内蒙古自治区|15
 *      东北地区：辽宁省|21，吉林省|22，黑龙江省|23
 *      华东地区：上海市|31，江苏省|32，浙江省|33，安徽省|34，福建省|35，江西省|36，山东省|37
 *      华中地区：河南省|41，湖北省|42，湖南省|43
 *      华南地区：广东省|44，广西壮族自治区|45，海南省|46
 *      西南地区：重庆市|50，四川省|51，贵州省|52，云南省|53，西藏自治区|54
 *      西北地区：陕西省|61，甘肃省|62，青海省|63，宁夏回族自治区|64，新疆维吾尔自治区|65
 *      特别地区：香港特别行政区|81，澳门特别行政区|82，台湾省|83[台湾省行政区划代码71]
 *  第3、4位表示地级行政区。
 *      01-20、51-70表示地级市
 *      21-50表示地区、自治州、盟
 *      90表示省直辖县级行政单位
 *      直辖市身份证中01表示市辖区，02表示县
 *  第5、6位表示县级行政区。
 *      01-18表示市辖区或地区、自治州、盟辖县级市
 *      21-80表示县、旗
 *      81-99表示省直辖县级行政单位
 * 5. 生日期码
 *  身份证号7~14位。表示编码对象出生的年、月、日，其中年份用4位数字表示，月、日用2位数字表示。年、月、日之间不用分隔符。按GB/T7408的规定执行。
 * 6. 顺序码
 *  身份证号15~17位。表示在同一地址码所标识的区域范围内，对同年、月、日出生的人员编定的顺序号。其中第17位奇数分给男性，偶数分给女性。
 * 7. 校验码
 *  身份证号第18位。根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。使用罗马数字X代表10。
 * </pre>
 *
 * @author XuYanhang
 * @since 2023-03-21
 */
public final class CitizenIdNum {
    /**
     * The length of Citizen identification number
     */
    public static final int CIN_LEN = 18;

    /**
     * The length of master number in Citizen identification number
     */
    public static final int MASTER_NUM_LEN = CIN_LEN - 1;

    /**
     * Calculate check number on a known master number.
     *
     * @param cin a string who must starts with master number with length {@value #MASTER_NUM_LEN}
     * @return the check number char in 0~9 and X.
     */
    public static char calCheckNum(String cin) {
        int value = 0;
        for (int i = 0; i < MASTER_NUM_LEN; i++) {
            int weight = (1 << (MASTER_NUM_LEN - i)) % 11;
            int num = Character.digit(cin.charAt(i), 10);
            if (num < 0) {
                throw new IllegalArgumentException("Digits expected");
            }
            value += weight * num;
        }
        int checkNum = (12 - value % 11) % 11;
        return checkNum == 10 ? 'X' : (char) (checkNum + '0');
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private CitizenIdNum() {
    }
}
