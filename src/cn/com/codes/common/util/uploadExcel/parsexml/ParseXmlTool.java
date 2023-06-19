package cn.com.codes.common.util.uploadExcel.parsexml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ParseXmlTool {
	public XmlEntity parseXml(String fileName) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		XmlEntity xmlEntity = new XmlEntity();
		try {
			doc = builder.build(getClass().getResourceAsStream(fileName));
			Element root = doc.getRootElement();
			List cloumnList = root.getChildren();
			String className = root.getAttributeValue("class.name");
			int length = Integer.parseInt(root.getAttributeValue("column.length"));
			xmlEntity.setClassName(className);
			xmlEntity.setLength(length);
			List<XmlEntity.CellConfig> cellList = new ArrayList<XmlEntity.CellConfig>();
			for (int i = 0; i < cloumnList.size(); i++) {
				Element elmColumn = (Element) cloumnList.get(i);
				int index = Integer.parseInt(elmColumn
						.getAttributeValue("index"));
				String propertyName = elmColumn
						.getAttributeValue("class.property");
				String group = elmColumn.getAttributeValue("group");
				String dataType = elmColumn.getAttributeValue("dataType");
				String errorInformation = elmColumn
						.getAttributeValue("errorInformation");
				boolean noNull = Boolean.parseBoolean(elmColumn
						.getAttributeValue("not-null"));
				XmlEntity.CellConfig cellConfig = xmlEntity.new CellConfig();
				cellConfig.setIndex(index);
				cellConfig.setPropetyName(propertyName);
				cellConfig.setNoNull(noNull);
				cellConfig.setDataType(dataType);
				cellConfig.setGroup(group);
				cellConfig.setErrorInformation(errorInformation);
				List matchList = elmColumn.getChildren();
				if (matchList.size() > 0) {
					StringBuffer temp = new StringBuffer();
					for (int j = 0; j < matchList.size(); j++) {
						Element elmMatch = (Element) matchList.get(j);
						String checkOut = elmMatch.getValue();
						if (elmMatch.getName().equalsIgnoreCase("match")) {
							temp.append(checkOut).append(";");
						} else {
							cellConfig.setMethod(checkOut);
						}
					}
					String tempStr = new String(temp);
					if (tempStr != null && !tempStr.equalsIgnoreCase("")) {
						String[] tempArray = tempStr.split(";");
						cellConfig.setMatch(tempArray);
					}
				}
				cellList.add(cellConfig);
			}
			xmlEntity.setCellConfigList(cellList);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xmlEntity;
	}

	public static void main(String[] args) {
		ParseXmlTool test = new ParseXmlTool();
		XmlEntity xmlEntity = test.parseXml("student.xml");
		System.out.println(xmlEntity.getClassName());
		for (int i = 0; i < xmlEntity.getCellConfigList().size(); i++) {
			XmlEntity.CellConfig cell = (XmlEntity.CellConfig) xmlEntity
					.getCellConfigList().get(i);
			System.out.println(cell.getIndex() + cell.getPropetyName()
					+ cell.getMatch() + cell.isNoNull()+cell.getMethod());
		}
	}

}
