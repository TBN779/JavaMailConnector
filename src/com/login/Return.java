package com.login;
/*package com.javamail;

import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class ReturnXMLElement {
	public static Element returnXMLElement() {

	  Element  EmailMessage = new Element("resultSet");
	EmailMessage.setAttribute("recordCount", "10");
	EmailMessage.setAttribute("columnCount", "-1");
	Document doc = new Document(EmailMessage);
	doc.setRootElement(EmailMessage);
	
	for(int i = 0; i < 10; i++) {
		
		Element record = new Element("record");		
		doc.getRootElement().addContent(record);
		
		Element column = new Element("column");
		column.setAttribute("name", "MSG_SUBJECT");		
		record.addContent(new Element("MSG_SUBJECT").setText("subject" + i));
		
		record.addContent(new Element("MSG_TO").setText("to" + i));
		doc.getRootElement().addContent(record);
	}

//		Element staff = new Element("staff");
//		staff.setAttribute(new Attribute("id", "1"));
//		staff.addContent(new Element("firstname").setText("yong"));
//		staff.addContent(new Element("lastname").setText("mook kim"));
//		staff.addContent(new Element("nickname").setText("mkyong"));
//		staff.addContent(new Element("salary").setText("199999"));
//
//		doc.getRootElement().addContent(staff);
//
//		Element staff2 = new Element("staff");
//		staff2.setAttribute(new Attribute("id", "2"));
//		staff2.addContent(new Element("firstname").setText("low"));
//		staff2.addContent(new Element("lastname").setText("yin fong"));
//		staff2.addContent(new Element("nickname").setText("fong fong"));
//		staff2.addContent(new Element("salary").setText("188888"));
//
//		doc.getRootElement().addContent(staff2);

	// new XMLOutputter().output(doc, System.out);
	XMLOutputter xmlOutput = new XMLOutputter();
	

	// display nice nice
	xmlOutput.setFormat(Format.getPrettyFormat());
	//xmlOutput.output(doc, new FileWriter("D:\\file.xml"));
	return EmailMessage;
	
	}
}*/