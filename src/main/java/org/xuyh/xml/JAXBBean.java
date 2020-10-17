/*
 * Copyright (c) 2020 XuYanhang
 * 
 */
package org.xuyh.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Bean handler on JAXB decoder and encoder. To handle JAXB on other entities,
 * some annotations is necessary like {@link XmlType}, {@link XmlAccessorType},
 * {@link XmlElement}, {@link XmlElementWrapper} and {@link XmlTransient}. To
 * support this entity at this time, it's necessary to declare it in
 * {@link XmlSeeAlso} in the JAXBBean for some generic type or other uncertain
 * type.
 * 
 * @author XuYanhang
 * @since 2020-10-18
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "root")
/* List all binding classes in this project */
@XmlSeeAlso({})
public class JAXBBean implements java.io.Serializable {

	/**
	 * Affords supplements on object serialize
	 */
	private static final long serialVersionUID = 2008969218225124711L;

	/**
	 * Configuration field who should be a null value, a wrapper value, or a value
	 * who is in a declared type but also declared in {@link XmlSeeAlso}.
	 */
	@XmlElement
	private Object config;

	/**
	 * Data field who should be a null value or a {@link List} whose each item
	 * should be a null value, a wrapper value, or a value who is in a declared type
	 * but also declared in {@link XmlSeeAlso}.
	 */
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<?> items;

	/**
	 * Constructor for decode from XML.
	 */
	public JAXBBean() {
		super();
	}

	/**
	 * Constructor for encode into XML.
	 * 
	 * @param config the configuration field to set
	 * @param items  the data field to set
	 */
	public JAXBBean(Object config, List<?> items) {
		super();
		this.config = config;
		this.items = items;
	}

	/**
	 * Returns the configuration field.
	 * 
	 * @param <T> the generic type of the configuration
	 * @return the config of this bean
	 */
	@SuppressWarnings("unchecked")
	public <T> T getConfig() {
		return (T) config;
	}

	/**
	 * Returns the data field of items.
	 * 
	 * @param <T> the generic type of the elements
	 * @return the items all items of the data field
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getItems() {
		return (List<T>) items;
	}

}
