package cn.com.codes.framework.servlet;

import java.util.HashMap;

public interface CacheUtilsFactory {
	public HashMap getCacheRegionMap();

	public void setCacheRegionMap(HashMap cacheRegionMap);
}
