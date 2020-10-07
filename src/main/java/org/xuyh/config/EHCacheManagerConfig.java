/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.DefaultResourceLoader;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

/**
 * Configuration on EHCache. Use CacheManager in an injected way.
 * 
 * @author XuYanhang
 *
 */
@org.springframework.context.annotation.Configuration
public class EHCacheManagerConfig {

	@Value("${ehcache.config}")
	private String configUrl;

	/**
	 * 
	 */
	public EHCacheManagerConfig() {
		super();
	}

	/**
	 * Initial the {@link CacheManager}. After this action, the CacheManager
	 * instance fetch operation is support for autowired inject from context manager
	 * as well as {@link CacheManager#getInstance()} while the injected method is
	 * suggested.
	 * 
	 * @throws IOException    when IO failed
	 * @throws CacheException when cache parse failed
	 * @return A CacheManager to inject into cache
	 */
	@Bean
	@Scope("singleton")
	public CacheManager createCacheManager() throws IOException, CacheException {
		return CacheManager.create(new DefaultResourceLoader(null).getResource(configUrl).getURL());
	}

}
