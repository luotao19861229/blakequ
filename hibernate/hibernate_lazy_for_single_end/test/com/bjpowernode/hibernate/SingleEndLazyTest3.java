package com.bjpowernode.hibernate;

import org.hibernate.Session;

import junit.framework.TestCase;

/**
 * 将<class>标签上的lazy设置为false，其他默认
 * @author Administrator
 *
 */
public class SingleEndLazyTest3 extends TestCase {

	public void testLoad1() {
		Session session = null;
		try {
			session = HibernateUtils.getSession();
			session.beginTransaction();
			
			//会发出sql
			User user = (User)session.load(User.class, 1);
			
			//不会发出sql
			System.out.println("user.name=" + user.getName());
			
			//不会发出sql
			Group group = user.getGroup();
			
			//会发出sql
			System.out.println("user.group.name=" + group.getName());
			session.getTransaction().commit();
		}catch(Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally {
			HibernateUtils.closeSession(session);
		}
	}		
	
}
