package com.superspeed.schemer;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

class SchemaResourceResolver implements LSResourceResolver {

    private static final Logger log = Logger.getLogger(SchemaResourceResolver.class.getName());

    /**
     * Allow the application to resolve external resources.
     * <p>
     * <p>
     * The LSParser will call this method before opening any external resource, including
     * the external DTD subset, external entities referenced within the DTD, and external
     * entities referenced within the document element (however, the top-level document
     * entity is not passed to this method). The application may then request that the
     * LSParser resolve the external resource itself, that it use an alternative URI,
     * or that it use an entirely different input source.
     * </p>
     * <p>
     * <p>
     * Application writers can use this method to redirect external system identifiers to
     * secure and/or local URI, to look up public identifiers in a catalogue, or to read
     * an entity from a database or other input source (including, for example, a dialog box).
     * </p>
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        log.info("\n>> Resolving " + "\n"
                + "TYPE: " + type + "\n"
                + "NAMESPACE_URI: " + namespaceURI + "\n"
                + "PUBLIC_ID: " + publicId + "\n"
                + "SYSTEM_ID: " + systemId + "\n"
                + "BASE_URI: " + baseURI + "\n");

        String schemaLocation = baseURI.substring(0, baseURI.lastIndexOf("/") + 1);

        if (systemId.indexOf("http://") < 0) {
            systemId = schemaLocation + systemId;
        }

        LSInput lsInput = new LSInputImpl();

        URI uri = null;
        try {
            uri = new URI(systemId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        File file = new File(uri);
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        lsInput.setSystemId(systemId);
        lsInput.setByteStream(is);

        return lsInput;
    }

    /**
     * Represents an input source for data
     */
    class LSInputImpl implements LSInput {

        private String publicId;
        private String systemId;
        private String baseURI;
        private InputStream byteStream;
        private Reader charStream;
        private String stringData;
        private String encoding;
        private boolean certifiedText;

        public LSInputImpl() {
        }

        public LSInputImpl(String publicId, String systemId, InputStream byteStream) {
            this.publicId = publicId;
            this.systemId = systemId;
            this.byteStream = byteStream;
        }

        @Override
        public String getBaseURI() {
            return baseURI;
        }

        @Override
        public InputStream getByteStream() {
            return byteStream;
        }

        @Override
        public boolean getCertifiedText() {
            return certifiedText;
        }

        @Override
        public Reader getCharacterStream() {
            return charStream;
        }

        @Override
        public String getEncoding() {
            return encoding;
        }

        @Override
        public String getPublicId() {
            return publicId;
        }

        @Override
        public String getStringData() {
            return stringData;
        }

        @Override
        public String getSystemId() {
            return systemId;
        }

        @Override
        public void setBaseURI(String baseURI) {
            this.baseURI = baseURI;
        }

        @Override
        public void setByteStream(InputStream byteStream) {
            this.byteStream = byteStream;
        }

        @Override
        public void setCertifiedText(boolean certifiedText) {
            this.certifiedText = certifiedText;
        }

        @Override
        public void setCharacterStream(Reader characterStream) {
            this.charStream = characterStream;
        }

        @Override
        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        @Override
        public void setPublicId(String publicId) {
            this.publicId = publicId;
        }

        @Override
        public void setStringData(String stringData) {
            this.stringData = stringData;
        }

        @Override
        public void setSystemId(String systemId) {
            this.systemId = systemId;
        }

    }

}
