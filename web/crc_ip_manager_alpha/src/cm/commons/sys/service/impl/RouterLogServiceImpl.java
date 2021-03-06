package cm.commons.sys.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cm.commons.dao.hiber.util.Element;
import cm.commons.exception.AppException;
import cm.commons.pojos.ComputerLog;
import cm.commons.pojos.RouterLog;
import cm.commons.sys.dao.RouterLogDao;
import cm.commons.sys.service.RouterLogService;
import cm.commons.util.PageModel;

public class RouterLogServiceImpl implements RouterLogService<Integer, RouterLog> {
	private static Log log = LogFactory.getLog(RouterLogServiceImpl.class);
	@Autowired
	private RouterLogDao routerLogDao;
	
	
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		log.debug("delete data "+this.getClass().getName());
		try {
			routerLogDao.deleteById(id);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("delete data fail! "+this.getClass().getName(), e);
			throw new AppException("删除路由日志失败");
		}
	}
	public RouterLog get(Integer id) {
		// TODO Auto-generated method stub
		log.debug("get data "+this.getClass().getName());
		try {
			return (RouterLog) routerLogDao.get(id);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get data fail! "+this.getClass().getName(), e);
			throw new AppException("获取路由日志失败");
		}
	}
	public List<RouterLog> getAll() {
		// TODO Auto-generated method stub
		log.debug("get all data "+this.getClass().getName());
		try {
			return routerLogDao.getAll();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get all data fail! "+this.getClass().getName(), e);
			throw new AppException("获取所有路由日志失败");
		}
	}
	public void save(RouterLog entity) {
		// TODO Auto-generated method stub
		log.debug("save data "+this.getClass().getName());
		try {
			routerLogDao.save(entity);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("save data fail! "+this.getClass().getName(), e);
			throw new AppException("存储路由日志失败");
		}
	}
	public void saveOrUpdate(RouterLog entity) {
		// TODO Auto-generated method stub
		log.debug("save or update "+this.getClass().getName());
		try {
			routerLogDao.saveOrUpdate(entity);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("save or update data fail! "+this.getClass().getName(), e);
			throw new AppException("存储或更新路由日志失败");
		}
	}
	public void update(RouterLog entity) {
		// TODO Auto-generated method stub
		log.debug("update data "+this.getClass().getName());
		try {
			routerLogDao.update(entity);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("update data fail! "+this.getClass().getName(), e);
			throw new AppException("更新路由日志失败");
		}
	}

	public void delete(RouterLog entity) {
		// TODO Auto-generated method stub
		log.debug("delete data "+this.getClass().getName());
		try {
			routerLogDao.delete(entity);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("delete data fail! "+this.getClass().getName(), e);
			throw new AppException("删除路由日志失败");
		}
	}

	public List<RouterLog> getAllSortByRouter() {
		// TODO Auto-generated method stub
		log.debug("get all router sort by router id "+this.getClass().getName());
		try {
			return routerLogDao.getAllSortByRouter();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get all router sort by router id fail! "+this.getClass().getName(), e);
			throw new AppException("根据路由排序获取日志失败");
		}
	}

	public List<RouterLog> getAllSortByTime() {
		// TODO Auto-generated method stub
		log.debug("get all router sort by time "+this.getClass().getName());
		try {
			return routerLogDao.getAllSortByTime();
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get all router sort by time fail! "+this.getClass().getName(), e);
			throw new AppException("根据时间排序获取日志失败");
		}
	}

	public List<RouterLog> getRouterLog(Integer routerId) {
		// TODO Auto-generated method stub
		log.debug("get all router by router id "+this.getClass().getName());
		try {
			return routerLogDao.getRouterLog(routerId);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get all router by router id fail! "+this.getClass().getName(), e);
			throw new AppException("根据路由id获取日志失败");
		}
	}

	public List<RouterLog> getRouterLogByStationNameOrId(String key) {
		// TODO Auto-generated method stub
		log.debug("get router log by station name or id "+this.getClass().getName());
		try {
			return routerLogDao.getRouterLogByStationNameOrId(key);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get router log by station name or id fail! "+this.getClass().getName(), e);
			throw new AppException("根据站点名字或id："+key+"获取日志失败");
		}
	}

	/**
	 * 分页获取数据，可以通过车站名字获取路由日志
	 */
	public PageModel<RouterLog> getAll(String queryString, int pageNo,
			int pageSize) {
		// TODO Auto-generated method stub
		log.debug("get all data by page "+this.getClass().getName());
		try {
			return routerLogDao.getAll(queryString, pageNo, pageSize);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("get all data by page fail! "+this.getClass().getName(), e);
			throw new AppException("通过分页获取所有数据失败");
		}
	}
	public void deleteItem(Integer[] ids) {
		// TODO Auto-generated method stub
		log.debug("delete item array"+this.getClass().getName());
		try {
			routerLogDao.deleteItem(ids);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("delete item array fail! "+this.getClass().getName(), e);
			throw new AppException("删除多个实体失败");
		}
	}
	public PageModel<RouterLog> getPagedWithCondition(
			List<Element> conditions, int pageNo, int pageSize) {
		try {
			PageModel pageModel = new PageModel();
			pageModel.setPageNo(pageNo);
			pageModel.setPageSize(pageSize);
			List<RouterLog> list=routerLogDao.findPaged((pageNo-1) * pageSize, pageSize, conditions);
			pageModel.setList(list);
			pageModel.setTotalRecords((int)routerLogDao.getCounts(conditions));
			return pageModel;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new AppException("查询满足条件的路由器历史信息出错");
		}
	}
	
}
