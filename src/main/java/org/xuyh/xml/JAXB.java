/*
 * Copyright (c) 2020-2023 XuYanhang
 */

package org.xuyh.xml;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * JAXB(Java Architecture for XML Binding) Tool to encode a bean to XML or
 * decode a bean from XML. The middle proxy bean is {@link JAXBBean}.
 *
 * @author XuYanhang
 * @since 2020-10-18
 */
public class JAXB {
    /**
     * Context of this JAXB
     */
    private volatile JAXBContext context;

    /**
     * Output Factory of this JAXB
     */
    private volatile XMLOutputFactory outputFactory;

    /**
     * Input Factory of this JAXB
     */
    private volatile XMLInputFactory inputFactory;

    /**
     * Initialize this JAXB while all fields are lazy initialized.
     */
    public JAXB() {
        super();
    }

    /**
     * Encodes a {@link JAXBBean} as a XML file.
     *
     * @param file target XML file to write XML into
     * @param bean bean to encode
     * @throws Exception on any exception happens
     */
    public void encode(File file, JAXBBean bean) throws Exception {
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {
            encode(out, bean);
        }
    }

    /**
     * Encodes a {@link JAXBBean} as a XML and writes the bytes on UTF-8 encoding
     * into an {@link OutputStream}.
     *
     * @param out  target stream to write XML into
     * @param bean bean to encode
     * @throws Exception on any exception happens
     */
    public void encode(OutputStream out, JAXBBean bean) throws Exception {
        Marshaller marshaller = getContext().createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
        JAXBStreamWriter writer = new JAXBStreamWriter(getOutputFactory().createXMLStreamWriter(out, "UTF-8"));
        marshaller.marshal(bean, writer);
    }

    /**
     * Decodes a file and returns a JAXB bean. Specially if this file doen't exists,
     * an empty {@link JAXBBean} returned.
     *
     * @param file the XML file to decode from
     * @return a JAXB bean
     * @throws Exception on any exception happens
     */
    public JAXBBean decode(File file) throws Exception {
        if (!file.exists()) return new JAXBBean();
        try (InputStream in = new BufferedInputStream(new FileInputStream(file))) {
            return decode(in);
        }
    }

    /**
     * Decodes an {@link InputStream} and returns a JAXB bean.
     *
     * @param in the XML stream to decode from
     * @return a JAXB bean
     * @throws Exception on any exception happens
     */
    public JAXBBean decode(InputStream in) throws Exception {
        Unmarshaller unmarshaller = getContext().createUnmarshaller();
        JAXBStreamReader reader = new JAXBStreamReader(getInputFactory().createXMLStreamReader(in, "UTF-8"));
        return (JAXBBean) unmarshaller.unmarshal(reader);
    }

    private JAXBContext getContext() throws Exception {
        if (context != null) return context;
        synchronized (this) {
            if (null == context) context = JAXBContext.newInstance(JAXBBean.class);
        }
        return context;
    }

    private XMLOutputFactory getOutputFactory() {
        if (null != outputFactory) return outputFactory;
        synchronized (this) {
            if (null == outputFactory) outputFactory = XMLOutputFactory.newInstance();
        }
        return outputFactory;
    }

    private XMLInputFactory getInputFactory() {
        if (null != inputFactory) return inputFactory;
        synchronized (this) {
            if (null == inputFactory) inputFactory = XMLInputFactory.newInstance();
        }
        return inputFactory;
    }
}
