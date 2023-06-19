/**
 * 
 */
package cn.com.codes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

	// 单位到数值映射
	static Map<String, Integer> unitMap = new HashMap<String, Integer>(5);
	// 大写到小写映射
	static Map<String, Integer> numMap = new HashMap<String, Integer>(10);

	// 单位和其后单位列表映射
	static Map<String, List<String>> affUnitMap = new HashMap<String, List<String>>(4);

	// 拾后单位列表
	static List<String> tenAfter = new ArrayList<String>(4);
	// 百后单位列表
	static List<String> hundredAfter = new ArrayList<String>(3);
	// 千后单位列表
	static List<String> thousandAfter = new ArrayList<String>(2);
	// 万后单位列表
	static List<String> millondAfter = new ArrayList<String>(1);

	static {
		unitMap.put("拾", 10);
		unitMap.put("佰", 100);
		unitMap.put("仟", 1000);
		unitMap.put("万", 10000);
		unitMap.put("亿", 100000000);

		affUnitMap.put("拾", tenAfter);
		affUnitMap.put("佰", hundredAfter);
		affUnitMap.put("仟", thousandAfter);
		affUnitMap.put("万", millondAfter);

		tenAfter.add("佰");
		tenAfter.add("仟");
		tenAfter.add("万");
		tenAfter.add("亿");

		hundredAfter.add("仟");
		hundredAfter.add("万");
		hundredAfter.add("亿");

		thousandAfter.add("万");
		thousandAfter.add("亿");

		millondAfter.add("亿");

		numMap.put("零", 0);
		numMap.put("壹", 1);
		numMap.put("贰", 2);
		numMap.put("叁", 3);
		numMap.put("肆", 4);
		numMap.put("伍", 5);
		numMap.put("陆", 6);
		numMap.put("柒", 7);
		numMap.put("捌", 8);
		numMap.put("玖", 9);
	}

	public static String strConvert2Num(char[] valueArr) {

		double value = 0;
		double valueSum = 0;
		int sepraPosition = 0;
		int i = 0;
		int calcPositionFlg = 1;
		for (; i < valueArr.length; i++) {
			String currCharStr = String.valueOf(valueArr[i]);
			Integer valueTemp = numMap.get(currCharStr);
			if (valueTemp != null) {
				if (0 == valueTemp.intValue()) {
					continue;
				}
				if (i < (valueArr.length - 2)) {
					if(calcPositionFlg==1){
						currCharStr = String.valueOf(valueArr[i + 1]);
						List<String> affterList = affUnitMap.get(currCharStr);
						if (affterList != null) {
							int cuurrPosition = i + 1;
							for (; cuurrPosition < valueArr.length; cuurrPosition++) {
								String currValueCharStr = String
										.valueOf(valueArr[cuurrPosition]);
								if (affterList.contains(currValueCharStr)) {
									sepraPosition = cuurrPosition;
									break;
								}
							}
							if(cuurrPosition>=valueArr.length-1){
								calcPositionFlg=0;
							}
						}
					}
					if(sepraPosition!=0){
						int p = i;
						for(;p<sepraPosition;p++){
							valueTemp = numMap.get(String.valueOf(valueArr[p]));
							if(valueTemp!=null){							
								if(unitMap.get(String.valueOf(valueArr[p+1]))!=null&&(p+1)!=sepraPosition){
									value = value + valueTemp * (1 * unitMap.get(String.valueOf(valueArr[p+1])));
								}else{
									value = value + valueTemp;
								}	
							}
						}
						value = value * (1 * unitMap.get(String.valueOf(valueArr[sepraPosition])));
						valueSum = valueSum + value;
						value = 0;
						i=sepraPosition;
						sepraPosition = 0;
					}else{
						if(unitMap.get(String.valueOf(valueArr[i+1]))!=null){
							value = value + valueTemp * (1 * unitMap.get(String.valueOf(valueArr[i+1])));
						}else{
							value = value + valueTemp;
						}
					}
				} else {
					String key = String.valueOf(valueArr[i]);
					if (unitMap.get(key) != null) {
						value = value * (1 * unitMap.get(key));
					} else {
						value = value + valueTemp;
					}
					valueSum = valueSum + value;
					value = 0;
				}
			} else {
				if ((i == valueArr.length - 1)) {
					valueSum = valueSum* (1 * (unitMap.get(currCharStr) == null ? 1: unitMap.get(currCharStr)));
				}else if (i + 1 == valueArr.length - 1 && i == 0&& currCharStr.equals("拾")) {
					valueSum = valueSum + 10;
				}
			}
		}
		return String.valueOf(valueSum);
	}

	public static void main(String[] args) {

		String[] data = {"捌佰零伍", "捌仟零伍亿肆仟零肆万肆仟捌佰零伍", "捌佰零伍亿肆仟伍佰零肆万肆仟捌佰玖拾伍", "捌万", "捌",
				"壹佰", "拾伍", "捌拾伍", "捌拾", "叁佰零伍万伍仟捌佰玖拾伍", "肆仟捌佰玖拾伍", "肆仟伍佰零捌万",
				"捌佰玖拾伍亿肆仟伍佰万肆仟捌佰玖拾伍", "伍佰万零玖", "肆仟伍佰零捌", "贰拾叁万", "壹仟",
				"伍仟捌佰玖拾伍", "伍仟捌佰玖拾伍万", "肆仟伍佰万", "贰拾叁万", "玖拾伍", "伍仟捌佰玖拾伍" };
		long startTime = System.currentTimeMillis();
		for (int l = 0; l < 1; l++) {
			for (int i = 0; i < data.length; i++) {
				System.out.println(data[i]);
				System.out.println(strConvert2Num((data[i]).toCharArray()));
				System.out.println("========" + (i + 1));
			}
		}
		System.out.println(" take " + (System.currentTimeMillis() - startTime)+ " ms");
	}
}
