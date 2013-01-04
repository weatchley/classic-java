package gov.ymp.csi.spring.ldap;

import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

public class SpringFrameworkLDAPClient {

	public static void main(String[] args) {
		try {
			Resource resource = new ClassPathResource("gov/ymp/csi/spring/ldap/slydservices.xml");
			BeanFactory factory = new XmlBeanFactory(resource);
			ContactDAO ldapContact = (LDAPContactDAO)factory.getBean("ldapContact");
			List contactList = ldapContact.getContactDetails("higashis","higashi");
			//List contactList = ldapContact.getAllContactNames();
			for( int i = 0 ; i < contactList.size(); i++){
				System.out.println("Contact Name " + contactList.get(i));
			}
			//ContactDTO newContactDTO = new ContactDTO();
			//newContactDTO.setCommonName("higashis");
			//newContactDTO.setLastName("higashi");
			//newContactDTO.setDescription("HTSI West");
			//ldapContact.updateContact(newContactDTO);
		} catch (DataAccessException e) {
			System.out.println("Error occured " + e.getCause());
		}
	}
}
