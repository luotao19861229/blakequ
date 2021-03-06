package cm.commons.stat.dao.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cm.commons.pojos.Computer;
import cm.commons.stat.dao.ComputerDao;
import junit.framework.TestCase;

public class ComputerDaoTest extends TestCase {

	private ComputerDao cd;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		String []locations={ "classpath:spring/applicationContext-*.xml"};
		ApplicationContext ac = new ClassPathXmlApplicationContext(locations);
		cd = (ComputerDao) ac.getBean("computerDao");
	}
	
	public void testDeleteComputerByIp(){
		cd.deleteComputerByIp("111");
	}
	
	public void testDelete(){
		cd.deleteById(1);
	}
	
	public void testGetComputerByIp(){
		Computer c = cd.getComputerByIp("2");
		System.out.println(c.getId());
		System.out.println(c.getStation());
	}
	
	public void testGetComputerByStationId(){
		System.out.println(cd.getComputerByStationId(1));
	}
	

}
