package cm.commons.dao.basic;

import java.io.Serializable;
import java.util.List;

import cm.commons.exception.AppException;

public interface BasicDao<K extends Serializable, E> {

	/**
	 * 保存实体对象
	 * @param entity 
	 */
	void save(E entity) throws AppException;
	
	/**
	 * 更新或者保存对象
	 * @param entity
	 * @throws AppException
	 */
	void saveOrUpdate(E entity) throws AppException;
	
	/**
	 * 更新实体
	 * @param entity
	 */
	void update(E entity) throws AppException;
	
	/**
	 * 获取实体对象
	 * @param id
	 * @return
	 */
	E get(K id) throws AppException;
	
	/**
	 * 获取所有实体对象
	 * @return
	 * @throws AppException
	 */
	List<E> getAll() throws AppException;
	
	/**
	 * 通过id删除实体对象
	 * @param id
	 */
	void deleteById(K id) throws AppException;
	
	/**
	 * 删除实体对象
	 * @param entity
	 * @throws AppException
	 */
	void delete(E entity) throws AppException;
	
}