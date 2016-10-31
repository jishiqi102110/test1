package STAX;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.EventFilter;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.omg.Messaging.SyncScopeHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
//基于光标的查找
public class TestStax {
    @Test
    public void test01(){
    	XMLInputFactory factory=XMLInputFactory.newFactory();
    	
    	InputStream is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    	
    	try {
			XMLStreamReader reader= factory.createXMLStreamReader(is);
			while(reader.hasNext()){
			    int type=reader.next();
			    //判断节点类型是否是开始元素，还是特征，还是结束元素
			    if(type==XMLStreamConstants.START_ELEMENT){
			    	System.out.println(reader.getName());
			    }else if(type==XMLStreamConstants.CHARACTERS){
			    	System.out.println(reader.getText().trim());
			    }else if(type==XMLStreamConstants.END_ELEMENT){
			    	System.out.println("/"+reader.getName());
			    }
			}
		} catch (XMLStreamException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    @Test
    public void test02(){
    	XMLInputFactory factory=XMLInputFactory.newFactory();
    	
    	InputStream is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    	
    	try {
			XMLStreamReader reader= factory.createXMLStreamReader(is);
			while(reader.hasNext()){
			    int type=reader.next();
			    if(type==XMLStreamConstants.START_ELEMENT){
			    	String name=reader.getName().toString();
			    	if(name=="book"){
			    		System.out.println(reader.getAttributeName(0)+":"+reader.getAttributeValue(0));
			    	}
			    }
			}
		} catch (XMLStreamException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    @Test
    public void test03(){
    	XMLInputFactory factory=XMLInputFactory.newFactory();
    	
    	InputStream is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    	
    	try {
			XMLStreamReader reader= factory.createXMLStreamReader(is);
			while(reader.hasNext()){
			    int type=reader.next();
			    if(type==XMLStreamConstants.START_ELEMENT){
			    	String name=reader.getName().toString();
			    	if(name.equals("title")){
			    		System.out.print(reader.getElementText());
			    	}
			    	if(name.equals("price")){
			    		System.out.println(reader.getElementText());
			    	}
			    }
			}
		} catch (XMLStreamException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    //基于迭代方式
    @Test
    public void test04(){
    	XMLInputFactory factory=XMLInputFactory.newFactory();
    	InputStream is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    	try {
			XMLEventReader reader=factory.createXMLEventReader(is);
			while(reader.hasNext()){
				//通过xmlEnvent 来获取是否是某种节点类型
			      XMLEvent event=reader.nextEvent();
			      if(event.isStartElement()){
			    	  //通过他的IS??什么的方式在来判断是否是什么节点
			    	  String name=event.asStartElement().getName().toString();
			    	  System.out.println(name);
			      }
			}
		} catch (XMLStreamException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    @Test
    public void test05(){
    	
    	XMLInputFactory factory=XMLInputFactory.newFactory();
    	InputStream is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    	try {
			XMLEventReader reader=factory.createFilteredReader(factory.createXMLEventReader(is),new EventFilter() {				
				@Override
				public boolean accept(XMLEvent event) {
					// TODO Auto-generated method stub
					if(event.isStartElement()){
						return true;
					}
					return false;
				}
			});
			int num=0;
			while(reader.hasNext()){
				//通过xmlEnvent 来获取是否是某种节点类型
				//基于FIlter来判断
			      XMLEvent event=reader.nextEvent();
			      if(event.isStartElement()){
			    	  //通过他的IS??什么的方式在来判断是否是什么节点
			    	  String name=event.asStartElement().getName().toString();
			    	  System.out.println(name);
			    	  num++;
			      }
			}
			System.out.println(num);
		} catch (XMLStreamException e) {
			
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    @Test
    //基于xpath的
    public void test06(){
    	InputStream is=null;
    	try {
    		XMLInputFactory factory=XMLInputFactory.newFactory();
    		 is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    		 //创建文档对象
    		 
		    DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //通过documentBUilder来创建doc的文档对象
		    Document doc=db.parse(is);
		    //创建xpath对象
		    XPath xpath=XPathFactory.newInstance().newXPath();
		    //第一个参数是xpath，第二个蚕食就是文档
		    NodeList list= (NodeList)xpath.evaluate("//book[@category='WEB']",doc,XPathConstants.NODESET);
		    for(int i=0;i<list.getLength();i++){
		    	Element e=(Element)list.item(i);
		    	System.out.println(e.getElementsByTagName("title").item(0).getTextContent());
		    }
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
    @Test
   
    public void test07(){
    	try {
			XMLStreamWriter xsd=XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
			xsd.writeStartDocument("UTF-8", "1.0");
			xsd.writeEndDocument();
			xsd.writeStartElement("person");
			xsd.writeStartElement("id");
			xsd.writeCharacters("1");
			xsd.writeEndElement();
			xsd.writeEndElement();
			xsd.flush();
			xsd.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @Test
    //通过transform来改变节点信息
    public void test08(){
    	InputStream is=null;
    	try {
    		XMLInputFactory factory=XMLInputFactory.newFactory();
    		 is=TestStax.class.getClassLoader().getResourceAsStream("books.xml");
    		 //创建文档对象
    		 
		    DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
            //通过documentBUilder来创建doc的文档对象
		    Document doc=db.parse(is);
		    //创建xpath对象
		    XPath xpath=XPathFactory.newInstance().newXPath();
		    //第一个参数是xpath，第二个蚕食就是文档
		    
		    Transformer trans=TransformerFactory.newInstance().newTransformer();
		    trans.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		    NodeList list= (NodeList)xpath.evaluate("//book[title='Learning XML']",doc,XPathConstants.NODESET);
		    	Element be=(Element)list.item(0);
		    	Element e=(Element)(be.getElementsByTagName("price").item(0));
		    	e.setTextContent("330");
		    	Result result=new StreamResult(System.out);
		    //transform来修改节点	
		   trans.transform(new DOMSource(doc),result); 	
		
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerFactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			try{
				if(is!=null) is.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
    }
}
