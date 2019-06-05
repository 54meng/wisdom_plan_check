package com.wpc.service.impl;

import java.util.List;

import com.wpc.commons.sql.SearchContainer;
import com.wpc.dfish.dao.Page;
import com.wpc.dfish.dao.impl.PubCommonDAOImpl;
import com.wpc.dfish.framework.FrameworkHelper;
import com.wpc.dfish.util.Utils;
import com.wpc.persistence.Praise;
import com.wpc.persistence.User;
import com.wpc.persistence.Video;
import com.wpc.service.ServiceLocator;

@SuppressWarnings("unchecked")
public class VideoDBMethod extends PubCommonDAOImpl{
	private static final String idName = "id";
	private static final String initId = "00000001";
	
	public Video save(Video entity) throws Exception {
		if (entity != null) {
			if (Utils.isEmpty(entity.getId())) {
				entity.setId(FrameworkHelper.getNewId("Video", idName, initId));
			}
			
			super.saveObject(entity);
		}
		return entity;
	}
	
	public boolean delete(Video entity) throws Exception {
		if(null == entity){
			return false;
		}
		int rs = this.deleteSQL("FROM Video t WHERE t.id = ?", entity.getId());
		return rs > 0 ? true : false;
	}
	
	public boolean deleteByIds(String[] ids) throws Exception {
		if (Utils.notEmpty(ids)) {
			String idStr = "";
			for(String id: ids){
				idStr += ","+"'"+id+"'";
			}	
			int rs = this.deleteSQL("FROM Video t WHERE t.id in("+idStr.substring(1)+")");
			return rs > 0 ? true : false;
		}
		return false;
	}
	
	public Video update(Video entity) throws Exception {
		if (entity != null) {
			super.updateObject(entity);
		}
		return entity;
	}
	
	public void updateRemainCount(String userId, String now) throws Exception {		
		this.update("UPDATE User t SET t.remainCount = t.watchCount, t.now='"+now+"' WHERE t.userid = '"+userId+"'");
	}
	
	public User getUserByNow(String userId, String now){
		if (Utils.notEmpty(userId) && Utils.notEmpty(now)) {
			List<User> list = getQueryList("FROM User t WHERE t.userid = ? and t.now = ?", userId, now);
			if ((list != null) && (list.size() > 0)) {
				return (User) list.get(0);
			}
		}
		return null;
	}
	
	public Video findById(String id) throws Exception {
		if (Utils.notEmpty(id)) {
			List<Video> list = getQueryList("FROM Video t WHERE t.id = ?", id);
			if ((list != null) && (list.size() > 0)) {
				return (Video) list.get(0);
			}
		}
		return null;
	}
	
	public List<Video> findByAuthorIds(String[] idArray, String userId) throws Exception {
		if(Utils.isEmpty(idArray)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String id : idArray){
			sb.append(",").append("'"+id+"'");
		}
		String hql = "FROM Video t WHERE authorId in("+sb.substring(1)+") " ;
		List<Video> list = getQueryList(hql);
		if(Utils.notEmpty(userId) && Utils.notEmpty(list)){
			List<Praise> pList = ServiceLocator.getPraiseService().findByUserId(userId);
			if(Utils.notEmpty(pList)){
				for(Video video : list){
					for(Praise praise : pList){
						if(video.getId().equals(praise.getVideoId())){
							video.setIsPraise(1);
							break;
						}
					}
				}
			}	
		}
		return list;
	}
	
	public List<Video> findByIds(String[] idArray, String userId) throws Exception {
		if(Utils.isEmpty(idArray)){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String id : idArray){
			sb.append(",").append("'"+id+"'");
		}
		String hql = "FROM Video t WHERE id in("+sb.substring(1)+") " ;
		List<Video> list = getQueryList(hql);
		if(Utils.notEmpty(userId) && Utils.notEmpty(list)){
			List<Praise> pList = ServiceLocator.getPraiseService().findByUserId(userId);
			if(Utils.notEmpty(pList)){
				for(Video video : list){
					for(Praise praise : pList){
						if(video.getId().equals(praise.getVideoId())){
							video.setIsPraise(1);
							break;
						}
					}
				}
			}	
		}
		return list;
	}

	public List<Video> findByPage(Page page, SearchContainer searchContainer, String userId) throws Exception {
		if(searchContainer == null){
			searchContainer = new SearchContainer();
			searchContainer.orderByDesc("t.id");
		}
		StringBuilder hql = new StringBuilder("FROM Video t ");
		hql.append(searchContainer.toHql());
		List<Video> list = null;
		if (page != null) {
			if(Utils.notEmpty(searchContainer.getArgs())){
				list = getQueryList(hql.toString(), page, true,searchContainer.getArgs());
			}else{
				list = getQueryList(hql.toString(), page, true, null);
			}
		} else {
			if(Utils.notEmpty(searchContainer.getArgs())){
				list = getQueryList(hql.toString(),searchContainer.getArgs());
			}else{
				list = getQueryList(hql.toString());
			}
		}
		if(Utils.notEmpty(userId) && Utils.notEmpty(list)){
			List<Praise> pList = ServiceLocator.getPraiseService().findByUserId(userId);
			if(Utils.notEmpty(pList)){
				for(Video video : list){
					for(Praise praise : pList){
						if(video.getId().equals(praise.getVideoId())){
							video.setIsPraise(1);
							break;
						}
					}
				}
			}	
		}
		return list;
	}

}
