package org.xuyh.config;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

@org.springframework.context.annotation.Configuration
public class CacheManagerConfig {

	/** The Cache Manager */
	private CacheManager cacheManager;

	/**
	 * 
	 */
	public CacheManagerConfig() {
		super();
	}

	/**
	 * Initial the {@link #cacheManager}.
	 * 
	 * @throws IOException
	 * @throws CacheException
	 */
	@javax.annotation.PostConstruct
	private void initCacheManager() throws Exception {
		cacheManager = CacheManager.create(new ClassPathResource("conf/ehcache.xml").getURL());
	}

}
