package cm.commons.stat.dao;

import java.io.Serializable;

import cm.commons.dao.basic.BasicDao;
import cm.commons.exception.AppException;
import cm.commons.pojos.Computer;

public interface ComputerDao<K extends Serializable, E> extends BasicDao<K, E> {

	/**
	 * 通过ip获取电脑
	 * @param ip
	 * @return
	 */
	Computer getComputerByIp(String ip) throws AppException;
	
	/**
	 * 通过ip删除电脑
	 * @param ip
	 */
	void deleteComputerByIp(String ip) throws AppException;
	
	/**
	 * 通过车站id获取电脑
	 * @param station_id
	 * @return
	 */
	E getComputerByStationId(K station_id) throws AppException;
}
