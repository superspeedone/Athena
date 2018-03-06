package com.superspeed.test.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class CustomedClassLoader extends ClassLoader {

    public CustomedClassLoader() {}

    public CustomedClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * Finds the class with the specified <a href="#name">binary name</a>.
     * This method should be overridden by class loader implementations that
     * follow the delegation model for loading classes, and will be invoked by
     * the {@link #loadClass <tt>loadClass</tt>} method after checking the
     * parent class loader for the requested class.  The default implementation
     * throws a <tt>ClassNotFoundException</tt>.  </p>
     *
     * @param name The <a href="#name">binary name</a> of the class
     * @return The resulting <tt>Class</tt> object
     * @throws ClassNotFoundException If the class could not be found
     * @since 1.2
     */
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String classFileName = name;
            classFileName = classFileName.replace(".", "/") + ".class";

            System.out.println("需要被加载的类的相对路径->" + classFileName);

            URL url = CustomedClassLoader.class.getClassLoader().getResource(classFileName);

            System.out.println("自定义类加载器获取资源根路径->" + CustomedClassLoader.class.getResource("/"));

            System.out.println("需要被加载的类的绝对路径->" + url.getPath());

            File file = new File(url.getPath());
            byte[] fb = getClassBytes(file);
            Class c = this.defineClass(name, fb, 0 , fb.length);
            return  c;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    private byte[] getClassBytes(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel wbc = Channels.newChannel(baos);
        ByteBuffer bf = ByteBuffer.allocate(1024);

        while (true) {
            int n = fc.read(bf);
            if (n == 0 || n == -1) break;
            bf.flip();
            wbc.write(bf);
            bf.clear();
        }
        fis.close();
        return  baos.toByteArray();
    }

}
