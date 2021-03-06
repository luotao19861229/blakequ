package cm.commons.stat.dao;

import java.io.Serializable;

import cm.commons.dao.basic.BasicDao;
import cm.commons.exception.AppException;
import cm.commons.pojos.Router;

public interface RouterDao<K extends Serializable, E> extends BasicDao<K, E> {

	/**
	 * 通过ip获取路由器
	 * @param ip
	 * @return
	 */
	Router getRouterByIp(String ip) throws AppException;
	
	/**
	 * 通过ip删除路由器
	 * @param ip
	 */
	void deleteRouterByIp(String ip)  throws AppException;
	
	/**
	 * 通过车站id获取路由器
	 * @param station_id
	 * @return
	 * @throws AppException
	 */
	E getRouterByStationId(K station_id) throws AppException;
	
}
