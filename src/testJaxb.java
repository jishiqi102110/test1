import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class testJaxb {
	@Test
    public void test01(){
		//java对象转化xml
    	try {
			JAXBContext jxc=JAXBContext.newInstance(Student.class);
			Marshaller marshaller=jxc.createMarshaller();
			Student stu=new Student(1,"张三",20,new Classroom(1,"10计算机应用技术",2010));
			marshaller.marshal(stu, System.out);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    }
	   //由xml转换java对象
	@Test
	public  void test02(){
	    String  xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><student><age>20</age><classroom><grade>2010</grade><id>1</id><name>10计算机应用技术</name></classroom><id>1</id><name>张三</name></student>";	
	   try {
		JAXBContext jxc=JAXBContext.newInstance(Student.class);
		Unmarshaller un=jxc.createUnmarshaller();
		Student stu=(Student)un.unmarshal(new StringReader(xml));
		System.out.println(stu);
	} catch (JAXBException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   
	}
}
